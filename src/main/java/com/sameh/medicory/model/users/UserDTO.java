package com.sameh.medicory.model.users;

import com.sameh.medicory.entity.enums.Role;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private Role role;
    private boolean isEnabled;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}