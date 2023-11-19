package com.cbfacademy.apiassessment.blog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogControllerTests {
    
    @LocalServerPort
	private int port;

	private URL baseURL;
	
    @Autowired
	private BlogController controller;

    @Autowired
	private TestRestTemplate restTemplate;

	@BeforeEach
	public void setUp() throws Exception {
		this.baseURL = new URL("http://localhost:" + port + "/blog");
	}

    @Test
	void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

    @Test
	@DisplayName("/ping endpoint returns expected response")
	public void ping_ExpectedResponse() {
		ResponseEntity<String> response = restTemplate.getForEntity(baseURL.toString() + "/ping", String.class);

		assertEquals(200, response.getStatusCode().value());
		assertTrue(response.getBody().startsWith("Service running successfully"));
	}

    @Test
	@DisplayName("/blogs endpoint returns list of blogs")
	public void blogShouldReturnListOfBlogs() {
        
		ResponseEntity<Blog[]> response = restTemplate.getForEntity("/blog/blogs", Blog[].class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), assertInstanceOf(Blog[].class, response.getBody()));
	}

    @Test
    @DisplayName("should create a new blog")
	public void shouldCreateANewBlog() {
		Blog blog = new Blog(19871L, Instant.parse("2023-04-01T23:59:10.511Z"), "test", "Test Blog Title", "Test Sample Text");
		
        ResponseEntity<Blog> createResponse = restTemplate.postForEntity("/blog/", blog, Blog.class);
		Blog createdBlog = createResponse.getBody();
        
		ResponseEntity<Blog> response = restTemplate.getForEntity(baseURL.toString()+"/"+ createdBlog.getId(), Blog.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(blog.getId(), response.getBody().getId());
        assertEquals(blog.getAuthor(), response.getBody().getAuthor());       
        assertThat(createResponse.getHeaders().getLocation().toString()).isEqualTo(baseURL.toString()+"/"+ createdBlog.getId());
    }
	

    @Test
    @DisplayName("should update a blog by id")
	public void shouldUpdateAnExistingBlog() {
		Blog blog = new Blog(11100L, Instant.parse("2023-04-01T23:59:10.511Z"), "testtest", "Test Blog Title", "Test Sample Text");
		ResponseEntity<Blog> createResponse = restTemplate.postForEntity(baseURL.toString()+"/", blog, Blog.class);

		Blog createdBlog = createResponse.getBody();
		createdBlog.setAuthor("UpdatedAuthor");
        createdBlog.setTitle("UpdatedTitle");

		restTemplate.put(baseURL.toString()+"/" + createdBlog.getId(), createdBlog);
		ResponseEntity<Blog> response = restTemplate.getForEntity(baseURL.toString()+"/" + createdBlog.getId(), Blog.class);

		assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("UpdatedAuthor", response.getBody().getAuthor());
        assertEquals("UpdatedTitle", response.getBody().getTitle());
	}

	@Test
	@DisplayName("should delete a blog with specified id")
	public void shouldDeleteAnExistingBlog() {
		Blog blog = new Blog(15000L, Instant.parse("2023-04-01T23:59:10.511Z"), "test", "Test Blog Title", "Test Sample Text");
		ResponseEntity<Blog> createResponse = restTemplate.postForEntity(baseURL.toString()+"/", blog, Blog.class);

		Blog createdBlog = createResponse.getBody();

		restTemplate.delete(baseURL.toString()+"/" + createdBlog.getId());

		ResponseEntity<Blog> response = restTemplate.getForEntity(baseURL.toString()+"/" + createdBlog.getId(), Blog.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createdBlog);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	@DisplayName("should return not found when id not in repository")
	public void invalidIdShouldReturnNotFound() {
		ResponseEntity<Blog> response = restTemplate.getForEntity(baseURL.toString()+"/2023", Blog.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
}
