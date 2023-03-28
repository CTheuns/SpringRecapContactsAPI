package com.switchfully.contacts;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class ContactsController {
    private final ContactService service;

    public ContactsController(ContactService service) {
        this.service = service;
    }

    @GetMapping
    public List<Contact> getAllContacts() { // TODO: refactor to ContactDTO
        return service.getAllContacts();
    }

    @GetMapping("{id}")
    // http://localhost:8080/*id*
    public Contact getContactById(@PathVariable String id) {
        return service.getContactById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // http://localhost:8080
        // BODY: {id, name, address}
    @ResponseStatus(HttpStatus.CREATED)
    public Contact addContact(@RequestBody Contact contactToAdd) {
        return service.addContact(contactToAdd);
    }
}
