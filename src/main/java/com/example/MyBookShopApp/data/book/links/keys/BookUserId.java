package com.example.MyBookShopApp.data.book.links.keys;

import java.io.Serializable;
import java.util.Objects;

public class BookUserId implements Serializable {

    private int book;
    private int user;

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookUserId that = (BookUserId) o;
        return book == that.book && user == that.user;
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, user);
    }
}
