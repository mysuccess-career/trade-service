package com.db.demo.demo.controllerAdvice;

import com.db.demo.demo.dto.ErrorTemplate;
import com.db.demo.demo.dto.ResponseTemplate;
import com.db.demo.demo.exception.TradeServiceException;
import com.db.demo.demo.messages.TradeServiceMessageSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Savitha
 */

@RestControllerAdvice(basePackages = "com.db.demo.demo.controller")
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ControllerAdvice.class);

    @Autowired
    TradeServiceMessageSource tradeServiceMessages;

    @ExceptionHandler(TradeServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<ResponseTemplate> handleServiceException(TradeServiceException e) {
        LOGGER.error("Exception occurred while creating/updating a trade{} ", e);
        List<ErrorTemplate> errorTemplates = new ArrayList<>();
        ErrorTemplate errorTemplate = ErrorTemplate.builder().errorCode(HttpStatus.BAD_REQUEST.value()).errorMessage(tradeServiceMessages.getMessage("service.version.low", "Trade service version is lower than the existing version of trade")).build();
        errorTemplates.add(errorTemplate);
        return new ResponseEntity<>((ResponseTemplate.builder().errors(errorTemplates).build())
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Object> handleParseException(ParseException e) {
        ErrorTemplate errorTemplate = ErrorTemplate.builder().errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).errorMessage(e.getMessage()).build();
        List<ErrorTemplate> errorTemplates = new ArrayList<>();
        return new ResponseEntity<>(ResponseTemplate.builder().errors(errorTemplates).build()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
