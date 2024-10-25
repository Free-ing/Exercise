package service.exerciseservice.base.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseSuccessCodeInterface {
    // For test
    _OK(HttpStatus.OK, "COMMON_200", "요청 처리에 성공하였습니다.")
    ;

    private final HttpStatus httpStatus;
    private final boolean isSuccess = true;
    private final String code;
    private final String message;

    @Override
    public BaseCodeDto getSuccessCode() {
        return BaseCodeDto.builder()
                .httpStatus(httpStatus)
                .isSuccess(isSuccess)
                .code(code)
                .message(message)
                .build();
    }
}
