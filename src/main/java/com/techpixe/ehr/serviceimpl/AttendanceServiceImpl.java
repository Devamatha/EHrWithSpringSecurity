package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.entity.Attendance;
import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.repository.AttendanceRepository;
import com.techpixe.ehr.repository.EmployeeTableRepository;
import com.techpixe.ehr.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attedenceRepository;
    @Autowired
    private EmployeeTableRepository employeeTableRepository;
    // Create
    public Attendance createAttendance(Long empId,LocalTime punchIn, String punchInMessage, LocalDate date) {
        EmployeeTable employeeTable = employeeTableRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException(empId + " is not present"));
        Attendance attendance = new Attendance();
        attendance.setEmployeeTable(employeeTable);
        attendance.setName(employeeTable.getClients().getFullName());
        attendance.setEmpCode(employeeTable.getEmpCode());
        attendance.setAttendance(true);
        attendance.setPunchIn(punchIn);
        attendance.setPunchInMessage(punchInMessage);
        attendance.setDate(date);
        return attedenceRepository.save(attendance);
    }

    // Read
    public List<Attendance> getAllAttendances() {
        return attedenceRepository.findAll();
    }

    public Optional<Attendance> getAttendanceById(Long id) {
        return attedenceRepository.findById(id);
    }

    // Update
    public Attendance updateAttendance(Long id,LocalTime punchOut,
                                      String punchOutMessage,
                                    LocalTime punchIn,
                                     String  punchInMessage,
                                     LocalDate  date,
                                    String   name) {
        Attendance attendance = attedenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        // Update fields
        attendance.setDate(date);
        attendance.setName(name);
        attendance.setPunchIn(punchIn);
        attendance.setPunchOut(punchOut);
        attendance.setPunchInMessage(punchInMessage);
        attendance.setPunchOutMessage(punchOutMessage);
        Duration duration = Duration.between(punchIn, punchOut);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        String formattedWorkingHours = String.format("%02d:%02d", hours, minutes);
        attendance.setWorkingHours(formattedWorkingHours);
        attendance.setEmpCode(attendance.getEmpCode());
        attendance.setEmployeeTable(attendance.getEmployeeTable());

        return attedenceRepository.save(attendance);
    }

    // Delete
    public void deleteAttendance(Long id) {
        attedenceRepository.deleteById(id);
    }

    @Override
    public Attendance getAttendanceByDate(Long empId, LocalDate date) {
        return attedenceRepository.findByEmployeeTableIdAndDate(empId, date);
    }

}
