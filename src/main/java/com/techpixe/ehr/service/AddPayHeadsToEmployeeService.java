package com.techpixe.ehr.service;

import com.techpixe.ehr.entity.AddPayHeadsToEmployee;

import java.util.List;
import java.util.Optional;

public interface AddPayHeadsToEmployeeService {
    AddPayHeadsToEmployee createAddPayHeadsToEmployee(AddPayHeadsToEmployee addPayHeadsToEmployee);

    List<AddPayHeadsToEmployee> getAllAddPayHeadsToEmployee();

    Optional<AddPayHeadsToEmployee> getAddPayHeadsToEmployeeById(Long id);

    AddPayHeadsToEmployee updateAddPayHeadsToEmployee(Long id,
                                                      AddPayHeadsToEmployee addPayHeadsToEmployeeDetails);

    void deleteAddPayHeadsToEmployee(Long id);

}
