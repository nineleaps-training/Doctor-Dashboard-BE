package com.dashboard.doctor_dashboard.services.patient_service.impl;

import com.dashboard.doctor_dashboard.entities.Patient;
import com.dashboard.doctor_dashboard.entities.dtos.AppointmentViewDto;
import com.dashboard.doctor_dashboard.entities.dtos.PatientEntityDto;
import com.dashboard.doctor_dashboard.entities.dtos.UserDetailsUpdateDto;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.*;
import com.dashboard.doctor_dashboard.services.patient_service.PatientService;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * implementation of PatientService interface
 */
@Service
@Slf4j
public class PatientServiceImpl implements PatientService {


    private PatientRepository patientRepository;


    private AttributeRepository attributeRepository;


    private DoctorRepository doctorRepository;

    private AppointmentRepository appointmentRepository;

    private PrescriptionRepository prescriptionRepository;


    private LoginRepo loginRepo;






    private ModelMapper mapper;
    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, AttributeRepository attributeRepository, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, PrescriptionRepository prescriptionRepository, LoginRepo loginRepo, ModelMapper mapper) {
        this.patientRepository = patientRepository;
        this.attributeRepository = attributeRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.loginRepo = loginRepo;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<GenericMessage> addPatient(PatientEntityDto patient, Long loginId) {
        log.info("inside: PatientServiceImpl::addPatient");
        var genericMessage = new GenericMessage();

        Long temp = loginRepo.isIdAvailable(loginId);
        if(temp != null){
            patientRepository.
                    insertIntoPatient(patient.getAge(),patient.getMobileNo(),patient.getAlternateMobileNo(),
                            patient.getGender(), patient.getAddress(), patient.getBloodGroup(),loginId);
            var patientDetails = patientRepository.getPatientByLoginId(loginId);
            genericMessage.setData(mapToDto(patientDetails));
            genericMessage.setStatus(Constants.SUCCESS);
            log.debug(Constants.PATIENT+": On boarding completed..");
            log.info("exit: PatientServiceImpl::addPatient");
            return new ResponseEntity<>(genericMessage, HttpStatus.OK) ;
        }else {
            log.info("PatientServiceImpl::addPatient"+Constants.LOGIN_DETAILS_NOT_FOUND);
            throw new ResourceNotFoundException(Constants.LOGIN_DETAILS_NOT_FOUND);
        }
    }


    @Override
    public ResponseEntity<GenericMessage> getPatientDetailsById(Long loginId) {
        log.info("inside: PatientServiceImpl::getPatientDetailsById");

        var genericMessage = new GenericMessage();

        if(loginRepo.isIdAvailable(loginId) != null){
            var patientDetails = patientRepository.getPatientByLoginId(loginId);
            genericMessage.setData(mapToDto(patientDetails));
            genericMessage.setStatus(Constants.SUCCESS);
            log.info("exit: PatientServiceImpl::getPatientDetailsById");
            return new ResponseEntity<>(genericMessage, HttpStatus.OK) ;
        }else {
            log.info("PatientServiceImpl::getPatientDetailsById"+Constants.LOGIN_DETAILS_NOT_FOUND);
            throw new ResourceNotFoundException(Constants.LOGIN_DETAILS_NOT_FOUND);
        }

    }



    @Override
    public ResponseEntity<GenericMessage> deletePatientById(Long id) {
        log.info("inside: PatientServiceImpl::deletePatientById");

        patientRepository.deleteById(id);
        log.debug(Constants.PATIENT+": patient deleted.");
        log.info("exit: PatientServiceImpl::deletePatientById");
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,"successfully deleted"),HttpStatus.OK);
    }




    // convert entity to dto
    private PatientEntityDto mapToDto(Patient patient) {
        return mapper.map(patient, PatientEntityDto.class);
    }


    @Override
    public ResponseEntity<GenericMessage> updatePatientDetails(Long id, UserDetailsUpdateDto patient) {
        log.info("inside: PatientServiceImpl::updatePatientDetails");


        var genericMessage = new GenericMessage();

        if (loginRepo.existsById(patient.getId()) && patientRepository.getId(patient.getId())!=null) {

            patientRepository.updateMobileNo(patient.getMobileNo(),patient.getId());
            genericMessage.setStatus(Constants.SUCCESS);
            log.debug(Constants.PATIENT+": Updated completed..");
            log.info("exit: PatientServiceImpl::updatePatientDetails");
            return new ResponseEntity<>(genericMessage, HttpStatus.OK);

        } else {
            log.info("PatientServiceImpl::updatePatientDetails"+Constants.PATIENT);
            throw new ResourceNotFoundException(Constants.PATIENT);
        }
    }

    @Override
    public ResponseEntity<GenericMessage> getNotifications(long patientId) {

        log.info("inside: PatientServiceImpl::getNotifications");

        if (loginRepo.existsById(patientId)&&patientRepository.getId(patientId)!=null) {
            log.info("exit: PatientServiceImpl::getNotifications");
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.getNotifications(patientRepository.getId(patientId))), HttpStatus.OK);
        }
        log.info("PatientServiceImpl::getNotifications"+Constants.PATIENT_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND);
    }
    @Override
    public ResponseEntity<GenericMessage> viewAppointment(Long appointmentId,long patientId){
        log.info("inside: PatientServiceImpl::viewAppointment");

        if(patientRepository.getId(patientId)!=null){
            if(appointmentRepository.findById(appointmentId).isPresent() && appointmentRepository.getDoctorId(appointmentId) != null ){

                Long doctorId = doctorRepository.isIdAvailable(appointmentRepository.getDoctorId(appointmentId));
                if(doctorId != null) {
                    AppointmentViewDto viewDto =appointmentRepository.getBasicAppointmentDetails(appointmentId,patientRepository.getId(patientId));
                    viewDto.setEmail(appointmentRepository.getEmailById(doctorId));
                    viewDto.setAttributes(attributeRepository.getAttribute(appointmentId));
                    viewDto.setPrescription(prescriptionRepository.getAllPrescriptionByAppointment(appointmentId));

                    log.info("exit: PatientServiceImpl::viewAppointment");
                    return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, viewDto), HttpStatus.OK);
                }
                else{
                    log.info("PatientServiceImpl::viewAppointment"+Constants.DOCTOR_NOT_FOUND);
                    throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);}
            }
            else{
                log.info("PatientServiceImpl::viewAppointment"+Constants.APPOINTMENT_NOT_FOUND);
                throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);}
        }
        else{
            log.info("PatientServiceImpl::viewAppointment"+Constants.PATIENT_NOT_FOUND);
            throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND);}
    }

}
