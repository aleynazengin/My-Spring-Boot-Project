package com.example.CrudApplicationUsingJpaMySql.controller;

import com.example.CrudApplicationUsingJpaMySql.annotation.AllowAnnonymous;
import com.example.CrudApplicationUsingJpaMySql.entity.*;
import com.example.CrudApplicationUsingJpaMySql.exception.*;
import com.example.CrudApplicationUsingJpaMySql.repo.UserRepository;
import com.example.CrudApplicationUsingJpaMySql.repo.UserScoreRepository;
import com.example.CrudApplicationUsingJpaMySql.service.JwtUtil;
import com.example.CrudApplicationUsingJpaMySql.service.UserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
public class Controller {
    private UserService userService;
    private UserRepository userRepository;
    private UserScoreRepository userScoreRepository;
    private JwtUtil jwtUtil;
    @Value("${link.expiration.time}")
    private int expirationTimeInSeconds;
    LocalDateTime lastRequestedTime=null;

    public Controller(UserService userService, UserScoreRepository userScoreRepository, JwtUtil jwtUtil,UserRepository userRepository) {
        this.userService = userService;

        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userScoreRepository= userScoreRepository;
    }

    @AllowAnnonymous
    @RequestMapping( value = "/api/users", method = RequestMethod.POST)
    public ApiException apiException(@Valid @RequestBody User user) throws EmailAlreadyExistsException {
        userService.saveUser(user);
        ApiException response =
                new ApiException("User successfully created",
                        null,HttpStatus.CREATED);
        return response;
    }

    //REGISTER
    @AllowAnnonymous
    @RequestMapping( value = "/api/register", method = RequestMethod.POST)
    public ApiException userCreate(@Valid @RequestBody UserRequest userRequest) throws EmailAlreadyExistsException, UsernameAlreadyExistsException {

        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        uuidAsString = UUID.randomUUID().toString().replace("-", "");

        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(uuidAsString);
        user.setEmailVerified(false);
        user.setAccountVerified(false);
        user.setExpireAt(LocalDateTime.now().plusSeconds(expirationTimeInSeconds));
        user.setLastMailSentTime(LocalDateTime.now());
        user.setUsername(userRequest.getUsername());
        if (userService.checkIfUsernameExists(user.getUsername())){
            throw new UsernameAlreadyExistsException();
        }
        if (userService.checkIfUserExists(user.getEmail())){
            throw new EmailAlreadyExistsException("User already exists for this email.");
        }
        userService.saveUser(user);
        userService.sendVerificationEmail(user, user.getEmail(),uuidAsString);

        ApiException response =
                new ApiException("To complete the registration please check your email.",
                        null,HttpStatus.CREATED);
        return response;
    }

    ///CONFIRM EMAIL
    @AllowAnnonymous
    @RequestMapping( value = "/confirm-email", method = RequestMethod.GET)
    public ApiException confirmAccount(@RequestParam("id")long id,@RequestParam("code")String code) throws NotAuthorizedException, ExpirationNotValidException, CodeDoesNotMatchException {

        ApiException response = null;
        User user = userRepository.findById(id);//check if user exists
        if( user!=null) {
            LocalDateTime time = user.getExpireAt();
            userService.checkIfExpirationValid(time);
            userService.checkIfCodeMatches(id,code,user);
            user.setEmailVerified(true);
            userRepository.save(user);
            response=
                    new ApiException("Email verified.To complete the registration and set your password, visit: /setPassword",
                            null,HttpStatus.OK);
        }
        else{
            response=
                    new ApiException("This email does not exist",
                            null,HttpStatus.NOT_FOUND);
        }
        return response;
    }

