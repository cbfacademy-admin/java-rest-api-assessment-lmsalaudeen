package com.cbfacademy.apiassessment.blog;

import java.util.ArrayList;
import java.util.List;

import com.cbfacademy.apiassessment.algorithm.Algorithm;

public class BlogService {
    
    List<Blog> blogs;

    public BlogService(List<Blog> blogs){
        this.blogs = blogs;
    }

    public List<Blog> findAllBlogs() {
        return this.blogs;
    }

    public Blog findBlog(Long id) {
        for (Blog blog: this.blogs)
            if(blog.getId().equals(id)){ 
                return blog;
            }
        return null;
    }

    public Blog createBlog(Blog blogToCreate) {
        Blog newBlog = new Blog(
                    blogToCreate.getId(),
                    blogToCreate.getDate(),
                    blogToCreate.getAuthor(),
                    blogToCreate.getTitle(),
                    blogToCreate.getContent());
        for (Blog blog: this.blogs) {
            if(blog.getId().equals(newBlog.getId())){
                throw new IdPresentException("Duplicate Ids not allowed");
            }
    }
        this.blogs.add(newBlog);
        return newBlog;
}

    public Blog updateBlog(Long id, Blog updatedBlog) {
        // Only update a blog if it can be found first.
        for (Blog blog: this.blogs) {
            if(blog.getId().equals(id)){ 
                blog.setId(updatedBlog.getId());
                blog.setDate(updatedBlog.getDate());
                blog.setAuthor(updatedBlog.getAuthor());
                blog.setTitle(updatedBlog.getTitle());
                blog.setContent(updatedBlog.getContent());
                return blog;
            }
        }
        return null;
    }

    public String deleteBlog(Long id) {
         for (Blog blog: this.blogs) {
            if(blog.getId().equals(id)){ 
                this.blogs.remove(blog);
                return "deleted";
            }
        } return null;
    }

    public List<Blog> findBlogsWithKeyword(String keyword) {
        return Algorithm.searchBlogContent(this.blogs, keyword);
    }

    public List<Blog> findBlogsByAuthor(String authorName) {
        return Algorithm.searchBlogAuthor(this.blogs, authorName);
    }

    public List<Blog> orderByOldest() {
        Algorithm.mergeSortBlog(this.blogs);
        return this.blogs;
    }

    public List<Blog> orderByLatest() {
        Algorithm.mergeSortBlog(this.blogs);
        var reversedBlog = new ArrayList<Blog>();
        for (int i = this.blogs.size()-1; i>0; i--) {
            reversedBlog.add(this.blogs.get(i));
        }
        return reversedBlog;
    }
}
