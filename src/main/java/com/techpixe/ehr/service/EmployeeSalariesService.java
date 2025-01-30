package com.techpixe.ehr.service;

import org.springframework.data.domain.Page;

import com.techpixe.ehr.enumClass.CategoryType;
import com.techpixe.ehr.enumClass.FormType;
import com.techpixe.ehr.enumClass.PaymentMode;
import com.techpixe.ehr.sentity.EmployeeSalaries;

public interface EmployeeSalariesService {

	public EmployeeSalaries saveData(String empId, String empName, FormType formType, CategoryType categoryType,
			long amount, String remarks, PaymentMode paymentmode,String transcationId,String email);

	public Page<EmployeeSalaries> getAllUsers(int offset, int pageSize);
}
