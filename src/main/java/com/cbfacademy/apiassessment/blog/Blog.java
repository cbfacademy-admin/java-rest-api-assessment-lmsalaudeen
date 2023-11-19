package com.cbfacademy.apiassessment.blog;

import java.time.Instant;

import org.springframework.lang.NonNull;

public class Blog {

    private Long id;

    private Instant date;
    
    private String author;
    
    private String title;

    private String content;

    public Blog(@NonNull Long id, Instant date, String author, String title, String content) {
        if (id == null) {
            throw new NullPointerException("id is marked non-null but is null");
        }
        this.id = id;
        this.date = date;
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public Blog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "{" +
                "id:'" + id + '\'' +
                ", date:'" + date + '\'' +
                ", author:'" + author + '\'' +
                ", title:'" + title + '\'' +
                ", content:'" + content +
                '}';
    }
}
