package com.example.MyBookShopApp.data.book;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "viewed_book")
public class ViewedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "user_id")
    private Integer userId;

    private LocalDateTime time;

    public ViewedBook(Integer bookId, Integer userId) {
        this.bookId = bookId;
        this.userId = userId;
        time = LocalDateTime.now();
    }

    public ViewedBook() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ViewedBook{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", userId=" + userId +
                ", time=" + time +
                '}';
    }
}
