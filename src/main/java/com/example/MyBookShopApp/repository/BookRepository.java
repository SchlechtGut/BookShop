package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
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

    Page<Book> findBooksByPubDateBetweenOrderByPubDateDesc(LocalDate from, LocalDate to, Pageable pageable);

    Page<Book> findBooksByPubDateAfterOrderByPubDateDesc(LocalDate from, Pageable pageable);

    Page<Book> findBooksByPubDateBeforeOrderByPubDateDesc(LocalDate pubDate, Pageable pageable);

    Page<Book> findBooksByOrderByPubDateDesc(Pageable pageable);

    @Query(value = "select * , bought + 0.7 * added_to_cart + 0.4 * postponed as \"popularity\" from books", nativeQuery = true)
    Page<Book> findPopularBooks(Pageable nextPage);

    @Query(value = "WITH viewed AS (select *, " +
                                        "(select count(*) " +
                                        "from viewed_book " +
                                        "where book_id = books.id) " +
                                    "from books) " +
            "select *, bought + 0.7 * added_to_cart + 0.4 * postponed + 0.2 * count as \"popularity\" from viewed", nativeQuery = true)
    Page<Book> testFindPopularBooks(Pageable nextPage);

    @Query(value = "SELECT * FROM books b " +
            "LEFT JOIN book2tag bt ON bt.book_id = b.id " +
            "LEFT JOIN tags t ON t.id = bt.tag_id " +
            "WHERE t.id = ?1", nativeQuery = true)
    Page<Book> findBooksByTag(Integer tagId, Pageable pageable);

    @Query(value = "SELECT * FROM books b " +
            "LEFT JOIN book2genre bg ON bg.book_id = b.id " +
            "LEFT JOIN genre g ON g.id = bg.genre_id " +
            "WHERE g.id = ?1", nativeQuery = true)
    Page<Book> findBooksByGenre(Integer genreId, Pageable pageable);

    @Query(value = "SELECT * FROM books b " +
            "LEFT JOIN book2author ba ON ba.book_id = b.id " +
            "LEFT JOIN authors a ON a.id = ba.author_id " +
            "WHERE a.id = ?1", nativeQuery = true)
    Page<Book> findBooksByAuthor(Integer authorId, Pageable pageable);

    @Query(value = "SELECT * FROM books b " +
            "LEFT JOIN book2author ba ON ba.book_id = b.id " +
            "LEFT JOIN authors a ON a.id = ba.author_id " +
            "WHERE a.id = ?1 LIMIT 10", nativeQuery = true)
    List<Book> findSeveralBooksByAuthorId(Integer authorId);

    @Query(value = "SELECT COUNT(*) FROM books b " +
            "LEFT JOIN book2author ba ON ba.book_id = b.id " +
            "LEFT JOIN authors a ON a.id = ba.author_id " +
            "WHERE a.slug = ?1", nativeQuery = true)
    int findBooksCountByAuthorSlug(String slug);

    @Query(value = "SELECT * FROM books b " +
            "LEFT JOIN book2author ba ON ba.book_id = b.id " +
            "LEFT JOIN authors a ON a.id = ba.author_id " +
            "WHERE a.slug = ?1", nativeQuery = true)
    Page<Book> findBooksByAuthorSlug(String slug, Pageable pageable);

    Book findBySlug(String slug);

    List<Book> findBooksBySlugIn(String[] slugs);

    @Query(value = "SELECT * FROM books b " +
            "LEFT JOIN book2author ba ON ba.book_id = b.id " +
            "LEFT JOIN authors a ON a.id = ba.author_id " +
            "WHERE a.name = ?1", nativeQuery = true)
    List<Book> findBooksByAuthorNameContaining(String name);

    List<Book> findBooksByTitleContainingIgnoreCase(String title);

    List<Book> findBooksByPriceOldBetween(Integer min, Integer max);

    @Query("from Book where isBestseller=1")
    List<Book> getBestsellers();

    @Query(value = "SELECT * FROM books WHERE discount = (SELECT MAX(discount) FROM books", nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();

    List<Book> findBooksByIdIn(Collection<Integer> id);

    //@Query("SELECT b FROM Book b " +
    //        "LEFT JOIN Book2AuthorEntity ba ON ba.book.id = b.id " +
    //        "LEFT JOIN Author a ON a.id = ba.author.id " +
    //        "WHERE a.name = ?1")
}
