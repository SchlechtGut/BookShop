package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.data.book.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



@Service
public class BookPopularityService {

    private final BookRepository bookRepository;

    @Autowired
    public BookPopularityService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<Book> getPageOfPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset,limit, Sort.by(Sort.Direction.DESC, "popularity"));

        return bookRepository.findPopularBooks(nextPage);
    }

    public Page<Book> getPageOfPopularBooksTest(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset,limit, Sort.by(Sort.Direction.DESC, "popularity"));

        return bookRepository.testFindPopularBooks(nextPage);
    }
}
