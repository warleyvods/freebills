package com.freebills.gateways.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Type {
    DOCUMENT(false),
    IMAGE(true);

    private final boolean publicAccessible;
}