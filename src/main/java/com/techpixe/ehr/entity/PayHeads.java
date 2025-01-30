package com.techpixe.ehr.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
////@Table(name = "pay_heads")
public class PayHeads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long payHeadId;
    private String payHeadName;
    private String payHeadDescription;
    private String payHeadType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_Id")
    @JsonBackReference
    private HR user;

}
