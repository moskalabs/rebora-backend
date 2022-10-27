package moska.rebora.Common;


import io.jsonwebtoken.JwtException;
import moska.rebora.Enum.ErrorCode;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    //Jwt 에러
    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<Object> HandlerIllegalJwtException(JwtException e) {

        return handleExceptionInternal(HttpStatus.UNAUTHORIZED, ErrorCode.JWT_UNAUTHORIZED.getStatus());
    }

    //중복 예외
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    protected ResponseEntity<Object> HandlerIllegalSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        return handleExceptionInternal(HttpStatus.CONFLICT, ErrorCode.DUPLICATION.getStatus());
    }

    //메일 사용자 에러
    @ExceptionHandler(AddressException.class)
    protected ResponseEntity<Object> handlerAddressException(AddressException e) {
        return handleExceptionInternal(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTER_SERVER_ERROR.getStatus());
    }

    //메일 에러
    @ExceptionHandler(MessagingException.class)
    protected ResponseEntity<Object> handlerMessagingException(MessagingException e) {
        return handleExceptionInternal(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTER_SERVER_ERROR.getStatus());
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Object> handlerNullPointerException(NullPointerException e) {
        return handleExceptionInternal(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTER_SERVER_ERROR.getStatus());
    }

    //예외 처리
    private ResponseEntity<Object> handleExceptionInternal(HttpStatus httpStatus, String errorCode) {
        return ResponseEntity.status(httpStatus).body(makeErrorResponse(errorCode));
    }

    //예외 응답 response 처리
    private BaseResponse makeErrorResponse(String errorCode) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(false);
        baseResponse.setErrorCode(errorCode);
        return baseResponse;
    }
}
