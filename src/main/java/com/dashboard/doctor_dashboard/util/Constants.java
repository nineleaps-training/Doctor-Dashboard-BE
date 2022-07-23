package com.dashboard.doctor_dashboard.util.wrappers;

import java.util.List;
@SuppressWarnings("squid:S6126")
public class Constants {
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String PATIENT = "Patient";

    public static final String LOGIN ="Login";


    //Login
    public static final String LOGIN_DETAILS_NOT_FOUND = "login details not found with the id provided.";



    //DOCTOR
    public static final String DOCTOR_NOT_FOUND = "doctor not found with the id provided.";
    public static final String DOCTOR_NOT_FOUND_SPECIALITY = "doctor not found with the speciality provided.";


    //PATIENT
    public static final String PATIENT_NOT_FOUND = "Patient not found with the id provided.";


    //APPOINTMENT
    public static final String APPOINTMENT_NOT_FOUND = "appointment not found with the id provided.";
    public static final String APPOINTMENT_CREATED = "appointment successfully created";

    public static final String APPOINTMENT_ALREADY_BOOKED = "appointment is already booked for this time, please refresh.";
    public static final String APPOINTMENT_CANNOT_BE_BOOKED = "appointment cannot be booked on this date";
    public static final String SELECT_SPECIFIED_DATES = "select dates from specified range";


    public static final String INVALID_PATIENT_NAME = "invalid patient name";
    public static final String INVALID_PATIENT_EMAIL = "invalid patient email";
    public static final String INVALID_DOCTOR_NAME = "invalid doctor name";

    public static final String MAIL_ERROR = "mail error!!! cant connect with the host server";

    public static final String PRESCRIPTION_CREATED = "prescription added";

    public static final String MAIL_PRESCRIPTION =  "<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
            + "</style></head>"
            + "<div style=\"background-color: white; color:black  \">\n"
            + " <p style=\"text-align: left; font-size:15px ;\">Hi [[name]],</p>\n"
            + " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
            + " font-family: 'Arial' \n" + " ;\n"
            + " \"\n" + " >\n"
            + "Dr. [[doctorName]] Completed Your Appointment. Check the prescription given below in the attachment.</p>"
            + " <p style=\" text-align: left ;font-size:13px \">\n"
            + " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
            + " >mecareapplication@gmail.com</span\n" + " >\n" + " </p>\n"
            + " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
            + " Thanks & Regards, </p>\n"
            + " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">meCare Application team</span\n"
            + " </div>";

    public static final String MAIL_APPOINTMENT = "<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
            + "</style></head>"
            + "<div style=\"background-color: white; color:black  \">\n"
            + " <p style=\"text-align: left; font-size:15px ;\">Hi [[name]],</p>\n"
            + " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
            + " font-family: 'Arial' \n" + " ;\n"
            + " \"\n" + " >\n"
            + "Your appointment has been booked. Check the details given below.</p>"

            + "<table><tr><th>Doctor Name</th><th>Doctor Email</th><th>Speciality</th><th>Date of Appointment</th><th>Appointment Time</th></tr><tr><td>Dr.[[doctorName]]</td><td>[[doctorEmail]]</td><td>[[speciality]]</td><td>[[dateOfAppointment]]</td><td>[[appointmentTime]]</td></tr></table>"

            + " <p style=\" text-align: left ;font-size:13px \">\n"
            + " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
            + " >mecareapplication@gmail.com</span\n" + " >\n" + " </p>\n"
            + " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
            + " Thanks & Regards, </p>\n"
            + " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">meCare team</span\n"
            + " </div>";

    public static final List<String> ages = List.of("0-2","3-14","15-25","26-64","65+");
    public static final String DETAILS_MISMATCH = "details mismached" ;
    public static final String FILE_NAME_PRESENT = "file name is empty";
    public static final String FILE_UPLOADED = "Uploaded the file successfully: ";
    public static final String REPORT_NOT_FOUND ="No Report Found!! " ;
    public static final String DEFAULT_PAGE_SIZE = "10";

    private Constants() {
        throw new IllegalArgumentException();
    }
}
