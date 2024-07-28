package com.example.jakarta_books.dto;

import com.example.jakarta_books.entity.Book;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookDtoTest {

    private static Validator validator;

    private Book book;
    private BookDto bookDto;

    @BeforeEach
    public void setupBook() {
        book = new Book();
        book.setTitle("title");
        book.setAuthor("author");
        book.setPublicationYear(1999);
        book.setGenre("genre");

        bookDto = new BookDto(
                "title",
                "author",
                1999,
                "genre");
    }
    @BeforeAll
    static void setupValidator() {
            ValidatorFactory factory = Validation.byDefaultProvider()
                    .configure()
                    .messageInterpolator(new ParameterMessageInterpolator())
                    .buildValidatorFactory();
            validator = factory.getValidator();
        }

    @Test
    @DisplayName("Dto contains correct entity fields after mapping")
    void dtoContainsCorrectEntityFieldsAfterMapping2() {
        BookDto correctBookDto = BookDto.map(book);
        assertEquals("title", correctBookDto.title());
        assertEquals("author", correctBookDto.author());
        assertEquals(1999, correctBookDto.publicationYear());
        assertEquals("genre", correctBookDto.genre());
    }

    @Test
    @DisplayName("Entity contains correct dto field after mapping")
    void entityContainsCorrectDtoFieldAfterMapping() {
        Book correctBook = BookDto.map(bookDto);
        assertEquals("title", correctBook.getTitle());
        assertEquals("author", correctBook.getAuthor());
        assertEquals(1999, correctBook.getPublicationYear());
        assertEquals("genre", correctBook.getGenre());
    }

    @Test
    @DisplayName("Empty title throws exception")
    void emptyTitleThrowsException() {
        BookDto invalidBookDto = new BookDto(
                "",
                "author",
                1999,
                "genre");
        var violations = validator.validate(invalidBookDto);
        assertEquals(1, violations.size());
        assertEquals("must not be empty", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Negative publication year throws exception")
    void negativePublicationYearThrowsException() {
        BookDto invalidBookDto = new BookDto(
                "title",
                "author",
                -1999,
                "genre");
        var violations = validator.validate(invalidBookDto);
        assertEquals(1, violations.size());
        assertEquals("must be greater than 0", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Mapping dto returns entity class")
    void mappingDtoReturnsEntityClass() {
        var correctBook = BookDto.map(bookDto);
        assertEquals(correctBook.getClass(), Book.class);
    }

    @Test
    @DisplayName("Mapping entity returns dto class")
    void mappingEntityReturnsDtoClass() {
        var correctBookDto = BookDto.map(book);
        assertEquals(correctBookDto.getClass(), BookDto.class);
    }
}

