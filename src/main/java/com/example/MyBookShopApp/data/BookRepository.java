package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {

//    List<Book> findBooksByAuthor_FirstName(String name);
//
//    @Query("from Book")
//    List<Book> customFindAllBooks();
//
//    @Query("SELECT b FROM Book b JOIN b.book2AuthorEntities a WHERE a.author.firstName LIKE %?1%")
//    List<Book> findBooksByAuthorFirstName(String keyword);
}
