package com.example.jakarta_books.service;

import com.example.jakarta_books.dto.BookDto;
import com.example.jakarta_books.entity.Book;
import com.example.jakarta_books.repository.BookRepository;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private final UUID testId = UUID.randomUUID();

    private final Book testBook = new Book();

    private final BookDto testBookDto = new BookDto(testId, "Test Title", "Test Author", 1999, "Test Genre");

    @BeforeEach
    void setUp() {
        testBook.setId(testId);
        testBook.setTitle("Test Title");
        testBook.setAuthor("Test Author");
        testBook.setPublicationYear(1999);
        testBook.setGenre("Test Genre");
    }

    @Test
    @DisplayName("findAll returns list of books")
    void findAllShouldReturnListOfBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(testBook));
        var result = bookService.allBooks();

        assertEquals(1, result.books().size());
        assertEquals("Test Title", result.books().get(0).title());

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById should return book")
    void oneBookShouldReturnBook() {
        when(bookRepository.findById(testId)).thenReturn(testBook);
        var result = bookService.oneBook(testId);

        assertEquals("Test Title", result.title());
        verify(bookRepository, times(1)).findById(testId);
    }

    @Test
    @DisplayName("oneBook should throw NotFoundException for nonexistent book")
    void oneBookShouldThrowNotFoundExceptionForNonexistentBook() {
        when(bookRepository.findById(testId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> bookService.oneBook(testId));
    }

    @Test
    @DisplayName("addBook should add book")
    void addBookShouldAddBook() {
        when(bookRepository.createNew(testBook)).thenReturn(testBook);

        var result = bookService.addBook(testBookDto);

        assertEquals("Test Title", result.getTitle());
        verify(bookRepository, times(1)).createNew(testBook);
    }

    @Test
    @DisplayName("deleteBook should delete book")
    void deleteBookShouldDeleteBook() {
        when(bookRepository.findById(testId)).thenReturn(testBook);
        bookService.deleteBook(testId);

        verify(bookRepository, times(1)).deleteById(testId);
        verify(bookRepository).deleteById(testId);
    }

    @Test
    @DisplayName("deleteBook should throw NotFoundException for nonexistent book")
    void deleteBookShouldThrowNotFoundExceptionForNonexistentBook() {
        when(bookRepository.findById(testId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> bookService.deleteBook(testId));
    }

    @Test
    @DisplayName("updateBook should update book")
    void updateBookShouldUpdateBook() {
        when(bookRepository.findById(testId)).thenReturn(testBook);
        when(bookRepository.updateBook(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0, Book.class));

        BookDto updatedDto = new BookDto(testId, "Updated Title", "Updated Author", 2000, "Updated Genre");
        var result = bookService.updateBook(testId, updatedDto);

        assertEquals("Updated Title", result.title());
        assertEquals("Updated Author", result.author());
        assertEquals(2000, result.publicationYear());
        assertEquals("Updated Genre", result.genre());
        
        verify(bookRepository, times(1)).updateBook(any(Book.class));
        verify(bookRepository).updateBook(any(Book.class));
    }
    @Test
    void shouldInstantiateService() {
        BookService service = new BookService();
        assertNotNull(service);
    }
}

