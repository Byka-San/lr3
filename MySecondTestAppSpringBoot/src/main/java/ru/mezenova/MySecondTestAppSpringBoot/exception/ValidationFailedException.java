package ru.mezenova.MySecondTestAppSpringBoot.exception;

import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;

@Slf4j
public class ValidationFailedException extends Exception {
    public ValidationFailedException(String message){
        super(message);
    }

}