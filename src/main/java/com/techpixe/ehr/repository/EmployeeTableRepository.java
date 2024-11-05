package com.techpixe.ehr.repository;

import com.techpixe.ehr.entity.AddPayHeadsToEmployee;
import com.techpixe.ehr.entity.Attendance;
import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.entity.LeaveApprovalTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeTableRepository extends JpaRepository<EmployeeTable, Long> {
    //EmployeeTable findByContactNo(Long contactNo);

  //  EmployeeTable findByEmailId(String emailId);

    @Query("SELECT aphte FROM AddPayHeadsToEmployee aphte WHERE aphte.employeeTable.id=:id")
    List<AddPayHeadsToEmployee> findAddPayHeadsToEmployeeByUserId(Long id);

    @Query("SELECT  u.companyName, u.authorizedCompanyName, u.address,u.logo, aphte "
            + "FROM EmployeeTable e " + "JOIN e.user u " + "JOIN e.addPayHeadsToEmployee aphte " + "WHERE e.id = :id")
    List<Object[]> findEmployeeWithPayHeads(Long id);

    @Query("SELECT a FROM Attendance a WHERE a.employeeTable.id=:id")
    List<Attendance> findAttendanceById(Long id);

    @Query("SELECT lt FROM LeaveApprovalTable lt WHERE lt.employeeTable.id=:id")
    List<LeaveApprovalTable> findLeaveapprovalTableById(Long id);
}