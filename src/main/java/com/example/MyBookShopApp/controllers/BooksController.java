package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.ResourceStorage;
import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.rating.BookRating;
import com.example.MyBookShopApp.service.BookRatingService;
import com.example.MyBookShopApp.service.BookReviewService;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BooksController extends DefaultController {

    private final BookService bookService;
    private final BookRatingService bookRatingService;
    private final BookReviewService bookReviewService;
    private final ResourceStorage storage;

    @Autowired
    public BooksController(BookService bookService, BookRatingService bookRatingService, BookReviewService bookReviewService, ResourceStorage storage) {
        this.bookService = bookService;
        this.bookRatingService = bookRatingService;
        this.bookReviewService = bookReviewService;
        this.storage = storage;
    }

    @GetMapping("/{slug}")
    public String bookPage(@PathVariable String slug, Model model) {
        Book book = bookService.getBookBySlug(slug);
        List<BookRating> rates = bookRatingService.getRatesDistribution(book.getId());

        model.addAttribute("slugBook", book);
        model.addAttribute("rating", book.getRating());
        model.addAttribute("ratesCount", book.getRatings().size());
        model.addAttribute("tagsCount", book.getTags().size());
        model.addAttribute("reviews", book.getBookReviews());
        model.addAttribute("oneStar",  rates.stream().filter(x->x.getValue() == 1).count());
        model.addAttribute("twoStars",  rates.stream().filter(x->x.getValue() == 2).count());
        model.addAttribute("threeStars",  rates.stream().filter(x->x.getValue() == 3).count());
        model.addAttribute("fourStars",  rates.stream().filter(x->x.getValue() == 4).count());
        model.addAttribute("fiveStars",  rates.stream().filter(x->x.getValue() == 5).count());

        model.addAttribute("signedIn", true); // let's say user is here

        return "/books/slug";
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("slug") String slug) throws IOException {
        String savePath = storage.saveNewBookImage(file, slug);
        Book bookToUpdate = bookService.getBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookService.save(bookToUpdate); //save new path in db here

        return ("redirect:/books/"+slug);
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException{
        Path path = storage.getBookFilePath(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: " + path);

        MediaType mediaType = storage.getBookFileMime(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: " + mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file data len: " + data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

}
