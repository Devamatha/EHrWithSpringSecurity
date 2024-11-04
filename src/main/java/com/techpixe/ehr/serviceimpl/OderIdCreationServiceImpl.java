package com.techpixe.ehr.serviceimpl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.techpixe.ehr.entity.OderIdCreation;
import com.techpixe.ehr.repository.OderIdCreationRepository;
import com.techpixe.ehr.service.OderIdCreationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OderIdCreationServiceImpl implements OderIdCreationService {
    @Autowired
    private OderIdCreationRepository oderIdCreationRepository;

    @Override
    public OderIdCreation save(OderIdCreation oderIdCreation) throws RazorpayException {

        RazorpayClient razorpay = new RazorpayClient("rzp_test_P7eTEWTbR1y2Sm", "gFMj8IVEIJuIKBOEeqRzslPt");

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", oderIdCreation.getAmount() * 100);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "receipt#1");
//		JSONObject notes = new JSONObject();
//		notes.put("notes_key_1","Tea, Earl Grey, Hot");
//		notes.put("notes_key_1","Tea, Earl Grey, Hot");
//		orderRequest.put("notes",notes);

        Order order = razorpay.orders.create(orderRequest);
        String orderId = order.get("id");
        oderIdCreation.setAmount(oderIdCreation.getAmount());
        oderIdCreation.setDate(LocalDate.now());
        oderIdCreation.setOder_id(orderId);

        return oderIdCreationRepository.save(oderIdCreation);
    }

}
