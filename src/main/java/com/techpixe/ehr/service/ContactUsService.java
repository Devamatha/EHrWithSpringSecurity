package com.techpixe.ehr.service;

import com.techpixe.ehr.entity.ContactUs;

import java.util.List;
import java.util.Optional;

public interface ContactUsService {
    ContactUs saveContactUs(ContactUs contactUs);

    Optional<ContactUs> getContactUsById(Long id);

    List<ContactUs> getAllContactUss();

    void deleteContactUs(Long id);

    ContactUs updateContactUs(Long id, ContactUs updatedContactUs);

}
