package com.techpixe.ehr.controller;

import com.techpixe.ehr.entity.AddPayHeadsToEmployee;
import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.repository.EmployeeTableRepository;
import com.techpixe.ehr.service.AddPayHeadsToEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addPayHeadsToEmployee")
public class AddPayHeadsToEmployeeController {

    @Autowired
    private AddPayHeadsToEmployeeService addPayHeadsToEmployeeService;

    @Autowired
    private EmployeeTableRepository employeeTableRepository;

    @PostMapping("/employee/{empId}")
    public ResponseEntity<AddPayHeadsToEmployee> createAddPayHeadsToEmployee(@PathVariable Long empId,
                                                                             @RequestBody AddPayHeadsToEmployee addPayHeadsToEmployee) {
        EmployeeTable employeeTable = employeeTableRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException(empId + "is not present"));

        addPayHeadsToEmployee.setEmployeeTable(employeeTable);
        addPayHeadsToEmployee.setEmpCode(employeeTable.getEmpCode());
        addPayHeadsToEmployee.setEmpName(employeeTable.getClients().getFullName());
        AddPayHeadsToEmployee createdAddPayHeadsToEmployee = addPayHeadsToEmployeeService
                .createAddPayHeadsToEmployee(addPayHeadsToEmployee);
        return ResponseEntity.ok(createdAddPayHeadsToEmployee);
    }



    @PostMapping("/employeeData/{empId}")
    public ResponseEntity<List<AddPayHeadsToEmployee>> createAddPayHeadsToEmployee(
            @PathVariable Long empId,
            @RequestBody List<AddPayHeadsToEmployee> addPayHeadsToEmployees) {

        EmployeeTable employeeTable = employeeTableRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException(empId + " is not present"));

        for (AddPayHeadsToEmployee addPayHeadsToEmployee : addPayHeadsToEmployees) {
            addPayHeadsToEmployee.setEmployeeTable(employeeTable);
            addPayHeadsToEmployee.setEmpCode(employeeTable.getEmpCode());
            addPayHeadsToEmployee.setEmpName(employeeTable.getClients().getFullName());
            addPayHeadsToEmployeeService.createAddPayHeadsToEmployee(addPayHeadsToEmployee);
        }

        return ResponseEntity.ok(addPayHeadsToEmployees);
    }


    @GetMapping
    public List<AddPayHeadsToEmployee> getAllAddPayHeadsToEmployee() {
        return addPayHeadsToEmployeeService.getAllAddPayHeadsToEmployee();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddPayHeadsToEmployee> getAddPayHeadsToEmployeeById(@PathVariable Long id) {
        return addPayHeadsToEmployeeService.getAddPayHeadsToEmployeeById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddPayHeadsToEmployee> updateAddPayHeadsToEmployee(@PathVariable Long id,
                                                                             @RequestBody AddPayHeadsToEmployee addPayHeadsToEmployeeDetails) {
        AddPayHeadsToEmployee updatedAddPayHeadsToEmployee = addPayHeadsToEmployeeService
                .updateAddPayHeadsToEmployee(id, addPayHeadsToEmployeeDetails);
        return ResponseEntity.ok(updatedAddPayHeadsToEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddPayHeadsToEmployee(@PathVariable Long id) {
        addPayHeadsToEmployeeService.deleteAddPayHeadsToEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
