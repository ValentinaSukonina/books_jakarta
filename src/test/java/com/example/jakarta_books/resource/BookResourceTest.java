package com.example.jakarta_books.resource;

import com.example.jakarta_books.dto.BookDto;
import com.example.jakarta_books.dto.Books;
import com.example.jakarta_books.entity.Book;
import com.example.jakarta_books.service.BookService;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.jboss.resteasy.spi.Dispatcher;
import java.util.List;
import java.util.UUID;

import static org.jboss.resteasy.mock.MockHttpRequest.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookResourceTest {
    private static final UUID id = UUID.randomUUID();
    @Mock
    BookService bookService;
    Dispatcher dispatcher;



    @BeforeEach
    void setUp() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        var resource = new BookResource(bookService);
        dispatcher.getRegistry().addSingletonResource(resource);
    }

    @Test
    @DisplayName("Return status 200 when call findAllBooks with GET")
    void ReturnStatus200whenCallFindAllBooksWithGet() throws Exception {
        when(bookService.allBooks()).thenReturn(new Books(List.of()));

        MockHttpRequest request = get("/books");
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        assertEquals(200, response.getStatus());
        assertEquals("{\"books\":[]}", response.getContentAsString());
    }

    @Test
    @DisplayName("Return status 200 when successfully finding book by id with GET")
    void ReturnStatus200whenSuccessfullyCallFindBookByIdWithGet() throws Exception {
        BookDto bookDto = new BookDto(id, "1984", "George Orwell", 1949, "Dystopian");

        when(bookService.oneBook(id)).thenReturn(Response.ok(bookDto).build().readEntity(BookDto.class));
        MockHttpRequest request = get("/books/" + id);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Return status 404 when id not found with GET")
    void ReturnStatus404whenIdNotFoundWithGet() throws Exception {

        when(bookService.oneBook(id)).thenThrow(new NotFoundException());
        MockHttpRequest request = MockHttpRequest.get("/books/" + id);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        assertEquals(404, response.getStatus());
    }

@Test
@DisplayName("Return status 201 when created with POST")
void returnStatus201WhenCreated() throws Exception {

    String requestBody = "{\"title\":\"Moby-Dick\",\"author\":\"Herman Melville\", \"publicationYear\":1851,\"genre\":\"Adventure\"}";
    Book createdBook = new Book(); // This should be a representation of the created book if necessary
    when(bookService.addBook(any())).thenReturn(createdBook);

    MockHttpRequest request = MockHttpRequest.post("/books")
            .content(requestBody.getBytes())
            .header("Content-Type", "application/json");
    MockHttpResponse response = new MockHttpResponse();
    dispatcher.invoke(request, response);

    assertEquals(201, response.getStatus());
}

    @Test
    @DisplayName("Return status 204 when DELETE is successful")
    void ReturnStatus204whenDeleteSuccessful() throws Exception {

        MockHttpRequest request = MockHttpRequest.delete("/books/" + id);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        assertEquals(204, response.getStatus());
    }

    @Test
    @DisplayName("Return status 404 when DELETE used for none existing id")
    void ReturnStatus404whenDeleteNoneExistingId() throws Exception {
        when(bookService.deleteBook(id)).thenThrow(new NotFoundException());
        MockHttpRequest request = MockHttpRequest.delete("/books/" + id);
        MockHttpResponse response = new MockHttpResponse();
        dispatcher.invoke(request, response);

        assertEquals(404, response.getStatus());
    }

@Test
@DisplayName("Return status 200 when update person with PUT")
void returnStatus200WhenUpdatePersonWithPut() throws Exception {
    BookDto bookDto = new BookDto(id, "1984", "George Orwell", 1949, "Dystopian");

    when(bookService.updateBook(any(UUID.class), any(BookDto.class)))
            .thenReturn(bookDto);

    String requestBody = "{\"title\":\"Moby-Dick\",\"author\":\"H. Melville\", \"publicationYear\":1851,\"genre\":\"Adventure\"}";

    MockHttpRequest request = MockHttpRequest.put("/books/" + id);

    request.contentType(MediaType.APPLICATION_JSON);
    request.content(requestBody.getBytes())
            .header("Content-Type", "application/json");
    MockHttpResponse response = new MockHttpResponse();
    dispatcher.invoke(request, response);

    assertEquals(200, response.getStatus());
}

@Test
@DisplayName("Return status 404 when updating entity that does not exist")
void return404WhenUpdatingEntityThatDoesNotExist() throws Exception {
    when(bookService.updateBook(any(UUID.class), any(BookDto.class)))
            .thenThrow(new NotFoundException());

    MockHttpRequest request = MockHttpRequest.put("/books/" + id);
    request.contentType(MediaType.APPLICATION_JSON);
    String requestBody = "{\"title\":\"Moby-Dick\",\"author\":\"H. Melville\", \"publicationYear\":1851,\"genre\":\"Adventure\"}";
    request.content(requestBody.getBytes())
            .header("Content-Type", "application/json");
    MockHttpResponse response = new MockHttpResponse();
    dispatcher.invoke(request, response);

    assertEquals(404, response.getStatus());
}

    @Test
    @DisplayName("Constructor creates objects of MovieResource class")
    void constructorCreatesObjectsOfMovieResourceClass() {
        BookResource bookResource = new BookResource();
        assertEquals(BookResource.class, bookResource.getClass());
    }


}