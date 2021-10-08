package com.example.CrudApplicationUsingJpaMySql.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="email",unique = true)
    @NotEmpty(message = "Email address cannot be null")
    @Email(message = "The email address is invalid.", flags = { Pattern.Flag.CASE_INSENSITIVE })
    private String email;

    @Column(name="username",unique = true)
    @Size(min = 3, max = 12,message = "Username must be between 3 and 12 characters")
    @NotEmpty(message = "Username cannot be null")
    private String username;

    @Column(name="password")
    @NotEmpty(message = "Password cannot be null.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
            message = "Password must contain at least 1 upper case, one lower case,one digit,one special character and must be minimum 8 in length.For Example: Abc1234*")
    private String password;

    @Column(name="emailVerified")
    @NotNull
    private boolean emailVerified;

    @Column(name="accountVerified")
    @NotNull
    private boolean accountVerified;

    @Column
    @Temporal(TemporalType.TIMESTAMP) //We use @Temporal annotation to insert date, time or both in database table
    @CreatedDate
    private Date createdAt;

    @Column
    @Basic
    private LocalDateTime expireAt;

    @Column
    private LocalDateTime lastMailSentTime;

    @OneToMany(cascade = CascadeType.ALL) //CascadeType.ALL means it will do all actions.(REFRESH,MERGE,PERSIST,REMOVE)
    @JoinColumn(name = "MY_USER_ID") //to declare the foreign key column.
    @JsonManagedReference //One to Many senaryolarda Parent table belirtmek için kullanılır.
    //User primary key exists on the UserScore table so we say User is the parent and UserScore is the child.
    private Set<UserScore> userScores;
}
