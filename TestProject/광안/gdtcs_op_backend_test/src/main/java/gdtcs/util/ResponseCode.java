package gdtcs.util;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ResponseCode {
	// 공통
//   SUCCESS(HttpStatus.OK, "S0001", "성공"),
	CLOSED(HttpStatus.BAD_REQUEST, "W0000001", "일마감된 날짜입니다."),
	UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0000001", "알 수 없는 오류가 발생했습니다."),
	UPDATE_FAIL(HttpStatus.BAD_REQUEST, "E0000002", "수정할 수 없습니다."),
	NO_DATA_CANCEL(HttpStatus.BAD_REQUEST, "E0000003", "데이터가 존재하지 않아 취소되었습니다."),
	DUPLICATED_FAIL(HttpStatus.BAD_REQUEST, "E0000004", "중복된 데이터로 처리실패 되었습니다."),
	INSERT_FAIL(HttpStatus.BAD_REQUEST, "E0000005", "데이터 생성이 실패했습니다."),
	// 영상심사
	IMG_CORR_BATCH_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "W0201001", "수기확정 처리는 일괄처리 할 수 없습니다."),
	IMG_CORR_HAS_HISTORY_VLTN(HttpStatus.BAD_REQUEST, "W020102", "위반심사 수정이력이 존재하여 처리할 수 없습니다."),
	IMG_CORR_RECOMMENDATION_VLTN(HttpStatus.BAD_REQUEST, "W0201003", "위반심사에서 처리해주세요."),
	IMG_CORR_HAS_HISTORY(HttpStatus.BAD_REQUEST, "W0201004", "심사내역이 있습니다. 심사에서 처리해주세요."),

	DB_CONN_FULL(HttpStatus.NOT_IMPLEMENTED, "E0000006", "DB 커넥션이 FULL 상태입니다. 서버를 재시작 합니다."),
	;

	private final HttpStatus status;
	private final String code;
	private final String message;
	private String detailMessage;

	ResponseCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}
}
