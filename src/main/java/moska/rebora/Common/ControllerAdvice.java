package moska.rebora.Common;


import com.mchange.util.DuplicateElementException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice{

    private static String initMessage;

    //Jwt 에러
    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<Object> HandlerIllegalJwtException(JwtException e) {

        return handleExceptionInternal(HttpStatus.UNAUTHORIZED, ErrorCode.JWT_UNAUTHORIZED.getStatus(), e.getMessage());
    }

    //Jwt 에러
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> HandlerIllegalArgumentException(IllegalArgumentException e) {

        return handleExceptionInternal(HttpStatus.UNAUTHORIZED, ErrorCode.INTER_SERVER_ERROR.getStatus(), "오류가 발생했습니다. 다시 시도해주세요");
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    protected ResponseEntity<Object> HandlerCertificateExpiredException(CredentialsExpiredException e){
        return handleExceptionInternal(HttpStatus.UNAUTHORIZED, ErrorCode.JWT_UNAUTHORIZED.getStatus(), e.getMessage());
    }

    //중복 예외
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    protected ResponseEntity<Object> HandlerIllegalSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        return handleExceptionInternal(HttpStatus.CONFLICT, ErrorCode.DUPLICATION.getStatus(), e.getMessage());
    }

    //중복 예외
    @ExceptionHandler(DuplicateElementException.class)
    protected ResponseEntity<Object> HandlerIllegalDuplicateElementException(DuplicateElementException e) {
        return handleExceptionInternal(HttpStatus.CONFLICT, ErrorCode.DUPLICATION.getStatus(), e.getMessage());
    }

    //메일 사용자 에러
    @ExceptionHandler(AddressException.class)
    protected ResponseEntity<Object> handlerAddressException(AddressException e) {
        initMessage = "메일 전송 중 오류가 발생하였습니다.";
        return handleExceptionInternal(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTER_SERVER_ERROR.getStatus(), initMessage);
    }

    //메일 에러
    @ExceptionHandler(MessagingException.class)
    protected ResponseEntity<Object> handlerMessagingException(MessagingException e) {
        initMessage = "메일 전송 중 오류가 발생하였습니다.";
        return handleExceptionInternal(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTER_SERVER_ERROR.getStatus() , initMessage);
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Object> handlerNullPointerException(NullPointerException e) {
        return handleExceptionInternal(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTER_SERVER_ERROR.getStatus(), e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        initMessage = "오류가 발생했습니다. 다시 시도해주세요";
        return handleExceptionInternal(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST.getStatus(), initMessage);
    }

    //
    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handlerBadCredentialsException(BadCredentialsException e) {
        initMessage = e.getMessage();
        return handleExceptionInternal(HttpStatus.UNAUTHORIZED, ErrorCode.JWT_UNAUTHORIZED.getStatus(), initMessage);
    }

    //예외 처리
    private ResponseEntity<Object> handleExceptionInternal(HttpStatus httpStatus, String errorCode, String message) {
        return ResponseEntity.status(httpStatus).body(makeErrorResponse(errorCode, message));
    }

    //예외 응답 response 처리
    private BaseResponse makeErrorResponse(String errorCode, String message) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(false);
        baseResponse.setErrorCode(errorCode);
        baseResponse.setMessage(message);
        return baseResponse;
    }
}
