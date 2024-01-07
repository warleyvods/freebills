package com.freebills.usecases;

import com.freebills.controllers.dtos.requests.UserPutPasswordRequestDTO;
import com.freebills.controllers.mappers.UserMapper;
import com.freebills.gateways.entities.ForgetPassword;
import com.freebills.gateways.EmailGateway;
import com.freebills.gateways.UserGateway;
import com.freebills.repositories.ForgetPasswordRepository;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.Optional;

@Component
public record ForgotPassword(UserGateway userGateway, ForgetPasswordRepository forgetPasswordRepository,
                             EmailGateway emailGateway, UpdateUser updateUser, UserMapper userMapper) {

    private static final String DEFAULT_PASS = "123456";

    public void forgotPassword(final String email) throws MailjetException, MailjetSocketTimeoutException {
        final var user = userGateway.findByEmail(email);
        if (user.isPresent()) {
            forgetPasswordRepository.findByEmail(user.get().getEmail()).ifPresent(password -> forgetPasswordRepository.deleteById(password.getId()));
            final String hash = DigestUtils.md5Hex(LocalDateTime.now().toString()).toUpperCase();
            final var forgetPasswordSaved = forgetPasswordRepository.save(new ForgetPassword(hash, user.get().getEmail()));
            sendEmail(forgetPasswordSaved);
        }
    }

    private void sendEmail(ForgetPassword forgetPasswordSaved) throws MailjetException, MailjetSocketTimeoutException {
        emailGateway.sendMail(forgetPasswordSaved.getEmail(), forgetPasswordSaved.getId());
    }

    public void validationForgetPassword(String hash) {
        final Optional<ForgetPassword> forget = forgetPasswordRepository.findById(hash);

        forget.flatMap(forgetPassword -> userGateway.findByEmail(forgetPassword.getEmail())).ifPresent(userToUpdate -> {
            updateUser.updatePassword(userMapper.updatePasswordFromDTO(new UserPutPasswordRequestDTO(userToUpdate.getId(), DEFAULT_PASS), userToUpdate));
            forgetPasswordRepository.deleteById(hash);
        });
    }
}
