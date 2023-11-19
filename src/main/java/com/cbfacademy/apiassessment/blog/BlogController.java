package com.cbfacademy.apiassessment.blog;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/blog")
public class BlogController {

     // items from blogRepository.java
    String repositoryPath = "src/main/java/com/cbfacademy/apiassessment/repository/blogRepository.json";
    List<Blog> blogs = JSONHandler.read(repositoryPath);
   
    BlogService blogService = new BlogService(blogs);

    @Operation(hidden = true)
    @GetMapping("/ping")
	public String ping() {
		return String.format("Service running successfully " + Instant.now().toString());
	}

    @Operation(summary = "Retrieve all blogs",
        description = "Get a list of all blogs in the repository. The response is an array of blogs",
        tags = { "Read" })
    @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(array = @ArraySchema(schema = @Schema(implementation = Blog.class)), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
      
    @GetMapping("/blogs")
    public ResponseEntity<List<Blog>> findAll() {
        List<Blog> blogs = blogService.findAllBlogs();
        return ResponseEntity.ok().body(blogs);
    }

    @Operation(summary = "Retrieve a Blog by Id",
        description = "Get a Blog object by specifying its id. The response is Blog object with id, date, title, author and content.",
        tags = { "Read" })
    @ApiResponses({
      @ApiResponse(responseCode = "302", content = { @Content(schema = @Schema(implementation = Blog.class), mediaType = "application/json")}),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}")
    public ResponseEntity<Blog> find(@PathVariable("id") Long id) {
        Blog blog = blogService.findBlog(id);
        if (blog != null) {
            return new ResponseEntity<>(blog, HttpStatus.FOUND);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Create a Blog",
        description = "Create a Blog object and add it to the list of blogs in the repository.",
        tags = { "Create" })
    @ApiResponses({
      @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = Blog.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Blog> create(@RequestBody Blog blog) {
        Blog created = blogService.createBlog(blog);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.getId())
            .toUri();
        // then write back to file
        JSONHandler.saveList(blogs, repositoryPath);
        return ResponseEntity.created(location).body(created);
        
    }

    @Operation(summary = "Update a Blog",
        description = "Update an existing Blog object and update its instance in the repository.",
        tags = { "Update" })
    @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Blog.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })

    @PutMapping("/{id}") 
    public ResponseEntity<Blog> update(@PathVariable Long id, @RequestBody Blog updatedBlog){
        Blog updated = blogService.updateBlog(id, updatedBlog);
        if (updated != null) {
            // then write back to file
            JSONHandler.saveList(blogs, repositoryPath);
            return new ResponseEntity<>(updated, HttpStatus.FOUND);
        } 
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Delete a blog with a specied id",
        description = "Delete a Blog object specified by id from the list of blogs in the repository.",
        tags = { "Delete" })
    @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id){
        String deleted = blogService.deleteBlog(id);
        if (deleted == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } 
        // then write back to file
        JSONHandler.saveList(blogs, repositoryPath);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Retrieve all blogs with keyword in content",
        description = "Get a list of all blogs in the repository with the specified keyword in their content. The response is an array of blogs",
        tags = { "Read" })
    @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(array = @ArraySchema(schema = @Schema(implementation = Blog.class)), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    
    @GetMapping("/blogs/keyword={keyword}")
    public ResponseEntity<List<Blog>> findKeyword(@PathVariable("keyword") String keyword) {
        List<Blog> blogs = blogService.findBlogsWithKeyword(keyword);
        if (blogs.size() > 0) {
            return ResponseEntity.ok().body(blogs);
        } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Retrieve all blogs by a specific author",
        description = "Get a list of all blogs in the repository with the specified author. The response is an array of blogs",
        tags = { "Read" })
    @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(array = @ArraySchema(schema = @Schema(implementation = Blog.class)), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    
    @GetMapping("/blogs/author={authorName}")
    public ResponseEntity<List<Blog>> findByAuthor(@PathVariable("authorName") String authorName) {
        List<Blog> blogs = blogService.findBlogsByAuthor(authorName);
        if (blogs.size() > 0) {
            return ResponseEntity.ok().body(blogs);
        } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
    }

    @Operation(summary = "Retrieve all blogs ordered from oldest to most recent",
        description = "Get a list of all blogs in the repository ordered from oldest to most recent. The response is an array of blogs",
        tags = { "Read" })
    @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(array = @ArraySchema(schema = @Schema(implementation = Blog.class)), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    
    @GetMapping("/blogs/orderByOldest")
    public ResponseEntity<List<Blog>> orderByOldest() {
        List<Blog> blogs = blogService.orderByOldest();
        return ResponseEntity.ok().body(blogs);
    }

    @Operation(summary = "Retrieve all blogs ordered from most recent to oldest",
        description = "Get a list of all blogs in the repository ordered from most recent to oldest. The response is an array of blogs",
        tags = { "Read" })
    @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(array = @ArraySchema(schema = @Schema(implementation = Blog.class)), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    
    @GetMapping("/blogs/orderByLatest")
    public ResponseEntity<List<Blog>> orderByLatest() {
        List<Blog> blogs = blogService.orderByLatest();
        return ResponseEntity.ok().body(blogs);
    }
}
