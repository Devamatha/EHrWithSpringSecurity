package com.techpixe.ehr.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techpixe.ehr.sentity.EmployeeSalaries;
import com.techpixe.ehr.service.EmployeeSalariesService;

@RestController
@RequestMapping("/api/salaries")
public class EmployeeSalariesController {

	@Autowired
	private EmployeeSalariesService employeeSalariesService;

	
	
	@GetMapping("/getAllEmployee")
	public ResponseEntity<Page<EmployeeSalaries>> getAllData(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int pageSize) {
		Page<EmployeeSalaries> fetchedAllUsers = employeeSalariesService.getAllUsers(page, pageSize);
		return new ResponseEntity<Page<EmployeeSalaries>>(fetchedAllUsers, HttpStatus.OK);
	}
	

}
