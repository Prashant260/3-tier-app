package com.bloghub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categories") //specifying table name
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Long id;


    @Column(unique = true)    //category name should be unique
    private String catName;

    private String descr;


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)           //one category can have multiple posts
    private List<Post> posts;
}