package com.zzq.ebook.microservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultDTO {
    public static int SUCCESS_CODE=20000;
    public static int ERROR_CODE=50000;
    public static int FORBIDDEN_CODE=40300;
    int code;
    Object data;
    String message;

    public static ResultDTO success(Object data) {
        return ResultDTO.builder()
                .data(data)
                .code(SUCCESS_CODE)
                .message("success")
                .build();
    }

    public static ResultDTO error(String errMsg) {
        return ResultDTO.builder()
                .code(ERROR_CODE)
                .message(errMsg)
                .build();
    }

    public static ResultDTO forbidden(String errMsg) {
        return ResultDTO.builder()
                .code(FORBIDDEN_CODE)
                .message(errMsg)
                .build();
    }

}
