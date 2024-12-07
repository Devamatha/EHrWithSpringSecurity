package com.techpixe.ehr.controller;

import com.techpixe.ehr.dto.AddJobDetailsDto;
import com.techpixe.ehr.entity.AddJobDetails;
import com.techpixe.ehr.service.AddJobDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/api/JobDetails")
@RestController
public class AddJobDetailsController {
	@Autowired
	private AddJobDetailsService addJobDetailsService;

	@PostMapping("/addJob/{user_Id}")
	public ResponseEntity<Map<String, String>> createJobDetails(@RequestBody AddJobDetails jobDetails,
			@PathVariable Long user_Id) throws Exception {
		addJobDetailsService.saveJobDetails(jobDetails, user_Id);
		return ResponseEntity.ok(Collections.singletonMap("message", "Data saved successfully"));
	}

	@GetMapping("/{id}")
	public ResponseEntity<AddJobDetails> getJobDetailsById(@PathVariable("id") Long jobId) {
		AddJobDetails jobDetails = addJobDetailsService.getJobDetailsById(jobId);
		return ResponseEntity.ok(jobDetails);
	}

	@DeleteMapping("/delete/{id}")
	public void deleteJobDetails(@PathVariable("id") Long jobId) throws Exception {
		try {
			addJobDetailsService.deleteJobDetails(jobId);
		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	@PutMapping("/update/{jobId}")
	public ResponseEntity<Map<String, String>> updateJobDetails(@PathVariable long jobId,
			@RequestBody AddJobDetailsDto updateDto) throws Exception {
		addJobDetailsService.updateJobDetails(jobId, updateDto);
		return ResponseEntity.ok(Collections.singletonMap("message", "Data updated successfully"));
	}

}