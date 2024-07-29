package com.example.jakarta_books.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
class BookTest {
    private Book book;
    @BeforeEach
    void setUp() {book = new Book();}

    @Test
    @DisplayName("getId returns correct id")
    void getIdReturnsCorrectId() {
        UUID id = UUID.randomUUID();
        book.setId(id);
        assertEquals(id, book.getId());
    }

    @Test
    @DisplayName("getTitle returns correct title")
    void getIdReturnsNull() {
        book.setTitle("title");
        assertEquals ("title", book.getTitle());
    }

    @Test
    @DisplayName("getAuthor returns correct author")
    void getAuthorReturnsCorrectAuthor() {
        book.setAuthor("author");
        assertEquals ("author", book.getAuthor());
    }

    @Test
    @DisplayName("getPublicationYear returns correct publicationYear")
    void getPublicationYearReturnsCorrectPublicationYear() {
        book.setPublicationYear(1999);
        assertEquals (1999, book.getPublicationYear());
    }

    @Test
    @DisplayName("getGenre returns correct genre")
    void getGenreReturnsCorrectGenre() {
        book.setGenre("genre");
        assertEquals ("genre", book.getGenre());
    }

    @Test
    void testHashCodeEquals(){
        EqualsVerifier.simple().forClass(Book.class).suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY).suppress(Warning.SURROGATE_KEY).verify();
    }

}