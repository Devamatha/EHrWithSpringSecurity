package com.techpixe.ehr.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.techpixe.ehr.enumClass.CategoryType;
import com.techpixe.ehr.enumClass.FormType;
import com.techpixe.ehr.enumClass.PaymentMode;
import com.techpixe.ehr.repo.EmployeeSalariesRepository;
import com.techpixe.ehr.sentity.EmployeeSalaries;
import com.techpixe.ehr.service.EmployeeSalariesService;

@Service
public class EmployeeSalariesServiceImpl implements EmployeeSalariesService {

	@Autowired
	private EmployeeSalariesRepository employeeSalariesRepository;

	@Override
	public EmployeeSalaries saveData(String empId, String empName, FormType formType, CategoryType categoryType,
			long amount, String remarks, PaymentMode paymentmode, String transcationId, String email) {
		EmployeeSalaries employeeSalaries = new EmployeeSalaries();
		employeeSalaries.setEmpId(empId);
		employeeSalaries.setEmpName(empName);
		employeeSalaries.setPaymentMode(paymentmode);
		employeeSalaries.setCategoryType(categoryType);
		employeeSalaries.setFormType(formType);
		employeeSalaries.setSalaryAmount(amount);
		employeeSalaries.setTransactionId(transcationId);
		employeeSalaries.setRemarks(remarks);
		employeeSalaries.setEmail(email);
		employeeSalaries.setCreatedAt(LocalDateTime.now());
		return employeeSalariesRepository.save(employeeSalaries);
	}

	@Override
	public Page<EmployeeSalaries> getAllUsers(int offset, int pageSize) {

		PageRequest pageRequest = PageRequest.of(offset, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

		Page<EmployeeSalaries> fetchAll = employeeSalariesRepository.findAll(pageRequest);
		if (fetchAll.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.OK, "No Records Found");
		}
		return fetchAll;
	}

}
