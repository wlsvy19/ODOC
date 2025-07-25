package gdtcs.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.google.common.base.Throwables;

@RestControllerAdvice
public class CommonResponseAdvice {
	protected Log log = LogFactory.getLog(this.getClass());
	
	@ExceptionHandler(TCSException.class)
	protected ResponseEntity<Object> handleTCSException(TCSException e) {
		@SuppressWarnings("serial")
		Map<String, Object> res = new HashMap<String, Object>(){{
			put("ERROR_CODE", e.getResponseCode().getCode());
			put("ERROR_MSG", e.getResponseCode().getMessage());
		}};
		log.info(String.format("[%s] %s", res.get("ERROR_CODE"), res.get("ERROR_MSG")));
		log.info(String.format("%s: %s", e.getClass().getSimpleName(), e.getResponseCode().getDetailMessage()));
		return ResponseEntity.status(e.getResponseCode().getStatus()).body(res);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleException(Exception e) {
		@SuppressWarnings("serial")
		Map<String, Object> res = new HashMap<String, Object>(){{
			put("ERROR_CODE", ResponseCode.UNKNOWN_ERROR.getCode());
			put("ERROR_MSG", ResponseCode.UNKNOWN_ERROR.getMessage());
		}};
		log.error(String.format("[%s] %s", res.get("ERROR_CODE"), res.get("ERROR_MSG")));
		log.info(e.getStackTrace()[0]);
		log.debug(Throwables.getStackTraceAsString(e));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
	}
}
