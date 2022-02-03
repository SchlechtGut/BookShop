package com.example.MyBookShopApp.data.book.tag;

import com.example.MyBookShopApp.data.book.Book;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book2tag",
            joinColumns = @JoinColumn(name = "tag_id", columnDefinition = "INT NOT NULL"),
            inverseJoinColumns = @JoinColumn(name = "book_id", columnDefinition = "INT NOT NULL"))
    private List<Book> books;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
