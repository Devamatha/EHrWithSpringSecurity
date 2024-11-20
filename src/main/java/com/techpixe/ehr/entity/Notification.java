package com.techpixe.ehr.entity;

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
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" }) // Avoids Lazy Initialization issues during
																	// serialization
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
	@JsonIgnoreProperties("sentNotifications") // Ignore sentNotifications to prevent recursion
	private HR sender;
	@JsonIgnore

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipient_id")
	@JsonIgnoreProperties("receivedNotifications") // Ignore receivedNotifications to prevent recursion
	private EmployeeTable recipient;
}
