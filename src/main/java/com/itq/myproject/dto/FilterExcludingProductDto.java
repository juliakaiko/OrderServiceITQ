package com.itq.myproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class FilterExcludingProductDto {

    @NotNull(message = "StartDate may not be empty")
    LocalDate startDate;

    @NotNull(message = "EndDate may not be empty")
    LocalDate endDate;

    @NotBlank(message = "Product Article may not be empty")
    String productArticle;
}
