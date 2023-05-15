package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Bean Validation 사용 -> groups 사용x -> 폼 데이터 전달을 위한 별도의 객체 사용(등록, 폼 객체를 다르게 만들기)
 * 검증 애노테이션 공식문서(ID검증, 이메일 검증 등 다양함)
 * https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/html_single/#validator-defineconstraints-spec
 */
@Slf4j
@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor // 자동 생성자 주입
public class ValidationItemControllerV4 {

	private final ItemRepository itemRepository;

	// 전체 Item들의 목록
	@GetMapping
	public String items(Model model) {
		List<Item> items = itemRepository.findAll();
		model.addAttribute("items", items);
		return "/validation/v4/items";
	} // end items()

	// 특정 Item 클릭시 정보
	@GetMapping("/{itemId}")
	public String item(@PathVariable long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "/validation/v4/item";
	} // end item()

	// 상품등록 버튼 클릭시 단순 페이지 이동
	@GetMapping("/add")
	public String addForm(Model model) {
		// 상품추가 폼인데도 빈 Item을 뷰단으로 보낸 이유: 검증 실패시 입력한 값 그대로 노출(재사용)
		model.addAttribute("item", new Item());
		return "/validation/v4/addForm";
	} // end addForm()

	// 모든 정보를 입력하고 저장버튼 클릭시 이동
	@PostMapping("/add")  // @Valid 사용 가능 (자바 표준)
	public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		// model.addAttribute("item", form); 와 동일
		// @ModelAttribute("item") 에 item 이름을 넣어준 부분을 주의하자.
		// 이것을 넣지 않으면 ItemSaveForm 의 경우 규칙에 의해 itemSaveForm 이라는 이름으로 MVC Model에 담기게 된다.

		// 특정 필드가 아닌 복합 룰 검증
		if (form.getPrice() != null && form.getQuantity() != null) {
			int resultPrice = form.getPrice() * form.getQuantity();
			if (resultPrice < 10000) {
				bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
			}
		}

		// 검증에 실패하면 다시 입력 폼으로
		if (bindingResult.hasErrors()) {
			log.info("errors = {}", bindingResult);
			return "/validation/v4/addForm";
		}

		// 성공 로직: Itme객체 생성 후 ItemSaveForm 객체를 값을 넣음
		Item item = new Item();
		item.setItemName(form.getItemName());
		item.setPrice(form.getPrice());
		item.setQuantity(form.getQuantity());

		Item savedItem = itemRepository.save(item);

		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v4/items/{itemId}";
	}  // end addItem()

	// 처음 상품수정 버튼을 클릭시 이동하는 페이지
	@GetMapping("/{itemId}/edit")
	public String editForm(@PathVariable Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "/validation/v4/editForm";
	} // end editForm()

	// 수정을 마치고 최종적으로 상품수정 버튼 클릭시 로직
	@PostMapping("/{itemId}/edit")
	public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {
		// 특정 필드가 아닌 복합 룰 검증
		if (form.getPrice() != null && form.getQuantity() != null) {
			int resultPrice = form.getPrice() * form.getQuantity();
			if (resultPrice < 10000) {
				bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
			}
		}

		// 수정할 때 에러가 있는 경우 다시 그 폼으로 이동
		if (bindingResult.hasErrors()) {
			log.info("errors={}", bindingResult);
			return "validation/v4/editForm";
		}

		// 성공 로직
		Item itemParam = new Item();
		itemParam.setItemName(form.getItemName());
		itemParam.setPrice(form.getPrice());
		itemParam.setQuantity(form.getQuantity());
		itemRepository.update(itemId, itemParam);

		return "redirect:/validation/v4/items/{itemId}";
	} // end edit()

}