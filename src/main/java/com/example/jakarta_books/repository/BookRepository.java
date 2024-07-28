package com.example.jakarta_books.repository;

import com.example.jakarta_books.entity.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

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
                .createQuery("SELECT b FROM Book b", Book.class)
                .getResultList();
    }

    public Book findById(long id) {
        return entityManager.find(Book.class, id);
    }


    @Transactional
    public void deleteBook(long id) {
        var book = findById(id);
        if (book == null) {
            throw new EntityNotFoundException();
        }
        entityManager.remove(book);
    }

    @Transactional
    public Book updateBook(Book book) {
        return entityManager.merge(book);
    }

}
