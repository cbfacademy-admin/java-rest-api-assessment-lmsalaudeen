package com.cbfacademy.apiassessment.algorithm;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.cbfacademy.apiassessment.blog.Blog;

public class AlgorithmTest {
    // sample items
    List<Blog> sampleBlog = new ArrayList<>(Arrays.asList(
                new Blog(7L, Instant.parse("2023-07-01T23:59:10.511Z"), "Latifah", "Blog Title 7", "Sample Text 7"), 
                new Blog(5L, Instant.parse("2023-05-01T23:59:20.511Z"), "Mojisola", "Blog Title 5", "Sample Text 5 with TARGET"),
                new Blog(6L, Instant.parse("2023-06-01T23:59:30.511Z"), "Salaudeen", "Blog Title 6", "Sample Text 6"),
                new Blog(3L, Instant.parse("2023-03-01T23:59:10.511Z"), "NotLatifah", "Blog Title 3", "Sample Text 4 contains tArgEt"), 
                new Blog(2L, Instant.parse("2023-02-01T23:59:20.511Z"), "NotMojisola", "Blog Title 2", "Sample Text 2 has target"),
                new Blog(1L, Instant.parse("2023-01-01T23:59:30.511Z"), "NotSalaudeen", "Blog Title 1", "Sample Text 1")
                ));

    @Test
    void testSearchBlogAuthor() {
        List<Blog> searchList = Algorithm.searchBlogAuthor(sampleBlog, "LATIFAH");

        assertNotNull(searchList);
        assertTrue(searchList.size() == 2);
    }

    @Test
    void testSearchBlogContent() {

        List<Blog> searchList = Algorithm.searchBlogContent(sampleBlog, "target");

        assertNotNull(searchList);
        assertTrue(searchList.size() == 3);
    }
}
