package com.dashboard.doctor_dashboard.services.doctor;


import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto;
import com.dashboard.doctor_dashboard.entities.dtos.PageRecords;
import com.dashboard.doctor_dashboard.entities.dtos.UserDetailsUpdateDto;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * implementation of DoctorService interface
 */
@Service
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private DoctorRepository doctorRepository;


    private LoginRepo loginRepo;


    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, LoginRepo loginRepo, JwtTokenProvider jwtTokenProvider) {
        this.doctorRepository = doctorRepository;
        this.loginRepo = loginRepo;
        this.jwtTokenProvider = jwtTokenProvider;
    }



    /**
     * This function of service is for getting all doctors
     * @param id  this variable contains Id.
     * @return It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> getAllDoctors(Long id){
                log.info("inside: DoctorServiceImpl::getAllDoctors");


        var genericMessage = new GenericMessage();

        if (doctorRepository.isIdAvailable(id) != null) {
            List<DoctorListDto> list = doctorRepository.getAllDoctors(id);
            genericMessage.setData(list);
            genericMessage.setStatus(Constants.SUCCESS);
                                            log.info("exit: DoctorServiceImpl::getAllDoctors");

            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }


                log.info("DoctorServiceImpl::getAllDoctors"+Constants.PATIENT_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);

    }

    /**
     * This function of service is for getting doctor details
     * @param id  this variable contains Id.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
     public ResponseEntity<GenericMessage> getDoctorById(long id){
                log.info("inside: DoctorServiceImpl::getDoctorById");


        var genericMessage = new GenericMessage();

        if (doctorRepository.isIdAvailable(id) != null) {
            genericMessage.setData(doctorRepository.findDoctorById(id));
            genericMessage.setStatus(Constants.SUCCESS);
                                            log.info("exit: DoctorServiceImpl::getDoctorById");

            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }

                log.info("DoctorServiceImpl::getDoctorById"+Constants.DOCTOR_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * This function of service is for adding doctor details
     * @param details this variable contains details.
     * @param id this variable contains Id.
     * @param request this variable contains request.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> addDoctorDetails(DoctorFormDto details, long id, HttpServletRequest request){
                log.info("inside: DoctorServiceImpl::addDoctorDetails");


        var genericMessage = new GenericMessage();
        Long doctorLoginId=jwtTokenProvider.getIdFromToken(request);
        if (loginRepo.isIdAvailable(doctorLoginId) != null) {

            if(doctorRepository.isIdAvailable(details.getLoginId())==null) {
                if (details.getLoginId() == id && details.getLoginId().equals(doctorLoginId)) {
                    doctorRepository.insertARowIntoTheTable(details.getLoginId(),details.getAge(),details.getSpeciality(),details.getPhoneNo(),details.getGender(),doctorLoginId,details.getExp(),details.getDegree());
                    genericMessage.setData( doctorRepository.getDoctorById(details.getLoginId()));
                    genericMessage.setStatus(Constants.SUCCESS);
                    log.debug("Doctor: Doctor on boarding completed.");
                                                    log.info("exit: DoctorServiceImpl::addDoctorDetails");

                    return new ResponseEntity<>(genericMessage,HttpStatus.OK);
                }
            }
            else {
                log.error("Doctor Service Impl: update not allowed in this API endpoint.");
                throw new APIException("update not allowed in this API endpoint.");
            }

        }

                log.info("DoctorServiceImpl::addDoctorDetails"+Constants.DOCTOR_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);

    }


    /**
     * This function of service is for updating doctor details
     * @param details this variable contains details.
     * @param id this variable contains Id.
     * @param request this variable contains request.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage>  updateDoctor(UserDetailsUpdateDto details, long id, HttpServletRequest request){
                log.info("inside: DoctorServiceImpl::updateDoctor");


        var genericMessage = new GenericMessage();

        Long doctorLoginId = jwtTokenProvider.getIdFromToken(request);
        if (loginRepo.isIdAvailable(doctorLoginId) != null && doctorRepository.isIdAvailable(details.getId()) != null) {
            if (details.getId().equals(id) && details.getId().equals(doctorLoginId)) {
                doctorRepository.updateDoctorDb(details.getMobileNo());
                genericMessage.setData( doctorRepository.getDoctorById(details.getId()));
                genericMessage.setStatus(Constants.SUCCESS);
                                                log.info("exit: DoctorServiceImpl::updateDoctor");

                return new ResponseEntity<>(genericMessage,HttpStatus.OK);
            }

                    log.info("DoctorServiceImpl::updateDoctor"+Constants.DETAILS_MISMATCH);

            throw new ResourceNotFoundException(Constants.DETAILS_MISMATCH);
        }
                log.info("DoctorServiceImpl::updateDoctor"+Constants.DOCTOR_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * this function of service is for deleting doctor details
     * @param id this variable contains Id.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> deleteDoctor(long id){
                log.info("inside: DoctorServiceImpl::deleteDoctor");


        var genericMessage = new GenericMessage();

        doctorRepository.deleteById(id);
        genericMessage.setData("Successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        log.debug("Doctor: Doctor deleted.");
                                        log.info("exit: DoctorServiceImpl::deleteDoctor");

        return new ResponseEntity<>(genericMessage,HttpStatus.OK);
    }
    /**
     * This function of service is for getting all doctor by speciality
     * @param speciality this variable contains speciality.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> getAllDoctorsBySpeciality(String speciality,int pageNo,int pageSize){
                log.info("inside: DoctorServiceImpl::getAllDoctorsBySpeciality");

        var genericMessage = new GenericMessage();
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        if (doctorRepository.isSpecialityAvailable(speciality) != null) {
            Page<DoctorListDto> list = doctorRepository.getAllDoctorsBySpeciality(speciality,pageable);
            genericMessage.setData(new PageRecords(list.toList(),pageNo,pageSize,list.getTotalElements(),list.getTotalPages(),list.isLast()));
            genericMessage.setStatus(Constants.SUCCESS);
                                            log.info("exit: DoctorServiceImpl::getAllDoctorsBySpeciality");

            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }
                log.info("DoctorServiceImpl::getAllDoctorsBySpeciality"+Constants.DOCTOR_NOT_FOUND_SPECIALITY);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND_SPECIALITY);
    }
    /**
     * This function of service is for fetching details for gender chart
     * @param doctorId this variable contains doctor Id.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> genderChart(Long doctorId){
                log.info("inside: DoctorServiceImpl::genderChart");

        Map<String,Integer> chart = new HashMap<>();
        if(doctorRepository.isIdAvailable(doctorId) != null) {

            List<String> genderChartValue = doctorRepository.genderChart(doctorId);

            for (String s:genderChartValue) {
                chart.put(s, Collections.frequency(genderChartValue,s));
            }
                                            log.info("exit: DoctorServiceImpl::genderChart");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chart),HttpStatus.OK);
        }
                log.info("DoctorServiceImpl::genderChart"+Constants.DOCTOR_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * This function of service is for fetching details for blood group chart
     * @param doctorId this variable contains doctor Id.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> bloodGroupChart(Long doctorId){
                log.info("inside: DoctorServiceImpl::bloodGroupChart");

         Map<String,Integer> chart = new HashMap<>();
        if(doctorRepository.isIdAvailable(doctorId) != null) {

            List<String> bloodGroupValue = doctorRepository.bloodGroupChart(doctorId);

            for (String s:bloodGroupValue) {
                chart.put(s, Collections.frequency(bloodGroupValue,s));
            }
                                            log.info("exit: DoctorServiceImpl::bloodGroupChart");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chart),HttpStatus.OK);
        }
                log.info("DoctorServiceImpl::bloodGroupChart"+Constants.DOCTOR_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }


    /**
     * This function of service is for fetching details for age group chart
     * @param doctorId  this variable contains doctor Id.
     * @return  It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> ageGroupChart(Long doctorId){
                log.info("inside: DoctorServiceImpl::ageGroupChart");

        Map<String,Integer> chart = new HashMap<>();
        chart.put(Constants.ages.get(0),0);
        chart.put(Constants.ages.get(1),0);
        chart.put(Constants.ages.get(2),0);
        chart.put(Constants.ages.get(3),0);
        chart.put(Constants.ages.get(4),0);


        if(doctorRepository.isIdAvailable(doctorId) != null) {
            List<Long> ageGroupValue = doctorRepository.ageGroupChart(doctorId);
            for (Long s:ageGroupValue) {

                if(s <= 2)
                {
                    chart.put(Constants.ages.get(0), chart.get(Constants.ages.get(0))+1);
                } else if (s<=14) {
                    chart.put(Constants.ages.get(1),chart.get(Constants.ages.get(1))+1);
                } else if (s<=25) {
                    chart.put(Constants.ages.get(2),chart.get(Constants.ages.get(2))+1);
                } else if (s<=64) {
                    chart.put(Constants.ages.get(3),chart.get(Constants.ages.get(3))+1);
                } else {
                    chart.put(Constants.ages.get(4),chart.get(Constants.ages.get(4))+1);
                }

            }
                                            log.info("exit: DoctorServiceImpl::ageGroupChart");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,chart),HttpStatus.OK);

        }
                log.info("DoctorServiceImpl::ageGroupChart"+Constants.DOCTOR_NOT_FOUND);

        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);

    }

}
