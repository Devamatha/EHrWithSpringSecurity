package com.techpixe.ehr.controller;

import com.techpixe.ehr.entity.Attendance;
import com.techpixe.ehr.entity.EmployeeTable;
import com.techpixe.ehr.repository.EmployeeTableRepository;
import com.techpixe.ehr.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttedenceController {

    @Autowired
    private AttendanceService attendanceService;


    // Create a new Attedence
    @PostMapping("/employee/{empId}")
    public ResponseEntity<Attendance> createAttendance(@PathVariable Long empId, @RequestParam LocalTime punchIn, @RequestParam String punchInMessage,@RequestParam LocalDate date) {
        Attendance createdAttedence = attendanceService.createAttendance(empId,punchIn,punchInMessage,date);
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
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long id,@RequestParam LocalTime punchOut,
                                                     @RequestParam  String punchOutMessage,
                                                     @RequestParam  LocalTime punchIn,
                                                    @RequestParam   String  punchInMessage,
                                                   @RequestParam    LocalDate  date,
                                                     @RequestParam  String   name) {
        Attendance updatedAttedence = attendanceService.updateAttendance(id, punchOut,
                punchOutMessage,
                punchIn,
                punchInMessage,
                date,
                name);
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
