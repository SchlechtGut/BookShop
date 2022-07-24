package com.example.MyBookShopApp.data.book.links;

import com.example.MyBookShopApp.data.enums.Book2UserType;
import com.example.MyBookShopApp.data.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "book2user")
public class Book2UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    @Column(name = "type_id", columnDefinition = "INT NOT NULL")
    private Book2UserType type;

    @Column(name = "book_id", columnDefinition = "INT NOT NULL")
    private int bookId;

//    @Column(name = "user_id", columnDefinition = "INT NOT NULL")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Book2UserType getType() {
        return type;
    }

    public void setType(Book2UserType type) {
        this.type = type;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book2UserEntity that = (Book2UserEntity) o;
        return bookId == that.bookId && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, bookId);
    }
}
