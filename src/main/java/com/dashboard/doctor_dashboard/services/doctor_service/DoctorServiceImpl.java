package com.dashboard.doctor_dashboard.services.doctor_service;


import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.exceptions.APIException;

import com.dashboard.doctor_dashboard.entities.dtos.*;

import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;


    @Autowired
    private LoginRepo loginRepo;


    @Autowired
    JwtTokenProvider jwtTokenProvider;


    @Override
    public DoctorDetails addDoctor(DoctorDetails details) {
        return doctorRepository.save(details);
    }

    @Override
    public ResponseEntity<GenericMessage> getAllDoctors(Long id) {

        GenericMessage genericMessage = new GenericMessage();

        if (doctorRepository.isIdAvailable(id) != null) {
            List<DoctorListDto> list = doctorRepository.getAllDoctors(id);
            genericMessage.setData(list);
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }


        throw new ResourceNotFoundException("doctor", "id", id);

    }

    @Override
     public ResponseEntity<GenericMessage> getDoctorById(long id) {

        GenericMessage genericMessage = new GenericMessage();

        if (doctorRepository.isIdAvailable(id) != null) {
            genericMessage.setData(doctorRepository.findDoctorById(id));
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }

        return null;
    }

    @Override
    public ResponseEntity<GenericMessage> addDoctorDetails(DoctorFormDto details, long id, HttpServletRequest request) {

        GenericMessage genericMessage = new GenericMessage();

        Long doctorLoginId=jwtTokenProvider.getIdFromToken(request);
        System.out.println(doctorLoginId);
        if (loginRepo.isIdAvailable(doctorLoginId) != null) {

            if(doctorRepository.isIdAvailable(details.getId())==null){
                if (details.getId() == id && details.getId()==doctorLoginId) {
                    doctorRepository.insertARowIntoTheTable(details.getId(),details.getAge(),details.getSpeciality(),details.getPhoneNo(),details.getGender(),doctorLoginId,details.getExp(),details.getDegree());
                    genericMessage.setData( doctorRepository.getDoctorById(details.getId()));
                    genericMessage.setStatus(Constants.SUCCESS);
                    return new ResponseEntity<>(genericMessage,HttpStatus.OK);
                }
            }
            else if(doctorRepository.isIdAvailable(details.getId())!=null)
                throw new APIException(HttpStatus.BAD_REQUEST,"update not allowed in this API endpoint.");
        }

        throw new ResourceNotFoundException("doctor", "id", id);

    }

    @Override
    public ResponseEntity<GenericMessage>  updateDoctor(DoctorFormDto details, long id, HttpServletRequest request) {

        GenericMessage genericMessage = new GenericMessage();

        Long doctorLoginId = jwtTokenProvider.getIdFromToken(request);
        System.out.println(loginRepo.isIdAvailable(doctorLoginId) + ", " + doctorLoginId);
        if (loginRepo.isIdAvailable(doctorLoginId) != null) {

            if (doctorRepository.isIdAvailable(details.getId()) != null) {
                if (details.getId() == id && details.getId() == doctorLoginId) {
                    doctorRepository.updateDoctorDb(details.getAge(), details.getSpeciality(), details.getGender(), details.getPhoneNo(), details.getId(), details.getExp(), details.getDegree());
                    genericMessage.setData( doctorRepository.getDoctorById(details.getId()));
                    genericMessage.setStatus(Constants.SUCCESS);
                    return new ResponseEntity<>(genericMessage,HttpStatus.OK);
                }

                return null;

            }

        }
        throw new ResourceNotFoundException("doctor", "id", id);
    }


    @Override
    public ResponseEntity<GenericMessage> deleteDoctor(long id) {

        GenericMessage genericMessage = new GenericMessage();

        doctorRepository.deleteById(id);
        genericMessage.setData("Successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> getAllDoctorsBySpeciality(String speciality) {
        GenericMessage genericMessage = new GenericMessage();

        if (doctorRepository.isSpecialityAvailable(speciality) != null) {
            List<DoctorListDto> list = doctorRepository.getAllDoctorsBySpeciality(speciality);
            genericMessage.setData(list);
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }
        throw new ResourceNotFoundException("doctor", speciality, 0);
    }

    @Override
    public ResponseEntity<GenericMessage> genderChart(Long doctorId) {
        Map<String,Integer> chart = new HashMap<>();
        if(doctorRepository.isIdAvailable(doctorId) != null){

            List<String> genderChartValue = doctorRepository.genderChart(doctorId);

            for (String s:genderChartValue) {
                chart.put(s, Collections.frequency(genderChartValue,s));
            }
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chart),HttpStatus.OK);
        }
        throw new ResourceNotFoundException("doctor", "id", doctorId);
    }

    @Override
    public ResponseEntity<GenericMessage> bloodGroupChart(Long doctorId) {
         Map<String,Integer> chart = new HashMap<>();
        if(doctorRepository.isIdAvailable(doctorId) != null){

            List<String> bloodGroupValue = doctorRepository.bloodGroupChart(doctorId);

            for (String s:bloodGroupValue) {
                chart.put(s, Collections.frequency(bloodGroupValue,s));
            }
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chart),HttpStatus.OK);
        }
        throw new ResourceNotFoundException("doctor", "id", doctorId);
    }

    @Override
    public ResponseEntity<GenericMessage> ageGroupChart(Long doctorId) {
        Map<String,Integer> chart = new HashMap<>();
        chart.put("0-2",0);


        if(doctorRepository.isIdAvailable(doctorId) != null){
            List<Long> ageGroupValue = doctorRepository.ageGroupChart(doctorId);

            for (Long s:ageGroupValue) {
                int temp = 0;
                if(s <= 2)
                {
                    chart.put("0-2",1);
                }
                //chart.put(s, Collections.frequency(ageGroupValue,s));
            }
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chart),HttpStatus.OK);
        }
        throw new ResourceNotFoundException("doctor", "id", doctorId);
    }
}
