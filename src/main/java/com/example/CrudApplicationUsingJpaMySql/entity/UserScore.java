package com.example.CrudApplicationUsingJpaMySql.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Entity
@Table(name="userscores")
public class UserScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="score")
    @NotNull
    private int score;

    @Column(name="createddate")
    @NotEmpty(message = "Date cannot be null")
    private String scoreDate;

    @JsonBackReference
    //Child belirtmek için kullanılır.
    //User primary key exists on the UserScore table so we say User is the parent and UserScore is the child.
    @ManyToOne
    @JoinColumn(name = "MY_USER_ID") //to declare the foreign key column.
    private User user;

    public UserScore() {

    }
}
