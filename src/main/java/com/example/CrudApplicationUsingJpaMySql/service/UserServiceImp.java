package com.example.CrudApplicationUsingJpaMySql.service;

import com.example.CrudApplicationUsingJpaMySql.entity.User;
import com.example.CrudApplicationUsingJpaMySql.entity.UserScore;
import com.example.CrudApplicationUsingJpaMySql.exception.*;
import com.example.CrudApplicationUsingJpaMySql.repo.UserRepository;
import com.example.CrudApplicationUsingJpaMySql.repo.UserScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class UserServiceImp implements UserService{

    private UserRepository userRepository;
    private UserScoreRepository userScoreRepository;
    @Value("${spring.mail.username}")
    private String from;

    @Value("${mail.subject}")
    private String subject;

    @Value("${mail.content}")
    private String content;

    @Value("${link.expiration.time}")
    private int expirationTimeInSeconds;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;

    public UserServiceImp(UserRepository userRepository, UserScoreRepository userScoreRepository) {
        this.userRepository = userRepository;
        this.userScoreRepository = userScoreRepository;
    }

    @Override
    public User saveUser(User user)  {
        encodePassword(user);
        return userRepository.save(user);
    }

    @Override
    public UserScore saveScores(UserScore userScore) {
        return userScoreRepository.save(userScore);
    }


    @Override
    public void sendVerificationEmail(User user, String to,String code) {

        SimpleMailMessage simpleMailMessage =new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        if (!user.isEmailVerified())
        {
            simpleMailMessage.setText(content+"http://localhost:8084/confirm-email?id="+ user.getId()+"&code="+code);
        }
        else {
            simpleMailMessage.setText("To reset your password click here: " + "http://localhost:8084/confirm-email?id=" + user.getId() + "&code=" + code);
        }
        try{
            mailSender.send(simpleMailMessage);
        }
        catch (MailException mailException) {

        }
    }

    @Override
    public void checkIfExpirationValid(LocalDateTime time) throws ExpirationNotValidException {
        if (Duration.between(LocalDateTime.now(), time).toSeconds()>expirationTimeInSeconds){
            throw new ExpirationNotValidException();
        }
    }

    @Override
    public void checkIfEmailVerified(String email) throws EmailNotVerifiedException {
        User user = userRepository.findByEmail(email);
        if (!user.isEmailVerified())
        {
            throw new EmailNotVerifiedException();
        }
    }

    @Override
    public void checkIfCodeMatches(long id, String code, User user) throws CodeDoesNotMatchException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!(passwordEncoder.matches(code, user.getPassword()) &&user.getId()==id))
        {
            throw new CodeDoesNotMatchException();
        }
    }

    @Override
    public void checkIfPasswordsMatch(String password, String password2) throws PasswordsDontMatchException {
        if (!password.equals(password2))
        {
            throw new PasswordsDontMatchException();
        }
    }

    @Override
    public void checkIfEmailAndCodeMatches(String code, String password) throws CodeDoesNotMatchException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!(encoder.matches(password, code)))
        {
            throw new CodeDoesNotMatchException();
        }
    }

    @Override
    public void checkIfUserActivated(User user) throws UserNotActivatedException {
        if (!user.isAccountVerified())
            throw new UserNotActivatedException();

    }

    @Override
    public void checkIfPasswordIsTrue(String requestpassword, String password) throws EmailAndPasswordDoNotMatch {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!(encoder.matches(requestpassword,password))) //sırasıyla (rawPassword, encodedPassword)
            throw new EmailAndPasswordDoNotMatch();
    }

    @Override
    public boolean checkIfUserExists(String email) {
        return userRepository.findByEmail(email)!=null?true:false;
    }

    @Override
    public boolean checkIfUsernameExists(String username) {
        return userRepository.findByUsername(username)!=null?true:false;
    }

    public void encodePassword(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        /*
        Hashed passwords are stored with a prefix to identify the algorithm used. BCrypt got the prefix $2$.

        $2a$
        The original BCrypt specification did not define how to handle non-ASCII characters, or how to handle a null terminator.
        The specification was revised to specify that when hashing strings:
        -the string must be UTF-8 encoded
        -the null character must be included
         */
    }

}
