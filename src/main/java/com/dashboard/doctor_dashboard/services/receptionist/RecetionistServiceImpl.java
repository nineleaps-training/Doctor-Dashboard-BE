package com.dashboard.doctor_dashboard.services.receptionist;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientViewDto;

import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecetionistServiceImpl implements ReceptionistService {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    AttributeRepository attributeRepository;
    @Override
    public ResponseEntity<GenericMessage> getDoctorDetails(int pageNo) {
        Pageable paging = PageRequest.of(pageNo, 10);
      return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, doctorRepository.getDoctorDetails(paging).toList()),HttpStatus.OK);

    }

    @Override
    public ResponseEntity<GenericMessage> getDoctorAppointments(Long doctorId,int pageNo) {
        Pageable paging = PageRequest.of(pageNo, 10);

        List<Appointment> appointmentList = appointmentRepository.receptionistDoctorAppointment(doctorId,paging).toList();

        List<PatientViewDto> patientViewDto = appointmentList.stream()
                .map(this::mapToDto2).collect(Collectors.toList());
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS , patientViewDto),HttpStatus.OK);
    }




//    @Override
//    public ResponseEntity<GenericMessage> updateAppointmentVitals(VitalsDto attributes, long appointmentId) {
//        if(appointmentRepository.existsById(appointmentId)){
//            attributeRepository.updateVitals(attributes.getBloodPressure(),attributes.getBodyTemp(),attributes.getGlucoseLevel(),appointmentId);
//            appointmentRepository.setStatus("Vitals updated",appointmentId);
//            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,"successful"), HttpStatus.OK);
//        }
//
//            throw new ResourceNotFoundException("appointments","id",appointmentId);
//
//    }

    @Override
    public ResponseEntity<GenericMessage> todayAllAppointmentForClinicStaff(int pageNo) {
        List<Appointment> appointments = new ArrayList<>();
        Pageable paging = PageRequest.of(pageNo, 10);
        List<Appointment> appointmentList1 = appointmentRepository.todayAllAppointmentForClinicStaff1(paging).toList();
        List<Appointment> appointmentList2 = appointmentRepository.todayAllAppointmentForClinicStaff2(paging).toList();
        appointments.addAll(appointmentList1);
        appointments.addAll(appointmentList2);

        List<PatientViewDto> patientViewDto = appointments.stream()
                .map(this::mapToDto2).collect(Collectors.toList());
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS , patientViewDto),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> addAppointmentVitals(Attributes vitalsDto, Long appointmentId) {
        if(appointmentRepository.existsById(appointmentId)){
            if(attributeRepository.checkAppointmentPresent(appointmentId) == null){
                appointmentRepository.setStatus("Vitals updated",appointmentId);
                attributeRepository.save(vitalsDto);
               return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,"successful"), HttpStatus.OK);
             }
            else
                throw new APIException(HttpStatus.BAD_REQUEST,"update not allowed in this API endpoint.");

        }
        throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND,"id",appointmentId);

    }

    private PatientViewDto mapToDto2(Appointment appointment) {
        return mapper.map(appointment, PatientViewDto.class);
    }

}
