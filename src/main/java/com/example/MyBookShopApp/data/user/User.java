package com.example.MyBookShopApp.data.user;

import com.example.MyBookShopApp.data.book.file.FileDownloadEntity;
import com.example.MyBookShopApp.data.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.data.book.review.MessageEntity;
import com.example.MyBookShopApp.data.payments.BalanceTransactionEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @Column(name = "reg_time", columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime regTime;

    @Column(columnDefinition = "INT NOT NULL")
    private int balance;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    private String email;
    private String phone;
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Book2UserEntity> book2UserEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "userId")
    private List<UserContactEntity> userContacts;

    @JsonIgnore
    @OneToMany(mappedBy = "userId")
    private List<FileDownloadEntity> fileDownloads;

    @JsonIgnore
    @OneToMany(mappedBy = "userId")
    private List<BalanceTransactionEntity> balanceTransactions;

    @JsonIgnore
    @OneToMany(mappedBy = "userId")
    private List<MessageEntity> messages;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<BookReviewEntity> bookReviews;

    @JsonIgnore
    @OneToMany(mappedBy = "userId")
    private List<BookReviewLikeEntity> bookReviewLikes;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public LocalDateTime getRegTime() {
        return regTime;
    }

    public void setRegTime(LocalDateTime regTime) {
        this.regTime = regTime;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book2UserEntity> getBook2UserEntities() {
        return book2UserEntities;
    }

    public void setBook2UserEntities(List<Book2UserEntity> book2UserEntities) {
        this.book2UserEntities = book2UserEntities;
    }

    public List<UserContactEntity> getUserContacts() {
        return userContacts;
    }

    public void setUserContacts(List<UserContactEntity> userContacts) {
        this.userContacts = userContacts;
    }

    public List<FileDownloadEntity> getFileDownloads() {
        return fileDownloads;
    }

    public void setFileDownloads(List<FileDownloadEntity> fileDownloads) {
        this.fileDownloads = fileDownloads;
    }

    public List<BalanceTransactionEntity> getBalanceTransactions() {
        return balanceTransactions;
    }

    public void setBalanceTransactions(List<BalanceTransactionEntity> balanceTransactions) {
        this.balanceTransactions = balanceTransactions;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }

    public List<BookReviewEntity> getBookReviews() {
        return bookReviews;
    }

    public void setBookReviews(List<BookReviewEntity> bookReviews) {
        this.bookReviews = bookReviews;
    }

    public List<BookReviewLikeEntity> getBookReviewLikes() {
        return bookReviewLikes;
    }

    public void setBookReviewLikes(List<BookReviewLikeEntity> bookReviewLikes) {
        this.bookReviewLikes = bookReviewLikes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", hash='" + hash + '\'' +
                ", regTime=" + regTime +
                ", balance=" + balance +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(regTime, user.regTime) && Objects.equals(name, user.name) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regTime, name, email);
    }
}
