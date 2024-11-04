package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.entity.LeaveApprovalTable;
import com.techpixe.ehr.repository.LeaveApprovalTableRepository;
import com.techpixe.ehr.service.LeaveApprovalTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveapprovalTableServiceImpl implements LeaveApprovalTableService {

    @Autowired
    private LeaveApprovalTableRepository leaveApprovalTableRepository;

    // Create
    @Override
    public LeaveApprovalTable createLeaveapprovalTable(LeaveApprovalTable leaveapprovalTable) {
        return leaveApprovalTableRepository.save(leaveapprovalTable);
    }

    @Override
    public List<LeaveApprovalTable> getAllLeaveApprovalTables() {
        return leaveApprovalTableRepository.findAll();
    }

    // Read by ID
    @Override
    public Optional<LeaveApprovalTable> getLeaveApprovalTableById(Long id) {
        return leaveApprovalTableRepository.findById(id);
    }

    // Update
    @Override
    public LeaveApprovalTable updateLeaveApprovalTable(Long id, LeaveApprovalTable leaveapprovalTableDetails) {
        LeaveApprovalTable leaveapprovalTable = leaveApprovalTableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveapprovalTable not found"));

        leaveapprovalTable.setSubject(leaveapprovalTableDetails.getSubject());
        leaveapprovalTable.setStartDate(leaveapprovalTableDetails.getStartDate());
        leaveapprovalTable.setEndDate(leaveapprovalTableDetails.getEndDate());
        leaveapprovalTable.setMessage(leaveapprovalTableDetails.getMessage());
        leaveapprovalTable.setType(leaveapprovalTableDetails.getType());
        leaveapprovalTable.setStatus(leaveapprovalTableDetails.getStatus());
        leaveapprovalTable.setEmployeeTable(leaveapprovalTableDetails.getEmployeeTable());
        leaveapprovalTable.setFullName(leaveapprovalTableDetails.getFullName());
        return leaveApprovalTableRepository.save(leaveapprovalTable);
    }

    // Delete
    @Override

    public void deleteLeaveApprovalTable(Long id) {
        leaveApprovalTableRepository.deleteById(id);
    }

    @Transactional
    @Override
    public LeaveApprovalTable updateLeaveApprovalStatus(Long id, String status) {
        LeaveApprovalTable leaveApproval = leaveApprovalTableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave Approval not found"));
        LocalDate today = LocalDate.now();
        if (leaveApproval.getStartDate().isBefore(today) || leaveApproval.getEndDate().isBefore(today)) {

            throw new RuntimeException("Cannot update status as the start date has passed");

        } else {
            leaveApproval.setStatus(status);
            leaveApprovalTableRepository.save(leaveApproval);
        }


        return leaveApproval;
    }
}
