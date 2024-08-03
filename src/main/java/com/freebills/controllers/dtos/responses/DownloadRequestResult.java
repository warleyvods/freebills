package com.freebills.controllers.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DownloadRequestResult {

    private String downloadSignedUrl;
}
