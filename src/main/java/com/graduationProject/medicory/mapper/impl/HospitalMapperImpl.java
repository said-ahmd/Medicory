package com.graduationProject.medicory.mapper.impl;

import com.graduationProject.medicory.entity.usersEntities.Hospital;
import com.graduationProject.medicory.entity.usersEntities.User;
import com.graduationProject.medicory.mapper.HospitalMapper;
import com.graduationProject.medicory.mapper.UserMapper;
import com.graduationProject.medicory.mapper.UserPhoneNumberMapper;
import com.graduationProject.medicory.model.users.hospital.HospitalRequestDTO;
import com.graduationProject.medicory.model.users.hospital.HospitalDTO;
import com.graduationProject.medicory.model.users.hospital.HospitalResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HospitalMapperImpl implements HospitalMapper {

    private final UserMapper map;
    private final UserPhoneNumberMapper phoneNumberMapper;

    @Override
    public HospitalDTO toDTO(Hospital hospital) {
        return new HospitalDTO(
                hospital.getId(),
                hospital.getName(),
                hospital.getGoogleMapsLink(),
                hospital.getAddress(),
                map.toDto(
                        hospital.getUser()
                )
        );
    }

    @Override
    public HospitalResponseDTO toResponseDTO(Hospital hospital) {
        return new HospitalResponseDTO(
                hospital.getId(),
                hospital.getName(),
                hospital.getUser().isEnabled()
        );
    }

    @Override
    public Hospital toEntity(HospitalDTO hospitalDTO) {
        return new Hospital(
                hospitalDTO.getId(),
                hospitalDTO.getName(),
                hospitalDTO.getGoogleMapsLink(),
                hospitalDTO.getAddress(),
                map.toEntity(
                        hospitalDTO.getUser()
                )
        );
    }

    @Override
    public Hospital toRequestEntity(HospitalRequestDTO hospital) {
        User user = User.builder()
                .role(hospital.getRole())
                .enabled(hospital.isEnabled())
                .email(hospital.getEmail())
                .userPhoneNumbers(
                        phoneNumberMapper.toRequestEntity(hospital.getUserPhoneNumbers())
                )
                .build();
        return Hospital.builder()
                .name(hospital.getName())
                .googleMapsLink(hospital.getGoogleMapsLink())
                .address(hospital.getAddress())
                .user(user)
                .build();
    }
}
