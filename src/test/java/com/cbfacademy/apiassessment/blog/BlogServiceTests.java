package com.cbfacademy.apiassessment.blog;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName(value = "The BlogService class should: ")
public class BlogServiceTests {
    
    // items from blogRepositoryStub.json
    List<Blog> blogStub = JSONHandler.read("src/test/java/com/cbfacademy/apiassessment/blog/blogRepositoryStub.json");
    
    BlogService blogService = new BlogService(blogStub);

    @Test
    @DisplayName("return a list of blogs")
    public void findAllBlogsTest() {
        List<Blog> blogList = blogService.findAllBlogs();

        assertTrue(blogList.size() > 0);
    }

    @Test
    @DisplayName("return a blog by id")
    public void findBlogByIdTest() {
        Long id = 10L;
        Blog blog = blogService.findBlog(id);

        assertInstanceOf(Blog.class, blog);
        assertTrue(blog.getId()==10L);
    }

    @Test
    @DisplayName("create a blog")
    public void createBlogTest() {
        Blog newBlogStub = new Blog(110L, getInstant(), "NotLatifah", "Test title", "Test Content");
                
        blogService.createBlog(newBlogStub);

        assertInstanceOf(Blog.class, newBlogStub);
       
    }

    @Test
    @DisplayName("update a blog")
    public void updateBlogByIdTest() {
        // update something and check
        Blog newBlogStub = new Blog(10L, getInstant(), "UpdatedAuthor", "UpdatedTitle", "Test Content");

        blogService.updateBlog(10L, newBlogStub);

        assertTrue(newBlogStub.getAuthor()=="UpdatedAuthor");
        assertTrue(newBlogStub.getTitle()=="UpdatedTitle");
                
    }

    @Test
    @DisplayName("delete a blog")
    public void deleteBlogByIdTest() {
        // check size or id
        Long id = 10L;
        String delete = blogService.deleteBlog(id);

        assertTrue(blogStub.size() ==2);

        assertThat(delete == "deleted");
        
    }

    private Instant getInstant() {
		// Get the current date and time in the system's default time zone
		ZoneId systemTimeZone = ZoneId.systemDefault();
		ZonedDateTime currentDateTime = ZonedDateTime.now(systemTimeZone);

		// Convert to Instant
		Instant instant = currentDateTime.toInstant();

		return instant;
	}

}

