package com.example.MyBookShopApp.data.book;

import com.example.MyBookShopApp.data.book.file.FileDownloadEntity;
import com.example.MyBookShopApp.data.book.links.Book2AuthorEntity;
import com.example.MyBookShopApp.data.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.genre.GenreEntity;
import com.example.MyBookShopApp.data.payments.BalanceTransactionEntity;

import javax.persistence.*;
import java.util.Date;
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

    @Column(columnDefinition = "TINYINT NOT NULL")
    private int isBestseller;

    @Column(columnDefinition = "DATE NOT NULL")
    private Date pubDate;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String slug;

    @OneToMany(mappedBy = "book")
    private List<Book2AuthorEntity> book2AuthorEntities;

    @OneToMany(mappedBy = "book")
    private List<Book2UserEntity> book2UserEntities;

    @OneToMany
    @JoinColumn(name = "book_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private List<FileDownloadEntity> fileDownloads;

    @OneToMany
    @JoinColumn(name = "book_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private List<BalanceTransactionEntity> balanceTransactions;

    @OneToMany
    @JoinColumn(name = "book_id", referencedColumnName = "id", columnDefinition = "INT NOT NULL")
    private List<BookReviewEntity> bookReviews;

    @ManyToMany
    @JoinTable(name = "book2genre",
            joinColumns = @JoinColumn(name = "book_id", columnDefinition = "INT NOT NULL"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", columnDefinition = "INT NOT NULL"))
    private List<GenreEntity> genres;

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

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
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

    public List<GenreEntity> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreEntity> genres) {
        this.genres = genres;
    }

    public List<Book2UserEntity> getBook2UserEntities() {
        return book2UserEntities;
    }

    public void setBook2UserEntities(List<Book2UserEntity> book2UserEntities) {
        this.book2UserEntities = book2UserEntities;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", book2AuthorEntities=" + book2AuthorEntities +
                ", book2UserEntities=" + book2UserEntities +
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
