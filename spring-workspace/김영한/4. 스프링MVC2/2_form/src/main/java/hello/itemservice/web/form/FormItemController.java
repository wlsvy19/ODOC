package hello.itemservice.web.form;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    // 중복코드( model.addAttribute(...) )제거 -> 면 해당 컨트롤러를 통해 메서드에 요청이 들어올 때 regions 에서 반환한 값이 자동으로 모델( model )에 담김
    @ModelAttribute("regions")
    public Map<String, String> regions() {
        // 다중 체크박스: 순서보장하기 위해 LinkedHashMap 사용
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울"); // 키, 값
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }

    @GetMapping

    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        // 빈 객체를 view로 던짐
        model.addAttribute("item", new Item());

        return "form/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        log.info("상품 판매여부 싱글 체크박스 = {}", item.getOpen());
        log.info("상품 등록지역 멀티 체크박스 = {}", item.getRegions());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    /**
     * 수정 화면 이동
     */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        
        return "form/editForm";
    }

    /**
     * 수정 로직 실행
     */
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        log.info("상품 판매여부 싱글 체크박스 = {}", item.getOpen());
        log.info("상품 등록지역 멀티 체크박스 = {}", item.getRegions());
        
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

}