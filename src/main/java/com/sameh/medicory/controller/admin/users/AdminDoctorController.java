package com.sameh.medicory.controller.admin.users;

import com.sameh.medicory.model.users.Doctor.DoctorRequestDTO;
import com.sameh.medicory.model.users.Doctor.DoctorResponseDTO;
import com.sameh.medicory.model.users.clinic.ClinicRequestDTO;
import com.sameh.medicory.model.users.clinic.ClinicResponseDTO;
import com.sameh.medicory.service.admin.users.AdminDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/doctors")
public class AdminDoctorController {

    private final AdminDoctorService doctorService;


    @GetMapping("/code/{code}")
    public ResponseEntity<DoctorResponseDTO> findDoctorByCode(@PathVariable String code) {
        DoctorResponseDTO doctor = doctorService.findDoctorByUserCode(code);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @GetMapping("/email/{userEmail}")
    public ResponseEntity<DoctorResponseDTO> findDoctorByUserEmai(@PathVariable String userEmail) {
        DoctorResponseDTO doctor = doctorService.findDoctoByUserEmail(userEmail);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @GetMapping("/name/{doctorName}")
    public ResponseEntity<List<DoctorResponseDTO>> findDoctorsByName(@PathVariable String doctorName) {
      List< DoctorResponseDTO> doctors = doctorService.findDoctorbyFullName(doctorName);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }


    @GetMapping("/id/{doctorId}/doctor")
    public ResponseEntity<DoctorRequestDTO> getAllDataOfDoctorById(@PathVariable long doctorId) {
        DoctorRequestDTO doctor = doctorService.showAllDoctorDataById(doctorId);
        return new ResponseEntity<>(doctor,HttpStatus.OK);
    }

    @PostMapping("/doctor")
   public ResponseEntity<String> addDoctor(@RequestBody DoctorRequestDTO newDoctor){
        String message = doctorService.addNewDoctor(newDoctor);
        return new ResponseEntity<>(message,HttpStatus.CREATED);
    }
    @PutMapping("/id/{doctorId}/doctor")
    public ResponseEntity<String> updateDoctor(@RequestBody DoctorRequestDTO updatedDoctor,@PathVariable long doctorId){
        String message = doctorService.updateDoctor(updatedDoctor,doctorId);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @DeleteMapping("/id/{doctorId}")
    public ResponseEntity<String> deleteDoctor(@PathVariable long doctorId){
        String  message = doctorService.deleteDoctorById(doctorId);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
}
