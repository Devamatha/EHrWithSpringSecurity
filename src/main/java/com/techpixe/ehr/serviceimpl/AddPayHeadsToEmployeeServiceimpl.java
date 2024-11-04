package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.entity.AddPayHeadsToEmployee;
import com.techpixe.ehr.repository.AddPayHeadsToEmployeeRepository;
import com.techpixe.ehr.service.AddPayHeadsToEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddPayHeadsToEmployeeServiceimpl implements AddPayHeadsToEmployeeService {
    @Autowired
    private AddPayHeadsToEmployeeRepository addPayHeadsToEmployeeRepository;

    // Create
    public AddPayHeadsToEmployee createAddPayHeadsToEmployee(AddPayHeadsToEmployee addPayHeadsToEmployee) {
        return addPayHeadsToEmployeeRepository.save(addPayHeadsToEmployee);
    }

    // Read
    public List<AddPayHeadsToEmployee> getAllAddPayHeadsToEmployee() {
        return addPayHeadsToEmployeeRepository.findAll();
    }

    public Optional<AddPayHeadsToEmployee> getAddPayHeadsToEmployeeById(Long id) {
        return addPayHeadsToEmployeeRepository.findById(id);
    }

    // Update
    public AddPayHeadsToEmployee updateAddPayHeadsToEmployee(Long id,
                                                             AddPayHeadsToEmployee addPayHeadsToEmployeeDetails) {
        AddPayHeadsToEmployee addPayHeadsToEmployee = addPayHeadsToEmployeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AddPayHeadsToEmployee not found"));

        // Update fields
        addPayHeadsToEmployee.setSelectedPayHead(addPayHeadsToEmployeeDetails.getSelectedPayHead());
        addPayHeadsToEmployee.setSelectedPayHeadType(addPayHeadsToEmployeeDetails.getSelectedPayHeadType());
        addPayHeadsToEmployee.setPayHeadAmount(addPayHeadsToEmployeeDetails.getPayHeadAmount());
        addPayHeadsToEmployee.setEmpCode(addPayHeadsToEmployeeDetails.getEmpCode());
        addPayHeadsToEmployee.setEmpName(addPayHeadsToEmployeeDetails.getEmpName());
        addPayHeadsToEmployee.setEmployeeTable(addPayHeadsToEmployeeDetails.getEmployeeTable());

        return addPayHeadsToEmployeeRepository.save(addPayHeadsToEmployee);
    }

    // Delete
    public void deleteAddPayHeadsToEmployee(Long id) {
        addPayHeadsToEmployeeRepository.deleteById(id);
    }
}
