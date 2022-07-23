package com.dashboard.doctor_dashboard.services.receptionist;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.dtos.AttributesDto;
import com.dashboard.doctor_dashboard.entities.dtos.PageRecords;
import com.dashboard.doctor_dashboard.entities.dtos.PatientViewDto;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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

    private ModelMapper mapper;

    private DoctorRepository doctorRepository;

    private AppointmentRepository appointmentRepository;

    private AttributeRepository attributeRepository;

    @Autowired
    public ReceptionistServiceImpl(ModelMapper mapper, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, AttributeRepository attributeRepository) {
        this.mapper = mapper;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.attributeRepository = attributeRepository;
    }

    @Override
    public ResponseEntity<GenericMessage> getDoctorDetails() {
        log.info("inside: ReceptionistServiceImpl::getDoctorDetails");
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, doctorRepository.getDoctorDetails()),HttpStatus.OK);

    }

    @Override
    public ResponseEntity<GenericMessage> getDoctorAppointments(Long doctorId,int pageNo,int pageSize) {
        log.info("inside: ReceptionistServiceImpl::getDoctorAppointments");

        Pageable paging = PageRequest.of(pageNo, pageSize);
        if(doctorRepository.isIdAvailable(doctorId) != null) {
            Page<Appointment> appointmentList = appointmentRepository.receptionistDoctorAppointment(doctorId,paging);
            List<PatientViewDto> patientViewDto = appointmentList.toList().stream()
                    .map(this::mapToDto2).collect(Collectors.toList());
            PageRecords pageRecords=new PageRecords(patientViewDto,pageNo,pageSize,appointmentList.getTotalElements(),appointmentList.getTotalPages(),appointmentList.isLast());

            log.info("exit: ReceptionistServiceImpl::getDoctorAppointments");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, pageRecords), HttpStatus.OK);
        }
        log.info("ReceptionistServiceImpl::getDoctorAppointments"+Constants.APPOINTMENT_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }



    @Override
    public ResponseEntity<GenericMessage> todayAllAppointmentForClinicStaff(int pageNo,int pageSize) {
        log.info("inside: ReceptionistServiceImpl::todayAllAppointmentForClinicStaff");

        List<Appointment> appointments = new ArrayList<>();
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Appointment> appointmentList1 = appointmentRepository.todayAllAppointmentForClinicStaff1(paging);
        Page<Appointment> appointmentList2 = appointmentRepository.todayAllAppointmentForClinicStaff2(paging);
        appointments.addAll(appointmentList1.toList());
        appointments.addAll(appointmentList2.toList());

        List<PatientViewDto> patientViewDto = appointments.stream()
                .map(this::mapToDto2).collect(Collectors.toList());
        PageRecords pageRecords=new PageRecords(patientViewDto,pageNo,pageSize,appointmentList1.getTotalElements()+appointmentList2.getTotalElements(),appointmentList1.getTotalPages()+appointmentList2.getTotalPages(), appointmentList1.isLast() && appointmentList2.isLast());
        log.info("exit: ReceptionistServiceImpl::todayAllAppointmentForClinicStaff");

        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS , pageRecords),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> addAppointmentVitals(AttributesDto attributesDto, Long appointmentId) {
        log.info("inside: ReceptionistServiceImpl::addAppointmentVitals");

        if(appointmentRepository.existsById(appointmentId)){
            if(attributeRepository.checkAppointmentPresent(appointmentId) == null){
                appointmentRepository.setStatus("Vitals updated",appointmentId);
                attributeRepository.save(mapper.map(attributesDto,Attributes.class));
                log.info("exit: ReceptionistServiceImpl::addAppointmentVitals");

                return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,"successful"), HttpStatus.OK);
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
