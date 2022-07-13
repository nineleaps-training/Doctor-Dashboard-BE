package com.dashboard.doctor_dashboard.services.prescription_service;

import com.dashboard.doctor_dashboard.entities.dtos.PatientDto;
import com.dashboard.doctor_dashboard.entities.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.PrescriptionRepository;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.MailServiceImpl;
import com.dashboard.doctor_dashboard.util.PdFGeneratorServiceImpl;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
@Slf4j
public class PrescriptionServiceImpl implements PrescriptionService   {


    private PrescriptionRepository prescriptionRepository;


    private AppointmentRepository appointmentRepository;


    private AttributeRepository attributeRepository;
    private MailServiceImpl mailService;

    private PdFGeneratorServiceImpl pdFGeneratorService;


    @Autowired
    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, AppointmentRepository appointmentRepository, AttributeRepository attributeRepository, PdFGeneratorServiceImpl pdFGeneratorService,MailServiceImpl mailService) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.attributeRepository = attributeRepository;
        this.pdFGeneratorService = pdFGeneratorService;
        this.mailService=mailService;
    }

    @Override
    public ResponseEntity<GenericMessage> addPrescription(Long appointId, UpdatePrescriptionDto updatePrescriptionDto) throws IOException, MessagingException, JSONException {

        if (appointmentRepository.getId(appointId) != null) {
            if(appointmentRepository.checkStatus(appointId).equals("Vitals updated")){
                if (appointId.equals(updatePrescriptionDto.getPrescriptions().get(0).getAppointment().getAppointId())) {

                    appointmentRepository.changeAppointmentStatus(appointId, updatePrescriptionDto.getStatus());
                    attributeRepository.changeNotes(appointId, updatePrescriptionDto.getNotes());
                    prescriptionRepository.saveAll(updatePrescriptionDto.getPrescriptions());
                    pdFGeneratorService.generatePdf(updatePrescriptionDto.getPrescriptions(), updatePrescriptionDto.getPatientDto(), updatePrescriptionDto.getNotes());
                    sendEmailToUserAfterPrescription(updatePrescriptionDto.getPatientDto());
                    log.debug(Constants.PRESCRIPTION_CREATED);
                    return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,Constants.PRESCRIPTION_CREATED),HttpStatus.CREATED);
                }
                throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);
            }
            else
                throw new APIException("Prescription cannot be added for other status like completed,follow Up, and to be attended");
        }
        throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);
    }

    @Override
    public ResponseEntity<GenericMessage> getAllPrescriptionByAppointment(Long appointId) {
        if(appointmentRepository.getId(appointId) != null){
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,prescriptionRepository.getAllPrescriptionByAppointment(appointId)),HttpStatus.OK);
        }
        throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);

    }

    @Override
    public ResponseEntity<GenericMessage> deleteAppointmentById(Long id) {
        var genericMessage = new GenericMessage();
        prescriptionRepository.deleteById(id);
        genericMessage.setData("successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage, HttpStatus.OK);
    }


    public void sendEmailToUserAfterPrescription(PatientDto patientDto) throws JSONException, MessagingException, UnsupportedEncodingException {
        log.info("Prescription Mail Service Started");
        String toEmail = patientDto.getPatientEmail();
        var fromEmail = "mecareapplication@gmail.com";
        var senderName = "meCare Application";
        var subject = "Prescription Updated";

        String content = Constants.MAIL_PRESCRIPTION;

        content = content.replace("[[name]]", patientDto.getPatientName());
        content = content.replace("[[doctorName]]", patientDto.getDoctorName());

        mailService.mailServiceHandler(fromEmail,toEmail,senderName,subject,content);
    }


}
