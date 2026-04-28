package com.bloghub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authors")//specifying table name
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto increment
    private Long id;//primary key

    private String name;      //author name

    @Column(unique = true)    //email should be unique
    private String email;     //author email

    private String about;     //author about

    private String password;  //author password

    @Column(nullable = false)  // role cannot be null
    private String role = "USER";       //author role


    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    //it is a one-to-many relationship author can have multiple posts
    private List<Post> posts;                                     //one author can have multiple posts

    public Author(String name, String email, String about, String password) {
        this.name = name;
        this.email = email;
        this.about = about;
        this.password = password;
    }
}