package com.techpixe.ehr.sentity;

import java.time.LocalDateTime;

import com.techpixe.ehr.enumClass.CategoryType;
import com.techpixe.ehr.enumClass.FormType;
import com.techpixe.ehr.enumClass.PaymentMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "categries_expenses")

public class CategriesExpenses {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private long amount;
	@Enumerated(EnumType.STRING)
	private PaymentMode paymentMode;
	private String remarks;
	@Enumerated(EnumType.STRING)
	private CategoryType categoryType;
	@Enumerated(EnumType.STRING)
	private FormType formType;
	private String transactionId;
	private String email;
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] image;
	private LocalDateTime createdAt;
}
