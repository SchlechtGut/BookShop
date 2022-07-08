package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.book.ViewedBook;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.data.book.Book;

import com.example.MyBookShopApp.repository.ViewedBookRepository;
import com.example.MyBookShopApp.security.UserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class BookPopularityService {

    private final BookRepository bookRepository;
    private final ViewedBookRepository viewedBookRepository;
    private final UserRegister userRegister;

    @Value("${viewed.time.expiring}")
    private int viewedExpireTime;

    @Autowired
    public BookPopularityService(BookRepository bookRepository, ViewedBookRepository viewedBookRepository, UserRegister userRegister) {
        this.bookRepository = bookRepository;
        this.viewedBookRepository = viewedBookRepository;
        this.userRegister = userRegister;
    }

    public Page<Book> getPageOfPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset,limit, Sort.by(Sort.Direction.DESC, "popularity"));

        return bookRepository.findPopularBooks(nextPage);
    }

    public List<Book> getViewedBooks(User user) {
        List<ViewedBook> viewedInstances = viewedBookRepository.findByUserId(user.getId());

        return bookRepository.findBooksByIdIn(viewedInstances.stream().map(ViewedBook::getBookId).collect(Collectors.toList()));
    }

    @Scheduled(cron = "* * */6 * * *")
    public void deleteExpiredBookViews() {
        Map<Integer, Integer> counts = new HashMap<>();

        List<ViewedBook> expiredViews = viewedBookRepository.findExpired(viewedExpireTime);

        expiredViews.forEach(System.out::println);

        for (ViewedBook viewedEvent : expiredViews) {
            int id = viewedEvent.getBookId();

            if (counts.containsKey(viewedEvent.getBookId())) {
                counts.merge(id, 1, Integer::sum);
            } else {
                counts.put(id, 1);
            }
        }

        viewedBookRepository.deleteAll(expiredViews);

        List<Book> books = bookRepository.findBooksByIdIn(counts.keySet());

        for (Book book : books) {
            book.setViewedCount(book.getViewedCount() - counts.get(book.getId()));
        }

        bookRepository.saveAll(books);
    }
}
