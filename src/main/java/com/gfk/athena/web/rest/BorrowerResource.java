package com.gfk.athena.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gfk.athena.domain.Borrower;
import com.gfk.athena.repository.BorrowerRepository;
import com.gfk.athena.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Borrower.
 */
@RestController
@RequestMapping("/api")
public class BorrowerResource {

    private final Logger log = LoggerFactory.getLogger(BorrowerResource.class);

    @Inject
    private BorrowerRepository borrowerRepository;

    /**
     * POST  /borrowers -> Create a new borrower.
     */
    @RequestMapping(value = "/borrowers",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Borrower borrower) throws URISyntaxException {
        log.debug("REST request to save Borrower : {}", borrower);
        if (borrower.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new borrower cannot already have an ID").build();
        }
        borrowerRepository.save(borrower);
        return ResponseEntity.created(new URI("/api/borrowers/" + borrower.getId())).build();
    }

    /**
     * PUT  /borrowers -> Updates an existing borrower.
     */
    @RequestMapping(value = "/borrowers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Borrower borrower) throws URISyntaxException {
        log.debug("REST request to update Borrower : {}", borrower);
        if (borrower.getId() == null) {
            return create(borrower);
        }
        borrowerRepository.save(borrower);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /borrowers -> get all the borrowers.
     */
    @RequestMapping(value = "/borrowers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Borrower>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Borrower> page = borrowerRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/borrowers", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /borrowers/:id -> get the "id" borrower.
     */
    @RequestMapping(value = "/borrowers/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Borrower> get(@PathVariable Long id) {
        log.debug("REST request to get Borrower : {}", id);
        return Optional.ofNullable(borrowerRepository.findOne(id))
            .map(borrower -> new ResponseEntity<>(
                borrower,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /borrowers/:id -> delete the "id" borrower.
     */
    @RequestMapping(value = "/borrowers/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Borrower : {}", id);
        borrowerRepository.delete(id);
    }
}
