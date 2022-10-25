package moska.rebora.Common;

import lombok.Getter;

@Getter
public class ResponseFormat {

    private Object data;
    private ErrorResponse errors;

    @Getter
    private class ErrorResponse {
        private String errorName;

        public ErrorResponse(Exception error) {
            errorName = error.toString();
        }
    }

    public ResponseFormat of(Object data) {
        this.data = data;
        return this;
    }

    public ResponseFormat of(Exception error) {
        this.data = "오류가 발생했습니다.";
        errors = new ErrorResponse(error);
        return this;
    }
}
