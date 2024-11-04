package com.techpixe.ehr.service;

import com.techpixe.ehr.entity.PayHeads;

import java.util.List;
import java.util.Optional;

public interface PayHeadsService {
    PayHeads createPayHead(PayHeads payHead);

    List<PayHeads> getAllPayHeads();

    Optional<PayHeads> getPayHeadById(Long payHeadId);

    PayHeads updatePayHead(Long payHeadId, String payHeadName, String payHeadDescription, String payHeadType);

    void deletePayHead(Long payHeadId);

}
