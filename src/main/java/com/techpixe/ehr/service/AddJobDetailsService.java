package com.techpixe.ehr.service;

import com.techpixe.ehr.dto.AddJobDetailsDto;
import com.techpixe.ehr.entity.AddJobDetails;

import java.util.List;
import java.util.Optional;

public interface AddJobDetailsService {
    AddJobDetails saveJobDetails(AddJobDetails jobDetails, Long user_Id)throws Exception;

    AddJobDetails getJobDetailsById(Long jobId);


    void deleteJobDetails(Long jobId);

    AddJobDetails updateJobDetails(Long jobId, AddJobDetailsDto updateDto)throws Exception ;


}