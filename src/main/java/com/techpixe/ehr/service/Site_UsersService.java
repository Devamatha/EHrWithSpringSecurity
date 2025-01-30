package com.techpixe.ehr.service;

import java.io.IOException;
import java.sql.Time;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.techpixe.ehr.sentity.Site_users;

public interface Site_UsersService {

	List<Site_users> getAllUsers();

	public Page<Site_users> getAllUsers(int offset, int pageSize);

	public boolean checkAttendanceForToday(long employeeId);
	 public Site_users saveclockIn(long employeeId,MultipartFile Clock_in_employee_img,String employee_name,String employee_designation ,String longitude,String latittude ,Time clock_in ,String checkIn_address) throws IOException;
	 public Site_users saveclockout(long employeeId,MultipartFile clock_out_employee_img ,Time clock_out,	String checkOut_address
) throws IOException;

}
