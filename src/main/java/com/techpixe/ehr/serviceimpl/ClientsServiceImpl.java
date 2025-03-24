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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

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
	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("$(spring.mail.username)")
	private String fromMail;
    @Override
    public RegisterDto registerEmployee(RegisterDto registerDto, Long id) {
        try {
            String password = userServiceImpl.generatePassword();
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            Clients clients = new Clients();
            clients.setFullName(registerDto.getFullName());
            clients.setEmail(registerDto.getEmail());
            clients.setMobileNumber(registerDto.getMobileNumber());
            clients.setRole("ROLE_EMPLOYEE");
            clients.setPassword(bCryptPasswordEncoder.encode(password));
            clients.setCreatedAt(LocalDate.now());
            clientsRepository.save(clients);

            if (clients.getRole().equals("ROLE_EMPLOYEE") && id != null) {
                Clients clientId = clientsRepository.findById(clients.getId()).orElseThrow(() -> new RuntimeException("Client not found"));
                HR hrId = userRepository.findById(id).orElseThrow(() -> new RuntimeException("HR not found"));
                EmployeeTable employee = new EmployeeTable();
                employee.setClients(clientId);
                employee.setUser(hrId);
                String empCode = employeeTableServiceImpl.generateEmpCode();
                employee.setEmpCode(empCode);
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
    public Clients registerHRAndAdmin(String fullName, String email, Long mobileNumber, String role, MultipartFile logo, String companyName, String authorizedCompanyName, String address){
        try {
            String password = userServiceImpl.generatePassword();
            HR hr = new HR();
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            Clients clients = new Clients();
            clients.setFullName(fullName);
            clients.setEmail(email);
            clients.setMobileNumber(mobileNumber);
            clients.setRole(role);
            clients.setPassword(bCryptPasswordEncoder.encode(password));
            clients.setCreatedAt(LocalDate.now());
            clientsRepository.save(clients);
            if (clients.getRole().equals("ROLE_HR")) {
                Clients ClientId = clientsRepository.findById(clients.getId()).orElseThrow(() -> new RuntimeException("Client not found"));
                hr.setAuthorizedCompanyName(authorizedCompanyName);
                hr.setCompanyName(companyName);
                hr.setAddress(address);
                hr.setClients(ClientId);
                hr.setLogo(logo.getBytes());
                hr.setActive(true);
                userServiceImpl.sendmail(fullName, email, mobileNumber, password);
                userRepository.save(hr);
                System.err.println(hr.getUser_Id()+"hr.getUser_Id()");
                clients.setId(hr.getUser_Id());
            }

            if (clients.getRole().equals("ROLE_ADMIN")) {
                userServiceImpl.sendmail(fullName, email, mobileNumber, password);

            }


            return clients;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


@Override
public ResponseEntity<?> changePassword(Long Id, String password, String confirmPassword) {
	Clients user = clientsRepository.findById(Id)
			.orElseThrow(() -> new RuntimeException("Id is not present: " + Id));


	if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {

		user.setPassword(bCryptPasswordEncoder.encode(confirmPassword));
		clientsRepository.save(user);
        return ResponseEntity.ok(Collections.singletonMap("message", "Password changed successfully."));

	} else {
		throw new RuntimeException("Incorrect current password. Please try again. ");
	}
}
@Override
public ResponseEntity<?> forgotPassword(String email) {
	Optional<Clients> clients = clientsRepository.findByEmail(email);

	if (clients != null) {
		Clients user = clients.get();
		String password = generatePassword();
		user.setPassword(bCryptPasswordEncoder.encode(password));
		clientsRepository.save(user);
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(fromMail);
		simpleMailMessage.setTo(email);
		simpleMailMessage.setSubject("New Password Generation completed Successfully in Smartaihr application\n");
		simpleMailMessage.setText("Dear " + user.getFullName()
				+ "\n\nPlease check your  email and generted password  \n UserEmail  :" + email + "\n  MobileNumber :"
				+ user.getMobileNumber() + "\n New Password   :" + password   + "\n\n"
				+ "you will be required to reset the New password upon login\n\n\n if you have any question or if you would like to request a call-back,please email us at support info@techpixe.com");
		javaMailSender.send(simpleMailMessage);
		return ResponseEntity.ok(Collections.singletonMap("message", "Password send to " + email + " successfully."));
	}  else {
		throw new RuntimeException("Incorrect email password. Please try again. "+email);

	}
}


private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
private static final String DIGITS = "0123456789";

public static String generatePassword() {
	Random random = new Random();

	StringBuilder lettersBuilder = new StringBuilder();
	for (int i = 0; i < 4; i++) {
		int index = random.nextInt(LETTERS.length());
		lettersBuilder.append(LETTERS.charAt(index));
	}

	StringBuilder digitsBuilder = new StringBuilder();
	for (int i = 0; i < 4; i++) {
		int index = random.nextInt(DIGITS.length());
		digitsBuilder.append(DIGITS.charAt(index));
	}

	return lettersBuilder.toString() + digitsBuilder.toString();
}

}
