package com.infopulse.exceptions.common;

import com.infopulse.dto.ErrorInfo;
import com.infopulse.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerService {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UserAlreadyExist.class,
            IncorrectParameterException.class,
            UserAlreadyBanedException.class,
            UserCanNotBeUnBanedException.class})
    @ResponseBody
    public ErrorInfo handleBadRequest(HttpServletRequest request, Exception exception){
        return new ErrorInfo().setMessage(exception.getMessage())
                .setDeveloperMessage(exception.toString())
                .setUri(request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    @ResponseBody
    public ErrorInfo handleNotFound(HttpServletRequest request, Exception exception){
        return new ErrorInfo().setMessage(exception.getMessage())
                .setDeveloperMessage(exception.toString())
                .setUri(request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({MessageException.class})
    @ResponseBody
    public ErrorInfo handleMessage(HttpServletRequest request, MessageException exception){
        return new ErrorInfo().setMessage(exception.getMessage())
                .setDeveloperMessage(exception.toString())
                .setUri(request.getRequestURI());
    }


}
