package com.techpixe.ehr.serviceimpl;

import java.io.IOException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.techpixe.ehr.repo.Site_UsersRepository;
import com.techpixe.ehr.sentity.Site_users;
import com.techpixe.ehr.service.Site_UsersService;

@Service
public class Site_UsersServiceImpl implements Site_UsersService {

	@Autowired
	private Site_UsersRepository siteUsersRepository;

	@Override
	public List<Site_users> getAllUsers() {
		return siteUsersRepository.findAll();
	}

	@Override
	public Page<Site_users> getAllUsers(int offset, int pageSize) {
		PageRequest pageRequest = PageRequest.of(offset, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

		Page<Site_users> fetchAll = siteUsersRepository.findAll(pageRequest);
		if (fetchAll.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.OK, "No Records Found");
		}
		return fetchAll;
	}

	@Override
	public boolean checkAttendanceForToday(long employeeId) {
		LocalDate today = LocalDate.now();
		Optional<Site_users> record = siteUsersRepository.findByDateAndEmployeeId(today, employeeId);
		return record.map(Site_users::isAttendance).orElse(false);
	}

	@Override
	public Site_users saveclockIn(long employeeId, MultipartFile Clock_in_employee_img, String employee_name,
			String employee_designation, String longitude, String latittude, Time clock_in, String checkIn_address)
			throws IOException {
		LocalDate today = LocalDate.now();

		Optional<Site_users> existingRecordOpt = siteUsersRepository.findByDateAndEmployeeId(today, employeeId);
		boolean isAttedence = existingRecordOpt.map(Site_users::isAttendance).orElse(false);
		Site_users record = new Site_users();
		if (!isAttedence) {
			record.setClock_in_employee_img(Clock_in_employee_img.getBytes());
			record.setEmployee_designation(employee_designation);
			record.setEmployee_name(employee_name);
			record.setEmployeeId(employeeId);
			record.setDate(today);
			record.setAttendance(true);
			record.setClock_in(clock_in);
			record.setLatittude(latittude);
			record.setLongitude(longitude);
			record.setCheckIn_address(checkIn_address);
			record.setCreatedAt(LocalDateTime.now());
			siteUsersRepository.save(record);
		}
		return record;
	}

	@Override
	public Site_users saveclockout(long employeeId, MultipartFile clock_out_employee_img, Time clock_out,
			String checkOut_address) throws IOException {
		LocalDate today = LocalDate.now();
		Optional<Site_users> existingRecordOpt = siteUsersRepository.findByDateAndEmployeeId(today, employeeId);
		boolean isAttedence = existingRecordOpt.map(Site_users::isAttendance).orElse(false);
		Site_users existingRecord = existingRecordOpt.get();

		if (isAttedence) {
			existingRecord.setId(existingRecord.getId());
			existingRecord.setClock_out_employee_img(clock_out_employee_img.getBytes());
			existingRecord.setEmployeeId(employeeId);

			LocalTime clockOutTime = clock_out.toLocalTime();
			LocalTime clockInTime = existingRecord.getClock_in().toLocalTime();
			Duration duration = Duration.between(clockInTime, clockOutTime);
			long hours = duration.toHours();
			long minutes = duration.toMinutesPart();
			long seconds = duration.toSecondsPart();
			existingRecord.setDuration(hours + ":" + minutes + ":" + seconds);
			existingRecord.setClock_out(clock_out);
			existingRecord.setCheckOut_address(checkOut_address);
			siteUsersRepository.save(existingRecord);
		} else {
			throw new RuntimeException("please clock In");
		}
		return existingRecord;
	}

}
