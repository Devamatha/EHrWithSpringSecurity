package com.techpixe.ehr.controller;

import com.techpixe.ehr.entity.HR;
import com.techpixe.ehr.entity.Holiday;
import com.techpixe.ehr.repository.UserRepository;
import com.techpixe.ehr.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

	@Autowired
	private HolidayService holidayService;
	@Autowired
	private UserRepository userRepository;

	@PostMapping("/user/{userId}")
	public ResponseEntity<Map<String, Object>> createHoliday(@PathVariable Long userId, @RequestBody Holiday holiday)
			throws Exception {
		try {
			HR user_Id = userRepository.findById(userId)
					.orElseThrow(() -> new RuntimeException(userId + " is not found"));
			holiday.setUser(user_Id);

			holidayService.createHoliday(holiday);
			return ResponseEntity.ok(Collections.singletonMap("message", "Holiday created successfully"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@GetMapping("/{holidayId}")
	public ResponseEntity<Holiday> getHolidayById(@PathVariable Long holidayId) {
		return holidayService.getHolidayById(holidayId).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/update/{holidayId}")
	public ResponseEntity<Map<String, Object>> updateHoliday(@PathVariable Long holidayId,
			@RequestParam(required = false) String holidayTitle, @RequestParam(required = false) String description,
			@RequestParam(required = false) String holidayDate, @RequestParam(required = false) String holidayType)
			throws Exception {
		try {
			holidayService.updateHoliday(holidayId, holidayTitle, description, holidayDate, holidayType);
			return ResponseEntity.ok(Collections.singletonMap("message", "Holiday updated successfully"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@DeleteMapping("/delete/{holidayId}")
	public void deleteHoliday(@PathVariable Long holidayId) {
		holidayService.deleteHoliday(holidayId);
	}
}
