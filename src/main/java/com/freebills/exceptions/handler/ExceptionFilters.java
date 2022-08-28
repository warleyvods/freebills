package com.freebills.exceptions.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ExceptionFilters {

    private String title;
    private Integer status;
    private String details;
    private LocalDateTime timestamp;
    private String devMsg;

}
