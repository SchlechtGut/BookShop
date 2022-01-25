package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

//    List<Book> findBooksByAuthor_FirstName(String name);
//
//    @Query("from Book")
//    List<Book> customFindAllBooks();
//
//    @Query("SELECT b FROM Book b JOIN b.book2AuthorEntities a WHERE a.author.firstName LIKE %?1%")
//    List<Book> findBooksByAuthorFirstName(String keyword);






    Page<Book> findBookByTitleContainingIgnoreCase(String bookTitle, Pageable nextPage);

    Page<Book> findBooksByPubDateAfter(LocalDate pubDate, Pageable pageable);

    Page<Book> findBooksByIsBestsellerEquals(int isBestseller, Pageable pageable);
}
