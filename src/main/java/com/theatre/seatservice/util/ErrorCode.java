package com.theatre.seatservice.util;

import lombok.Getter;

@Getter
public enum ErrorCode {

    DEFAULT("ERR_00", "Internal Server Error"),
    PERFORMANCE_NOT_FOUND("PRF_01", "Performance Not Found"),
    SEAT_ZONE_NOT_FOUND("SZN_01", "Seat Zone Not Found");

    final String errorCode;
    final String errorDescription;

    ErrorCode(String errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

}
