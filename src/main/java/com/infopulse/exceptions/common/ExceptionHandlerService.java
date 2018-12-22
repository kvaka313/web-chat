package com.infopulse.exceptions.common;

import com.infopulse.dto.ErrorInfo;
import com.infopulse.exceptions.MessageException;
import com.infopulse.exceptions.UserAlreadyExist;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerService {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UserAlreadyExist.class})
    @ResponseBody
    public ErrorInfo handle(HttpServletRequest request, Exception exception){
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
