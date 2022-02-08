package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.book.tag.Tag;
import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final BookRepository bookRepository;

    public TagService(TagRepository tagRepository, BookRepository bookRepository) {
        this.tagRepository = tagRepository;
        this.bookRepository = bookRepository;
    }

    public List<Tag> getTags() {

        List<Tag> allTags = tagRepository.findAll();

        double booksCount = bookRepository.findAll().size();
        Tag mostPopularTag = new Tag();
        mostPopularTag.setBooks(new ArrayList<>());
        double ratio;

        for (Tag tag : allTags) {
            if (tag.getBooks().size() > mostPopularTag.getBooks().size()) {
                mostPopularTag = tag;
            }
        }

        double maxSimpleWeight = mostPopularTag.getBooks().size() / booksCount;
        ratio = 1 / maxSimpleWeight;

        for (Tag tag : allTags) {
            double simpleWeight = tag.getBooks().size() / booksCount;
            double normalizedWeight = ratio * simpleWeight;
            tag.setWeight(normalizedWeight);
        }

        return allTags;
    }

    public Tag getTagBySlug(String slug) {
        return tagRepository.findBySlug(slug);
    }


}
