package hello.itemservice;

import hello.itemservice.web.validation.ItemValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 8088
 *
 */
@SpringBootApplication
public class ItemServiceApplication{

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	// 글로벌 Validate 설정 - implements WebMvcConfigurer 해주기
//	@Override
//	public Validator getValidator() {
//		return new ItemValidator();
//	}
}