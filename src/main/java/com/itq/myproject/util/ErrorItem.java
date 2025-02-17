package com.itq.myproject.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // @RequiredArgsConstructor (final fields)+@ToString+@EqualsAndHashCode+@Getter+@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorItem {

    private int status;
    private String message;
    private String timestamp;

}
