package com.switchfully.contacts;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ContactsRepository {
    //private final List<Contact> contactList = new ArrayList<>();
    private final Map<String, Contact> contactList = new HashMap<>();

    public List<Contact> getAllContacts(){
        return List.copyOf(contactList.values());
        //return List.copyOf(contactList);
    }

    public Optional<Contact> getContactById(String id) {
        return Optional.ofNullable(contactList.get(id));
    }

    public Contact addContact(Contact newContact) {
        contactList.put(newContact.getId(), newContact);
        return contactList.get(newContact.getId());
    }
}
