package com.freebills.controllers;

import com.freebills.controllers.mappers.CCTransactionMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CC Transaction Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/cc-transaction")
public class CCTransactionController {

    private final CCTransactionMapper ccTransactionMapper;

}
