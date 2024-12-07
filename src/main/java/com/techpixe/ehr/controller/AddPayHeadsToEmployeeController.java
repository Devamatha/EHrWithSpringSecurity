package com.techpixe.ehr.controller;

import com.techpixe.ehr.entity.AddPayHeadsToEmployee;
import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.repository.EmployeeTableRepository;
import com.techpixe.ehr.service.AddPayHeadsToEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/addPayHeadsToEmployee")
public class AddPayHeadsToEmployeeController {

	@Autowired
	private AddPayHeadsToEmployeeService addPayHeadsToEmployeeService;

	@Autowired
	private EmployeeTableRepository employeeTableRepository;

	@PostMapping("/employee/{empId}")
	public ResponseEntity<Map<String, Object>> createAddPayHeadsToEmployee(@PathVariable Long empId,
			@RequestBody AddPayHeadsToEmployee addPayHeadsToEmployee) throws Exception {
		try {
			EmployeeTable employeeTable = employeeTableRepository.findById(empId)
					.orElseThrow(() -> new RuntimeException(empId + "is not present"));

			addPayHeadsToEmployee.setEmployeeTable(employeeTable);
			addPayHeadsToEmployee.setEmpCode(employeeTable.getEmpCode());
			addPayHeadsToEmployee.setEmpName(employeeTable.getClients().getFullName());
			addPayHeadsToEmployeeService.createAddPayHeadsToEmployee(addPayHeadsToEmployee);
			return ResponseEntity.ok(Collections.singletonMap("message",
					"PayHead added to" + employeeTable.getClients().getFullName() + " employee created successfully"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@PostMapping("/employeeData/{empId}")
	public ResponseEntity<Map<String, Object>> createAddPayHeadsToEmployee(@PathVariable Long empId,
			@RequestBody List<AddPayHeadsToEmployee> addPayHeadsToEmployees) throws Exception {
		try {
			EmployeeTable employeeTable = employeeTableRepository.findById(empId)
					.orElseThrow(() -> new RuntimeException(empId + " is not present"));

			for (AddPayHeadsToEmployee addPayHeadsToEmployee : addPayHeadsToEmployees) {
				addPayHeadsToEmployee.setEmployeeTable(employeeTable);
				addPayHeadsToEmployee.setEmpCode(employeeTable.getEmpCode());
				addPayHeadsToEmployee.setEmpName(employeeTable.getClients().getFullName());
				addPayHeadsToEmployeeService.saveOrUpdatePayHead(addPayHeadsToEmployee);
			}

			return ResponseEntity.ok(Collections.singletonMap("message", "PayHead added to"
					+ employeeTable.getClients().getFullName() + "   employee created successfully"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
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
	public ResponseEntity<Map<String, Object>> updateAddPayHeadsToEmployee(@PathVariable Long id,
			@RequestBody AddPayHeadsToEmployee addPayHeadsToEmployeeDetails) throws Exception {
		try {
			addPayHeadsToEmployeeService.updateAddPayHeadsToEmployee(id, addPayHeadsToEmployeeDetails);
			return ResponseEntity.ok(Collections.singletonMap("message", "PayHead updated successfully"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public void deleteAddPayHeadsToEmployee(@PathVariable Long id) throws Exception {
		try {
			addPayHeadsToEmployeeService.deleteAddPayHeadsToEmployee(id);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}
}
