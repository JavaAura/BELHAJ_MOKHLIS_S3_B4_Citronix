package com.app.Citronix.Model.DTO.Response;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArbreResponseLight {
    private LocalDate datePlantation;
    private long age;
} 