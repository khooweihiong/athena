package com.gfk.athena.repository;

import com.gfk.athena.domain.Book;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Book entity.
 */
public interface BookRepository extends JpaRepository<Book,Long> {

    @Query("select book from Book book where book.owner.login = ?#{principal.username}")
    List<Book> findAllForCurrentUser();

}
