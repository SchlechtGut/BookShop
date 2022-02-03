package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.book.Book;

import java.util.List;

public class BooksPageDto {

    private Long count;
    private List<Book> books;

    public BooksPageDto(List<Book> books) {
        this.count = (long) books.size();
        this.books = books;
    }

    public BooksPageDto(Long overallCount, List<Book> books) {
        this.count = overallCount;
        this.books = books;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}

