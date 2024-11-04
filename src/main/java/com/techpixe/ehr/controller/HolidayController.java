package com.techpixe.ehr.controller;

import com.techpixe.ehr.entity.HR;
import com.techpixe.ehr.entity.Holiday;
import com.techpixe.ehr.repository.UserRepository;
import com.techpixe.ehr.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user/{userId}")
    public ResponseEntity<Holiday> createHoliday(@PathVariable Long userId, @RequestBody Holiday holiday) {

        HR user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(userId + " is not found"));
        holiday.setUser(user_Id);

        Holiday createdHoliday = holidayService.createHoliday(holiday);
        return ResponseEntity.ok(createdHoliday);
    }

    @GetMapping
    public List<Holiday> getAllHolidays() {
        return holidayService.getAllHolidays();
    }

    @GetMapping("/{holidayId}")
    public ResponseEntity<Holiday> getHolidayById(@PathVariable Long holidayId) {
        return holidayService.getHolidayById(holidayId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{holidayId}")
    public ResponseEntity<Holiday> updateHoliday(@PathVariable Long holidayId,
                                                 @RequestParam(required = false) String holidayTitle, @RequestParam(required = false) String description,
                                                 @RequestParam(required = false) String holidayDate, @RequestParam(required = false) String holidayType) {
        Holiday updatedHoliday = holidayService.updateHoliday(holidayId, holidayTitle, description, holidayDate,
                holidayType);
        return ResponseEntity.ok(updatedHoliday);
    }

    @DeleteMapping("/delete/{holidayId}")
    public ResponseEntity<Void> deleteHoliday(@PathVariable Long holidayId) {
        holidayService.deleteHoliday(holidayId);
        return ResponseEntity.noContent().build();
    }
}
