package com.example.jakarta_books.resource;

import com.example.jakarta_books.dto.BookDto;
import com.example.jakarta_books.dto.Books;
import com.example.jakarta_books.service.BookService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    @Context
    UriInfo uriInfo;

    private BookService bookService;

    public BookResource() {
    }

    @Inject
    public BookResource(BookService bookRepository) {
        this.bookService = bookRepository;
    }

    @GET
    public Books getAllBooks() {
        return bookService.allBooks();
    }

    @GET
    @Path("/{id}")
    public BookDto getBookById(@PathParam("id") long id) {
        return bookService.oneBook(id);
    }

    @POST
    public Response addBook(@Valid BookDto bookDto) {
        var book = bookService.addBook(bookDto);
        return Response.created(
                URI.create(uriInfo.getAbsolutePath().toString() + "/" + book.getId())).build();
    }

//    @DELETE
//    @Path("/{id}")
//    public Response deleteBook(@PathParam("id") long id) {
//        try {
//            bookService.deleteBook(id);
//        } catch (EntityNotFoundException e) {
//            // Return 404 Not Found if the book does not exist
//            return Response.status(Response.Status.NOT_FOUND)
//                    .entity("Book with ID " + id + " not found")
//                    .build();
//        }
//        // Return 204 No Content if the book was successfully deleted
//        return Response.noContent().build();
//    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") long id) {
        bookService.deleteBook(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public BookDto updateBook(@PathParam("id") @NotNull @Valid long id, @Valid BookDto bookDto) {
        return bookService.updateBook(id, bookDto);
    }

}
