package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.entity.Attendance;
import com.techpixe.ehr.repository.AttendanceRepository;
import com.techpixe.ehr.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attedenceRepository;

    // Create
    public Attendance createAttendance(Attendance attendance) {
        attendance.setAttendance(true);
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
    public Attendance updateAttendance(Long id, Attendance attedenceDetails) {
        Attendance attendance = attedenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        // Update fields
        attendance.setDate(attedenceDetails.getDate());
        attendance.setName(attedenceDetails.getName());
        attendance.setPunchIn(attedenceDetails.getPunchIn());
        attendance.setPunchOut(attedenceDetails.getPunchOut());
        attendance.setPunchInMessage(attedenceDetails.getPunchInMessage());
        attendance.setPunchOutMessage(attedenceDetails.getPunchOutMessage());
        Duration duration = Duration.between(attedenceDetails.getPunchIn(), attedenceDetails.getPunchOut());
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        String formattedWorkingHours = String.format("%02d:%02d", hours, minutes);
        attendance.setWorkingHours(formattedWorkingHours);
        attendance.setEmpCode(attedenceDetails.getEmpCode());
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
