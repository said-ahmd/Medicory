package com.sameh.medicory.service.doctor;

import com.sameh.medicory.entity.labTestsEntities.LabTest;
import com.sameh.medicory.entity.otherEntities.Allergies;
import com.sameh.medicory.entity.otherEntities.ChronicDiseases;
import com.sameh.medicory.entity.otherEntities.Immunization;
import com.sameh.medicory.entity.otherEntities.Surgery;
import com.sameh.medicory.entity.phoneEntities.OwnerPhoneNumber;
import com.sameh.medicory.entity.phoneEntities.RelativePhoneNumber;
import com.sameh.medicory.entity.usersEntities.Owner;
import com.sameh.medicory.entity.usersEntities.User;
import com.sameh.medicory.exception.RecordNotFoundException;
import com.sameh.medicory.mapper.*;
import com.sameh.medicory.model.immunization.ImmunizationRequestDTO;
import com.sameh.medicory.model.immunization.ImmunizationResponseDTO;
import com.sameh.medicory.model.chronicDisease.ChronicDiseasesRequestDTO;
import com.sameh.medicory.model.chronicDisease.ChronicDiseasesResponseDTO;
import com.sameh.medicory.model.surgery.SurgeryRequestDTO;
import com.sameh.medicory.model.surgery.SurgeryResponseDTO;
import com.sameh.medicory.model.allergies.AllergiesRequestDTO;
import com.sameh.medicory.model.allergies.AllergiesResponseDTO;
import com.sameh.medicory.model.labtests.LabTestDTO;
import com.sameh.medicory.model.patient.PatientPersonalInformation;
import com.sameh.medicory.repository.*;
import com.sameh.medicory.utils.OwnerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final OwnerRepository ownerRepository;
    private final ChronicDiseasesMapper chronicDiseasesMapper;
    private final OwnerContext ownerContext;
    private final AllergiesMapper allergiesMapper;
    private final ImmunizationMapper immunizationMapper;
    private final SurgeryMapper surgeryMapper;
    private final LabTestMapper labTestMapper;
    private final LabTestRepository labTestRepository;
    private final ChronicDiseasesRepository chronicDiseasesRepository;
    private final AllergiesRepository allergiesRepository;
    private final ImmunizationRepository immunizationRepository;
    private final SurgeryRepository surgeryRepository;

    @Override
    public PatientPersonalInformation getPatientPersonalInformation(Long ownerId) {
        User patientUser = ownerContext.getCurrentUser(ownerId);
        Owner patientOwner = ownerContext.getCurrentOwner(ownerId);
        log.info("Doctor Wants to get patient personal information for owner with id {}", patientOwner.getId());
        PatientPersonalInformation patientPersonalInformation = new PatientPersonalInformation(
                patientOwner.getFirstName() + " " + patientOwner.getMiddleName() + " " + patientOwner.getLastName(),
                getCurrentAge(patientOwner.getDateOfBirth()),
                patientOwner.getGender().name(),
                getPatientPhoneNumbers(patientUser),
                getPatientRelativePhoneNumbers(patientUser),
                patientOwner.getAddress(),
                patientUser.getEmail(),
                patientOwner.getBloodType().name()
        );
        log.info("patient personal information for owner with id: {} is {}", patientOwner.getId(), patientPersonalInformation);
        return patientPersonalInformation;
    }

    @Override
    public List<ChronicDiseasesResponseDTO> getPatientChronicDiseases(Long ownerId) {
        Owner patientOwner = ownerContext.getCurrentOwner(ownerId);
        log.info("Doctor Wants to get patient Chronic Diseases for owner with id {}", patientOwner.getId());
        List<ChronicDiseases> chronicDiseases = patientOwner.getChronicDiseases();
        List<ChronicDiseasesResponseDTO> chronicDiseasesResponseDTOS = chronicDiseases
                .stream()
                .map(chronicDiseasesMapper::toDTO)
                .collect(Collectors.toList());
        log.info("patient Chronic Diseases for owner with id: {} is {}", patientOwner.getId(), chronicDiseases);
        return chronicDiseasesResponseDTOS;
    }

    @Override
    public String addNewChronicDiseasesForPatient(ChronicDiseasesRequestDTO chronicDiseasesRequestDTO, Long ownerId) {
        Owner patientOwner = ownerContext.getCurrentOwner(ownerId);
        log.info("Doctor wants to add a new chronic disease: {} for patient with owner id: {}", chronicDiseasesRequestDTO, patientOwner.getId());

        ChronicDiseases newChronicDisease = chronicDiseasesMapper.toEntity(chronicDiseasesRequestDTO);
        newChronicDisease.setOwner(patientOwner);
        List<ChronicDiseases> patientChronicDiseases = patientOwner.getChronicDiseases();
        patientChronicDiseases.add(newChronicDisease);
        log.info("patientChronicDiseases {}", patientChronicDiseases);
        patientOwner.setChronicDiseases(patientChronicDiseases);
        ownerRepository.save(patientOwner);

        log.info("The new chronic disease added successfully. All the patient chronic diseases: {}", patientOwner.getChronicDiseases());
        return "Chronic Disease added successfully";
    }

    @Override
    public ChronicDiseasesResponseDTO findChronicDiseasesById(Long diseasesId) {
        ChronicDiseases chronicDisease =  chronicDiseasesRepository.findById(diseasesId)
                .orElseThrow(() -> new RecordNotFoundException("This Chronic Disease with id "+diseasesId+" doesn't exist"));
        ChronicDiseasesResponseDTO chronicDiseasesResponseDTO = chronicDiseasesMapper.toDTO(chronicDisease);
        log.info("Chronic Disease with id  {}, details {}", diseasesId, chronicDiseasesResponseDTO);
        return chronicDiseasesResponseDTO;
    }

    @Override
    public String updateChronicDisease(ChronicDiseasesRequestDTO chronicDiseasesRequestDTO, Long diseasesId, Long ownerId) {
        Owner patientOwner = ownerContext.getCurrentOwner(ownerId);
        log.info("Doctor want to update chronic disease with id {}", diseasesId);
        ChronicDiseases existChronicDiseases = chronicDiseasesRepository.findById(diseasesId)
                .orElseThrow(() -> new RecordNotFoundException("This Chronic Disease with id "+diseasesId+" doesn't exist"));
        ChronicDiseases updatedChronicDiseases = chronicDiseasesMapper.toEntity(chronicDiseasesRequestDTO);
        updatedChronicDiseases.setOwner(patientOwner);
        updatedChronicDiseases.setId(diseasesId);
        chronicDiseasesRepository.save(updatedChronicDiseases);
        log.info("Chronic Disease updated successfully this before update {} and This after update {}", existChronicDiseases, updatedChronicDiseases);
        return "Success";
    }

    @Override
    public String deleteChronicDiseases(Long diseasesId) {
        log.info("Doctor want to delete Chronic chronic disease with id {}", diseasesId);
        ChronicDiseases chronicDisease = chronicDiseasesRepository.findById(diseasesId)
                .orElseThrow(() -> new RecordNotFoundException("This Chronic Disease with id "+diseasesId+" Already doesn't exist"));
        chronicDiseasesRepository.delete(chronicDisease);
        log.info("Chronic Disease deleted successfully");
        return "success";
    }




    @Override
    public List<AllergiesResponseDTO> getPatientAllergies(Long ownerId) {
        Owner patientOwner = ownerContext.getCurrentOwner(ownerId);
        log.info("Doctor want to get patient Allergies owner with id: {}", patientOwner.getAllergies());
        List<AllergiesResponseDTO> allergiesResponseDTOS = patientOwner.getAllergies()
                .stream()
                .map(allergiesMapper::toDTO)
                .collect(Collectors.toList());
        log.info("All Patient Allergies {}", allergiesResponseDTOS);
        return allergiesResponseDTOS;
    }

    @Override
    public String addNewAllergiesForPatient(AllergiesRequestDTO allergiesRequestDTO, Long ownerId) {
        Owner patientOwner = ownerContext.getCurrentOwner(ownerId);
        log.info("Doctor want to add new Allergies: {} for patient owner his ID: {}", allergiesRequestDTO,patientOwner.getId());
        Allergies allergies = allergiesMapper.toEntity(allergiesRequestDTO);
        allergies.setOwner(patientOwner);
        List<Allergies> patientAllergies = patientOwner.getAllergies();
        patientAllergies.add(allergies);
        patientOwner.setAllergies(patientAllergies);
        ownerRepository.save(patientOwner);
        log.info("the new Allergies Added successfully {}", patientOwner.getAllergies());
        return "New Allergies Added successfully";
    }

    @Override
    public AllergiesResponseDTO findAllergiesById(Long allergiesId) {
        Allergies allergies = allergiesRepository.findById(allergiesId)
                .orElseThrow(() -> new RecordNotFoundException("This Allergies with id "+allergiesId+" doesn't exist"));
          AllergiesResponseDTO allergiesResponseDTO = allergiesMapper.toDTO(allergies);
        log.info("Allergies with id  {}, details {}", allergiesId, allergiesResponseDTO);
        return allergiesResponseDTO;
    }

    @Override
    public String updateAllergies(AllergiesRequestDTO allergiesRequestDTO, Long allergiesId, Long ownerId) {
        Owner patientOwner = ownerContext.getCurrentOwner(ownerId);
        log.info("Doctor want to update Allergies with id {}", allergiesId);
        Allergies existAllergies = allergiesRepository.findById(allergiesId)
                .orElseThrow(() -> new RecordNotFoundException("This Allergies with id "+allergiesId+" doesn't exist"));
        Allergies updatedAllergies = allergiesMapper.toEntity(allergiesRequestDTO);
        updatedAllergies.setOwner(patientOwner);
        updatedAllergies.setId(allergiesId);
        allergiesRepository.save(updatedAllergies);
        log.info("Allergies updated successfully this before update {} and This after update {}", existAllergies, updatedAllergies);
        return "Success";
    }

    @Override
    public String deleteAllergies(Long allergiesId) {
        log.info("Doctor want to delete Allergies disease with id {}", allergiesId);
        Allergies allergies = allergiesRepository.findById(allergiesId)
                .orElseThrow(() -> new RecordNotFoundException("This Allergies with id "+allergiesId+" Already doesn't exist"));
        allergiesRepository.delete(allergies);
        log.info("Allergies deleted successfully");
        return "success";
    }





    @Override
    public List<ImmunizationResponseDTO> getaAllPatientImmunizations(Long ownerId) {
        Owner patientOwner = ownerContext.getCurrentOwner(ownerId);
        log.info("Doctor want to get All Immunizations for patient owner with id {}", patientOwner.getId());
        List<ImmunizationResponseDTO> immunizationResponseDTOS = patientOwner.getImmunizations()
                .stream()
                .map(immunizationMapper::toDTO)
                .collect(Collectors.toList());
        log.info("All Immunizations {}", immunizationResponseDTOS);
        return immunizationResponseDTOS;
    }

    @Override
    public String addNewImmunizationForPatient(ImmunizationRequestDTO immunizationRequestDTO, Long ownerId) {
        Owner patientOwner = ownerContext.getCurrentOwner(ownerId);
        log.info("Doctor want to add this new Immunization {}, for owner with id {}", immunizationRequestDTO, patientOwner.getId());
        Immunization newImmunization = immunizationMapper.toEntity(immunizationRequestDTO);
        newImmunization.setOwner(patientOwner);
        List<Immunization> allPatientImmunizations = patientOwner.getImmunizations();
        allPatientImmunizations.add(newImmunization);
        patientOwner.setImmunizations(allPatientImmunizations);
        ownerRepository.save(patientOwner);
        log.info("the New immunization added successfully {}", patientOwner.getImmunizations());
        return "New Immunization Added Successfully";
    }

    @Override
    public ImmunizationResponseDTO findImmunizationById(Long immunizationId) {
        Immunization immunization = immunizationRepository.findById(immunizationId)
                .orElseThrow(() -> new RecordNotFoundException("This immunization with id "+immunizationId+" doesn't exist"));
        ImmunizationResponseDTO immunizationResponseDTO = immunizationMapper.toDTO(immunization);
        log.info("immunization with id  {}, details {}", immunizationId, immunizationResponseDTO);
        return immunizationResponseDTO;
    }

    @Override
    public String updateImmunization(ImmunizationRequestDTO immunizationRequestDTO, Long immunizationId, Long ownerId) {
        Owner patientOwner = ownerContext.getCurrentOwner(ownerId);
        log.info("Doctor want to update Immunization with id {}", immunizationId);
        Immunization existImmunization = immunizationRepository.findById(immunizationId)
                .orElseThrow(() -> new RecordNotFoundException("This Immunization with id "+immunizationId+" doesn't exist"));
        Immunization updatedImmunization = immunizationMapper.toEntity(immunizationRequestDTO);
        updatedImmunization.setOwner(patientOwner);
        updatedImmunization.setId(immunizationId);
        immunizationRepository.save(updatedImmunization);
        log.info("Immunization updated successfully this before update {} and This after update {}", existImmunization, updatedImmunization);
        return "Success";
    }

    @Override
    public String deleteImmunization(Long immunizationId) {
        log.info("Doctor want to delete Immunization disease with id {}", immunizationId);
        Immunization immunization = immunizationRepository.findById(immunizationId)
                .orElseThrow(() -> new RecordNotFoundException("This Immunization with id "+immunizationId+" Already doesn't exist"));
        immunizationRepository.delete(immunization);
        log.info("Immunization deleted successfully");
        return "success";

    }




    @Override
    public List<SurgeryResponseDTO> getPatientSurgicalHistory(Long ownerId) {
        Owner patientOwner = ownerContext.getCurrentOwner(ownerId);
        log.info("Doctor want to Surgical History for patient owner with id {}", patientOwner.getId());
        List<SurgeryResponseDTO> surgeryResponseDTOS = patientOwner.getSurgeries()
                .stream()
                .map(surgeryMapper::toDTO)
                .collect(Collectors.toList());
        log.info("patient Surgical History {}", surgeryResponseDTOS);
        return surgeryResponseDTOS;
    }

    @Override
    public String addNewSurgeryForPatient(SurgeryRequestDTO surgeryRequestDTO, Long ownerId) {
        Owner patientOwner = ownerContext.getCurrentOwner(ownerId);
        log.info("Doctor want to add this new SurgeryRepository {}, for owner with id {}", surgeryRequestDTO, patientOwner.getId());
        Surgery surgery = surgeryMapper.toEntity(surgeryRequestDTO);
        surgery.setOwner(patientOwner);
        List<Surgery> allPatientSurgeries = patientOwner.getSurgeries();
        allPatientSurgeries.add(surgery);
        patientOwner.setSurgeries(allPatientSurgeries);
        ownerRepository.save(patientOwner);
        log.info("the New SurgeryRepository added successfully {}", patientOwner.getSurgeries());
        return "New SurgeryRepository Added Successfully";
    }

    @Override
    public SurgeryResponseDTO findSurgeryById(Long surgeryId) {
        Surgery surgery = surgeryRepository.findById(surgeryId)
                .orElseThrow(() -> new RecordNotFoundException("This Surgery with id "+surgeryId+" doesn't exist"));
        SurgeryResponseDTO surgeryResponseDTO = surgeryMapper.toDTO(surgery);
        log.info("Surgery with id  {}, details {}", surgeryId, surgeryResponseDTO);
        return surgeryResponseDTO;
    }

    @Override
    public String updateSurgery(SurgeryRequestDTO surgeryRequestDTO, Long surgeryId, Long ownerId) {
        Owner patientOwner = ownerContext.getCurrentOwner(ownerId);
        log.info("Doctor want to update Surgery with id {}", surgeryId);
        Surgery existSurgery = surgeryRepository.findById(surgeryId)
                .orElseThrow(() -> new RecordNotFoundException("This Surgery with id "+surgeryId+" doesn't exist"));
        Surgery updatedSurgery = surgeryMapper.toEntity(surgeryRequestDTO);
        updatedSurgery.setOwner(patientOwner);
        updatedSurgery.setId(surgeryId);
        surgeryRepository.save(updatedSurgery);
        log.info("Surgery updated successfully this before update {} and This after update {}", existSurgery, updatedSurgery);
        return "Success";
    }

    @Override
    public String deleteSurgery(Long surgeryId) {
        log.info("Doctor want to delete Surgery disease with id {}", surgeryId);
        Surgery surgery = surgeryRepository.findById(surgeryId)
                .orElseThrow(() -> new RecordNotFoundException("This Surgery with id "+surgeryId+" Already doesn't exist"));
        surgeryRepository.delete(surgery);
        log.info("Surgery deleted successfully");
        return "success";
    }




    @Override
    public List<LabTestDTO> findAllLabTestsForPatient(Long ownerId) {
        Owner patientOwner = ownerContext.getCurrentOwner(ownerId);
        log.info("Doctor want to get all Lab Tests for owner with id {}", patientOwner.getId());
        List<LabTestDTO> labTestDTOS = patientOwner.getLabTests()
                .stream()
                .map(labTestMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Patient Lab Tests {}", labTestDTOS);
        return labTestDTOS;
    }

    @Override
    public String deleteLabTestFromHistory(Long testId) {
        LabTest labTest = labTestRepository.findById(testId)
                .orElseThrow(() -> new RecordNotFoundException("This Lab test Already doesn't exists"));
        labTestRepository.delete(labTest);
        return "deleted successfully";
    }

    private Integer getCurrentAge(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(dateOfBirth, currentDate);
        return period.getYears();
    }

    private List<String> getPatientPhoneNumbers(User patientUser){
        List<OwnerPhoneNumber> phoneNumbers =patientUser.getOwnerPhoneNumbers();
        List<String> patientPhoneNumbers = phoneNumbers.stream()
                .map(phoneNumber -> phoneNumber.getPhone())
                .collect(Collectors.toList());
        return patientPhoneNumbers;
    }

    private List<String> getPatientRelativePhoneNumbers(User patientUser){
        List<RelativePhoneNumber> relativePhoneNumbers = patientUser.getRelativePhoneNumbers();
        List<String> patientRelativePhoneNumbers = relativePhoneNumbers.stream()
                .map(phoneNumber -> phoneNumber.getPhone())
                .collect(Collectors.toList());
        return patientRelativePhoneNumbers;
    }

}