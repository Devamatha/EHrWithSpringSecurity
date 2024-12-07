package com.techpixe.ehr.serviceimpl;

import com.techpixe.ehr.dto.AddJobDetailsDto;
import com.techpixe.ehr.entity.AddJobDetails;
import com.techpixe.ehr.entity.HR;
import com.techpixe.ehr.repository.AddJobDetailsRepository;
import com.techpixe.ehr.repository.UserRepository;
import com.techpixe.ehr.service.AddJobDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AddJobDetailsServiceImpl implements AddJobDetailsService {

	@Autowired
	private AddJobDetailsRepository addJobDetailsRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public AddJobDetails saveJobDetails(AddJobDetails jobDetails, Long user_Id) throws Exception {

		try {

			HR userId = userRepository.findById(user_Id)
					.orElseThrow(() -> new RuntimeException("HR ID  " + user_Id + " is not found"));

			jobDetails.setCreatedAt(LocalDate.now());
			jobDetails.setUser(userId);
			return addJobDetailsRepository.save(jobDetails);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public AddJobDetails getJobDetailsById(Long jobId) {
		return addJobDetailsRepository.findByjob(jobId);
	}

	

	@Override
	public void deleteJobDetails(Long jobId) {
		addJobDetailsRepository.deleteById(jobId);
	}

	@Override
	public AddJobDetails updateJobDetails(Long jobId, AddJobDetailsDto updateDto) throws Exception {
		try {
		AddJobDetails existingJobDetails = addJobDetailsRepository.findById(jobId)
				.orElseThrow(() -> new RuntimeException("Job not found"));

		existingJobDetails.setJobTitle(updateDto.getJobTitle());
		// existingJobDetails.setJobkeyskills(updateDto.getJobkeyskills());
		existingJobDetails.setCreatedAt(updateDto.getCreatedAt());
		existingJobDetails.setYearsOfExperience(updateDto.getYearsOfExperience());
		existingJobDetails.setNoOfVacancies(updateDto.getNoOfVacancies());
		String formattedPercentage = updateDto.getOverallPercentage();
		existingJobDetails.setOverallPercentage(formattedPercentage);

		return addJobDetailsRepository.save(existingJobDetails);
		}catch(Exception e) {
			throw new Exception(e.getMessage());
		}
	}

}