package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.entity.Clients;
import com.techpixe.ehr.repository.ClientsRepository;
import com.techpixe.ehr.service.ClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClientsServiceImpl implements ClientsService {
    @Autowired
    public UserServiceImpl userServiceImpl;
    @Autowired
    private ClientsRepository clientsRepository;

    @Override
    public Clients saveClient(Clients clients) {
        String password = userServiceImpl.generatePassword();
        System.out.println(password);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        clients.setFullName(clients.getFullName());
        clients.setEmail(clients.getEmail());
        clients.setMobileNumber(clients.getMobileNumber());
        clients.setRole(clients.getRole());
        clients.setPassword(bCryptPasswordEncoder.encode(password));
        clients.setCreatedAt(LocalDate.now());
        return clientsRepository.save(clients);

    }
}
