package com.gfk.athena.web.rest;

import com.gfk.athena.Application;
import com.gfk.athena.domain.Book;
import com.gfk.athena.repository.BookRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BookResource REST controller.
 *
 * @see BookResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BookResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_AUTHOR = "SAMPLE_TEXT";
    private static final String UPDATED_AUTHOR = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_PUBLISHER = "SAMPLE_TEXT";
    private static final String UPDATED_PUBLISHER = "UPDATED_TEXT";

    private static final Boolean DEFAULT_AVAILABLE = false;
    private static final Boolean UPDATED_AVAILABLE = true;

    private static final DateTime DEFAULT_CREATED = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATED = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATED_STR = dateTimeFormatter.print(DEFAULT_CREATED);

    private static final DateTime DEFAULT_UPDATED = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_UPDATED = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_UPDATED_STR = dateTimeFormatter.print(DEFAULT_UPDATED);

    @Inject
    private BookRepository bookRepository;

    private MockMvc restBookMockMvc;

    private Book book;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookResource bookResource = new BookResource();
        ReflectionTestUtils.setField(bookResource, "bookRepository", bookRepository);
        this.restBookMockMvc = MockMvcBuilders.standaloneSetup(bookResource).build();
    }

    @Before
    public void initTest() {
        book = new Book();
        book.setTitle(DEFAULT_TITLE);
        book.setAuthor(DEFAULT_AUTHOR);
        book.setDescription(DEFAULT_DESCRIPTION);
        book.setPublisher(DEFAULT_PUBLISHER);
        book.setAvailable(DEFAULT_AVAILABLE);
        book.setCreated(DEFAULT_CREATED);
        book.setUpdated(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    public void createBook() throws Exception {
        int databaseSizeBeforeCreate = bookRepository.findAll().size();

        // Create the Book
        restBookMockMvc.perform(post("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(book)))
                .andExpect(status().isCreated());

        // Validate the Book in the database
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeCreate + 1);
        Book testBook = books.get(books.size() - 1);
        assertThat(testBook.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBook.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testBook.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBook.getPublisher()).isEqualTo(DEFAULT_PUBLISHER);
        assertThat(testBook.getAvailable()).isEqualTo(DEFAULT_AVAILABLE);
        assertThat(testBook.getCreated().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATED);
        assertThat(testBook.getUpdated().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(bookRepository.findAll()).hasSize(0);
        // set the field null
        book.setTitle(null);

        // Create the Book, which fails.
        restBookMockMvc.perform(post("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(book)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(0);
    }

    @Test
    @Transactional
    public void checkAuthorIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(bookRepository.findAll()).hasSize(0);
        // set the field null
        book.setAuthor(null);

        // Create the Book, which fails.
        restBookMockMvc.perform(post("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(book)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(0);
    }

    @Test
    @Transactional
    public void checkPublisherIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(bookRepository.findAll()).hasSize(0);
        // set the field null
        book.setPublisher(null);

        // Create the Book, which fails.
        restBookMockMvc.perform(post("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(book)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(0);
    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(bookRepository.findAll()).hasSize(0);
        // set the field null
        book.setCreated(null);

        // Create the Book, which fails.
        restBookMockMvc.perform(post("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(book)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllBooks() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the books
        restBookMockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(book.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].publisher").value(hasItem(DEFAULT_PUBLISHER.toString())))
                .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())))
                .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED_STR)))
                .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED_STR)));
    }

    @Test
    @Transactional
    public void getBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get the book
        restBookMockMvc.perform(get("/api/books/{id}", book.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(book.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.publisher").value(DEFAULT_PUBLISHER.toString()))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE.booleanValue()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED_STR))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingBook() throws Exception {
        // Get the book
        restBookMockMvc.perform(get("/api/books/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

		int databaseSizeBeforeUpdate = bookRepository.findAll().size();

        // Update the book
        book.setTitle(UPDATED_TITLE);
        book.setAuthor(UPDATED_AUTHOR);
        book.setDescription(UPDATED_DESCRIPTION);
        book.setPublisher(UPDATED_PUBLISHER);
        book.setAvailable(UPDATED_AVAILABLE);
        book.setCreated(UPDATED_CREATED);
        book.setUpdated(UPDATED_UPDATED);
        restBookMockMvc.perform(put("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(book)))
                .andExpect(status().isOk());

        // Validate the Book in the database
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeUpdate);
        Book testBook = books.get(books.size() - 1);
        assertThat(testBook.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBook.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testBook.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBook.getPublisher()).isEqualTo(UPDATED_PUBLISHER);
        assertThat(testBook.getAvailable()).isEqualTo(UPDATED_AVAILABLE);
        assertThat(testBook.getCreated().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATED);
        assertThat(testBook.getUpdated().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void deleteBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

		int databaseSizeBeforeDelete = bookRepository.findAll().size();

        // Get the book
        restBookMockMvc.perform(delete("/api/books/{id}", book.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeDelete - 1);
    }
}
