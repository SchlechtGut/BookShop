package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.errs.BookstoreApiWrongParameterException;
import com.example.MyBookShopApp.repository.BookRatingRepository;
import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.book.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookRatingRepository bookRatingRepository;

    @Autowired
    public BookService(BookRepository bookRepository, BookRatingRepository bookRatingRepository) {
        this.bookRepository = bookRepository;
        this.bookRatingRepository = bookRatingRepository;
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
        Page<Book> page;

        if (from == null && to == null) {
            page = getPageOfRecentBooks(offset, limit);

            return new BooksPageDto(page.getTotalElements(), page.getContent());
        } else if (to == null) {
            LocalDate dateFrom = convertToLocalDate(from);
            page = getPageOfRecentBooksAfter(dateFrom, offset, limit);
            return new BooksPageDto(page.getTotalElements(), page.getContent());
        } else if (from == null) {
            LocalDate dateTo = convertToLocalDate(to);
            page = getPageOfRecentBooksBefore(dateTo, offset, limit);
            return new BooksPageDto(page.getTotalElements(), page.getContent());
        }

        LocalDate dateFrom = convertToLocalDate(from);
        LocalDate dateTo = convertToLocalDate(to);
        page = getPageOfRecentBooksBetweenDates(dateFrom, dateTo, offset, limit);

        return new BooksPageDto(page.getTotalElements(), page.getContent());
    }



    public Page<Book> getPageOfSearchResultBooks(String searchWord, int offset, int limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookByTitleContainingIgnoreCase(searchWord, nextPage);
    }


    public Page<Book> getPageOfBooksByTag(Integer tagId, int offset, int limit) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "pub_date"));

        return bookRepository.findBooksByTag(tagId, nextPage);
    }

    public Page<Book> getPageOfBooksByGenre(Integer id, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "pub_date"));

        return bookRepository.findBooksByGenre(id, nextPage);
    }

    public Page<Book> getPageOfAuthorBooks(Integer id, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "pub_date"));

        return bookRepository.findBooksByAuthor(id, nextPage);
    }

    public int getBooksCountByAuthorSlug(String slug) {
        return bookRepository.findBooksCountByAuthorSlug(slug);
    }

    public Page<Book> getPageOfAuthorBooksBySlug(String slug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "pub_date"));
        return bookRepository.findBooksByAuthorSlug(slug, nextPage);
    }

    public List<Book> getSeveralBooksByAuthorID(Integer id) {
        return bookRepository.findSeveralBooksByAuthorId(id);
    }

    //////////private///////////////////////////////////////////////////////////////////////////////////////////////////

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

    public Book getBookBySlug(String slug) {
        return bookRepository.findBySlug(slug);
    }

    public void save(Book bookToUpdate) {
        bookRepository.save(bookToUpdate);
    }

    //NEW BOOK SERVICE METHODS

    public List<Book> getBooksByAuthor(String authorName){
        return bookRepository.findBooksByAuthorNameContaining(authorName);
    }

    public List<Book> getBooksByTitle(String title) throws BookstoreApiWrongParameterException {
        if (title.length() <= 1) {
            throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
        } else {
            List<Book> data = bookRepository.findBooksByTitleContainingIgnoreCase(title);
            if (data.size() > 0) {
                return data;
            } else {
                throw new BookstoreApiWrongParameterException("No data found with specified parameters...");
            }
        }
    }

    public List<Book> getBooksWithPriceBetween(Integer min, Integer max){
        return bookRepository.findBooksByPriceOldBetween(min, max);
    }

    public List<Book> getBooksWithMaxPrice(){
        return bookRepository.getBooksWithMaxDiscount();
    }

    public List<Book> getBestsellers(){
        return bookRepository.getBestsellers();
    }

    public List<Book> findBooksBySlugIn(String[] cookieSlugs) {
        return bookRepository.findBooksBySlugIn(cookieSlugs);
    }

    public List<Book> getBooksByIdIn(List<Integer> bookIds) {
        return bookRepository.findBooksByIdIn(bookIds);
    }

    public Book getBookById(Integer bookId) {
        return bookRepository.findById(bookId).get();
    }
}
