package com.dashboard.doctor_dashboard.Controller;

import com.dashboard.doctor_dashboard.Entity.DoctorDetails;
import com.dashboard.doctor_dashboard.Service.doctor_service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService service;

    @GetMapping
    public List<DoctorDetails> getAllDoctors(){
        return service.getAllDoctors();
    }

    @GetMapping("/id/{id}")
    public DoctorDetails getDoctorsById(@PathVariable("id") long id){
        return service.getDoctorById(id);
    }


    @GetMapping("/name/{name}")
    public List<DoctorDetails> getDoctorsByName(@PathVariable("name") String name){
        return service.getDoctorByFirstName(name);
    }

    @GetMapping("/age/{age}")
    public List<DoctorDetails> getDoctorsByAge(@PathVariable("age") short age){
        return service.getDoctorByAge(age);
    }


    @GetMapping("/email/{email}")
    public DoctorDetails getDoctorsByEmail(@PathVariable("email") String email){
        return service.getDoctorByEmail(email);
    }


    @GetMapping("/speciality/{speciality}")
    public List<DoctorDetails> getDoctorsBySpeciality(@PathVariable("speciality") String speciality){
        return service.getDoctorBySpeciality(speciality);
    }

    @PutMapping("/{id}")
    public DoctorDetails updateDoctorDetails(@PathVariable("id") long id, @RequestBody DoctorDetails details){
        return service.updateDoctor(details,id);
    }

    @PostMapping()
    public DoctorDetails addNewDoctor(@RequestBody DoctorDetails doctorDetails){
        return service.addDoctor(doctorDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteDoctor(@PathVariable("id") int id){
        return service.deleteDoctor(id);
    }

}