package com.example.MyBookShopApp.data.book;

import com.example.MyBookShopApp.data.author.Author;
import com.example.MyBookShopApp.data.book.file.FileDownloadEntity;
import com.example.MyBookShopApp.data.book.links.Book2AuthorEntity;
import com.example.MyBookShopApp.data.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.book.tag.Tag;
import com.example.MyBookShopApp.data.genre.Genre;
import com.example.MyBookShopApp.data.payments.BalanceTransactionEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String title;

    @Column(columnDefinition = "INT NOT NULL")
    private String price;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 0")
    private int discount;

    @Column(columnDefinition = "VARCHAR(255)")
    private String image;

    @Column(name = "is_bestseller", columnDefinition = "SMALLINT NOT NULL")
    private int isBestseller;

    @Column(name = "pub_date", columnDefinition = "DATE NOT NULL")
    private LocalDate pubDate;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @Column(columnDefinition = "INTEGER NOT NULL DEFAULT 0")
    private int bought;
    @Column(name = "added_to_cart", columnDefinition = "INTEGER NOT NULL DEFAULT 0")
    private int addedToCart;
    @Column(columnDefinition = "INTEGER NOT NULL DEFAULT 0")
    private int postponed;

    @OneToMany(mappedBy = "bookId")
    private List<Book2AuthorEntity> book2AuthorEntities;

    @OneToMany(mappedBy = "bookId")
    private List<Book2UserEntity> book2UserEntities;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "book_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private List<FileDownloadEntity> fileDownloads;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "book_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private List<BalanceTransactionEntity> balanceTransactions;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "book_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private List<BookReviewEntity> bookReviews;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "book2genre",
            joinColumns = @JoinColumn(name = "book_id", columnDefinition = "INT NOT NULL"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", columnDefinition = "INT NOT NULL"))
    private List<Genre> genres;

    @JsonIgnore
    @ManyToMany(mappedBy = "books")
    private List<Tag> tags;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIsBestseller() {
        return isBestseller;
    }

    public void setIsBestseller(int isBestseller) {
        this.isBestseller = isBestseller;
    }

    public LocalDate getPubDate() {
        return pubDate;
    }

    public void setPubDate(LocalDate pubDate) {
        this.pubDate = pubDate;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Book2AuthorEntity> getBook2AuthorEntities() {
        return book2AuthorEntities;
    }

    public void setBook2AuthorEntities(List<Book2AuthorEntity> book2AuthorEntities) {
        this.book2AuthorEntities = book2AuthorEntities;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public List<BookReviewEntity> getBookReviews() {
        return bookReviews;
    }

    public void setBookReviews(List<BookReviewEntity> bookReviews) {
        this.bookReviews = bookReviews;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Book2UserEntity> getBook2UserEntities() {
        return book2UserEntities;
    }

    public void setBook2UserEntities(List<Book2UserEntity> book2UserEntities) {
        this.book2UserEntities = book2UserEntities;
    }

    public int getBought() {
        return bought;
    }

    public void setBought(int bought) {
        this.bought = bought;
    }

    public int getAddedToCart() {
        return addedToCart;
    }

    public void setAddedToCart(int addedToCart) {
        this.addedToCart = addedToCart;
    }

    public int getPostponed() {
        return postponed;
    }

    public void setPostponed(int postponed) {
        this.postponed = postponed;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", book2AuthorEntities="  +
                ", book2UserEntities=" +
                ", fileDownloads=" + fileDownloads +
                ", balanceTransactions=" + balanceTransactions +
                ", bookReviews=" + bookReviews +
                ", genres=" + genres +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", discount=" + discount +
                ", image='" + image + '\'' +
                ", isBestseller=" + isBestseller +
                ", pubDate=" + pubDate +
                ", slug='" + slug + '\'' +
                '}';
    }
}
