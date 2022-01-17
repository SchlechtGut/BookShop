package com.example.MyBookShopApp.data.book.links;

import com.example.MyBookShopApp.data.author.Author;
import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.links.keys.BookAuthorId;

import javax.persistence.*;

@Entity
@Table(name = "book2author")
@IdClass(BookAuthorId.class)
public class Book2AuthorEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private Book book;

    @Id
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private Author author;

    @Column(name = "sort_index" ,columnDefinition = "INT NOT NULL  DEFAULT 0")
    private int sortIndex;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }
}
