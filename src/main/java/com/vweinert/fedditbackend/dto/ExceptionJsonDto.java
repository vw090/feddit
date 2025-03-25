package com.vweinert.fedditbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionJsonDto {
    private String path;
    private String error;
    private LocalDateTime timestamp;
    private int status;
}
