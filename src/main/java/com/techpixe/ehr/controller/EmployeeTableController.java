package com.techpixe.ehr.controller;

import com.techpixe.ehr.dto.EmployeePayHeadDTO;
import com.techpixe.ehr.entity.*;
import com.techpixe.ehr.service.EmployeeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeTableController {

	@Autowired
	private EmployeeTableService employeeTableService;

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeTable> getEmployeeById(@PathVariable Long id) {
		return employeeTableService.getEmployeeById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/delete/{id}")
	public void deleteEmployee(@PathVariable Long id) throws Exception {
		try {
			employeeTableService.deleteEmployee(id);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Map<String, String>> updateEmployee(@PathVariable Long id,
			@RequestParam(required = false) String empCode, @RequestParam(required = false) Date dob,
			@RequestParam(required = false) String gender, @RequestParam(required = false) String maritalStatus,
			@RequestParam(required = false) String nationality, @RequestParam(required = false) String address,
			@RequestParam(required = false) String city, @RequestParam(required = false) String state,
			@RequestParam(required = false) String country,

			@RequestParam(required = false) String identification, @RequestParam(required = false) String idNumber,
			@RequestParam(required = false) String employeeType, @RequestParam(required = false) LocalDate joiningDate,
			@RequestParam(required = false) String bloodGroup, @RequestParam(required = false) String designation,
			@RequestParam(required = false) String department, @RequestParam(required = false) Long panNo,
			@RequestParam(required = false) String bankName, @RequestParam(required = false) Long bankAccountNo,
			@RequestParam(required = false) String iFSCCode, @RequestParam(required = false) String pfAccountNo,
			@RequestParam(required = false) Integer totalDays, @RequestParam(required = false) Integer presentDays,
			@RequestParam(required = false) byte[] photograph, @RequestParam(required = false) HR user)
			throws Exception {
		try {
			employeeTableService.updateEmployee(id, empCode, dob, gender, maritalStatus, nationality, address, city,
					state, country, identification, idNumber, employeeType, joiningDate, bloodGroup, designation,
					department, panNo, bankName, bankAccountNo, iFSCCode, pfAccountNo, totalDays, presentDays,
					photograph, user);

			return ResponseEntity.ok(Collections.singletonMap("message", "Employee data  updated successfully"));
		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	@GetMapping("/addPayHead/{id}")
	public List<AddPayHeadsToEmployee> getAllAddPayHeadsToEmployeebyUserid(@PathVariable Long id) {
		return employeeTableService.getAddPayHeadsToEmployeeByEmployeeId(id);
	}

	@GetMapping("/addPayHeaddetails/{id}")
	public EmployeePayHeadDTO getAllAddPayHeadsToEmployeefbyUserid(@PathVariable Long id) {
		return employeeTableService.getEmployeeWithPayHeads(id);
	}

	@GetMapping("/attedence/{id}")
	public List<Attendance> getallAttedenceByEmployeeId(@PathVariable Long id) {
		return employeeTableService.getAttendanceByEmployee(id);
	}

	@GetMapping("/leave/{id}")
	public List<LeaveApprovalTable> getallLeaveapprovalTable(@PathVariable Long id) {
		return employeeTableService.getLeaveapprovalTableByEmployeeId(id);

	}

}
