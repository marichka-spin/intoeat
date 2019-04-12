package net.lviv.intoeat.exceptions;

import net.lviv.intoeat.utils.ResponseInfo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class IntoeatExceptionHandler { //FIXME

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseInfo entityNotFound(EntityNotFoundException ex) {
        return new ResponseInfo(ex.getMessage());
    }

    @ExceptionHandler(InvalidInputParametersException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseInfo invalidInputParameters(InvalidInputParametersException ex) {
        return new ResponseInfo(ex.getMessage());
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseInfo duplicateEntity(DuplicateEntityException ex) {
        return new ResponseInfo(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseInfo couldNotExecuteException(DataIntegrityViolationException ex) {
        return new ResponseInfo(ex.getMessage());
    }

}
