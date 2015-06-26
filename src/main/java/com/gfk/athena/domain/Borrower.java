package com.gfk.athena.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gfk.athena.domain.util.CustomDateTimeDeserializer;
import com.gfk.athena.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Borrower.
 */
@Entity
@Table(name = "BORROWER")
public class Borrower implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "taken_date")
    private DateTime takenDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "return_date")
    private DateTime returnDate;

    @ManyToOne
    private User borrower;

    @ManyToOne
    private Book bookId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getTakenDate() {
        return takenDate;
    }

    public void setTakenDate(DateTime takenDate) {
        this.takenDate = takenDate;
    }

    public DateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(DateTime returnDate) {
        this.returnDate = returnDate;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User user) {
        this.borrower = user;
    }

    public Book getBookId() {
        return bookId;
    }

    public void setBookId(Book book) {
        this.bookId = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Borrower borrower = (Borrower) o;

        if ( ! Objects.equals(id, borrower.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Borrower{" +
                "id=" + id +
                ", takenDate='" + takenDate + "'" +
                ", returnDate='" + returnDate + "'" +
                '}';
    }
}
