package com.freebills.gateways;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailGateway {

    @Value("${mail.apiKey}")
    private String apiKey;

    @Value("${mail.apiSecret}")
    private String apiSecret;

    @Async
    public void sendMail(final String to, final String code) throws MailjetException, MailjetSocketTimeoutException {
        MailjetRequest request;
        MailjetResponse response;
        MailjetClient client = new MailjetClient(apiKey, apiSecret, new ClientOptions("v3.1"));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", to)
                                        .put("Name", "Free Bills"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", to)
                                                .put("Name", "Recuperação de Senha")))
                                .put(Emailv31.Message.SUBJECT, "Recuperação de senha - Free Bills")
                                .put(Emailv31.Message.TEXTPART, "Recuperação de Senha")
                                .put(Emailv31.Message.HTMLPART, "<h3>Houve uma solicitação para recuperação de senha do usuário: " + to + "" +
                                        "</h3><br />Codigo: <strong>" + code + "</strong><br /><br />" +
                                        "Caso não tenha solicitado a alteração de senha favor desconsiderar.")
                                .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
        response = client.post(request);
        log.info(String.valueOf(response.getStatus()));
        log.info(String.valueOf(response.getData()));
    }
}
