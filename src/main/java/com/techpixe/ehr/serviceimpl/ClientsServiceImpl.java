package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.dto.RegisterDto;
import com.techpixe.ehr.entity.Clients;
import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.entity.HR;
import com.techpixe.ehr.repository.ClientsRepository;
import com.techpixe.ehr.repository.EmployeeTableRepository;
import com.techpixe.ehr.repository.UserRepository;
import com.techpixe.ehr.service.ClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
public class ClientsServiceImpl implements ClientsService {
    @Autowired
    public UserServiceImpl userServiceImpl;
    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private EmployeeTableServiceImpl employeeTableServiceImpl;

    @Autowired
    private EmployeeTableRepository employeeTableRepository;

    @Override
    public RegisterDto registerEmployee(RegisterDto registerDto, Long id) {
        try {
            String password = userServiceImpl.generatePassword();
            HR hr = new HR();
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            Clients clients = new Clients();
            clients.setFullName(registerDto.getFullName());
            clients.setEmail(registerDto.getEmail());
            clients.setMobileNumber(registerDto.getMobileNumber());
            clients.setRole(registerDto.getRole());
            clients.setPassword(bCryptPasswordEncoder.encode(password));
            clients.setCreatedAt(LocalDate.now());
            clientsRepository.save(clients);

            if (registerDto.getRole().equals("ROLE_EMPLOYEE") && id != null) {
                Clients clientId = clientsRepository.findById(clients.getId()).orElseThrow(() -> new RuntimeException("Client not found"));
                HR hrId = userRepository.findById(id).orElseThrow(() -> new RuntimeException("HR not found"));
                EmployeeTable employee = new EmployeeTable();
                employee.setClients(clientId);
                employee.setUser(hrId);
                String empCode = employeeTableServiceImpl.generateEmpCode();
                employee.setEmpCode(empCode);
                System.err.println(registerDto.getDob());
                employee.setDob(registerDto.getDob());
                employee.setGender(registerDto.getGender());
                employee.setMaritalStatus(registerDto.getMaritalStatus());
                employee.setNationality(registerDto.getNationality());
                employee.setAddress(registerDto.getAddress());
                employee.setCity(registerDto.getCity());
                employee.setState(registerDto.getState());
                employee.setCountry(registerDto.getCountry());
                employee.setIdentification(registerDto.getIdentification());
                employee.setIdNumber(registerDto.getIdNumber());
                employee.setEmployeeType(registerDto.getEmployeeType());
                employee.setJoiningDate(registerDto.getJoiningDate());
                employee.setBloodGroup(registerDto.getBloodGroup());
                //  employee.setPhotograph(photograph.getBytes());
                employee.setActive(true);
                userServiceImpl.sendmail(registerDto.getFullName(), registerDto.getEmail(), registerDto.getMobileNumber(), password);
                employeeTableRepository.save(employee);


            }

            return registerDto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RegisterDto registerClients(RegisterDto registerDto) {
        try {
            String password = userServiceImpl.generatePassword();
            HR hr = new HR();
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            Clients clients = new Clients();
            clients.setFullName(registerDto.getFullName());
            clients.setEmail(registerDto.getEmail());
            clients.setMobileNumber(registerDto.getMobileNumber());
            clients.setRole(registerDto.getRole());
            clients.setPassword(bCryptPasswordEncoder.encode(password));
            clients.setCreatedAt(LocalDate.now());
            clientsRepository.save(clients);
            if (registerDto.getRole().equals("ROLE_HR")) {
                Clients ClientId = clientsRepository.findById(clients.getId()).orElseThrow(() -> new RuntimeException("Client not found"));
                hr.setAuthorizedCompanyName(registerDto.getAuthorizedCompanyName());
                hr.setCompanyName(registerDto.getCompanyName());
                hr.setAddress(registerDto.getAddress());
                hr.setClients(ClientId);
                //hr.setLogo(logo.getBytes());
                hr.setActive(true);
                userServiceImpl.sendmail(registerDto.getFullName(), registerDto.getEmail(), registerDto.getMobileNumber(), password);
                userRepository.save(hr);
            }

            if (registerDto.getRole().equals("ROLE_ADMIN")) {
                userServiceImpl.sendmail(registerDto.getFullName(), registerDto.getEmail(), registerDto.getMobileNumber(), password);

            }
            return registerDto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
