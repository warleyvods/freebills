package com.freebills.controllers;


import com.freebills.controllers.dtos.responses.DashboardExpenseResponseDTO;
import com.freebills.controllers.dtos.responses.DashboardGraphResponseDTO;
import com.freebills.controllers.dtos.responses.DashboardResponseDTO;
import com.freebills.controllers.dtos.responses.DashboardRevenueResponseDTO;
import com.freebills.domains.enums.TransactionType;
import com.freebills.usecases.Dashboard;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/dashboard")
public class DashboardController {

    private final Dashboard dashboard;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/expense-graph")
    public DashboardGraphResponseDTO getDashboardExpenseGraph(
            @RequestParam(required = false) final Integer month,
            @RequestParam(required = false) final Integer year,
            final Principal principal) {
        final var date = LocalDate.now(); //TODO provisório remover após adicionar botoes de troca no front.
        return dashboard.getDonutsGraph(
                principal.getName(),
                month == null ? date.getMonthValue() : month,
                year == null ? date.getYear() : year,
                TransactionType.EXPENSE);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/revenue-graph")
    public DashboardGraphResponseDTO getDashboardRevenueGraph(
            @RequestParam(required = false) final Integer month,
            @RequestParam(required = false) final Integer year,
            final Principal principal) {
        final var date = LocalDate.now(); //TODO provisório remover após adicionar botoes de troca no front.
        return dashboard.getDonutsGraph(
                principal.getName(),
                month == null ? date.getMonthValue() : month,
                year == null ? date.getYear() : year,
                TransactionType.REVENUE);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/total")
    public DashboardResponseDTO getTotalDashboard(
            @RequestParam(required = false) final Integer month,
            @RequestParam(required = false) final Integer year,
            final Principal principal) {
        return dashboard.getTotalDashboard(principal.getName(), month, year);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/revenue")
    public DashboardRevenueResponseDTO getTotalRevenueDashboard(
            @RequestParam(required = false) final Integer month,
            @RequestParam(required = false) final Integer year,
            final Principal principal) {
        return dashboard.getDashboardRevenue(principal.getName(), month, year);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/expense")
    public DashboardExpenseResponseDTO getTotalExpenseDashboard(
            @RequestParam(required = false) final Integer month,
            @RequestParam(required = false) final Integer year,
            final Principal principal) {
        return dashboard.getDashboardExpense(principal.getName(), month, year);
    }
}
