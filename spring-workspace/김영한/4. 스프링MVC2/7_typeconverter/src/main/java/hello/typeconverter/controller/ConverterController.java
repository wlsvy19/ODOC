package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 뷰 템플릿에 컨버터를 적용하는 방법
 * 객체 -> 문자 바꿔서 뷰템플릿에 리턴해야함
 */
@Controller
public class ConverterController {

	// localhost:8091/converter-view
	@GetMapping("/converter-view")
	public String converterView(Model model) {
		model.addAttribute("number", 10000);
		model.addAttribute("ipPort", new IpPort("127.0.0.1", 8080));
		return "converter-view";
	}

	// localhost:8091/converter/edit
	// th:field 가 자동으로 컨버전 서비스를 적용해주어서 ${{ipPort}} 처럼 적용이 되었다. 따라서 IpPort -> String 으로 변환
	@GetMapping("/converter/edit")
	public String converterEdit(Model model) {
		IpPort ipPort = new IpPort("127.0.0.1", 8080);
		Form form = new Form(ipPort);
		model.addAttribute("form", form);
		return "converter-form";
	}

	// @ModelAttribute 를 사용해서 String -> IpPort 로 변환
	@PostMapping("/converter/edit")
	public String converterEdit(@ModelAttribute Form from, Model model) {
		IpPort ipPort = from.getIpPort();
		model.addAttribute("ipPort", ipPort);
		return "converter-view";
	}

	@Data
	static class Form {
		private IpPort ipPort;
		public Form(IpPort ipPort) {
			this.ipPort = ipPort;
		}
	}
}
