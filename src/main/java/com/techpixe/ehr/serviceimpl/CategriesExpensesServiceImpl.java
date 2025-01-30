package com.techpixe.ehr.serviceimpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.techpixe.ehr.enumClass.CategoryType;
import com.techpixe.ehr.enumClass.FormType;
import com.techpixe.ehr.enumClass.PaymentMode;
import com.techpixe.ehr.repo.CategriesExpensesRepository;
import com.techpixe.ehr.sentity.CategriesExpenses;
import com.techpixe.ehr.service.CategriesExpensesService;

@Service
public class CategriesExpensesServiceImpl implements CategriesExpensesService {

	@Autowired
	private CategriesExpensesRepository categriesExpensesRepository;

	@Override
	public CategriesExpenses saveData(FormType formType, CategoryType categoryType, long amount, String remarks,
			PaymentMode paymentmode, String transcationId, String email) throws IOException {
		CategriesExpenses categriesExpenses = new CategriesExpenses();
		categriesExpenses.setAmount(amount);
		categriesExpenses.setPaymentMode(paymentmode);
		categriesExpenses.setRemarks(remarks);
		categriesExpenses.setFormType(formType);
		categriesExpenses.setTransactionId(transcationId);
		categriesExpenses.setCategoryType(categoryType);
		categriesExpenses.setEmail(email);
		categriesExpenses.setCreatedAt(LocalDateTime.now());
		return categriesExpensesRepository.save(categriesExpenses);
	}

	@Override
	public CategriesExpenses saveImage(FormType formType, String remarks, MultipartFile image, String email)
			throws IOException {
		CategriesExpenses categriesExpenses = new CategriesExpenses();
		categriesExpenses.setRemarks(remarks);
		categriesExpenses.setFormType(formType);
		categriesExpenses.setEmail(email);
		categriesExpenses.setCreatedAt(LocalDateTime.now());
		categriesExpenses.setImage(image.getBytes());
		return categriesExpensesRepository.save(categriesExpenses);
	}

	@Override
	public Page<CategriesExpenses> getAllUsers(int offset, int pageSize) {
		PageRequest pageRequest = PageRequest.of(offset, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

		Page<CategriesExpenses> fetchAll = categriesExpensesRepository.findAll(pageRequest);
		if (fetchAll.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.OK, "No Records Found");
		}
		return fetchAll;
	}

	@Override
	public CategriesExpenses saveRemarks(FormType formType, String remarks, String email) throws IOException {
		CategriesExpenses categriesExpenses = new CategriesExpenses();
		categriesExpenses.setRemarks(remarks);
		categriesExpenses.setFormType(formType);
		// categriesExpenses.setCategoryType(categoryType);
		categriesExpenses.setEmail(email);
		categriesExpenses.setCreatedAt(LocalDateTime.now());
		return categriesExpensesRepository.save(categriesExpenses);

	}

}
