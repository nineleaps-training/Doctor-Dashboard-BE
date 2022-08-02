package com.dashboard.doctor_dashboard.services.prescription;

import com.dashboard.doctor_dashboard.dtos.PatientDto;
import com.dashboard.doctor_dashboard.dtos.UpdatePrescriptionDto;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.PrescriptionRepository;
import com.dashboard.doctor_dashboard.services.impl.MailServiceImpl;
import com.dashboard.doctor_dashboard.services.impl.PdFGeneratorServiceImpl;
import com.dashboard.doctor_dashboard.util.Constants;
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

/**
 * implementation of PrescriptionService interface
 */
@Service
@Slf4j
public class PrescriptionServiceImpl implements PrescriptionService   {


    private final PrescriptionRepository prescriptionRepository;


    private final AppointmentRepository appointmentRepository;


    private final AttributeRepository attributeRepository;
    private final MailServiceImpl mailService;

    private final PdFGeneratorServiceImpl pdFGeneratorService;


    @Autowired
    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, AppointmentRepository appointmentRepository, AttributeRepository attributeRepository, PdFGeneratorServiceImpl pdFGeneratorService,MailServiceImpl mailService) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.attributeRepository = attributeRepository;
        this.pdFGeneratorService = pdFGeneratorService;
        this.mailService=mailService;
    }

    /**
     * This function of service is for adding prescription of patient.
     * @param appointId
     * @param updatePrescriptionDto which contains patientDto,prescription status,notes etc
     * @return ResponseEntity<GenericMessage> with status code 201.
     * @throws IOException
     * @throws MessagingException
     * @throws JSONException
     */
    @Override
    public ResponseEntity<GenericMessage> addPrescription(Long appointId, UpdatePrescriptionDto updatePrescriptionDto) throws IOException, MessagingException, JSONException {
        log.info("inside: PrescriptionServiceImpl::addPrescription");
        if (appointmentRepository.getId(appointId) != null) {
            if(appointmentRepository.checkStatus(appointId).equals("Vitals updated")){
                if (appointId.equals(updatePrescriptionDto.getPrescriptions().get(0).getAppointment().getAppointId())) {

                    appointmentRepository.changeAppointmentStatus(appointId, updatePrescriptionDto.getStatus());
                    attributeRepository.changeNotes(appointId, updatePrescriptionDto.getNotes());
                    prescriptionRepository.saveAll(updatePrescriptionDto.getPrescriptions());
                    pdFGeneratorService.generatePdf(updatePrescriptionDto.getPrescriptions(), updatePrescriptionDto.getPatientDto(), updatePrescriptionDto.getNotes());
                    sendEmailToUserAfterPrescription(updatePrescriptionDto.getPatientDto());
                    log.debug(Constants.PRESCRIPTION_CREATED);
                    log.info("exit: PrescriptionServiceImpl::addPrescription");
                    return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,Constants.PRESCRIPTION_CREATED),HttpStatus.CREATED);
                }
                log.info("PrescriptionServiceImpl::addPrescription "+Constants.APPOINTMENT_NOT_FOUND);

                throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);
            }
            else {
                log.info("PrescriptionServiceImpl::addPrescription");
                throw new APIException("Prescription cannot be added for other status like completed,follow Up, and to be attended");

            }
        }
        log.info("PrescriptionServiceImpl::addPrescription"+Constants.APPOINTMENT_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);
    }

    /**
     * This function of service is for getting all prescription of patient by appointment id
     * @param appointId
     * @return ResponseEntity<GenericMessage> with status code 200 and list of prescription
     */
    @Override
    public ResponseEntity<GenericMessage> getAllPrescriptionByAppointment(Long appointId) {
        log.info("inside: PrescriptionServiceImpl::getAllPrescriptionByAppointment");

        if(appointmentRepository.getId(appointId) != null){
            log.info("exit: PrescriptionServiceImpl::getAllPrescriptionByAppointment");
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,prescriptionRepository.getAllPrescriptionByAppointment(appointId)),HttpStatus.OK);
        }
        log.info("PatientServiceImpl::viewAppointment"+Constants.APPOINTMENT_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);

    }

    /**
     * This function of service is for deleting appointment by id
     * @param id
     * @return ResponseEntity<GenericMessage> with status code 204 and message successfully deleted.
     */
    @Override
    public ResponseEntity<GenericMessage> deleteAppointmentById(Long id) {
        var genericMessage = new GenericMessage();
        prescriptionRepository.deleteById(id);
        genericMessage.setData("successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        log.info("exit: PrescriptionServiceImpl::deleteAppointmentById");
        return new ResponseEntity<>(genericMessage, HttpStatus.OK);
    }


    /**
     * This function of service is for sending mail to user after successfully updating his prescription.
     * @param patientDto which contains field category,doctorName,status,patientName etc
     * @throws JSONException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendEmailToUserAfterPrescription(PatientDto patientDto) throws JSONException, MessagingException, UnsupportedEncodingException {
        log.info("inside: PrescriptionServiceImpl::sendEmailToUserAfterPrescription");
        String toEmail = patientDto.getPatientEmail();
        var fromEmail = "mecareapplication@gmail.com";
        var senderName = "meCare Application";
        var subject = "Prescription Updated";

        String content = Constants.MAIL_PRESCRIPTION;

        content = content.replace("[[name]]", patientDto.getPatientName());
        content = content.replace("[[doctorName]]", patientDto.getDoctorName());

        mailService.mailServiceHandler(fromEmail,toEmail,senderName,subject,content);
        log.info("exit: PrescriptionServiceImpl::sendEmailToUserAfterPrescription");

    }


}
