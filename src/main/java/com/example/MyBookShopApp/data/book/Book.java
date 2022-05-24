package com.example.MyBookShopApp.data.book;

import com.example.MyBookShopApp.data.book.file.BookFile;
import com.example.MyBookShopApp.data.book.file.FileDownloadEntity;
import com.example.MyBookShopApp.data.book.links.Book2AuthorEntity;
import com.example.MyBookShopApp.data.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.book.rating.BookRating;
import com.example.MyBookShopApp.data.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.book.tag.Tag;
import com.example.MyBookShopApp.data.genre.Genre;
import com.example.MyBookShopApp.data.payments.BalanceTransactionEntity;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String title;

    @Column(name = "price", columnDefinition = "INT NOT NULL")
    @JsonProperty("price")
    private Integer priceOld;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 0")
    private Integer discount;

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
    @OneToMany(mappedBy = "bookId")
    private List<FileDownloadEntity> fileDownloads;

    @JsonIgnore
    @OneToMany(mappedBy = "bookId")
    private List<BalanceTransactionEntity> balanceTransactions;

    @JsonIgnore
    @OneToMany(mappedBy = "bookId")
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

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<BookFile> bookFileList = new ArrayList<>();

    @OneToMany(mappedBy = "bookId")
    @JsonIgnore
    private Set<BookRating> ratings;

    @JsonIgnore
    public Integer getRating() {
        return Math.toIntExact(Math.round(ratings.stream().map(BookRating::getValue).mapToInt(Integer::intValue).average().orElse(0)));
    }

    @JsonGetter
    public Integer getDiscountPrice() {
        return priceOld - priceOld * discount / 100;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
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

    public Integer getPriceOld() {
        return priceOld;
    }

    public void setPriceOld(Integer price) {
        this.priceOld = price;
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

    public List<BookFile> getBookFileList() {
        return bookFileList;
    }

    public void setBookFileList(List<BookFile> bookFileList) {
        this.bookFileList = bookFileList;
    }

    public Set<BookRating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<BookRating> ratings) {
        this.ratings = ratings;
    }
}
