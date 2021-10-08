package com.example.CrudApplicationUsingJpaMySql.service;

import com.example.CrudApplicationUsingJpaMySql.entity.User;
import com.example.CrudApplicationUsingJpaMySql.entity.UserScore;
import com.example.CrudApplicationUsingJpaMySql.exception.*;

import java.time.LocalDateTime;

public interface UserService {

    void checkIfPasswordIsTrue(String requestpassword, String password) throws CodeDoesNotMatchException, EmailAndPasswordDoNotMatch;
    boolean checkIfUserExists(String email);
    boolean checkIfUsernameExists(String username);
    User saveUser(User user) throws EmailAlreadyExistsException;
    UserScore saveScores(UserScore userScore);
    void sendVerificationEmail(User user, String to,String code);
    void checkIfExpirationValid(LocalDateTime time) throws ExpirationNotValidException;
    void checkIfEmailVerified(String email) throws EmailNotVerifiedException;
    void checkIfCodeMatches(long id, String code, User user) throws CodeDoesNotMatchException;
    void checkIfPasswordsMatch(String password, String password2) throws PasswordsDontMatchException;

    void checkIfEmailAndCodeMatches(String code, String email) throws CodeDoesNotMatchException;

    void checkIfUserActivated(User user) throws UserNotActivatedException;
}
