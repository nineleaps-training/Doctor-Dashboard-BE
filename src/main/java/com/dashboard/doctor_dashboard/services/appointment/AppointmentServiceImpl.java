package com.dashboard.doctor_dashboard.services.appointment;

import com.dashboard.doctor_dashboard.dtos.*;
import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.LoginDetails;
import com.dashboard.doctor_dashboard.exceptions.InvalidDate;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.exceptions.ValidationsException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.repository.PatientRepository;
import com.dashboard.doctor_dashboard.util.Constants;
import com.dashboard.doctor_dashboard.util.wrappers.GenericMessage;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * implementation of AppointmentService interface
 */
@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    private final LoginRepo loginRepo;
    private final PdFGeneratorServiceImpl pdFGeneratorService;

    private final JwtTokenProvider jwtTokenProvider;
    private final MailServiceImpl mailService;
    private final ModelMapper mapper;


    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, LoginRepo loginRepo, JwtTokenProvider jwtTokenProvider, ModelMapper mapper,PdFGeneratorServiceImpl pdFGeneratorService,MailServiceImpl mailService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.loginRepo = loginRepo;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mapper = mapper;
        this.pdFGeneratorService = pdFGeneratorService;
        this.mailService=mailService;
    }

    private static final  Map<Long,Map<LocalDate,List<Boolean>>> slots=new HashMap<>();

