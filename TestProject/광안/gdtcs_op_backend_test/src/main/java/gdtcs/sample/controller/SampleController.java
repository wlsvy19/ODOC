package gdtcs.sample.controller;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import gdtcs.util.ResponseCode;
import gdtcs.util.TCSException;

@Controller
@ResponseBody
@RequestMapping("/api/sample")
public class SampleController {
	protected Log log = LogFactory.getLog(this.getClass());

	@PostMapping("/getSample")
	public ResponseEntity<Map<String, Object>> getSample(@RequestBody Map<String, Object> param) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", 1);
		data.put("name", "Name");
				
		return ResponseEntity.ok(data);
	}
	
	@SuppressWarnings("serial")
	@PostMapping("/getSamples")
	public ResponseEntity<List<Map<String, Object>>> getSamples(@RequestBody Map<String, Object> param) throws Exception {

		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		data.add(
			new HashMap<String, Object>(){{
				put("id", 1);
				put("name", "Name");
			}}
		);
		return ResponseEntity.ok(data);
	}
	
	@PostMapping("/setSampleTCSException")
	public ResponseEntity<Map<String, Object>> setSampleTCSException(@RequestBody Map<String, Object> param) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", 1);
		data.put("name", "Name");
		if (true) {
			throw new TCSException(ResponseCode.NO_DATA_CANCEL, "이 메시지는 서버 로그만 남습니다.");
		}
		return ResponseEntity.ok(data);
	}
	
	@PostMapping("/getSampleException")
	public ResponseEntity<Map<String, Object>> getSampleException(@RequestBody Map<String, Object> param) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", 1);
		data.put("name", "Name");
		if (true) {
			throw new Exception("에러 메시지");
		}
		return ResponseEntity.ok(data);
	}
	
	@PostMapping("/getSampleRuntimeException")
	public ResponseEntity<Map<String, Object>> getSampleRuntimeException(@RequestBody Map<String, Object> param) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", 1);
		data.put("name", "Name");
		if (true) {
			throw new RuntimeException("에러 메시지");
		}
		return ResponseEntity.ok(data);
	}
	
	@PostMapping("/getSampleIOException")
	public ResponseEntity<Map<String, Object>> getSampleIOException(@RequestBody Map<String, Object> param) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", 1);
		data.put("name", "Name");
		if (true) {
			throw new IOException("에러 메시지");
		}
		return ResponseEntity.ok(data);
	}
	
	@PostMapping("/getSampleSQLException")
	public ResponseEntity<Map<String, Object>> getSampleSQLException(@RequestBody Map<String, Object> param) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", 1);
		data.put("name", "Name");
		if (true) {
			throw new SQLException("에러 메시지");
		}
		return ResponseEntity.ok(data);
	}
	
	@PostMapping("/getSampleSQLDataException")
	public ResponseEntity<Map<String, Object>> getSampleSQLDataException(@RequestBody Map<String, Object> param) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", 1);
		data.put("name", "Name");
		if (true) {
			throw new SQLDataException("에러 메시지");
		}
		return ResponseEntity.ok(data);
	}
	
	@PostMapping("/getSampleSQLClientInfoException")
	public ResponseEntity<Map<String, Object>> getSampleSQLClientInfoException(@RequestBody Map<String, Object> param) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", 1);
		data.put("name", "Name");
		if (true) {
			throw new SQLClientInfoException();
		}
		return ResponseEntity.ok(data);
	}
}
