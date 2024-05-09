package com.graduationProject.medicory.model.users.admin;

import com.graduationProject.medicory.entity.enums.Gender;
import com.graduationProject.medicory.entity.enums.MaritalStatus;
import com.graduationProject.medicory.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRequestDTO {
    private String firstName;
    private String lastName;
    private MaritalStatus maritalStatus;
    private Gender gender;
    private String email;
    private Role role;
    private boolean isEnabled;
    private List<String> phoneNumbers;
}
