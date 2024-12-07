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
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttedenceController {

	@Autowired
	private AttendanceService attendanceService;

	@PostMapping("/employee/{empId}")
	public ResponseEntity<Map<String, Object>> createAttendance(@PathVariable Long empId,
			@RequestParam LocalTime punchIn, @RequestParam String punchInMessage, @RequestParam LocalDate date)
			throws Exception {
		try {
			attendanceService.createAttendance(empId, punchIn, punchInMessage, date);
			return ResponseEntity.ok(Collections.singletonMap("message", "Attendance created successfully"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}


	

	@PutMapping("/update/{id}")
	public ResponseEntity<Map<String, Object>> updateAttendance(@PathVariable Long id, @RequestParam LocalTime punchOut,
			@RequestParam String punchOutMessage, @RequestParam LocalTime punchIn, @RequestParam String punchInMessage,
			@RequestParam LocalDate date, @RequestParam String name) throws Exception {
		try {
			attendanceService.updateAttendance(id, punchOut, punchOutMessage, punchIn, punchInMessage, date, name);
			return ResponseEntity.ok(Collections.singletonMap("message", "logout added successfully"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}


	@GetMapping("/employee/attendance/{empId}/{date}")
	public ResponseEntity<Attendance> getAttendanceByDate(@PathVariable Long empId, @PathVariable LocalDate date) {
		Attendance attendance = attendanceService.getAttendanceByDate(empId, date);
		return ResponseEntity.ok(attendance);
	}
}
