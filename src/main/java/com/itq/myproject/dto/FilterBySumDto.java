package com.itq.myproject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class FilterBySumDto {

    @NotNull(message = "Sum may not be empty")
    private Long totalSum;

    @NotNull(message = "Date may not be empty")
    @Past(message = "Date cannot be in the future")
    private LocalDate orderDate;
}
