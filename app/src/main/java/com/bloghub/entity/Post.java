package com.bloghub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")//specifying table name
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)             //auto increment
    private Long id;
    private String title;                                           //post title
    private String content;                                         //post content
    private LocalDateTime createdAt;                                 //post creation time

    @ManyToOne(fetch = FetchType.LAZY)                               //many posts can belong to one author
    @JoinColumn(name = "author_id", nullable = false)                  //foreign key column
    private Author author;                                            //many posts can belong to one author

    @ManyToOne(fetch = FetchType.LAZY)                                 //many posts can belong to one category
    @JoinColumn(name = "category_id", nullable = false)                //foreign key column
    private Category category;                                          //many posts can belong to one category
}