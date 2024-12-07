package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.entity.Holiday;
import com.techpixe.ehr.repository.HolidayRepository;
import com.techpixe.ehr.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HolidayServiceImpl implements HolidayService {

	@Autowired
	private HolidayRepository holidayRepository;

	public Holiday createHoliday(Holiday holiday) {
		return holidayRepository.save(holiday);
	}

	public Optional<Holiday> getHolidayById(Long holidayId) {
		return holidayRepository.findById(holidayId);
	}

	@Override
	public Holiday updateHoliday(Long holidayId, String holidayTitle, String description, String holidayDate,
			String holidayType) {

		Holiday holiday = holidayRepository.findById(holidayId)
				.orElseThrow(() -> new RuntimeException("Holiday not found"));

		if (holidayTitle != null && !holidayTitle.isEmpty()) {
			holiday.setHolidayTitle(holidayTitle);
		}
		if (description != null && !description.isEmpty()) {
			holiday.setDescription(description);
		}
		if (holidayDate != null) {
			holiday.setHolidayDate(holidayDate);
		}
		if (holidayType != null && !holidayType.isEmpty()) {
			holiday.setHolidayType(holidayType);
		}

		return holidayRepository.save(holiday);

	}

	public void deleteHoliday(Long holidayId) {
		holidayRepository.deleteById(holidayId);
	}
}
