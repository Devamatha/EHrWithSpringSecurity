package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.dto.EmployeePayHeadDTO;
import com.techpixe.ehr.entity.*;
import com.techpixe.ehr.repository.EmployeeTableRepository;
import com.techpixe.ehr.repository.UserRepository;
import com.techpixe.ehr.service.EmployeeTableService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeTableServiceImpl implements EmployeeTableService {

    @Autowired
    private EmployeeTableRepository employeeTableRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Override
    public EmployeeTable createEmployee(String fullName, Date dob, String gender, String maritalStatus,
                                        String nationality, String address, String city, String state, String country, String emailId,
                                        Long contactNo, String identification, String idNumber, String employeeType, LocalDate joiningDate,
                                        String bloodGroup, MultipartFile photograph, Long userId) throws IOException {

        HR user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(userId + " is not found"));

        EmployeeTable employee = new EmployeeTable();
        String empCode = generateEmpCode();
        String password = generatePassword();
        employee.setEmpCode(empCode);
        employee.setDob(dob);
        employee.setGender(gender);
        employee.setMaritalStatus(maritalStatus);
        employee.setNationality(nationality);
        employee.setAddress(address);
        employee.setCity(city);
        employee.setState(state);
        employee.setCountry(country);
        employee.setIdentification(identification);
        employee.setIdNumber(idNumber);
        employee.setEmployeeType(employeeType);
        employee.setJoiningDate(joiningDate);
        employee.setBloodGroup(bloodGroup);
        employee.setPhotograph(photograph.getBytes());
        employee.setUser(user_Id);
        employee.setActive(true);
        userServiceImpl.sendmail(fullName, emailId, contactNo, password);
        return employeeTableRepository.save(employee);
    }

    public String generateEmpCode() {
        String letters = RandomStringUtils.randomAlphabetic(2).toUpperCase();
        String digits = RandomStringUtils.randomNumeric(2);
        return letters + digits;
    }

    private String generatePassword() {
        String letters = RandomStringUtils.randomAlphabetic(4).toUpperCase();
        String digits = RandomStringUtils.randomNumeric(4);
        return letters + digits;
    }

    public List<EmployeeTable> getAllEmployees() {
        return employeeTableRepository.findAll();
    }

    public Optional<EmployeeTable> getEmployeeById(Long id) {
        return employeeTableRepository.findById(id);
    }

    @Override
    public Optional<EmployeeTable> updateEmployee(Long id, String fullName, String empCode, Date dob, String gender,
                                                  String maritalStatus, String nationality, String address, String city, String state, String country,
                                                  String emailId, Long contactNo, String identification, String idNumber, String employeeType,
                                                  LocalDate joiningDate, String bloodGroup, String password, String role, String designation,
                                                  String department, Long panNo, String bankName, Long bankAccountNo, String iFSCCode, String pfAccountNo,
                                                  Integer totalDays, Integer presentDays, byte[] photograph, HR user) {

        return employeeTableRepository.findById(id).map(employee -> {

            if (empCode != null)
                employee.setEmpCode(empCode);
            if (dob != null)
                employee.setDob(dob);
            if (gender != null)
                employee.setGender(gender);
            if (maritalStatus != null)
                employee.setMaritalStatus(maritalStatus);
            if (nationality != null)
                employee.setNationality(nationality);
            if (address != null)
                employee.setAddress(address);
            if (city != null)
                employee.setCity(city);
            if (state != null)
                employee.setState(state);
            if (country != null)
                employee.setCountry(country);

            if (identification != null)
                employee.setIdentification(identification);
            if (idNumber != null)
                employee.setIdNumber(idNumber);
            if (employeeType != null)
                employee.setEmployeeType(employeeType);
            if (joiningDate != null)
                employee.setJoiningDate(joiningDate);
            if (bloodGroup != null)
                employee.setBloodGroup(bloodGroup);

            if (designation != null)
                employee.setDesignation(designation);
            if (department != null)
                employee.setDepartment(department);
            if (panNo != null)
                employee.setPanNo(panNo);
            if (bankName != null)
                employee.setBankName(bankName);
            if (bankAccountNo != null)
                employee.setBankAccountNo(bankAccountNo);
            if (iFSCCode != null)
                employee.setIFSCCode(iFSCCode);
            if (pfAccountNo != null)
                employee.setPfAccountNo(pfAccountNo);
            if (totalDays != null)
                employee.setTotalDays(totalDays);
            if (presentDays != null)
                employee.setPresentDays(presentDays);
            if (photograph != null)
                employee.setPhotograph(photograph);
            if (user != null)
                employee.setUser(user);
            return employeeTableRepository.save(employee);
        });
    }

    public void deleteEmployee(Long id) {
        employeeTableRepository.deleteById(id);
    }

    @Override
    public List<AddPayHeadsToEmployee> getAddPayHeadsToEmployeeByEmployeeId(Long id) {
        return employeeTableRepository.findAddPayHeadsToEmployeeByUserId(id);
    }

    @Override
    public EmployeePayHeadDTO getEmployeeWithPayHeads(Long id) {
        List<Object[]> results = employeeTableRepository.findEmployeeWithPayHeads(id);
        EmployeePayHeadDTO dto = new EmployeePayHeadDTO();

        if (!results.isEmpty()) {
            Object[] result = results.get(0); // Get the first result
            dto.setEmployeeFullName((String) result[0]);
            dto.setCompanyName((String) result[1]);
            dto.setAuthorizedCompanyName((String) result[2]);
            dto.setAddress((String) result[3]);
            dto.setLogo((byte[]) result[4]);
            List<AddPayHeadsToEmployee> payHeads = new ArrayList<>();
            for (Object[] row : results) {
                AddPayHeadsToEmployee payHead = (AddPayHeadsToEmployee) row[5];
                payHeads.add(new AddPayHeadsToEmployee(payHead.getId(), payHead.getSelectedPayHead(),
                        payHead.getSelectedPayHeadType(), payHead.getPayHeadAmount(), payHead.getEmpCode(),
                        payHead.getEmpName(), payHead.getEmployeeTable())); // Populate only pay head ID for now
            }
            dto.setPayHeads(payHeads);
        }

        return dto;
    }

    @Override
    public List<Attendance> getAttendanceByEmployee(Long id) {

        return employeeTableRepository.findAttendanceById(id);
    }

    @Override
    public List<LeaveApprovalTable> getLeaveapprovalTableByEmployeeId(Long id) {
        return employeeTableRepository.findLeaveapprovalTableById(id);
    }

}
