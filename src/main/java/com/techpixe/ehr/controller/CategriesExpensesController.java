package com.techpixe.ehr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techpixe.ehr.sentity.CategriesExpenses;
import com.techpixe.ehr.service.CategriesExpensesService;

@RestController
@RequestMapping("/api/categoreis")
public class CategriesExpensesController {
	@Autowired
	CategriesExpensesService categriesExpensesService;

		
	
	
	@GetMapping("/getAllExpenses")
	public ResponseEntity<Page<CategriesExpenses>> getAllData(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int pageSize) {
		Page<CategriesExpenses> fetchedAllUsers = categriesExpensesService.getAllUsers(page, pageSize);
		return new ResponseEntity<Page<CategriesExpenses>>(fetchedAllUsers, HttpStatus.OK);
	}
}
