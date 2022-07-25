package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.enums.Book2UserType;
import com.example.MyBookShopApp.data.payments.BalanceTransactionEntity;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.BalanceTransactionRepository;
import com.example.MyBookShopApp.repository.Book2UserRepository;
import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.repository.UserRepository;
import com.example.MyBookShopApp.security.UserRegister;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttachedBooksService {

    private final BookRepository bookRepository;
    private final UserRegister userRegister;
    private final BalanceTransactionRepository balanceTransactionRepository;
    private final Book2UserRepository book2UserRepository;
    private final UserRepository userRepository;


    public AttachedBooksService(BookRepository bookRepository, UserRegister userRegister, BalanceTransactionRepository balanceTransactionRepository, Book2UserRepository book2UserRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRegister = userRegister;
        this.balanceTransactionRepository = balanceTransactionRepository;
        this.book2UserRepository = book2UserRepository;
        this.userRepository = userRepository;
    }

    public String addToPostponedOrCart(User user, String[] ids, String status) {
        String error = null;
        List<Integer> idList = new ArrayList<>();

        Arrays.stream(ids).forEach(x -> idList.add(Integer.valueOf(x)));
        List<Book> books = bookRepository.findAllById(idList);

        Book2UserType type = status.equals("KEPT") ? Book2UserType.KEPT : Book2UserType.CART;

        List<Book2UserEntity> book2UserForSave = new ArrayList<>();

        for (Book book : books) {
            Book2UserEntity existingBook2UserEntity = book2UserRepository.findByBookIdAndUserId(book.getId(), user.getId());

            if (existingBook2UserEntity == null) {
                existingBook2UserEntity = new Book2UserEntity();
                existingBook2UserEntity.setBookId(book.getId());
                existingBook2UserEntity.setUser(user);
                existingBook2UserEntity.setType(type);
                existingBook2UserEntity.setTime(LocalDateTime.now());

                book2UserForSave.add(existingBook2UserEntity);

            } else if (existingBook2UserEntity.getType().equals(Book2UserType.PAID) ||
                    existingBook2UserEntity.getType().equals(Book2UserType.ARCHIVED)) {
                error = "can't put in a book that'd been already bought";

            } else if (!existingBook2UserEntity.getType().equals(type)) {
                existingBook2UserEntity.setType(type);
                existingBook2UserEntity.setTime(LocalDateTime.now());

                book2UserForSave.add(existingBook2UserEntity);
            }
        }

        book2UserRepository.saveAll(book2UserForSave);

        return error;
    }

    public void removeFromCartPostponed(String id, HttpServletRequest request, Authentication authentication) {
        Book book = bookRepository.getById(Integer.valueOf(id));
        User user;

        if (authentication != null) {
            user = userRegister.getCurrentUser(authentication);
        } else {
            HttpSession session = request.getSession();
            user = (User) session.getAttribute("empty_user");
        }

        book2UserRepository.deleteByUserAndBookIdAndTypeIn(user, book.getId(), List.of(Book2UserType.CART, Book2UserType.KEPT));
    }

    private void updatePostponedCount(HttpServletResponse response, Model model, Cookie cookie) {
        cookie.setPath("/books");
        response.addCookie(cookie);

        Cookie numberOfPostponed = new Cookie("postponedCount", String.valueOf(cookie.getValue().split("/").length));
        numberOfPostponed.setPath("/");
        response.addCookie(numberOfPostponed);

        model.addAttribute("isPostponedEmpty", false);
    }

    public String buyBooks(String cartContents, HttpServletResponse response, Model model, Authentication authentication) {
        cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
        cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
        String[] cookieSlugs = cartContents.split("/");
        List<Book> booksFromCookieSlugs = bookRepository.findBooksBySlugIn(cookieSlugs);

        double paymentSumTotal = booksFromCookieSlugs.stream().mapToDouble(Book::getDiscountPrice).sum();
        User user;
        try {
            user = userRegister.getCurrentUser(authentication);
        } catch (Exception e){
            return "redirect:/signin";
        }
        int balance = user.getBalance();

        if (balance < paymentSumTotal){
            model.addAttribute("notEnough", true);
            return "redirect:/cart";
        }



        List <BalanceTransactionEntity> transactions = new ArrayList<>();
        List <Book2UserEntity> book2UserEntities = new ArrayList<>();
        for (Book book : booksFromCookieSlugs) {
            double price = book.getDiscountPrice().doubleValue();
            BalanceTransactionEntity transaction = new BalanceTransactionEntity();

            transaction.setBookId(book.getId());
            transaction.setValue((int) -price);
            transaction.setUserId(user.getId());
            transaction.setDescription("Покупка " + book.getTitle());
            transaction.setTime(LocalDateTime.now());

            Book2UserEntity book2UserEntity = new Book2UserEntity();
            book2UserEntity.setBookId(book.getId());
            book2UserEntity.setUser(user);
            book2UserEntity.setTime(LocalDateTime.now());
            book2UserEntity.setType(Book2UserType.PAID);

            transactions.add(transaction);
            book2UserEntities.add(book2UserEntity);
        }

        user.setBalance((int) (balance - paymentSumTotal));

        balanceTransactionRepository.saveAll(transactions);
        book2UserRepository.saveAll(book2UserEntities);
        userRepository.save(user);


        response.addCookie(new Cookie("cartContents", ""));
        model.addAttribute("success", true);
        model.addAttribute("isCartEmpty", true);
        model.addAttribute("cartContentsCount", 0);


        return "redirect:/cart";
    }


    public void archiveBook(String slug, Authentication authentication) {
        Book book = bookRepository.findBySlug(slug);
        int userId = userRegister.getCurrentUser(authentication).getId();

        Book2UserEntity book2UserEntity = book2UserRepository.findByBookIdAndUserId(book.getId(), userId);

        book2UserEntity.setType(Book2UserType.ARCHIVED);
        book2UserRepository.save(book2UserEntity);

    }

    public void deArchiveBook(String slug, Authentication authentication) {
        Book book = bookRepository.findBySlug(slug);
        int userId = userRegister.getCurrentUser(authentication).getId();

        Book2UserEntity book2UserEntity = book2UserRepository.findByBookIdAndUserId(book.getId(), userId);

        book2UserEntity.setType(Book2UserType.PAID);
        book2UserRepository.save(book2UserEntity);
    }

    public void showInnerContent(Model model, HttpServletRequest request, Authentication authentication, Book2UserType type) {
        HttpSession session = request.getSession();
        User emptyUser = (User) session.getAttribute("empty_user");
        List<Book2UserEntity> book2UserEntities = new ArrayList<>();

        if (authentication != null) {
            book2UserEntities = userRegister.getCurrentUser(authentication).getBook2UserEntities().stream().filter(x-> x.getType().equals(type)).collect(Collectors.toList());
        } else if (emptyUser != null) {
            book2UserEntities = userRepository.getById(emptyUser.getId()).getBook2UserEntities().stream().filter(x-> x.getType().equals(type)).collect(Collectors.toList());
        }

        List<Book> books;
        books = bookRepository.findAllById(book2UserEntities.stream().map(Book2UserEntity::getBookId).collect(Collectors.toList()));

        if (books.isEmpty()) {
            model.addAttribute("isCartEmpty", true);
        } else {
            model.addAttribute("isCartEmpty", false);
            model.addAttribute("booksIn", books);
            model.addAttribute("totalPrice", books.stream().mapToInt(Book::getDiscountPrice).sum());
            model.addAttribute("totalOldPrice", books.stream().mapToInt(Book::getPriceOld).sum());
            model.addAttribute("booksIds", books.stream().map(x -> String.valueOf(x.getId())).collect(Collectors.joining(",")));
        }
    }
}
