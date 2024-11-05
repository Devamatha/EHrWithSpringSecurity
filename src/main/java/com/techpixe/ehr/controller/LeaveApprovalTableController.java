package com.techpixe.ehr.controller;

import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.entity.LeaveApprovalTable;
import com.techpixe.ehr.repository.EmployeeTableRepository;
import com.techpixe.ehr.service.LeaveApprovalTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaveApproval")
public class LeaveApprovalTableController {
    @Autowired
    private LeaveApprovalTableService leaveapprovalTableService;

    @Autowired
    private EmployeeTableRepository employeeTableRepository;

    @PostMapping("/employee/{empId}")
    public ResponseEntity<LeaveApprovalTable> createLeaveapprovalTable(@PathVariable Long empId,
                                                                       @RequestBody LeaveApprovalTable leaveapprovalTable) {
        EmployeeTable employeeTable = employeeTableRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException(empId + "is not present"));
        leaveapprovalTable.setEmployeeTable(employeeTable);
        leaveapprovalTable.setFullName(employeeTable.getClients().getFullName());
        LeaveApprovalTable createdLeaveapprovalTable = leaveapprovalTableService
                .createLeaveapprovalTable(leaveapprovalTable);
        return ResponseEntity.ok(createdLeaveapprovalTable);
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
    public ResponseEntity<LeaveApprovalTable> updateLeaveapprovalTable(@PathVariable Long id,
                                                                       @RequestBody LeaveApprovalTable leaveapprovalTableDetails) {
        LeaveApprovalTable updatedLeaveapprovalTable = leaveapprovalTableService.updateLeaveApprovalTable(id,
                leaveapprovalTableDetails);
        return ResponseEntity.ok(updatedLeaveapprovalTable);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveapprovalTable(@PathVariable Long id) {
        leaveapprovalTableService.deleteLeaveApprovalTable(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/status/{id}")
    public LeaveApprovalTable updateStatus(@PathVariable Long id, @RequestParam String status) {
        LeaveApprovalTable updatedLeaveApproval = leaveapprovalTableService.updateLeaveApprovalStatus(id, status);
        return updatedLeaveApproval;
    }
}
