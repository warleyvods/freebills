package com.freebills.controllers;

import com.freebills.usecases.ForgotPassword;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Forgot Password Controller")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RequestMapping("v1/forget")
@RestController
public class ForgotPasswordController {

    private final ForgotPassword forgotPassword;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/password")
    public void forgetPassword(@RequestParam final String email) throws MailjetSocketTimeoutException, MailjetException {
        forgotPassword.forgotPassword(email);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/confirmation")
    public void confirmationPassword(@RequestParam final String hash) {
        forgotPassword.validationForgetPassword(hash);
    }
}
