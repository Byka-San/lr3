package ru.mezenova.MySecondTestAppSpringBoot.model;


import lombok.*;

import javax.validation.constraints.NotBlank;


@Data
@Builder
@ToString
public class Response {
    private String uid;
    private String operationUid;
    private String systemTime;
    private Codes code;
    private ErrorCodes errorCode;
    private ErrorMessages errorMassage;
}
