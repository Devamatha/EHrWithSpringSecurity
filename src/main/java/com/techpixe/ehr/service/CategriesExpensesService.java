package com.techpixe.ehr.service;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.techpixe.ehr.enumClass.CategoryType;
import com.techpixe.ehr.enumClass.FormType;
import com.techpixe.ehr.enumClass.PaymentMode;
import com.techpixe.ehr.sentity.CategriesExpenses;

public interface CategriesExpensesService {
	

	public CategriesExpenses saveData(FormType formType, CategoryType categoryType, long amount, String remarks,
			PaymentMode paymentmode,String transcationId,String email) throws IOException;

	public CategriesExpenses saveImage(FormType formType, String remarks, MultipartFile image,String email)
			throws IOException;
	
	public CategriesExpenses saveRemarks(FormType formType, String remarks,String email)
			throws IOException;

	public Page<CategriesExpenses> getAllUsers(int offset, int pageSize);

}
