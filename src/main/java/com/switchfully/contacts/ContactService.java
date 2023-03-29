package com.switchfully.contacts;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    private final ContactsRepository repository;

    public ContactService(ContactsRepository repository) {
        this.repository = repository;
    }

    public List<Contact> getAllContacts() {
        return repository.getAllContacts();
    }

    public Contact getContactById(String id) {
        return repository.getContactById(id).orElseThrow(UserNotFoundException::new);
    }

    public Contact addContact(Contact newContact) {
        return repository.addContact(newContact);
    }
}
