package com.ysf.ipadress.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EResult {

    success("Başarılı!"),error("Hatalı!");
    private final String durum;
}
