package com.graduationProject.medicory.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class ErrorDetails {
    private final String message;
    private final int statusCode;
    private final Date timeStamp;
}
