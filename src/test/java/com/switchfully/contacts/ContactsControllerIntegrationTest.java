package com.switchfully.contacts;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ContactsControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ContactsRepository repository;

    @Test
    void whenThereIsOneUserInTheRepository_thenICanRetrieveThisUserById() {
                // GIVEN
        Contact newContact = new Contact("1", "William", "BeCentral");
        repository.addContact(newContact);

                // WHEN
        Contact contact = RestAssured
                .given()
                .contentType(ContentType.JSON)
                //.header(new Header("Authorization", "Basic username:password"))
                .auth().preemptive().basic("username", "password")
                .log().all()
                .when()
                .port(port)
                .get("/contacts/" + newContact.getId()) // http://localhost:???/contacts/1
                // THEN
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value()) // status 200
                .extract()
                .as(Contact.class); // Get a contact from the system

        Assertions.assertThat(contact).isEqualTo(newContact);
    }

    @Test
    void whenTheRepositoryIsEmpty_thenIReceiveA404WhenRequestingAUserById() {
        RestAssured
                // GIVEN
                .given()
                .contentType(ContentType.JSON)
                // WHEN
                .when()
                .port(port)
                .get("/contacts/1") // http://localhost:???/contacts/100
                // THEN
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    @Test
    void whenIPostAContact_thenTheRepositoryContainsThisContact() {
        Contact newContact = new Contact("1", "William", "BeCentral");
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(newContact)
                .when()
                .port(port)
                .post("/contacts") // http://localhost:???/contacts
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());

        Assertions.assertThat(repository.getAllContacts()).contains(newContact);
    }

    @Test
    void whenTheRepositoryContains2Contact_thenICanRetrieveThemViaTheAPI() {
        Contact newContact1 = new Contact("1", "William", "BeCentral");
        Contact newContact2 = new Contact("2", "Christoph", "BeCentral");
        repository.addContact(newContact1);
        repository.addContact(newContact2);

        List<Contact> list = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .get("/contacts") // http://localhost:???/contacts
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .jsonPath()
                .getList(".", Contact.class);

        Assertions.assertThat(list).containsExactlyInAnyOrder(newContact1, newContact2);
    }
}