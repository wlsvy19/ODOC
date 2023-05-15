package hello.itemservice.web.validation;

import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Bean Validation을 @RequestBody에 적용해보기
 * @ModelAttribute 는 HTTP 요청 파라미터(URL 쿼리 스트링, POST Form)를 다룰 때 사용한다.
 * @RequestBody 는 HTTP Body의 데이터를 객체로 변환할 때 사용한다. 주로 API JSON 요청을 다룰 때 사용한다.
 *
 * POST MAN으로 테스트
 * http://localhost:8088/validation/api/items/add
 * body에 JSON 데이터 담아 보내기: row클릭후 JNSON선택하고 아래 테스트 데이터 입력
 * {
 *     "itemName": "hello",
 *     "price": 1000,
 *     "quantity": 10
 * }
 */
@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

	@PostMapping("/add")
	public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {
		log.info("API 컨트롤러 호출");

		if (bindingResult.hasErrors()) {
			log.info("검증 오류 발생 errors={}", bindingResult);
			return bindingResult.getAllErrors();
		}
		log.info("성공 로직 실행");
		return form;
	}
}
