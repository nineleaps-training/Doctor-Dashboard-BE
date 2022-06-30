package com.dashboard.doctor_dashboard.services.patient_service.impl;

import com.dashboard.doctor_dashboard.entities.Patient;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.*;
import com.dashboard.doctor_dashboard.services.patient_service.PatientService;
import com.dashboard.doctor_dashboard.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private LoginRepo loginRepo;



    public static final String PATIENT = "Patient";


    @Autowired
    private ModelMapper mapper;


    @Override
    public ResponseEntity<GenericMessage> addPatient(PatientEntityDto patient,Long loginId) {

        GenericMessage genericMessage = new GenericMessage();

        Long temp = loginRepo.isIdAvailable(loginId);
        if(temp != null){
            patientRepository.
                    insertIntoPatient(patient.getAge(),patient.getMobileNo(),patient.getAlternateMobileNo(),
                            patient.getGender(), patient.getAddress(), patient.getBloodGroup(),loginId);
            var patientDetails = patientRepository.getPatientByLoginId(loginId);
            genericMessage.setData(mapToDto(patientDetails));
            genericMessage.setStatus(Constants.SUCCESS);
            log.debug("Patient: On boarding completed..");
            return new ResponseEntity<>(genericMessage, HttpStatus.OK) ;
        }else {
            throw new ResourceNotFoundException(Constants.LOGIN_DETAILS_NOT_FOUND, "id", loginId);
        }
    }


    @Override
    public ResponseEntity<GenericMessage> getPatientDetailsById(Long loginId) {
        GenericMessage genericMessage = new GenericMessage();

        if(loginRepo.isIdAvailable(loginId) != null){
            var patientDetails = patientRepository.getPatientByLoginId(loginId);
            genericMessage.setData(mapToDto(patientDetails));
            genericMessage.setStatus(Constants.SUCCESS);

            return new ResponseEntity<>(genericMessage, HttpStatus.OK) ;
        }else {
            throw new ResourceNotFoundException(Constants.LOGIN_DETAILS_NOT_FOUND, "id", loginId);
        }

    }



    @Override
    public ResponseEntity<GenericMessage> deletePatientById(Long id) {
        patientRepository.deleteById(id);
        log.debug("Patient: patient deleted.");
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,"successfully deleted"),HttpStatus.OK);
    }




    // convert entity to dto
    private PatientEntityDto mapToDto(Patient patient) {
        return mapper.map(patient, PatientEntityDto.class);
    }


    @Override
    public ResponseEntity<GenericMessage> updatePatientDetails(Long id, PatientDetailsUpdateDto patient) {

        GenericMessage genericMessage = new GenericMessage();

        if (loginRepo.existsById(patient.getPatientId()) && patientRepository.getId(patient.getPatientId())!=null) {

            patientRepository.updateMobileNo(patient.getMobileNo(),patient.getPatientId());
            genericMessage.setStatus(Constants.SUCCESS);
            log.debug("Patient: Updated completed..");
            return new ResponseEntity<>(genericMessage, HttpStatus.OK);

        } else {
            throw new ResourceNotFoundException(PATIENT, "id", id);
        }
    }

    @Override
    public ResponseEntity<GenericMessage> getNotifications(long patientId) {
            if (loginRepo.existsById(patientId)&&patientRepository.getId(patientId)!=null) {

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.getNotifications(patientRepository.getId(patientId))), HttpStatus.OK);
        }
        throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND,"id",patientId);
    }
    @Override
    public ResponseEntity<GenericMessage> viewAppointment(Long appointmentId,long patientId){

        if(loginRepo.isIdAvailable(patientId)!=null&&patientRepository.getId(patientId)!=null){
            if(attributeRepository.findById(appointmentId).isPresent()){

                AppointmentViewDto viewDto =appointmentRepository.getBasicAppointmentDetails(appointmentId,patientRepository.getId(patientId));

                Long doctorId=doctorRepository.isIdAvailable(appointmentRepository.getDoctorId(appointmentId));
                viewDto.setEmail(appointmentRepository.getEmailById(doctorId));
                viewDto.setAttributes(attributeRepository.getAttribute(appointmentId));
                viewDto.setPrescription(prescriptionRepository.getAllPrescriptionByAppointment(appointmentId));
                return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,viewDto),HttpStatus.OK);
            }
            else {
                throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND,"id",appointmentId);
            }
        }else {
            throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND,"id",patientId);
        }
    }

}
