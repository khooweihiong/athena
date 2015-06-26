package com.gfk.athena.web.rest;

import com.gfk.athena.Application;
import com.gfk.athena.domain.Borrower;
import com.gfk.athena.repository.BorrowerRepository;

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
 * Test class for the BorrowerResource REST controller.
 *
 * @see BorrowerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BorrowerResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_TAKEN_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_TAKEN_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_TAKEN_DATE_STR = dateTimeFormatter.print(DEFAULT_TAKEN_DATE);

    private static final DateTime DEFAULT_RETURN_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_RETURN_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_RETURN_DATE_STR = dateTimeFormatter.print(DEFAULT_RETURN_DATE);

    @Inject
    private BorrowerRepository borrowerRepository;

    private MockMvc restBorrowerMockMvc;

    private Borrower borrower;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BorrowerResource borrowerResource = new BorrowerResource();
        ReflectionTestUtils.setField(borrowerResource, "borrowerRepository", borrowerRepository);
        this.restBorrowerMockMvc = MockMvcBuilders.standaloneSetup(borrowerResource).build();
    }

    @Before
    public void initTest() {
        borrower = new Borrower();
        borrower.setTakenDate(DEFAULT_TAKEN_DATE);
        borrower.setReturnDate(DEFAULT_RETURN_DATE);
    }

    @Test
    @Transactional
    public void createBorrower() throws Exception {
        int databaseSizeBeforeCreate = borrowerRepository.findAll().size();

        // Create the Borrower
        restBorrowerMockMvc.perform(post("/api/borrowers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(borrower)))
                .andExpect(status().isCreated());

        // Validate the Borrower in the database
        List<Borrower> borrowers = borrowerRepository.findAll();
        assertThat(borrowers).hasSize(databaseSizeBeforeCreate + 1);
        Borrower testBorrower = borrowers.get(borrowers.size() - 1);
        assertThat(testBorrower.getTakenDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_TAKEN_DATE);
        assertThat(testBorrower.getReturnDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_RETURN_DATE);
    }

    @Test
    @Transactional
    public void getAllBorrowers() throws Exception {
        // Initialize the database
        borrowerRepository.saveAndFlush(borrower);

        // Get all the borrowers
        restBorrowerMockMvc.perform(get("/api/borrowers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(borrower.getId().intValue())))
                .andExpect(jsonPath("$.[*].takenDate").value(hasItem(DEFAULT_TAKEN_DATE_STR)))
                .andExpect(jsonPath("$.[*].returnDate").value(hasItem(DEFAULT_RETURN_DATE_STR)));
    }

    @Test
    @Transactional
    public void getBorrower() throws Exception {
        // Initialize the database
        borrowerRepository.saveAndFlush(borrower);

        // Get the borrower
        restBorrowerMockMvc.perform(get("/api/borrowers/{id}", borrower.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(borrower.getId().intValue()))
            .andExpect(jsonPath("$.takenDate").value(DEFAULT_TAKEN_DATE_STR))
            .andExpect(jsonPath("$.returnDate").value(DEFAULT_RETURN_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingBorrower() throws Exception {
        // Get the borrower
        restBorrowerMockMvc.perform(get("/api/borrowers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBorrower() throws Exception {
        // Initialize the database
        borrowerRepository.saveAndFlush(borrower);

		int databaseSizeBeforeUpdate = borrowerRepository.findAll().size();

        // Update the borrower
        borrower.setTakenDate(UPDATED_TAKEN_DATE);
        borrower.setReturnDate(UPDATED_RETURN_DATE);
        restBorrowerMockMvc.perform(put("/api/borrowers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(borrower)))
                .andExpect(status().isOk());

        // Validate the Borrower in the database
        List<Borrower> borrowers = borrowerRepository.findAll();
        assertThat(borrowers).hasSize(databaseSizeBeforeUpdate);
        Borrower testBorrower = borrowers.get(borrowers.size() - 1);
        assertThat(testBorrower.getTakenDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_TAKEN_DATE);
        assertThat(testBorrower.getReturnDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_RETURN_DATE);
    }

    @Test
    @Transactional
    public void deleteBorrower() throws Exception {
        // Initialize the database
        borrowerRepository.saveAndFlush(borrower);

		int databaseSizeBeforeDelete = borrowerRepository.findAll().size();

        // Get the borrower
        restBorrowerMockMvc.perform(delete("/api/borrowers/{id}", borrower.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Borrower> borrowers = borrowerRepository.findAll();
        assertThat(borrowers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
