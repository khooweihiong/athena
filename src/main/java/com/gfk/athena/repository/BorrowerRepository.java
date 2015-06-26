package com.gfk.athena.repository;

import com.gfk.athena.domain.Borrower;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Borrower entity.
 */
public interface BorrowerRepository extends JpaRepository<Borrower,Long> {

    @Query("select borrower from Borrower borrower where borrower.borrower.login = ?#{principal.username}")
    List<Borrower> findAllForCurrentUser();

}
