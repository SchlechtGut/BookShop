package com.example.MyBookShopApp.data.book.links;

import com.example.MyBookShopApp.data.enums.Book2UserType;

import javax.persistence.*;

@Entity
@Table(name = "book2user_type")
public class Book2UserTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", columnDefinition = "VARCHAR(255) NOT NULL")
    private Book2UserType code;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    {
        if (code != null)
        name = code.getName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book2UserType getCode() {
        return code;
    }

    public void setCode(Book2UserType code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
