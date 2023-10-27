package com.phonecompany.billing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CallDetails {

    private String telNum;
    private LocalDateTime start;
    private LocalDateTime end;
    private int occurrence;

}
