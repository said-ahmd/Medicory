package com.graduationProject.medicory.service.pharmacy.impl;

import com.graduationProject.medicory.entity.medicationEntities.Prescription;
import com.graduationProject.medicory.mapper.medicationsMappers.PrescriptionMapper;
import com.graduationProject.medicory.model.prescription.PrescriptionResponse;
import com.graduationProject.medicory.repository.MedicationsRepositories.PrescriptionRepository;
import com.graduationProject.medicory.service.pharmacy.PharmacyPrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PharmacyPrescriptionServiceImpl implements PharmacyPrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionMapper prescriptionMapper;

    @Override
    public List<PrescriptionResponse> getAllPrescriptionNeedPharmacy(String userCode) {
        List<Prescription> allPrescriptions = prescriptionRepository.findAllByUserCodePharmacyNeededSortedByUpdatedAt(userCode);

        List<PrescriptionResponse> response = allPrescriptions.stream()
                .map(prescription -> prescriptionMapper.toResponse(prescription))
                .toList();

        return response;
    }
    @Override
    public List<PrescriptionResponse> getActivePrescriptionNeedPharmacy(String userCode) {
        List<Prescription> allPrescriptions = prescriptionRepository.findAllActiveByOwnerIdPharmacyNeededSortedByUpdatedAt(userCode);

        List<PrescriptionResponse> response = allPrescriptions.stream()
                .map(prescription -> prescriptionMapper.toResponse(prescription))
                .toList();

        return response;
    }

    @Override
    public PrescriptionResponse getPrescriptionById(String userCode, Long prescriptionId) {

        Prescription prescription = prescriptionRepository.findPrescriptionByUserCodeAndPrescriptionId(userCode,prescriptionId).orElseThrow(
                ()->new IllegalArgumentException("Prescription id is wrong!")
        );
        PrescriptionResponse response = prescriptionMapper.toResponse(prescription);
        return response;
    }

}