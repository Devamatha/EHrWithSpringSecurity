package com.techpixe.ehr.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data

public class OderIdCreation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long amount;
    private LocalDate date;
    private String oder_id;
}