    //SET PASSWORD
    @AllowAnnonymous
    @RequestMapping( value = "/setPassword", method = RequestMethod.POST)
    public ApiException setPassword(@Valid @RequestBody PasswordSetRequest passwordSetRequest) throws EmailNotVerifiedException, EmailAlreadyExistsException, PasswordsDontMatchException, CodeDoesNotMatchException {
        ApiException response = null;
        User user = userRepository.findByEmail(passwordSetRequest.getEmail()); //find user by email

        if( user!=null) {
            userService.checkIfEmailVerified(passwordSetRequest.getEmail()); //check if email verified
            //check if passwords match
            userService.checkIfPasswordsMatch(passwordSetRequest.getPassword(), passwordSetRequest.getConfirmPassword());
            userService.checkIfEmailAndCodeMatches(user.getPassword(),passwordSetRequest.getCode());
            user.setPassword(passwordSetRequest.getPassword());
            user.setAccountVerified(true);
            userService.saveUser(user);
            response=
                    new ApiException("Your registration is completed.You can go to login page now.",
                            null,HttpStatus.OK);
        }
        else{
            response=
                    new ApiException("This user for this email does not exist",
                            null,HttpStatus.NOT_FOUND);
        }
        return response;
    }

    //RESEND EMAIL
    @AllowAnnonymous
    @RequestMapping( value = "/sendEmail", method = RequestMethod.POST)
    public ApiException sendEmail(@Valid @RequestBody EmailRequest emailRequest) throws EmailAlreadyExistsException {
        ApiException response = null;
        User user = userRepository.findByEmail(emailRequest.getEmail()); //Find user by email

        if( user!=null) {
            UUID uuid = UUID.randomUUID(); //generate code again
            String uuidAsString = uuid.toString();
            uuidAsString = UUID.randomUUID().toString().replace("-", "");

            long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), user.getLastMailSentTime());
            Duration.between(user.getLastMailSentTime(), LocalDateTime.now()).toSeconds();

            //If there is at least 1 minute time between previous request
            if (Duration.between(user.getLastMailSentTime(), LocalDateTime.now()).toSeconds()>60){
                user.setPassword(uuidAsString); //set password this generated code
                userService.sendVerificationEmail(user, user.getEmail(),uuidAsString); //send email
                user.setLastMailSentTime(LocalDateTime.now()); //set new mail sent time
                user.setExpireAt(LocalDateTime.now().plusSeconds(expirationTimeInSeconds));
                userService.saveUser(user);
                response= new ApiException("Link sent to this email",
                                        null,HttpStatus.OK);
            }
            else
            {
                response= new ApiException("You can only send email request in every 1 minute,please wait.",
                                null,HttpStatus.FORBIDDEN);
            }
        }
        else{
            response=
                    new ApiException("No registration found for this email. You must register first!",
                            null,HttpStatus.NOT_FOUND);
        }
        return response;
    }

    //LOGIN
    @AllowAnnonymous
    @RequestMapping( value = "/authenticate", method = RequestMethod.POST)
    //ResponseEntity represents an HTTP response, including headers, body, and status
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        User user= userRepository.findByEmail(authenticationRequest.getEmail());
        if (user==null)
        {
            throw new EmailDoesNotExistException();
        }
        userService.checkIfEmailVerified(user.getEmail());
        userService.checkIfUserActivated(user);
        userService.checkIfPasswordIsTrue(authenticationRequest.getPassword(),user.getPassword());
        String token = jwtUtil.generateToken(user.getEmail(),user);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @AllowAnnonymous
    @RequestMapping( value = "/api/welcome", method = RequestMethod.GET)
    public String welcome(){
        return "Welcome";
    }

    @RequestMapping( value = "/api/hello", method = RequestMethod.GET)
    public String hello(){
        return "Hello";
    }

    @RequestMapping( value = "/api/score", method = RequestMethod.POST)
    public ApiException apiException(@Valid @RequestBody ScoreRequest scoreRequest, HttpServletRequest request, HttpServletResponse response) throws EmailAlreadyExistsException, NotAuthorizedException {

        String authorizationHeader = request.getHeader("Authorization");
        String jwt=authorizationHeader.substring(7);

        String email = jwtUtil.getSubject(jwt);
        User user = userRepository.findByEmail(email);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        UserScore score = new UserScore();
        score.setScore(scoreRequest.getScore());
        score.setScoreDate(dtf.format(now));
        score.setUser(user);
        userService.saveScores(score);

        ApiException apiresponse =
                new ApiException("Scor successfully added",
                        null,HttpStatus.CREATED);
        return apiresponse;
    }

    @RequestMapping( value = "/api/scoreList", method = RequestMethod.GET)
    private List<UserScore> getAllScores()
    {
        return userScoreRepository.findAllInfo();
    }





}
