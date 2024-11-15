package com.freebills.controllers;


import com.freebills.controllers.dtos.responses.DashboardExpenseResponseDTO;
import com.freebills.controllers.dtos.responses.DashboardGraphResponseDTO;
import com.freebills.controllers.dtos.responses.DashboardResponseDTO;
import com.freebills.controllers.dtos.responses.DashboardRevenueResponseDTO;
import com.freebills.exceptions.PermissionDeniedException;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.usecases.Dashboard;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/dashboard")
public class DashboardController {

    private final Dashboard dashboard;

    @ResponseStatus(OK)
    @GetMapping("/expense-graph")
    public DashboardGraphResponseDTO getDashboardExpenseGraph(
            @RequestParam(required = false) final Integer month,
            @RequestParam(required = false) final Integer year,
            final Principal principal) {
        final var date = LocalDate.now();

        if (month != null) {
            validateDate(month);
        }

        return dashboard.getDonutsGraph(
                principal.getName(),
                month,
                year == null ? date.getYear() : year,
                TransactionType.EXPENSE);
    }

    @ResponseStatus(OK)
    @GetMapping("/revenue-graph")
    public DashboardGraphResponseDTO getDashboardRevenueGraph(
            @RequestParam(required = false) final Integer month,
            @RequestParam(required = false) final Integer year,
            final Principal principal) {
        final var date = LocalDate.now();

        if (month != null) {
            validateDate(month);
        }

        return dashboard.getDonutsGraph(
                principal.getName(),
                month,
                year == null ? date.getYear() : year,
                TransactionType.REVENUE);
    }

    @ResponseStatus(OK)
    @GetMapping("/total")
    public DashboardResponseDTO getTotalDashboard(
            @RequestParam(required = false) final Integer month,
            @RequestParam(required = false) final Integer year,
            final Principal principal) {
        final var date = LocalDate.now();

        if (month != null) {
            validateDate(month);
        }

        return dashboard.getTotalDashboard(principal.getName(),
                month,
                year == null ? date.getYear() : year
        );
    }

    @ResponseStatus(OK)
    @GetMapping("/revenue")
    public DashboardRevenueResponseDTO getTotalRevenueDashboard(
            @RequestParam(required = false) final Integer month,
            @RequestParam(required = false) final Integer year,
            final Principal principal) {
        final var date = LocalDate.now();

        if (month != null) {
            validateDate(month);
        }

        return dashboard.getDashboardRevenue(
                principal.getName(),
                month,
                year == null ? date.getYear() : year
        );
    }

    @ResponseStatus(OK)
    @GetMapping("/expense")
    public DashboardExpenseResponseDTO getTotalExpenseDashboard(
            @RequestParam(required = false) final Integer month,
            @RequestParam(required = false) final Integer year,
            final Principal principal) {
        final var date = LocalDate.now();

        if (month != null) {
            validateDate(month);
        }

        return dashboard.getDashboardExpense(
                principal.getName(),
                month,
                year == null ? date.getYear() : year
        );
    }

    private void validateDate(final Integer month) {
        if (month < 1 || month > 12) {
            throw new PermissionDeniedException("invalid month!");
        }
    }
}
