package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.entities.*;
import org.codehaus.jettison.json.JSONException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

/**
 * interface for prescription service layer.
 */
@Service
public interface PrescriptionService {

    String addPrescription(Long id, UpdatePrescriptionDto updatePrescriptionDto) throws IOException, MessagingException, JSONException;

    List<Prescription> getAllPrescriptionByAppointment(Long appointId);

    String deleteAppointmentById(Long id);
}