package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.BookRepository;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.book.Book;
import org.apache.catalina.WebResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooksData(){
        ArrayList<Integer> list = new ArrayList<>();
        list.toArray(new Integer[0]);

        return bookRepository.findAll();
    }

    public Page<Book> getPageOfRecommendedBooks(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.findAll(nextPage);
    }

    public BooksPageDto getRightPageOfRecentBooks(String from, String to, Integer offset, Integer limit) {
        if (from == null && to == null) {
            return new BooksPageDto(getPageOfRecentBooks(offset, limit).getContent());
        } else if (to == null) {
            LocalDate dateFrom = convertToLocalDate(from);
            return new BooksPageDto(getPageOfRecentBooksAfter(dateFrom, offset, limit).getContent());
        } else if (from == null) {
            LocalDate dateTo = convertToLocalDate(to);
            return new BooksPageDto(getPageOfRecentBooksBefore(dateTo, offset, limit).getContent());
        }

        LocalDate dateFrom = convertToLocalDate(from);
        LocalDate dateTo = convertToLocalDate(to);

        return new BooksPageDto(getPageOfRecentBooksBetweenDates(dateFrom, dateTo, offset, limit).getContent());
    }

    public Page<Book> getPageOfPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.findBooksByIsBestsellerEquals(1, nextPage);
    }

    public Page<Book> getPageOfSearchResultBooks(String searchWord, int offset, int limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookByTitleContainingIgnoreCase(searchWord, nextPage);
    }

    private Page<Book> getPageOfRecentBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByOrderByPubDateDesc(nextPage);
    }

    private Page<Book> getPageOfRecentBooksAfter(LocalDate from, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByPubDateAfterOrderByPubDateDesc(from, nextPage);
    }

    private Page<Book> getPageOfRecentBooksBefore(LocalDate to, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByPubDateBeforeOrderByPubDateDesc(to, nextPage);
    }

    private Page<Book> getPageOfRecentBooksBetweenDates(LocalDate from, LocalDate to, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByPubDateBetweenOrderByPubDateDesc(from, to, nextPage);
    }

    private LocalDate convertToLocalDate(String dateInString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(dateInString, formatter);
    }
}
