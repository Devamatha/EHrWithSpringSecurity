package com.techpixe.ehr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techpixe.ehr.entity.AddJobDetails;
import com.techpixe.ehr.entity.Attendance;
import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.entity.HR;
import com.techpixe.ehr.entity.Holiday;
import com.techpixe.ehr.entity.LeaveApprovalTable;
import com.techpixe.ehr.entity.PayHeads;
import com.techpixe.ehr.entity.PersonalInformation;
import com.techpixe.ehr.entity.SubscriptionPlan;
@Repository
public interface UserRepository extends JpaRepository<HR, Long> {

    //HR findByMobileNumber(Long mobileNumber);

   // HR findByEmail(String email);

    // @Query("SELECT u.fullName, u.email, u.mobileNumber,u.user_Id,u.password FROM
    // User u WHERE u.email = :email")
    // Object[] findByEmail(@Param("email") String email);

//    @Query("SELECT h.fullName, h.email, h.mobileNumber,h.password FROM HR h WHERE h.user_Id = :userId")
//    Object[] findPartialUserById(@Param("userId") Long userId);

    @Query("SELECT ajd FROM AddJobDetails ajd WHERE ajd.user.user_Id = :userId")
    List<AddJobDetails> findAddJobDetailsByUserId(Long userId);

    @Query("SELECT et FROM EmployeeTable et WHERE et.user.user_Id = :userId")
    List<EmployeeTable> findEmployeeTableByUserId(Long userId);

    @Query("SELECT h FROM Holiday h WHERE h.user.user_Id = :userId")
    List<Holiday> findHolidayByUserId(Long userId);

    @Query("SELECT ph FROM PayHeads ph WHERE ph.user.user_Id = :userId")
    List<PayHeads> findPayHeadsByUserId(Long userId);

    @Query("SELECT pi FROM PersonalInformation pi WHERE pi.user.user_Id = :userId")
    List<PersonalInformation> findPersonalInformationByUserId(Long userId);

    @Query("SELECT e.leaveapprovalTable FROM EmployeeTable e WHERE e.user.user_Id = :userId")
    List<LeaveApprovalTable> findLeaveApprovalsByUserId(Long userId);

    @Query("SELECT e.attendance FROM EmployeeTable e WHERE e.user.user_Id=:userId")
    List<Attendance> findAttendanceByUserId(Long userId);

    @Query("SELECT sp FROM SubscriptionPlan sp WHERE sp.user.user_Id = :userId")
    List<SubscriptionPlan> findSubscriptionPlansByUserId(@Param("userId") Long userId);


    @Query("SELECT h.user_Id FROM HR h")
    List<Long> getAllUserIds();

    @Query("SELECT h FROM HR h WHERE h.user_Id = :userId")
    HR findById(long userId);


    @Query("SELECT h.user_Id FROM HR h WHERE h.clients.id = :clientId")
    List<Long> findHRIdsByClientId(@Param("clientId") Long clientId);
    
   
    



}

