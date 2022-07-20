package com.dashboard.doctor_dashboard.services.prescription_service;

import com.dashboard.doctor_dashboard.entities.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;

/**
 * interface for prescription service layer.
 */
@Service
public interface PrescriptionService {

    ResponseEntity<GenericMessage> addPrescription(Long id, UpdatePrescriptionDto updatePrescriptionDto) throws IOException, MessagingException, JSONException;

    ResponseEntity<GenericMessage> getAllPrescriptionByAppointment(Long appointId);

    ResponseEntity<GenericMessage> deleteAppointmentById(Long id);
}
