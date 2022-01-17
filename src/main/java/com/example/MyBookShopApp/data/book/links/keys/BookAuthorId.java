package com.example.MyBookShopApp.data.book.links.keys;

import java.io.Serializable;
import java.util.Objects;

public class BookAuthorId implements Serializable {

    private int book;
    private int author;

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookAuthorId that = (BookAuthorId) o;
        return book == that.book && author == that.author;
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, author);
    }
}
