package com.dashboard.doctor_dashboard.Service.patient_service;

import org.springframework.stereotype.Service;

@Service
public interface AttributeService {
    public void changeNotes(Long id,String notes);
}
