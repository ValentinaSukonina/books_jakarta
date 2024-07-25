package com.example.jakarta_books.dto;

import com.example.jakarta_books.entity.Book;

public record BookDto(String title, String author, int publicationYear, String genre) {
    public BookDto map(Book book) {
        return new BookDto(book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getGenre());
    }

    public static Book map(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.title);
        book.setAuthor(bookDto.author);
        book.setPublicationYear(bookDto.publicationYear);
        book.setGenre(bookDto.genre);
        return book;
    }

}
