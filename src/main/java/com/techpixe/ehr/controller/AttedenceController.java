package com.techpixe.ehr.controller;

import com.techpixe.ehr.entity.Attendance;
import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.repository.EmployeeTableRepository;
import com.techpixe.ehr.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttedenceController {

    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private EmployeeTableRepository employeeTableRepository;

    // Create a new Attedence
    @PostMapping("/employee/{empId}")
    public ResponseEntity<Attendance> createAttendance(@PathVariable Long empId, @RequestBody Attendance attendance) {

        EmployeeTable employeeTable = employeeTableRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException(empId + "is not present"));
        attendance.setEmployeeTable(employeeTable);
        attendance.setName(employeeTable.getClients().getFullName());
        attendance.setEmpCode(employeeTable.getEmpCode());

        Attendance createdAttedence = attendanceService.createAttendance(attendance);
        return ResponseEntity.ok(createdAttedence);
    }

    @GetMapping
    public List<Attendance> getAllAttendances() {
        return attendanceService.getAllAttendances();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable Long id) {
        return attendanceService.getAttendanceById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long id, @RequestBody Attendance attedenceDetails) {
        Attendance updatedAttedence = attendanceService.updateAttendance(id, attedenceDetails);
        return ResponseEntity.ok(updatedAttedence);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employee/attendance/{empId}/{date}")
    public ResponseEntity<Attendance> getAttendanceByDate(@PathVariable Long empId, @PathVariable LocalDate date) {
        Attendance attendance = attendanceService.getAttendanceByDate(empId, date);
        return ResponseEntity.ok(attendance);
    }
}
