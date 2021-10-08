package com.example.CrudApplicationUsingJpaMySql.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class PasswordSetRequest {

    @NotEmpty(message = "Password cannot be null.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!)+_@$%^(&*-]).{8,}$",
            message = "Password must contain at least 1 upper case, one lower case,one digit,one special character and must be minimum 8 in length.For Example: Abc1234*")
    private String password;

    private String confirmPassword;
    @Email(message = "This email address is invalid.", flags = { Pattern.Flag.CASE_INSENSITIVE })
    private String email;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PasswordSetRequest(String password, String confirmPassword,String email,String code) {
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email=email;
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
