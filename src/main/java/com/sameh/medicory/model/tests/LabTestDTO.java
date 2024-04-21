package com.sameh.medicory.model.tests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabTestDTO {
    private Long id;
    private Long name;
    private String description;
    private String imageResult;
    private String resultNotes;
    private boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
