package com.vweinert.fedditbackend.exception;

import com.vweinert.fedditbackend.dto.ExceptionJsonDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalControllerExceptionHandler{
    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody ExceptionJsonDto handleMethodArgumentNotValidException(HttpServletRequest request, Exception ex) {
        String error = ex.getMessage().substring(ex.getMessage().indexOf("default"));
        logger.debug("Invalid model on method call in controller: {}",error);
        return ExceptionJsonDto
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getServletPath())
                .timestamp(LocalDateTime.now())
                .error(error)
                .build();
    }
}
