package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.entity.PayHeads;
import com.techpixe.ehr.repository.PayHeadsRepository;
import com.techpixe.ehr.service.PayHeadsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PayHeadsServiceImpl implements PayHeadsService {
    @Autowired
    private PayHeadsRepository payHeadsRepository;

    // Create
    public PayHeads createPayHead(PayHeads payHead) {
        return payHeadsRepository.save(payHead);
    }

    // Read
    public List<PayHeads> getAllPayHeads() {
        return payHeadsRepository.findAll();
    }

    public Optional<PayHeads> getPayHeadById(Long payHeadId) {
        return payHeadsRepository.findById(payHeadId);
    }

    // Update
    public PayHeads updatePayHead(Long payHeadId, PayHeads payHeadDetails) {
        PayHeads payHead = payHeadsRepository.findById(payHeadId)
                .orElseThrow(() -> new RuntimeException("PayHead not found"));

        // Update fields
        payHead.setPayHeadName(payHeadDetails.getPayHeadName());
        payHead.setPayHeadDescription(payHeadDetails.getPayHeadDescription());
        payHead.setPayHeadType(payHeadDetails.getPayHeadType());
        payHead.setUser(payHeadDetails.getUser());

        return payHeadsRepository.save(payHead);
    }

    @Override
    public PayHeads updatePayHead(Long payHeadId, String payHeadName, String payHeadDescription, String payHeadType) {
        PayHeads updatePayhead = payHeadsRepository.findById(payHeadId)
                .orElseThrow(() -> new RuntimeException("PayHeadId not found"));

        if (payHeadName != null && !payHeadName.isEmpty()) {
            updatePayhead.setPayHeadName(payHeadName);
        }
        if (payHeadDescription != null && !payHeadDescription.isEmpty()) {
            updatePayhead.setPayHeadDescription(payHeadDescription);

        }

        if (payHeadType != null && !payHeadType.isEmpty()) {
            updatePayhead.setPayHeadType(payHeadType);
        }

        return payHeadsRepository.save(updatePayhead);
    }

    // Delete
    public void deletePayHead(Long payHeadId) {
        payHeadsRepository.deleteById(payHeadId);
    }
}
