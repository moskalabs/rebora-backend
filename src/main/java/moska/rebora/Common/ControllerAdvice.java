package moska.rebora.Common;


import io.jsonwebtoken.JwtException;
import moska.rebora.Enum.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<ResponseFormat> HandlerIllegalJwtException(JwtException e) {

        ResponseFormat res = new ResponseFormat().of(e);
        return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    protected ResponseEntity<BaseResponse> HandlerIllegalSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(false);
        baseResponse.setErrorCode(ErrorCode.DUPLICATION.getStatus());

        return new ResponseEntity<>(baseResponse, HttpStatus.CONFLICT);
    }
}
