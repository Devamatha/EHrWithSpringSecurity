package com.techpixe.ehr.service;

import com.techpixe.ehr.entity.Holiday;

import java.util.Optional;

public interface HolidayService {
	Holiday createHoliday(Holiday holiday);

	Optional<Holiday> getHolidayById(Long holidayId);

	Holiday updateHoliday(Long holidayId, String holidayTitle, String description, String holidayDate,
			String holidayType);

	void deleteHoliday(Long holidayId);

}