//    ArrayList<>(Collections.nCopies(12,true))
    static final List<Boolean> timesSlots=List.of(true,true,true,true,true,true,true,true,true,true,true,true);
    List<String> times=Arrays.asList("10:00","10:30","11:00","11:30","12:00","12:30","14:00","14:30","15:00","15:30","16:00","16:30");





    /**
     * This function of service  is for adding appointment details
     * @param appointment  this variable contains appointment details.
     * @param request  this variable contains request.
     * @return It returns a ResponseEntity<GenericMessage> with status code 201.
     * @throws MessagingException
     * @throws JSONException
     * @throws UnsupportedEncodingException
     * This function is used for booking appointments.
     */
    @Override
    public ResponseEntity<GenericMessage>  addAppointment(AppointmentDto appointment, HttpServletRequest request) throws MessagingException, JSONException, UnsupportedEncodingException {
        log.info("inside: appointment service::addAppointment");
        Map<String,String> response = new HashMap<>();
        Long loginId=jwtTokenProvider.getIdFromToken(request);
        if (loginRepo.isIdAvailable(loginId) != null) { //checking if the patient exists database.
            Long patientId=patientRepository.getId(appointment.getPatient().getPID());
            if ( patientId!= null && doctorRepository.isIdAvailable(appointment.getDoctorDetails().getId()) != null) { //checking if the doctor and patient details exists in database.
                checkSanityOfAppointment(mapper.map(appointment,Appointment.class)); //cross-checking the values inside the object with the database.
                appointment.getPatient().setPID(patientId);
                LocalDate appDate=appointment.getDateOfAppointment();
                if(appDate.isAfter(LocalDate.now())&&appDate.isBefore(LocalDate.now().plusDays(8))) { //checking if the date of appointment is a future date and in one week range from current date.
                    if(Boolean.TRUE.equals(appointment.getIsBookedAgain())) { //checking if the appointment is a follow-up appointment,
                        bookAgainHandler(mapper.map(appointment,Appointment.class)); // checking if a previous appointment exist for the follow-up appointment.
                    }
                    isAppointmentTimeValid(mapper.map(appointment,Appointment.class)); //checking if the selected appointment time is valid and is the slot empty.
                    appointment.setStatus("To be attended");
                    var appointment1 = appointmentRepository.save(mapper.map(appointment,Appointment.class));

                    log.debug("appointment service::addAppointment "+ Constants.APPOINTMENT_CREATED);

                    response.put("appointId",appointment1.getAppointId().toString());
                    response.put("message",Constants.APPOINTMENT_CREATED);
                    sendEmailToUser(mapper.map(appointment,Appointment.class)); //sending mail to user on successful appointment booking.
                    log.info("exit: appointment service::addAppointment");
                    return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,response),HttpStatus.CREATED);
                }
                log.info("appointment service::addAppointment "+Constants.APPOINTMENT_CANNOT_BE_BOOKED);
                throw new InvalidDate(appDate+":"+Constants.APPOINTMENT_CANNOT_BE_BOOKED);
            }
            else if(patientId == null){

                log.info("appointment service:: addAppointment"+Constants.PATIENT_NOT_FOUND);
                throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND);
            }
            else {
                log.info("appointment service::addAppointment"+Constants.DOCTOR_NOT_FOUND);
                throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
            }
        }
        log.info("appointment service::addAppointment"+Constants.LOGIN_DETAILS_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.LOGIN_DETAILS_NOT_FOUND);
    }

    /**
     * This function of service is for checking of previous appointment for follow-up appointment
     * @param appointment this variable contains appointment details.
     */
    private void bookAgainHandler(Appointment appointment){ // checking if a previous appointment exist for the follow-up appointment.

        log.info("inside: appointment service::bookAgainHandler");
        if(appointment.getFollowUpAppointmentId()!=null && appointmentRepository.existsById(appointment.getFollowUpAppointmentId())) {
            var getAppointmentById = appointmentRepository.getAppointmentById(appointment.getFollowUpAppointmentId());
            if (!appointment.getPatient().getPID().equals(getAppointmentById.getPatient().getPID())) {
                log.info("appointment service::bookAgainHandler"+Constants.APPOINTMENT_NOT_FOUND);
                throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);
            }
            appointmentRepository.changeFollowUpStatus(appointment.getFollowUpAppointmentId());
            appointment.setIsBookedAgain(null);
        }
        else {
            log.error("appointment service::bookAgainHandler"+Constants.APPOINTMENT_NOT_FOUND);
            throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);
        }
    }
    /**
     * This function of service is for checking appointment time valid or not
     * @param appointment this variable contains appointment details.
     */
    private void isAppointmentTimeValid(Appointment appointment){ //checking if the selected appointment time is valid and is the slot empty.

        log.info("inside: appointment service::isAppointmentTimeValid");
        var time=appointment.getAppointmentTime().toString();
        int index = times.indexOf(time);
        if(index==-1)
            throw new InvalidDate(time+":Invalid time");
        List<Boolean> c = new ArrayList<>(checkSlots(appointment.getDateOfAppointment(), appointment.getDoctorDetails().getId())); // checking if the selected time slot is empty.
         if(Boolean.TRUE.equals(c.get(index))) {
            c.set(index, false);
        }else {
            log.info("appointment service::isAppointmentTimeValid"+Constants.APPOINTMENT_ALREADY_BOOKED);
            throw new InvalidDate(time+":"+Constants.APPOINTMENT_ALREADY_BOOKED);
        }
        slots.get(appointment.getDoctorDetails().getId()).put(appointment.getDateOfAppointment(), c);
        log.info("exit: appointment service::isAppointmentTimeValid");

    }
    /**
     * This function of service is for checking data inside database
     * @param appointment this variable contains appointment details.
     */
    public void checkSanityOfAppointment(Appointment appointment){ //cross-checking the values inside the object with the database.

        log.info("inside: appointment service::checkSanityOfAppointment");

        long patientId=appointment.getPatient().getPID();
        long doctorId=appointment.getDoctorDetails().getId();

        Optional<LoginDetails> patientDetails= loginRepo.findById(patientId);
        Optional<LoginDetails> doctorDetails= loginRepo.findById(doctorId);

        if(patientDetails.isPresent() && doctorDetails.isPresent()) {

            var patientLoginDetails = patientDetails.get();
            var doctorLoginDetails = doctorDetails.get();

            if (!patientLoginDetails.getEmailId().equals(appointment.getPatientEmail())) {
                log.info("appointment service::checkSanityOfAppointment "+Constants.INVALID_PATIENT_EMAIL);
                log.info(new ArrayList<>(List.of(Constants.INVALID_PATIENT_EMAIL)).toString());
                throw new ValidationsException(new ArrayList<>(List.of(Constants.INVALID_PATIENT_EMAIL)));
            }
            if (!patientLoginDetails.getName().equals(appointment.getPatientName())) {

                log.info("appointment service::checkSanityOfAppointment"+Constants.INVALID_PATIENT_NAME);
                log.info(new ArrayList<>(List.of(Constants.INVALID_PATIENT_NAME)).toString());
                throw new ValidationsException(new ArrayList<>(List.of(Constants.INVALID_PATIENT_NAME)));
            }

            if (!doctorLoginDetails.getName().equals(appointment.getDoctorName())) {
                log.info("appointment service:: checkSanityOfAppointment"+Constants.INVALID_DOCTOR_NAME);
                throw new ValidationsException(new ArrayList<>(List.of(Constants.INVALID_DOCTOR_NAME)));
            }
        }
        log.info("exit: appointment service::checkSanityOfAppointment");
    }
    /**
     * This function of service of getting all appointment by patient
     * @param loginId this variable contains login id.
     * @param pageNo this variable contains Page no.
     * @return It returns a ResponseEntity<GenericMessage> with status code 201.
     */
    @Override
    public ResponseEntity<GenericMessage> getAllAppointmentByPatientId(Long loginId, int pageNo,int pageSize ){
        log.info("inside: appointment service::getAllAppointmentByPatientId");
        var genericMessage = new GenericMessage();
        Map<String, PageRecords> m = new HashMap<>();
        Pageable paging= PageRequest.of(pageNo, pageSize);

        Long patientId=patientRepository.getId(loginId);
        if(patientId != null) {
            Page<Appointment> past=appointmentRepository.pastAppointment(patientId,paging);
            Page<Appointment> upcoming=appointmentRepository.upcomingAppointment(patientId,paging);
            Page<Appointment> today1=appointmentRepository.todayAppointment1(patientId,paging);
            List<PatientAppointmentListDto> today = new ArrayList<>();
            Page<Appointment> today2 = appointmentRepository.todayAppointment2(patientId, PageRequest.of(pageNo, pageSize - today1.getNumberOfElements())) ;
            today.addAll(mapToAppointList(today1.toList()));
            today.addAll(mapToAppointList(today2.toList()));
            var pageRecordsPast = new PageRecords(mapToAppointList(past.toList()),pageNo,pageSize,past.getTotalElements(),past.getTotalPages(),past.isLast());
            var pageRecordsUpcoming = new PageRecords(mapToAppointList(upcoming.toList()),pageNo,pageSize,upcoming.getTotalElements(),upcoming.getTotalPages(),upcoming.isLast());
            var pageRecordsToday = new PageRecords(today,pageNo,pageSize,today.size(),today1.getTotalPages(),today1.isLast());


            m.put("past",pageRecordsPast);
            m.put("today",pageRecordsToday);
            m.put("upcoming",pageRecordsUpcoming);

            genericMessage.setData(m);
            genericMessage.setStatus(Constants.SUCCESS);


            log.info("exit: appointment service::getAllAppointmentByPatientId");

            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }

        log.info("appointment service::getAllAppointmentByPatientId"+Constants.PATIENT_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND);
    }

    /**
     * This function of service of getting all appointment by doctor
     * @param loginId this variable contains login id.
     * @param pageNo this variable contains Page no.
     * @return It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> getAllAppointmentByDoctorId(Long loginId,int pageNo,int pageSize ){
        log.info("inside: appointment service::getAllAppointmentByDoctorId");
        var genericMessage = new GenericMessage();
        List<DoctorAppointmentListDto> today = new ArrayList<>();
        Map<String,PageRecords> m = new HashMap<>();
        Long doctorId = doctorRepository.isIdAvailable(loginId);
        Pageable paging= PageRequest.of(pageNo, 10);


        if(doctorId != null) {



            Page<Appointment> past = appointmentRepository.pastDoctorAppointment(doctorId,paging);
            Page<Appointment> upcoming = appointmentRepository.upcomingDoctorAppointment(doctorId,paging);
            Page<Appointment> today1 = appointmentRepository.todayDoctorAppointment1(doctorId,paging);
            Page<Appointment> today2 = appointmentRepository.todayDoctorAppointment2(doctorId,paging);
            today.addAll(mapToAppointDoctorList(today1.toList()));
            today.addAll(mapToAppointDoctorList(today2.toList()));
            var pageRecordsPast = new PageRecords(mapToAppointDoctorList(past.toList()),pageNo,pageSize,past.getTotalElements(),past.getTotalPages(),past.isLast());
            var pageRecordsUpcoming = new PageRecords(mapToAppointDoctorList(upcoming.toList()),pageNo,pageSize,upcoming.getTotalElements(),upcoming.getTotalPages(),upcoming.isLast());
            var pageRecordsToday = new PageRecords(today,pageNo,pageSize,today.size(),today1.getTotalPages(),today1.isLast());


            m.put("past",pageRecordsPast);
            m.put("today",pageRecordsToday);
            m.put("upcoming",pageRecordsUpcoming);

            genericMessage.setData(m);
            genericMessage.setStatus(Constants.SUCCESS);


            log.info("exit: appointment service::getAllAppointmentByDoctorId");

            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }
        log.info("appointment service::getAllAppointmentByDoctorId"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * This function is used for getting follow-up details for book again appointment.
     * @param appointId this variable contains appointment details.
     * @return It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> getFollowDetails(Long appointId){
        log.info("inside: appointment service::getFollowDetails");

        if(appointmentRepository.getId(appointId) != null && appointId.equals(appointmentRepository.getId(appointId))){
            log.info("exit: appointment service::getFollowDetails");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,mapper.map(appointmentRepository.getFollowUpData(appointId), FollowUpDto.class)),HttpStatus.OK);
        }
        log.info("appointment service::getFollowDetails"+Constants.APPOINTMENT_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);
    }

    /**
     * This function is used for getting appointment details based on the appointment id.
     * @param appointId this variable contains appointment details.
     * @return It returns a  ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> getAppointmentById(Long appointId){
        log.info("inside: appointment service::getAppointmentById");
        if(appointmentRepository.getId(appointId) != null){
            var appointment = appointmentRepository.getAppointmentById(appointId);
            log.info("exit: appointment service::getAppointmentById");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,mapper.map(appointment, PatientProfileDto.class)),HttpStatus.OK);
        }
        log.info("appointment service::getAppointmentById"+Constants.APPOINTMENT_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);

    }
    /**
     * This function is used for getting weekly visits to the doctor in a month.
     * @param doctorId this variable contains appointment details.
     * @return  It returns a  ResponseEntity<GenericMessage> for weeklyDoctorCountChart.
     */
    @Override
    public ResponseEntity<GenericMessage> weeklyDoctorCountChart(Long doctorId){
        log.info("inside: appointment service::weeklyDoctorCountChart");
        if(doctorRepository.isIdAvailable(doctorId) != null) {
            ArrayList<java.sql.Date> dateList = appointmentRepository.getAllDatesByDoctorId(doctorId);
            log.info("exit: appointment service::weeklyDoctorCountChart");

            return weeklyGraph(dateList);
        }
        log.info("appointment service::weeklyDoctorCountChart"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * This function is used for getting weekly visits of the patient to the doctors in a month.
     * @param patientId this variable contains patient Id.
     * @return  It returns a  ResponseEntity<GenericMessage> for  weeklyPatientCountChart
     */
    @Override
    public ResponseEntity<GenericMessage> weeklyPatientCountChart(Long patientId){
        log.info("inside: appointment service::weeklyPatientCountChart");
        Long id = patientRepository.getId(patientId);
        if(id != null) {
            ArrayList<java.sql.Date> dateList = appointmentRepository.getAllDatesByPatientId(id);
            log.info("exit: appointment service::weeklyPatientCountChart");

            return weeklyGraph(dateList);
        }
        log.info("appointment service::weeklyPatientCountChart"+Constants.PATIENT_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND);
    }

    /**
     * This function is used for getting upcoming appointments of the doctor in the current day.
     * @param doctorId this variable contains doctor Id.
     * @return It returns ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> recentAppointment(Long doctorId){

        log.info("inside: appointment service::recentAppointment");
        if(doctorRepository.isIdAvailable(doctorId) != null) {
            List<Appointment> appointments = appointmentRepository.recentAppointment(doctorId);
            List<DoctorAppointmentListDto> list =  appointments.stream()
                    .map(this::mapToDto).collect(Collectors.toList());
                                log.info("exit: appointment service::recentAppointment");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list),HttpStatus.OK);
        }
        log.info("appointment service::recentAppointment"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * This function used for getting sum of total appointments till date.
     * @param doctorId this variable contains doctor Id.
     * @return It returns ResponseEntity<GenericMessage> with status code 200.
     */

    @Override
    public ResponseEntity<GenericMessage> totalNoOfAppointment(Long doctorId){
        log.info("inside: appointment service::totalNoOfAppointment");

        if(doctorRepository.isIdAvailable(doctorId) != null) {
                                log.info("exit: appointment service::totalNoOfAppointment");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.totalNoOfAppointment(doctorId)), HttpStatus.OK);
        }
        log.info("appointment service::totalNoOfAppointment"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * This function used for getting total number of current day appointments.
     * @param doctorId this variable contains doctor Id.
     * @return  It returns ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> todayAppointments(Long doctorId){
        log.info("inside: appointment service::todayAppointments");

        if(doctorRepository.isIdAvailable(doctorId) != null) {
                                log.info("exit: appointment service::todayAppointments");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.todayAppointments(doctorId)), HttpStatus.OK);
        }
        log.info("appointment service::todayAppointments"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * This function used for getting total number of appointments added in current week.
     * @param doctorId  this variable contains doctor Id.
     * @return  It returns ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(Long doctorId){
        log.info("inside: appointment service::totalNoOfAppointmentAddedThisWeek");

        if(doctorRepository.isIdAvailable(doctorId) != null) {
                                log.info("exit: appointment service::totalNoOfAppointmentAddedThisWeek");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.totalNoOfAppointmentAddedThisWeek(doctorId)), HttpStatus.OK);
        }
        log.info("appointment service::totalNoOfAppointmentAddedThisWeek"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * This function used for getting the sum of different categories the patient visited till date.
     * @param loginId this variable contains login Id.
     * @return  It returns ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> patientCategoryGraph(Long loginId){
        log.info("inside: appointment service::patientCategoryGraph");

        Long patientId = patientRepository.getId(loginId);
        if(patientId != null) {
                                log.info("exit: appointment service::patientCategoryGraph");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.patientCategoryGraph(patientId)), HttpStatus.OK);
        }
        log.info("appointment service::patientCategoryGraph"+Constants.PATIENT_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND);

    }

    /**
     * This function used for getting the available slots of doctor
     * @param date this variable contains date.
     * @param doctorId this variable contains doctor Id.
     * @return It returns Map<Long,Map<LocalDate,List<Boolean>>> for  checkSlotsAvail
     */
    public Map<Long,Map<LocalDate,List<Boolean>>> checkSlotsAvail(LocalDate date,Long doctorId){
                log.info("inside: appointment service::checkSlotsAvail");
//checking if the slots of  doctor in the DB and adding to Map.

        Map<LocalDate,List<Boolean>> dateAndTime=new HashMap<>();
        List<Boolean> docTimesSlots=new ArrayList<>(Collections.nCopies(12,true));
        List<LocalTime> doctorBookedSlots;
        List<Time> dates=appointmentRepository.getTimesByIdAndDate(date,doctorId);
        doctorBookedSlots=dates
                .stream()
                .map(Time::toLocalTime).collect(Collectors.toList());
        if(!doctorBookedSlots.isEmpty()){
            for (LocalTime doctorBookedSlot : doctorBookedSlots) {
                docTimesSlots.set(times.indexOf(doctorBookedSlot.toString()), false);
            }
            if(slots.get(doctorId)==null){
                dateAndTime.put(date,docTimesSlots);
                slots.put(doctorId, dateAndTime);
            }
            else {
                slots.get(doctorId).putIfAbsent(date, docTimesSlots);
            }
                                log.info("exit1: appointment service::checkSlotsAvail");

            return slots;
        }
        if(slots.get(doctorId)==null){
            dateAndTime.put(date,timesSlots);
            slots.put(doctorId, dateAndTime);

        }
        else {
            slots.get(doctorId).computeIfAbsent(date, k -> timesSlots);
        }
                            log.info("exit2: appointment service::checkSlotsAvail");

        return slots;
    }



    /**
     * @param date this variable contains date.
     * @param doctorId this variable contains doctor Id.
     * @return  It returns List<Boolean> for checkSlots.
     */
    public List<Boolean> checkSlots(LocalDate date,Long doctorId){
                log.info("inside: appointment service::checkSlots");
// checking if the selected time slot is empty.
        if(doctorRepository.isIdAvailable(doctorId)!=null) {

            if(slots.get(doctorId) != null) { //checking if the doctorId available in the map.
                                    log.info("exit1: appointment service::checkSlots");

                return doctorIdPresentInSlots(date, doctorId);
            }

            else {
                                    log.info("exit2: appointment service::checkSlots");

                return doctorIdNotPresentInSlots(date, doctorId);
            }

        }
        log.info("appointment service::checkSlots"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * @param date this variable contains date.
     * @param doctorId this variable contains doctor Id.
     * @return  It returns List<Boolean> for doctorIdPresentInSlots.
     */
    List<Boolean> doctorIdPresentInSlots(LocalDate date, Long doctorId){
        log.info("inside: appointment service::doctorIdPresentInSlots");

        if (Boolean.TRUE.equals(pdFGeneratorService.dateHandler(date))) {
            if (slots.get(doctorId).get(date) != null) { //checking if the doctor slot details available in the map.
                                    log.info("exit1: appointment service::doctorIdPresentInSlots");

                return slots.get(doctorId).get(date);
            } else {
                                    log.info("exit2: appointment service::doctorIdPresentInSlots");

                return checkSlotsAvail(date, doctorId).get(doctorId).get(date); //checking if the slots of  doctor in the DB and adding to Map.
            }
        }else {
            log.info("appointment service::doctorIdPresentInSlots"+Constants.SELECT_SPECIFIED_DATES);
            throw new InvalidDate(date+":"+Constants.SELECT_SPECIFIED_DATES);
        }

    }

    /**
     * @param date this variable contains date.
     * @param doctorId this variable contains doctor Id.
     * @return  It returns List<Boolean> for doctorIdNotPresentInSlots.
     */
    List<Boolean> doctorIdNotPresentInSlots(LocalDate date, Long doctorId){
        log.info("inside: appointment service::doctorIdNotPresentInSlots");

        if (Boolean.TRUE.equals(pdFGeneratorService.dateHandler(date))){
                                log.info("exit: appointment service::doctorIdNotPresentInSlots");

            return checkSlotsAvail(date, doctorId).get(doctorId).get(date);
        }
        else {
            log.info("appointment service::doctorIdNotPresentInSlots"+Constants.SELECT_SPECIFIED_DATES);
            throw new InvalidDate(date+":"+Constants.SELECT_SPECIFIED_DATES);
        }
    }






    /**
     * @param appointments  this variable contains appointments.
     * @return It returns List<PatientAppointmentListDto>
     */

    List<PatientAppointmentListDto> mapToAppointList(List<Appointment> appointments){
                            log.info("appointment service::mapToAppointList");
        return appointments.stream()
                .map(this::mapToDto2).collect(Collectors.toList());
    }

    /**
     * @param appointments  this variable contains appointments.
     * @return It returns List<DoctorAppointmentListDto>
     */
    List<DoctorAppointmentListDto> mapToAppointDoctorList(List<Appointment> appointments){
                            log.info("appointment service::mapToAppointDoctorList");

        return appointments.stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }
    /**
     * @param appointment  this variable contains appointments.
     * @return  It returns DoctorAppointmentListDto.
     */
    private DoctorAppointmentListDto mapToDto(Appointment appointment) {
                            log.info("appointment service::mapToDto");

        return mapper.map(appointment, DoctorAppointmentListDto.class);
    }
    /**
     * @param appointment this variable contains appointments.
     * @return  It returns PatientAppointmentListDto.
     */
    private PatientAppointmentListDto mapToDto2(Appointment appointment) {
                            log.info("appointment service::mapToDto2");

        return mapper.map(appointment, PatientAppointmentListDto.class);
    }

    /**
     * This function used for sending a mail to the patient on successfully booking.
     * @param appointment this variable contains appointments.
     * @throws JSONException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendEmailToUser(Appointment appointment) throws JSONException, MessagingException, UnsupportedEncodingException {
        log.info("inside: appointment service::sendEmailToUser");
        String doctorEmail = loginRepo.email(appointment.getDoctorDetails().getId());
        String toEmail = appointment.getPatientEmail();
        var fromEmail = "mecareapplication@gmail.com";
        var senderName = "meCare Team";
        var subject = "Appointment Confirmed";

        String content = Constants.MAIL_APPOINTMENT;

        content = content.replace("[[name]]", appointment.getPatientName());
        content = content.replace("[[doctorName]]", appointment.getDoctorName());

        content = content.replace("[[doctorEmail]]", doctorEmail);
        content = content.replace("[[speciality]]", appointment.getCategory());

        content = content.replace("[[dateOfAppointment]]",pdFGeneratorService.formatDate(appointment.getDateOfAppointment().toString()));

        content = content.replace("[[appointmentTime]]", appointment.getAppointmentTime().toString());

        mailService.mailServiceHandler(fromEmail,toEmail,senderName,subject,content);
        log.info("exit: appointment service::sendEmailToUser");
    }
    /**
     * @param dateList this variable contains Date details.
     * @return It returns ResponseEntity<GenericMessage> with status code 200.
     * This function gives weekly count of vists.
     */
    ResponseEntity<GenericMessage> weeklyGraph(ArrayList<java.sql.Date> dateList){
        log.info("inside: appointment service::weeklyGraph");

        int lengthOfMonth = LocalDate.now().lengthOfMonth();
        ArrayList<String> newList = new ArrayList<>();
        var year= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        Calendar.getInstance().get(Calendar.MONTH);
        var month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
        month = pdFGeneratorService.monthHandler(month);
        var firstWeek="1-7";
        var secondWeek="8-14";
        var thirdWeek="15-21";
        var fourthWeek="22-28";
        var lastWeek="29-"+lengthOfMonth;
        var firstWeekCount=0;
        var secondWeekCount=0;
        var thirdWeekCount=0;
        var fourthWeekCount=0;
        var lastWeekCount=0;



        ArrayList<LocalDate> localDateList =new ArrayList<>();
        for (java.sql.Date date : dateList) {
            localDateList.add(date.toLocalDate());
        }

        for (LocalDate localDate : localDateList) {
            if (localDate.isBefore(LocalDate.parse(year + "-" + month + "-" + "08"))) {
                firstWeekCount++;
            } else if (localDate.isBefore(LocalDate.parse(year + "-" + month + "-" + "15"))) {
                secondWeekCount++;
            } else if (localDate.isBefore(LocalDate.parse(year + "-" + month + "-" + "22"))) {
                thirdWeekCount++;
            } else if (localDate.isBefore(LocalDate.parse(year + "-" + month + "-" + "29"))) {
                fourthWeekCount++;
            } else if (localDate.isBefore(LocalDate.parse(year + "-" + month + "-" + lengthOfMonth))) {
                lastWeekCount++;
            } else if (localDate.equals(LocalDate.parse(year + "-" + month + "-" + lengthOfMonth))) {
                lastWeekCount++;
            } else{
                log.info("appointment service::weeklyGraph Invalid date");
                throw new InvalidDate("Invalid date");
            }
        }
        newList.add(firstWeek+","+firstWeekCount);
        newList.add(secondWeek+","+secondWeekCount);
        newList.add(thirdWeek+","+thirdWeekCount);
        newList.add(fourthWeek+","+fourthWeekCount);
        newList.add(lastWeek+","+lastWeekCount);
        log.info("exit: appointment service::weeklyGraph");
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,newList),HttpStatus.OK);
    }

}