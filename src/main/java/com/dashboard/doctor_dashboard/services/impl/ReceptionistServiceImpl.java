package com.dashboard.doctor_dashboard.services.impl;

import com.dashboard.doctor_dashboard.dtos.*;
import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.services.ReceptionistService;
import com.dashboard.doctor_dashboard.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * implementation of ReceptionistService interface
 */
@Service
@Slf4j
public class ReceptionistServiceImpl implements ReceptionistService {

    private final ModelMapper mapper;

    private final DoctorRepository doctorRepository;

    private final AppointmentRepository appointmentRepository;

    private final AttributeRepository attributeRepository;

    @Autowired
    public ReceptionistServiceImpl(ModelMapper mapper, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, AttributeRepository attributeRepository) {
        this.mapper = mapper;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.attributeRepository = attributeRepository;
    }

    /**
     * This function of service is for getting all the doctor present.
     *
     * @return ResponseEntity<GenericMessage> with status code 200 and list doctor present in the database.
     */
    @Override
    public List<DoctorDropdownDto> getDoctorDetails() {
        log.info("inside: ReceptionistServiceImpl::getDoctorDetails");
        return doctorRepository.getDoctorDetails();

    }

    /**
     * This function of service is for getting all the appointments of the doctor
     *
     * @param doctorId
     * @param pageNo
     * @return ResponseEntity<GenericMessage> with status code 200 and list of appointments for the particular doctor
     */
    @Override
    public PageRecords getDoctorAppointments(Long doctorId, int pageNo, int pageSize) {
        log.info("inside: ReceptionistServiceImpl::getDoctorAppointments");

        Pageable paging = PageRequest.of(pageNo, pageSize);
        if(doctorRepository.isIdAvailable(doctorId) != null) {
            Page<Appointment> appointmentList = appointmentRepository.receptionistDoctorAppointment(doctorId,paging);
            List<PatientViewDto> patientViewDto = appointmentList.toList().stream()
                    .map(this::mapToDto2).collect(Collectors.toList());
            var pageRecords=new PageRecords(patientViewDto,pageNo,pageSize,appointmentList.getTotalElements(),appointmentList.getTotalPages(),appointmentList.isLast());

            log.info("exit: ReceptionistServiceImpl::getDoctorAppointments");

            return pageRecords;
        }
        log.info("ReceptionistServiceImpl::getDoctorAppointments"+Constants.APPOINTMENT_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }


    /**
     * This function of service is for getting all the today's appointments present for vitals update.
     *
     * @param pageNo
     * @return ResponseEntity<GenericMessage> with status code 200 and list of today appointments
     */
    @Override
    public PageRecords todayAllAppointmentForClinicStaff(int pageNo, int pageSize) {
        log.info("inside: ReceptionistServiceImpl::todayAllAppointmentForClinicStaff");

        List<Appointment> appointments = new ArrayList<>();
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Appointment> appointmentList1 = appointmentRepository.todayAllAppointmentForClinicStaff1(paging);
        Page<Appointment> appointmentList2 = appointmentRepository.todayAllAppointmentForClinicStaff2(paging);
        appointments.addAll(appointmentList1.toList());
        appointments.addAll(appointmentList2.toList());

        List<PatientViewDto> patientViewDto = appointments.stream()
                .map(this::mapToDto2).collect(Collectors.toList());
        var pageRecords=new PageRecords(patientViewDto,pageNo,pageSize,appointmentList1.getTotalElements()+appointmentList2.getTotalElements(),appointmentList1.getTotalPages()+appointmentList2.getTotalPages(), appointmentList1.isLast() && appointmentList2.isLast());
        log.info("exit: ReceptionistServiceImpl::todayAllAppointmentForClinicStaff");

        return  pageRecords;
    }


    /**
     * This function of service is for adding vitals of patients.
     *
     * @param attributesDto which contains fields bloodGroup,bodyTemp,notes and glucose level...
     * @param appointmentId
     * @return ResponseEntity<GenericMessage> with status code 201.
     */
    @Override
    public String addAppointmentVitals(AttributesDto attributesDto, Long appointmentId) {
        log.info("inside: ReceptionistServiceImpl::addAppointmentVitals");

        if(appointmentRepository.existsById(appointmentId)){
            if(attributeRepository.checkAppointmentPresent(appointmentId) == null){
                appointmentRepository.setStatus("Vitals updated",appointmentId);
                attributeRepository.save(mapper.map(attributesDto,Attributes.class));
                log.info("exit: ReceptionistServiceImpl::addAppointmentVitals");

                return "successful";
            }
            else {
                log.info("ReceptionistServiceImpl::addAppointmentVitals"+Constants.APPOINTMENT_NOT_FOUND);
                throw new APIException("update not allowed in this API endpoint.");
            }
        }
        log.info("ReceptionistServiceImpl::addAppointmentVitals"+Constants.APPOINTMENT_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);

    }

    private PatientViewDto mapToDto2(Appointment appointment) {
        return mapper.map(appointment, PatientViewDto.class);
    }

}