package com.example.MyBookShopApp.data.DTO;

import java.util.ArrayList;
import java.util.List;

public class GenreDto {

    private Integer id;

    private Integer parentId;

    private String slug;

    private String name;

    private List<GenreDto> children;

    private int booksCount;

    private boolean embedded;

    public GenreDto() {
        children = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GenreDto> getChildren() {
        return children;
    }

    public void setChildren(List<GenreDto> children) {
        this.children = children;
    }

    public int getBooksCount() {
        return booksCount;
    }

    public void setBooksCount(int booksCount) {
        this.booksCount = booksCount;
    }

    public boolean isEmbedded() {
        return embedded;
    }

    public void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }

    @Override
    public String toString() {
        return "GenreDto{" +
                "name='" + name + '\'' +
                ", children=" + children +
                '}';
    }
}
