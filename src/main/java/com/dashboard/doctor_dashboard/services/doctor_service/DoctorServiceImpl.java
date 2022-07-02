package com.dashboard.doctor_dashboard.services.doctor_service;


import com.dashboard.doctor_dashboard.Util.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;


    @Autowired
    private LoginRepo loginRepo;


    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseEntity<GenericMessage> getAllDoctors(Long id) {

        GenericMessage genericMessage = new GenericMessage();

        if (doctorRepository.isIdAvailable(id) != null) {
            List<DoctorListDto> list = doctorRepository.getAllDoctors(id);
            genericMessage.setData(list);
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }


        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND, "id", id);

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
        if (loginRepo.isIdAvailable(doctorLoginId) != null) {

            if(doctorRepository.isIdAvailable(details.getId())==null){
                if (details.getId() == id && details.getId()==doctorLoginId) {
                    doctorRepository.insertARowIntoTheTable(details.getId(),details.getAge(),details.getSpeciality(),details.getPhoneNo(),details.getGender(),doctorLoginId,details.getExp(),details.getDegree());
                    genericMessage.setData( doctorRepository.getDoctorById(details.getId()));
                    genericMessage.setStatus(Constants.SUCCESS);
                    log.debug("Doctor: Doctor onBoarding completed");
                    return new ResponseEntity<>(genericMessage,HttpStatus.OK);
                }
            }
            else if(doctorRepository.isIdAvailable(details.getId())!=null)
                throw new APIException(HttpStatus.BAD_REQUEST,"update not allowed in this API endpoint.");
        }

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND, "id", id);

    }

    @Override
    public ResponseEntity<GenericMessage>  updateDoctor(DoctorFormDto details, long id, HttpServletRequest request) {

        GenericMessage genericMessage = new GenericMessage();

        Long doctorLoginId = jwtTokenProvider.getIdFromToken(request);
        if (loginRepo.isIdAvailable(doctorLoginId) != null) {

            if (doctorRepository.isIdAvailable(details.getId()) != null) {
                if (details.getId() == id && details.getId() == doctorLoginId) {
                    doctorRepository.updateDoctorDb(details.getPhoneNo());
                    genericMessage.setData( doctorRepository.getDoctorById(details.getId()));
                    genericMessage.setStatus(Constants.SUCCESS);
                    log.debug("Doctor: Doctor details updated");
                    return new ResponseEntity<>(genericMessage,HttpStatus.OK);
                }

            return null;
            }

        }
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND, "id", id);
    }


    @Override
    public ResponseEntity<GenericMessage> deleteDoctor(long id) {

        GenericMessage genericMessage = new GenericMessage();

        doctorRepository.deleteById(id);
        genericMessage.setData("Successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        log.debug("Doctor: Doctor details deleted");
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
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND, speciality, 0);
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
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND, "id", doctorId);
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
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND, "id", doctorId);
    }

    @Override
    public ResponseEntity<GenericMessage> ageGroupChart(Long doctorId) {
        Map<String,Integer> chart = new HashMap<>();
        chart.put(Constants.ages[0], 0);
        chart.put(Constants.ages[1],0);
        chart.put(Constants.ages[2],0);
        chart.put(Constants.ages[3],0);
        chart.put(Constants.ages[4],0);


        if(doctorRepository.isIdAvailable(doctorId) != null){
            List<Long> ageGroupValue = doctorRepository.ageGroupChart(doctorId);
            System.out.println(ageGroupValue);
            for (Long s:ageGroupValue) {

                if(s <= 2)
                {
                    chart.put(Constants.ages[0], chart.get(Constants.ages[0])+1);
                } else if ( s<=14) {
                    chart.put(Constants.ages[1],chart.get(Constants.ages[1])+1);
                } else if ( s<=25) {
                    chart.put(Constants.ages[2],chart.get(Constants.ages[2])+1);
                } else if (s<=64) {
                    chart.put(Constants.ages[3],chart.get(Constants.ages[3])+1);
                } else if (s>=65) {
                    chart.put(Constants.ages[4],chart.get(Constants.ages[4])+1);
                }

            }
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chart),HttpStatus.OK);

        }
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND, "id", doctorId);

    }

}
