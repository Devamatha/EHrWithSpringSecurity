package com.techpixe.ehr.controller;

import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.entity.LeaveApprovalTable;
import com.techpixe.ehr.repository.EmployeeTableRepository;
import com.techpixe.ehr.service.LeaveApprovalTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaveApproval")
public class LeaveApprovalTableController {
	@Autowired
	private LeaveApprovalTableService leaveapprovalTableService;

	@Autowired
	private EmployeeTableRepository employeeTableRepository;

	@PostMapping("/employee/{empId}")
	public ResponseEntity<Map<String, Object>> createLeaveapprovalTable(@PathVariable Long empId,
			@RequestBody LeaveApprovalTable leaveapprovalTable) throws Exception {
		try {
			EmployeeTable employeeTable = employeeTableRepository.findById(empId)
					.orElseThrow(() -> new RuntimeException(empId + "is not present"));
			leaveapprovalTable.setEmployeeTable(employeeTable);
			leaveapprovalTable.setFullName(employeeTable.getClients().getFullName());
			LeaveApprovalTable createdLeaveapprovalTable = leaveapprovalTableService
					.createLeaveapprovalTable(leaveapprovalTable);
			return ResponseEntity.ok(Collections.singletonMap("message", "Leave Appiled   successfully"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@GetMapping
	public List<LeaveApprovalTable> getAllLeaveapprovalTables() {
		return leaveapprovalTableService.getAllLeaveApprovalTables();
	}

	@GetMapping("/{id}")
	public ResponseEntity<LeaveApprovalTable> getLeaveapprovalTableById(@PathVariable Long id) {
		return leaveapprovalTableService.getLeaveApprovalTableById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> updateLeaveapprovalTable(@PathVariable Long id,
			@RequestBody LeaveApprovalTable leaveapprovalTableDetails) throws Exception {
		try {
			leaveapprovalTableService.updateLeaveApprovalTable(id, leaveapprovalTableDetails);
			return ResponseEntity.ok(Collections.singletonMap("message", "leave updated successfully"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public void deleteLeaveapprovalTable(@PathVariable Long id) throws Exception {
		try {
			leaveapprovalTableService.deleteLeaveApprovalTable(id);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@PutMapping("/status/{id}")
	public ResponseEntity<Map<String, Object>> updateStatus(@PathVariable Long id, @RequestParam String status) {
		leaveapprovalTableService.updateLeaveApprovalStatus(id, status);
		return ResponseEntity.ok(Collections.singletonMap("message", "leave status updated successfully"));
	}
}
