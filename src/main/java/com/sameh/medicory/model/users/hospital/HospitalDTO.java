package com.sameh.medicory.model.users.hospital;

import com.sameh.medicory.model.users.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalDTO {
    private Long id;
    private String name;
    private String googleMapsLink;
    private String address;
    private UserDTO user;

}