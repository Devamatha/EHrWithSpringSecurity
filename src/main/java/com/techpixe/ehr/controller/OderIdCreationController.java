package com.techpixe.ehr.controller;


import com.razorpay.RazorpayException;
import com.techpixe.ehr.entity.OderIdCreation;
import com.techpixe.ehr.service.OderIdCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oderId")
public class OderIdCreationController {
    @Autowired
    OderIdCreationService oderIdCreationService;

    @PostMapping("/save/oderId")
    public OderIdCreation saveData(@RequestBody OderIdCreation OderIdCreation) throws RazorpayException {
        return oderIdCreationService.save(OderIdCreation);

    }
}
