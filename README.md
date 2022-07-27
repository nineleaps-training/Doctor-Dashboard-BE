# Doctor-Dashboard-BE

Description
Technology is constantly changing the way we live our lives, from living in the metaverse to getting a prescription on a smartphone app, our lives are transforming fast. My Care an interactive Appointment booking system serves right from booking an appointment to tracking down the performance of the patient and manage the appointment flow. The flow of it helps the patient, doctor and a Patient care technician to easily track down the respective details of the Patient Appointment ,Patient details which include various vitals such as the Blood Group , Blood Pressure, Glucose level and the Temperature which is updated by the Patient care assistant and which can be reviewed by the doctor . The doctor would be able to assist the patient accordingly from the details updated.
The Patient will be able to book and reserve his appointment with the respective doctor for the diagnosed category within a week. The patient status right from the booking until the doctor completes and if its a follow is visible on the dashboard , there's section for the doctor where the previous prescription for the respective patient is visible. My Care includes all the necessary section to track the patient details right from booking the appointment to prescribing a medicine and assisting the patient.

# Technologies

### Project is created with:
```
Spring Boot
java 11
MySQL
Maven
JPA buddy
```

# Folder Structure of Backend
```
|-- pom.xml
|-- src
|   |-- main
|   |   |-- java
|   |   |   `-- com
|   |   |       `-- dashboard
|   |   |           |-- DashboardApplication.java
|   |   |           `-- doctor_dashboard
|   |   |               |-- controllers
|   |   |               |   |-- AppointmentController.java
|   |   |               |   |-- DoctorController.java
|   |   |               |   |-- FileController.java
|   |   |               |   |-- LoginController.java
|   |   |               |   |-- PatientController.java
|   |   |               |   |-- PrescriptionController.java
|   |   |               |   |-- ReceptionistController.java
|   |   |               |   `-- TodoController.java
|   |   |               |-- dtos
|   |   |               |   |-- AppointmentDto.java
|   |   |               |   |-- AppointmentViewDto.java
|   |   |               |   |-- AttributesDto.java
|   |   |               |   |-- DoctorAppointmentListDto.java
|   |   |               |   |-- DoctorBasicDetailsDto.java
|   |   |               |   |-- DoctorDropdownDto.java
|   |   |               |   |-- DoctorFormDto.java
|   |   |               |   |-- DoctorListDto.java
|   |   |               |   |-- ErrorMessage.java
|   |   |               |   |-- FollowUpDto.java
|   |   |               |   |-- JwtToken.java
|   |   |               |   |-- NotificationDto.java
|   |   |               |   |-- PageRecords.java
|   |   |               |   |-- PatientAppointmentListDto.java
|   |   |               |   |-- PatientDto.java
|   |   |               |   |-- PatientEntityDto.java
|   |   |               |   |-- PatientProfileDto.java
|   |   |               |   |-- PatientViewDto.java
|   |   |               |   |-- PrescriptionDto.java
|   |   |               |   |-- TodoListDto.java
|   |   |               |   |-- UpdatePrescriptionDto.java
|   |   |               |   |-- UserDetailsUpdateDto.java
|   |   |               |   `-- VitalsDto.java
|   |   |               |-- entities
|   |   |               |   |-- Appointment.java
|   |   |               |   |-- Attributes.java
|   |   |               |   |-- DoctorDetails.java
|   |   |               |   |-- FileDB.java
|   |   |               |   |-- LoginDetails.java
|   |   |               |   |-- Patient.java
|   |   |               |   |-- Prescription.java
|   |   |               |   `-- Todolist.java
|   |   |               |-- exceptions
|   |   |               |   |-- APIException.java
|   |   |               |   |-- CommonExceptionHandler.java
|   |   |               |   |-- GoogleLoginException.java
|   |   |               |   |-- InvalidDate.java
|   |   |               |   |-- MailErrorException.java
|   |   |               |   |-- ResourceNotFoundException.java
|   |   |               |   `-- ValidationsException.java
|   |   |               |-- jwt
|   |   |               |   |-- config
|   |   |               |   |   `-- SecurityConfig.java
|   |   |               |   |-- entities
|   |   |               |   |   |-- AuthenticationResponse.java
|   |   |               |   |   |-- Claims.java
|   |   |               |   |   `-- Login.java
|   |   |               |   |-- security
|   |   |               |   |   |-- CustomAuthenticationEntryPoint.java
|   |   |               |   |   |-- CustomUserDetailsService.java
|   |   |               |   |   |-- JwtAuthenticationEntryPoint.java
|   |   |               |   |   |-- JwtAuthenticationFilter.java
|   |   |               |   |   |-- JwtTokenProvider.java
|   |   |               |   |   `-- MyUserDetails.java
|   |   |               |   `-- service
|   |   |               |       |-- JwtService.java
|   |   |               |       `-- JwtServiceImpl.java
|   |   |               |-- repository
|   |   |               |   |-- AppointmentRepository.java
|   |   |               |   |-- AttributeRepository.java
|   |   |               |   |-- DoctorRepository.java
|   |   |               |   |-- FileDBRepository.java
|   |   |               |   |-- LoginRepo.java
|   |   |               |   |-- PatientRepository.java
|   |   |               |   |-- PrescriptionRepository.java
|   |   |               |   `-- TodoRepository.java
|   |   |               |-- services
|   |   |               |   |-- appointment
|   |   |               |   |   |-- AppointmentService.java
|   |   |               |   |   |-- AppointmentServiceImpl.java
|   |   |               |   |   |-- MailServiceImpl.java
|   |   |               |   |   `-- PdFGeneratorServiceImpl.java
|   |   |               |   |-- doctor
|   |   |               |   |   |-- DoctorService.java
|   |   |               |   |   `-- DoctorServiceImpl.java
|   |   |               |   |-- file
|   |   |               |   |   |-- FileStorage.java
|   |   |               |   |   `-- FileStorageServiceImpl.java
|   |   |               |   |-- login
|   |   |               |   |   |-- LoginService.java
|   |   |               |   |   `-- LoginServiceImpl.java
|   |   |               |   |-- patient
|   |   |               |   |   |-- PatientService.java
|   |   |               |   |   `-- PatientServiceImpl.java
|   |   |               |   |-- prescription
|   |   |               |   |   |-- PrescriptionService.java
|   |   |               |   |   `-- PrescriptionServiceImpl.java
|   |   |               |   |-- receptionist
|   |   |               |   |   |-- ReceptionistService.java
|   |   |               |   |   `-- ReceptionistServiceImpl.java
|   |   |               |   `-- todo
|   |   |               |       |-- TodoService.java
|   |   |               |       `-- TodoServiceImpl.java
|   |   |               `-- util
|   |   |                   |-- Constants.java
|   |   |                   `-- wrappers
|   |   |                       |-- ErrorDetails.java
|   |   |                       |-- GenericMessage.java
|   |   |                       `-- ValidationsSchema.java
|   |   `-- resources
|   |       |-- application-dev.properties
|   |       |-- application-prod.properties
|   |       |-- application.properties
|   |       |-- db
|   |       |   `-- migration
|   |       |       |-- V1.1__update_tables.sql
|   |       |       |-- V1.2__add_delete_field.sql
|   |       |       `-- V1__create_tables.sql
|   |       `-- logback.xml

```

# ER Diagram
![miCare ER updated](https://user-images.githubusercontent.com/99714712/180491880-fcd3707a-13f2-458a-bb53-96437e3ed0f0.png)
