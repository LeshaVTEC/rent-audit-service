package org.alexey.rentauditservice.exception;


import lombok.extern.slf4j.Slf4j;
import org.alexey.rentauditservice.exception.body.ErrorResponse;
import org.alexey.rentauditservice.exception.body.StructuredErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ResponseBody
@RestControllerAdvice
public class ExceptionHandlerResolver {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleEntityNotFound(EntityNotFoundException exception) {
        return new ErrorResponse("error", exception.getMessage());
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public StructuredErrorResponse handleValidation(MethodArgumentNotValidException exception) {
//        return new StructuredErrorResponse(
//                "structured_error",
//                exception.getBindingResult()
//                        .getAllErrors()
//                        .stream()
//                        .map(this::convert)
//                        .toList()
//        );
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception){
//        return new ErrorResponse("error", exception.getMessage());
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(InvalidLinkException.class)
//    public ErrorResponse handleInvalidLink(InvalidLinkException exception) {
//        return new ErrorResponse("error", exception.getMessage());
//    }
//
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(UnauthorizedException.class)
//    public String handleUnauthorized(UnauthorizedException exception) {
//        return exception.getMessage();
//    }
//
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    @ExceptionHandler(AccessDeniedException.class)
//    public String handleAccessDenied(AccessDeniedException exception) {
//        return exception.getMessage();
//    }
//
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(Exception.class)
//    public ErrorResponse handleException(Exception exception) {
//        return new ErrorResponse("error",
//                "Сервер не смог корректно обработать запрос. Пожалуйста обратитесь к администратор");
//    }
//
//    private ErrorField convert(ObjectError objectError){
//        return new ErrorField(
//                ((FieldError) objectError).getField(),
//                objectError.getDefaultMessage()
//        );
//    }
}
