package com.techpixe.ehr.service;

import com.techpixe.ehr.entity.LeaveApprovalTable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface LeaveApprovalTableService {
    LeaveApprovalTable createLeaveapprovalTable(LeaveApprovalTable leaveapprovalTable);

    List<LeaveApprovalTable> getAllLeaveApprovalTables();

    Optional<LeaveApprovalTable> getLeaveApprovalTableById(Long id);

    LeaveApprovalTable updateLeaveApprovalTable(Long id, LeaveApprovalTable leaveApprovalTableDetails);

    void deleteLeaveApprovalTable(Long id);

    LeaveApprovalTable updateLeaveApprovalStatus(Long id, String status);


}
