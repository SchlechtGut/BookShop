package com.example.MyBookShopApp.data.book.review;

import com.example.MyBookShopApp.data.user.UserEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "book_review")
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    @Column(name = "book_id", columnDefinition = "INT NOT NULL")
    private Integer bookId;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "INT NOT NULL")
    private UserEntity user;

    @OneToMany(mappedBy = "reviewId")
    @Where(clause = "value = 1")
    private Set<BookReviewLikeEntity> likeSet;

    @OneToMany(mappedBy = "reviewId")
    @Where(clause = "value = -1")
    private Set<BookReviewLikeEntity> dislikeSet;

    public BookReviewEntity(LocalDateTime time, String text, Integer bookId, UserEntity user) {
        this.time = time;
        this.text = text;
        this.bookId = bookId;
        this.user = user;
    }

    public BookReviewEntity() {
    }

    public Integer getStars() {
        if (getLikesCount() == 0) {
            return 0;
        }
        return Math.toIntExact(getLikesCount() / (getLikesCount() + getDislikesCount()));
    }

    public long getRating() {
        return getLikesCount() - getDislikesCount();
    }

    public long getLikesCount() {
        return likeSet.size();
    }

    public long getDislikesCount() {
        return dislikeSet.size();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity userId) {
        this.user = userId;
    }

    public Set<BookReviewLikeEntity> getLikeSet() {
        return likeSet;
    }

    public void setLikeSet(Set<BookReviewLikeEntity> likedAndDislikes) {
        this.likeSet = likedAndDislikes;
    }

    public Set<BookReviewLikeEntity> getDislikeSet() {
        return dislikeSet;
    }

    public void setDislikeSet(Set<BookReviewLikeEntity> dislikeSet) {
        this.dislikeSet = dislikeSet;
    }
}
