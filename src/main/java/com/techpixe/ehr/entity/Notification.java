package com.techpixe.ehr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" }) 
////@Table(name = "notification")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String messageContent;

	private LocalDateTime timestamp;

	private String messageType;

	private Boolean readStatus;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	@JsonIgnoreProperties("sentNotifications")
	private HR sender;
	
//	
//	@JsonIgnore
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "recipient_id")
//	@JsonBackReference(value = "employee-notification")
//	private EmployeeTable recipient;
	
	
	  @ManyToOne(fetch = FetchType.EAGER)
	    @JsonBackReference(value = "employee-notification")
	    @JoinColumn(name = "empId")
	    private EmployeeTable employeeTable;
}
