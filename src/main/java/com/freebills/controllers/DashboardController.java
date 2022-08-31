package com.freebills.controllers;


import com.freebills.controllers.dtos.responses.DashboardResponseDTO;
import com.freebills.usecases.Dashboard;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/dashboard")
public class DashboardController {

    private final Dashboard dashboard;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public DashboardResponseDTO getAll(@RequestParam final Long userId) {
        return dashboard.totalBalanceById(userId);
    }
}
