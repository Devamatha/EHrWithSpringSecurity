package com.techpixe.ehr.controller;

import com.techpixe.ehr.dto.AddJobDetailsDto;
import com.techpixe.ehr.entity.AddJobDetails;
import com.techpixe.ehr.service.AddJobDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/JobDetails")
@RestController
public class AddJobDetailsController {
    @Autowired
    private AddJobDetailsService addJobDetailsService;

    @PostMapping("/addJob/{user_Id}")
    public ResponseEntity<AddJobDetails> createJobDetails(@RequestBody AddJobDetails jobDetails,
                                                          @PathVariable Long user_Id) {
        //jobDetails.setJobkeyskills(jobDetails.getJobkeyskills());
        AddJobDetails createdJobDetails = addJobDetailsService.saveJobDetails(jobDetails, user_Id);
        //System.err.println(jobDetails.getJobkeyskills());
        return ResponseEntity.ok(createdJobDetails);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddJobDetails> getJobDetailsById(@PathVariable("id") Long jobId) {
        Optional<AddJobDetails> jobDetails = addJobDetailsService.getJobDetailsById(jobId);
        return jobDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AddJobDetails>> getAllJobDetails() {
        List<AddJobDetails> jobDetailsList = addJobDetailsService.getAllJobDetails();
        return ResponseEntity.ok(jobDetailsList);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteJobDetails(@PathVariable("id") Long jobId) {
        addJobDetailsService.deleteJobDetails(jobId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/update/{jobId}")
    public ResponseEntity<AddJobDetails> updateJobDetails(@PathVariable long jobId,
                                                          @RequestBody AddJobDetailsDto updateDto) {
        AddJobDetails updatedJobDetails = addJobDetailsService.updateJobDetails(jobId, updateDto);
        return ResponseEntity.ok(updatedJobDetails);
    }
}