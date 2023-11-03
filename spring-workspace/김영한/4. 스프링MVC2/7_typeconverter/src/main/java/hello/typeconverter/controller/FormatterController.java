package hello.typeconverter.controller;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class FormatterController {

	// localhost:8091/formatter/edit
	@GetMapping("/formatter/edit")
	public String formatterForm(Model model) {
		Form form = new Form();
		form.setNumber(10000);
		form.setLocalDateTime(LocalDateTime.now());


		model.addAttribute("form", form);

		// template/formatter-form
		return "formatter-form";
	}

	@PostMapping("/formatter/edit")
	public String formatterEdit(@ModelAttribute Form form) {
		return "formatter-view";
	}

	@Data
	static class Form {
		// 스프링에서 제공하는 어노테이션
		@NumberFormat(pattern = "###,###")
		private Integer number;

		// 스프링에서 제공하는 어노테이션
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private LocalDateTime localDateTime;
	}
}
