package ateneu.sgcti.shared.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CustomHandler {

    private String message;
    private String details;
    private int status;
    private LocalDateTime timestamp;
}
