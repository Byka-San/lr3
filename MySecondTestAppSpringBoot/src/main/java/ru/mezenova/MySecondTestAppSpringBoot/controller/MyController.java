package ru.mezenova.MySecondTestAppSpringBoot.controller;

import java.util.Date;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import ru.mezenova.MySecondTestAppSpringBoot.exception.UnsupertCodeException;
import ru.mezenova.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.mezenova.MySecondTestAppSpringBoot.model.*;
import ru.mezenova.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.mezenova.MySecondTestAppSpringBoot.service.ModifySystemTimeResponseService;
import ru.mezenova.MySecondTestAppSpringBoot.service.UnsupportedCodeService;
import ru.mezenova.MySecondTestAppSpringBoot.service.ValidationService;
import ru.mezenova.MySecondTestAppSpringBoot.util.DateTimeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@RestController
public class MyController {
    private final ValidationService validationService;
    private final UnsupportedCodeService unsupportedCodeService;
    private final ModifySystemTimeResponseService modifySystemTimeResponseService;
    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService") ModifySystemTimeResponseService modifySystemTimeResponseService,
                        UnsupportedCodeService unsupportedCodeService) {
        this.validationService = validationService;
        this.unsupportedCodeService = unsupportedCodeService;
        this.modifySystemTimeResponseService = modifySystemTimeResponseService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request, BindingResult bindingResult) {

        log.info("request:{}",request);
        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormatFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMassage(ErrorMessages.EMPTY)
                .build();


        try{
            validationService.isValid(bindingResult);
            unsupportedCodeService.isCode(request);
        }catch (ValidationFailedException e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMassage(ErrorMessages.VALIDATION);
            log.error(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch (UnsupertCodeException e){
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMassage(ErrorMessages.UNSUPPORTED);
            log.error(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.I_AM_A_TEAPOT);
        }
        catch (Exception e){
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMassage(ErrorMessages.UNKNOWN);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("response:{}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public ModifySystemTimeResponseService getModifySystemTimeResponseService() {
        return modifySystemTimeResponseService;
    }
}
