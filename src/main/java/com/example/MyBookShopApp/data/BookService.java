package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.book.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooksData(){
        ArrayList<Integer> list = new ArrayList<>();
        list.toArray(new Integer[0]);

        return bookRepository.findAll();
    }
}
