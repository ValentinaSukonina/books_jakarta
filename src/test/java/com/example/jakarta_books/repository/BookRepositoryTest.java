package com.example.jakarta_books.repository;

import com.example.jakarta_books.entity.Book;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.persistence.TypedQuery;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class BookRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private BookRepository bookRepository;

    @Test
    @DisplayName("createNew method should persist a Book object")
    void createNewTest() {
        var book = new Book();
        bookRepository.createNew(book);
        verify(entityManager, times(1)).persist(book);
    }

    @Test
    void whenFindAll_thenReturnBookList() {
        var book1 = new Book();
        var book2 = new Book();
        List<Book> bookList = Arrays.asList(book1, book2);
        TypedQuery<Book> typedQueryMock = mock(TypedQuery.class);

        when(entityManager.createQuery("select b from Book b", Book.class)).thenReturn(typedQueryMock);
        when(typedQueryMock.getResultList()).thenReturn(bookList);

        List<Book> actualBooks = bookRepository.findAll();

        assertEquals(bookList.size(), actualBooks.size(), "The size of the returned movie list should match");
        assertEquals(bookList, actualBooks, "The returned movie list should match the expected list");

        verify(entityManager, times(1)).createQuery("select b from Book b", Book.class);
        verify(typedQueryMock, times(1)).getResultList();
    }

    @Test
    void whenFindById_thenReturnBook() {
        var book = new Book();
        UUID id = UUID.randomUUID();
        book.setId(id);

        when(entityManager.find(Book.class, id)).thenReturn(book);

        Book foundBook = bookRepository.findById(id);
        assertEquals(book, foundBook);
        assertEquals(book.getId(), foundBook.getId());
        verify(entityManager, times(1)).find(Book.class, id);
    }

    @Test
    void deleteBookRemoveExistingBook() {
        var book = new Book();
        UUID id = UUID.randomUUID();
        book.setId(id);

        when(entityManager.find(Book.class, id)).thenReturn(book);
        bookRepository.deleteBook(id);
        verify(entityManager, times(1)).remove(book);
    }

    @Test
    void deleteBookThrowNotFoundExceptionWhenBookDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(entityManager.find(Book.class, id)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> bookRepository.deleteBook(id));
    }

    @Test
    void updateBookTest() {
        var book = new Book();
        UUID id = UUID.randomUUID();
        book.setId(id);
        book.setId(id);
        book.setTitle("title");
        book.setAuthor("author");
        book.setPublicationYear(1999);
        book.setGenre("genre");

        Book updatedBook = new Book();
        updatedBook.setId(id);
        updatedBook.setTitle("Updated title");
        updatedBook.setAuthor("Updated author");
        updatedBook.setPublicationYear(2000);
        updatedBook.setGenre("Updated genre");

        when(entityManager.merge(updatedBook)).thenReturn(updatedBook);

        Book result = bookRepository.updateBook(updatedBook);

        assertEquals(updatedBook, result);
        verify(entityManager, times(1)).merge(updatedBook);

        assertAll(
                () -> assertEquals("Updated title", result.getTitle()),
                () -> assertEquals("Updated author", result.getAuthor()),
                () -> assertEquals(2000, result.getPublicationYear()),
                () -> assertEquals("Updated genre", result.getGenre())
         );
       }
}
