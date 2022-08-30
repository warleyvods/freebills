package com.freebills.usecases;

import com.freebills.gateways.AccountGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public record DeleteAccount(AccountGateway accountService,FindAccount findAccount) {


    public void byId(final Long id){
        accountService.deleteById(id);
        log.info("[deleteAccount][account deleted: {}]",id);
    }


}
