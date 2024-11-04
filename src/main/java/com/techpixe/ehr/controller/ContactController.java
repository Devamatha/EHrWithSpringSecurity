package com.techpixe.ehr.controller;


import com.techpixe.ehr.entity.ContactUs;
import com.techpixe.ehr.service.ContactUsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactUsService contactService;

    @GetMapping
    public List<ContactUs> getAllContacts() {
        return contactService.getAllContactUss();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactUs> getContactById(@PathVariable Long id) {
        Optional<ContactUs> contact = contactService.getContactUsById(id);
        return contact.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ContactUs createContact(@RequestBody ContactUs contact) {
        return contactService.saveContactUs(contact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactUs> updateContact(@PathVariable Long id, @RequestBody ContactUs updatedContact) {
        Optional<ContactUs> contact = contactService.getContactUsById(id);
        if (contact.isPresent()) {
            return ResponseEntity.ok(contactService.updateContactUs(id, updatedContact));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        if (contactService.getContactUsById(id).isPresent()) {
            contactService.deleteContactUs(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
