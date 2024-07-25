package com.example.jakarta_books.repository;

import com.example.jakarta_books.entity.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class BookRepository {

    @PersistenceContext(unitName = "mysql")
    EntityManager entityManager;

    @Transactional
    public Book createBook(Book book) {
        entityManager.persist(book);
        return book;
    }

    public List<Book> readAllBooks() {
        return entityManager
                .createQuery("SELECT b FROM Book b", Book.class)
                .getResultList();
    }

    public Book findBookById(long id) {
        return entityManager.find(Book.class, id);
    }

    @Transactional
    public Book updateBook(Book updatedBook) {
        return entityManager.merge(updatedBook);
    }

    @Transactional
    public void deleteBook(long id) {
        var book = findBookById(id);
        entityManager.remove(book);
    }
}
