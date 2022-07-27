--alter table login_details add created_at datetime not null datetime after role;
--alter table login_details add deleted bit(1)  default 0 after created_at;
--
--alter table doctor_details add created_at datetime not null after speciality;
--alter table doctor_details add updated_at datetime after created_at;
--alter table doctor_details add deleted bit(1)  default 0 after updated_at;
--
--alter table patient_details add created_at datetime not null after login_id;
--alter table patient_details add updated_at datetime after created_at;
--alter table patient_details add deleted bit(1)  default 0 after updated_at;
--
--alter table appointment_details add created_at datetime not null after patient_id;
--alter table appointment_details add updated_at datetime after created_at;
--alter table appointment_details add deleted bit(1)  default 0 after updated_at;
--
--alter table patient_attributes add created_at datetime not null after appointment_id;
--
--alter table patient_files add created_at datetime not null after type;
--
--alter table doctors_todolist add created_at datetime not null after doctor_id;
--
--alter table patient_prescriptions add created_at datetime not null after appointment_id;
