package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.entity.ContactUs;
import com.techpixe.ehr.repository.ContactUsRepository;
import com.techpixe.ehr.service.ContactUsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactUsServiceImpl implements ContactUsService {

    @Autowired
    private ContactUsRepository contactUsRepository;

    @Override
    public List<ContactUs> getAllContactUss() {
        return contactUsRepository.findAll();
    }

    @Override
    public Optional<ContactUs> getContactUsById(Long id) {
        return contactUsRepository.findById(id);
    }

    @Override
    public ContactUs saveContactUs(ContactUs ContactUs) {
        return contactUsRepository.save(ContactUs);
    }

    @Override
    public void deleteContactUs(Long id) {
        contactUsRepository.deleteById(id);
    }

    @Override
    public ContactUs updateContactUs(Long id, ContactUs updatedContactUs) {
        return contactUsRepository.findById(id).map(ContactUs -> {
            ContactUs.setName(updatedContactUs.getName());
            ContactUs.setEmail(updatedContactUs.getEmail());
            ContactUs.setMessage(updatedContactUs.getMessage());
            return contactUsRepository.save(ContactUs);
        }).orElseGet(() -> {
            updatedContactUs.setId(id);
            return contactUsRepository.save(updatedContactUs);
        });
    }

}
