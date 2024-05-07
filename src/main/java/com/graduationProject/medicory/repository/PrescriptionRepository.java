package com.graduationProject.medicory.repository;

import com.graduationProject.medicory.entity.medicationEntities.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    @Query("FROM Prescription where owner.user.code = :userCode AND isPharmacyNeeded=true ")
    List<Prescription> findAllByOwnerIdPharmacyNeededSortedByUpdatedAt(String userCode);
    @Query("FROM Prescription WHERE id=:id AND owner.user.code = :userCode")
    Optional<Prescription> findPrescriptionByUserCodeAndPrescriptionId(String userCode, Long id);
    @Query("SELECT p FROM Prescription p where p.owner.user.code = :userCode AND p.prescriptionStatus=true AND p.isPharmacyNeeded=true order by p.updatedAt")
    List<Prescription> findAllActiveByOwnerIdPharmacyNeededSortedByUpdatedAt(String userCode);

    ///////
    @Query("FROM Prescription where owner.user.code = :userCode AND isLabNeeded=true ")
    List<Prescription> findAllByOwnerIdLabNeededSortedByUpdatedAt(String userCode);
//    @Query("FROM Prescription WHERE id=:id AND owner.user.code = :userCode")
//    Optional<Prescription> findPrescriptionByUserCodeAndTestId(String userCode, Long id);
    @Query("SELECT p FROM Prescription p where p.owner.user.code = :userCode AND p.prescriptionStatus=true AND p.isLabNeeded=true order by p.updatedAt")
    List<Prescription> findAllActiveByOwnerIdLabNeededSortedByUpdatedAt(String userCode);

}
