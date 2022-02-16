package com.example.MyBookShopApp.data.book.file;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.enums.BookFileType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "book_file")
public class BookFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String path;

    @Column(name = "type_id", columnDefinition = "INT NOT NULL")
    private int typeId;

    @ManyToOne
    @JoinColumn(name = "book_id",referencedColumnName = "id")
    @JsonIgnore
    private Book book;

    public String getBookFileExtensionString() {
        return BookFileType.getExtensionStringByTypeID(typeId);
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
