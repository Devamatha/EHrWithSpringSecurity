package com.techpixe.ehr.controller;

import com.techpixe.ehr.entity.HR;
import com.techpixe.ehr.entity.PayHeads;
import com.techpixe.ehr.repository.UserRepository;
import com.techpixe.ehr.service.PayHeadsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payHeads")
public class PayHeadsController {

	@Autowired
	private PayHeadsService payHeadsService;
	@Autowired
	private UserRepository userRepository;

	// Create a new PayHead
	@PostMapping("/user/{userId}")
	public ResponseEntity<Map<String, String>> createPayHead(@PathVariable Long userId, @RequestBody PayHeads payHead)
			throws Exception {
		try {

			HR user_Id = userRepository.findById(userId)
					.orElseThrow(() -> new RuntimeException(userId + " is not found"));
			payHead.setUser(user_Id);
			payHead.setPayHeadName(payHead.getPayHeadName());
			payHead.setPayHeadType(payHead.getPayHeadType());
			payHead.setPayHeadDescription(payHead.getPayHeadDescription());
			payHeadsService.createPayHead(payHead);
			return ResponseEntity.ok(Collections.singletonMap("message", "PayHead created successfully"));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@GetMapping("/{payHeadId}")
	public ResponseEntity<PayHeads> getPayHeadById(@PathVariable Long payHeadId) {
		return payHeadsService.getPayHeadById(payHeadId).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/update/{payHeadId}")
	public ResponseEntity<Map<String, String>> updatePayHead(@PathVariable Long payHeadId,
			@RequestParam String payHeadName, @RequestParam String payHeadDescription,
			@RequestParam String payHeadType) {

		payHeadsService.updatePayHead(payHeadId, payHeadName, payHeadDescription, payHeadType);
		return ResponseEntity.ok(Collections.singletonMap("message", "PayHead updated successfully"));
	}

	@DeleteMapping("/delete/{payHeadId}")
	public void deletePayHead(@PathVariable Long payHeadId) {
		payHeadsService.deletePayHead(payHeadId);
	}
}
