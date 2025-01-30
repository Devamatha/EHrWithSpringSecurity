package com.techpixe.ehr.controller;

import java.sql.Time;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techpixe.ehr.sentity.Site_users;
import com.techpixe.ehr.service.Site_UsersService;

@RestController
@RequestMapping("/api/")
public class Site_UsersController {

	@Autowired
	private Site_UsersService site_UsersService;

	@GetMapping("getAllSiteUsersData")
	public ResponseEntity<Page<Site_users>> getAllData(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int pageSize) {
		Page<Site_users> fetchedAllUsers = site_UsersService.getAllUsers(page, pageSize);
		return new ResponseEntity<Page<Site_users>>(fetchedAllUsers, HttpStatus.OK);
	}

	@GetMapping("getAllSiteUser")
	public List<Site_users> getAllData() {
		return site_UsersService.getAllUsers();

	}

	@GetMapping("check-attendance/{employeeId}")
	public ResponseEntity<Map<String, Object>> checkAttendance(@PathVariable long employeeId) {
		boolean response = site_UsersService.checkAttendanceForToday(employeeId);
		return ResponseEntity.ok(Collections.singletonMap("isAttedence", response));

	}

	@PostMapping("saveClockIn/{employeeId}")
	public ResponseEntity<Map<String, String>> saveclockIn(@PathVariable long employeeId,
			@RequestParam MultipartFile Clock_in_employee_img, @RequestParam String employee_name,
			@RequestParam String employee_designation, @RequestParam String longitude, @RequestParam String latittude,@RequestParam Time clock_in,@RequestParam String checkIn_address)
			throws Exception {
		try {
			site_UsersService.saveclockIn(employeeId, Clock_in_employee_img, employee_name, employee_designation,
					longitude, latittude,clock_in,checkIn_address);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
		return ResponseEntity.ok(Collections.singletonMap("Message", "Clock in Data Saved " + employee_name));
	}

	@PutMapping("saveClockOut/{employeeId}")
	public ResponseEntity<Map<String, String>> saveclockout(@PathVariable long employeeId,
			@RequestParam MultipartFile clock_out_employee_img ,@RequestParam Time clock_out,@RequestParam String checkOut_address) throws Exception {
		try {
			site_UsersService.saveclockout(employeeId, clock_out_employee_img,clock_out,checkOut_address);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
		return ResponseEntity.ok(Collections.singletonMap("Message", "Clock out Data Saved "));
	}

}
