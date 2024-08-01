package com.example.jakarta_books.resource;

import com.example.jakarta_books.dto.BookDto;
import com.example.jakarta_books.dto.Books;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookIT {

    @Container
    public static ComposeContainer testContainer = new ComposeContainer(new File("src/test/resources/compose-test.yml"))
            .withExposedService("db", 3306)
            .withExposedService("wildfly", 8080, Wait.forHttp("/api/books")
                    .forStatusCode(200))
            .withLocalCompose(true);
    static String host;
    static int port;

    @BeforeAll
    static void beforeAll() {
        host = testContainer.getServiceHost("wildfly", 8080);
        port = testContainer.getServicePort("wildfly", 8080);
    }

    @BeforeEach
    void before() {
        RestAssured.baseURI = "http://" + host + "/api";
        RestAssured.port = port;
    }

    @Test
    @Order(1)
    @DisplayName("findAllBooks should return status 200 and empty list with GET")
    void findAllBooksShouldReturnListWithGet() {
        Books books = RestAssured.get("/books")
                .then()
                .statusCode(200)
                .extract()
                .as(Books.class);
        assertEquals (List.of(), books.books());
    }

    @Test
    @Order(2)
    @DisplayName("findBookById should return 200 and book with that id with GET")
    void findBookByIdShouldReturn200AndBookWithThatIdWithGet() {
        RequestSpecification request = setUpRequest("{\"title\":\"Moby-Dick\",\"author\":\"Herman Melville\", \"publicationYear\":1851,\"genre\":\"Adventure\"}");
        UUID id = getUuidFromResponse(request.post());
        BookDto bookDto = RestAssured.get("/books/" + id)
                .then()
                .statusCode(200)
                .extract()
                .as(BookDto.class);

        assertEquals(new BookDto(id, "Moby-Dick", "Herman Melville", 1851, "Adventure"), bookDto);
    }

    @Test
    @Order(3)
    @DisplayName("findBookById with invalid id should return 404")
    void findBookByIdWithInvalidIdShouldReturn404() {
        UUID invalidId = UUID.randomUUID();
        RestAssured.get("/books/" + invalidId)
                .then()
                .statusCode(404);
    }

    @Test
    @Order(4)
    @DisplayName("addBook should return 201 and findAllBooks should return list with that book")
    void addBookShouldReturnBookWithThatIdWithPost() {
        RequestSpecification request = setUpRequest("{\"title\":\"Moby-Dick\",\"author\":\"Herman Melville\", \"publicationYear\":1851,\"genre\":\"Adventure\"}");

        UUID id = getUuidFromResponse(request.post());
        request.post().then().statusCode(201);

        Books books = RestAssured.get("/books/").as(Books.class);
        assertThat(books.books()).contains(new BookDto(id, "Moby-Dick", "Herman Melville", 1851, "Adventure"));
     }

    @Test
    @Order(5)
    @DisplayName("addBook should return 201 and increase book count")
        void addBookShouldReturn201AndIncreaseBookCount() {

            int bookCount = RestAssured
                    .when()
                    .get("/books/")
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(Books.class).books().size();

            RequestSpecification request = setUpRequest("{\"title\":\"Moby-Dick\",\"author\":\"Herman Melville\", \"publicationYear\":1851,\"genre\":\"Adventure\"}");
            request.post().then().statusCode(201);

            int newBookCount = RestAssured
                    .when()
                    .get("/books/")
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(Books.class).books().size();

            assertEquals(bookCount + 1, newBookCount);
        }

    @Test
    @Order(6)
    @DisplayName("addBook with invalid fields should return 400")
    void addBookWithInvalidFieldsShouldReturn400() {
        RequestSpecification request = setUpRequest("{\"title\":\"\",\"author\":\"\", \"publicationYear\":-1851,\"genre\":\"Adventure\"}");

        Response response = request.post()
                .then()
                .statusCode(400)
                .extract().response();

        assertThat(response.asString()).contains("{\"field\":\"title\",\"violationMessage\":\"must not be blank\"}", "{\"field\":\"author\",\"violationMessage\":\"must not be empty\"}", "{\"field\":\"publicationYear\",\"violationMessage\":\"must be greater than 0\"}");
    }

    @Test
    @Order(7)
    @DisplayName("deleteBook should return 204")
    void deleteBookShouldReturn204() {
        RequestSpecification request = setUpRequest("{\"title\":\"Moby-Dick\",\"author\":\"Herman Melville\", \"publicationYear\":1851,\"genre\":\"Adventure\"}");
        UUID id = getUuidFromResponse(request.post());

        RestAssured.delete("/books/" + id).then().statusCode(204);
        }

    @Test
    @Order(8)
    @DisplayName("deleteBook should return 404 when id does not exist")
    void deleteBookShouldReturn404whenIdDoesNotExist() {
        RestAssured.delete("/books/" + UUID.randomUUID()).then().statusCode(404);

    }

    @Test
    @Order(9)
    @DisplayName("updateBook should return 200 and updated bookDto")
    void updateBookShouldReturn200AndUpdatedBookDto() {
        RequestSpecification request = setUpRequest("{\"title\":\"Moby-Dick\",\"author\":\"Herman Melville\", \"publicationYear\":1851,\"genre\":\"Adventure\"}");
        UUID id = getUuidFromResponse(request.post());

        String json = "{\"title\":\"Moby-Dick\",\"author\":\"H. Melville\", \"publicationYear\":1851,\"genre\":\"Dystopian\"}";
        BookDto bookDto = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/books/" + id)
                .then()
                .statusCode(200)
                .extract()
                .as(BookDto.class);

        assertEquals(new BookDto(id, "Moby-Dick", "H. Melville", 1851, "Dystopian"), bookDto);
    }



    @NotNull
    private static RequestSpecification setUpRequest(String jsonString) {
    RequestSpecification request = RestAssured.given();
    request.contentType(ContentType.JSON);
    request.baseUri("http://localhost:" + port + "/api/books");
    request.body(jsonString);
    return request;
    }

    @NotNull
    private static UUID getUuidFromResponse (Response response){
    String responseHeader = response.getHeader("Location");
    String[] split = responseHeader.split("/");
    return UUID.fromString(split[split.length - 1]);
    }
}


