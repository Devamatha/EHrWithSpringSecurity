package com.techpixe.ehr.service;

import com.razorpay.RazorpayException;
import com.techpixe.ehr.entity.OderIdCreation;

public interface OderIdCreationService {
    OderIdCreation save(OderIdCreation oderIdCreation) throws RazorpayException;
}
