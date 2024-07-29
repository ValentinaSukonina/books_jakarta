package com.example.jakarta_books.repository;

import com.example.jakarta_books.entity.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BookRepository {

    @PersistenceContext(unitName = "mysql")
    EntityManager entityManager;

    @Transactional
    public Book createNew(Book book) {
        entityManager.persist(book);
        return book;
    }

    public List<Book> findAll() {
        return entityManager
                .createQuery("select b from Book b", Book.class)
                .getResultList();
    }

    public Book findById(UUID id) {
        return entityManager.find(Book.class, id);
    }

    @Transactional
    public void deleteBook(UUID id) {
        var book = findById(id);
        if (book == null) {
            throw new NotFoundException();
        }
        entityManager.remove(book);
    }

    @Transactional
    public Book updateBook(Book book) {
        return entityManager.merge(book);
    }

}
