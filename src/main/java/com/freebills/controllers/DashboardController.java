package com.freebills.controllers;


import com.freebills.controllers.dtos.responses.DashboardResponseDTO;
import com.freebills.usecases.Dashboard;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/dashboard")
public class DashboardController {

    private final Dashboard dashboard;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public DashboardResponseDTO getAll(final Principal pricipal,
                                       @RequestParam(required = false) final Integer month,
                                       @RequestParam(required = false) final Integer year,
                                       Principal principal) {
        return dashboard.totalBalanceById(principal.getName(), month, year);
    }
}
