package hello.itemservice.web.form;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
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
    
    // 이넘 사용하여 뷰로 데이터 전달하는 방법
    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        // values: 해당 ENUM의 모든 정보를 배열로 반환
        return ItemType.values();
    }

    // 자바 객체 사용하여 뷰로 데이터 전달 하는 방법
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("ROCKET", "로켓 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        return deliveryCodes;
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
        log.info("============================================================");
        log.info("상품 판매여부 싱글 체크박스 = {}", item.getOpen());
        log.info("상품 등록지역 멀티 체크박스 = {}", item.getRegions());
        log.info("상품 타입 = {}", item.getItemType());
        log.info("배송 종류 = {}", item.getDeliveryCode());

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
        log.info("============================================================");
        log.info("상품 판매여부 싱글 체크박스 = {}", item.getOpen());
        log.info("상품 등록지역 멀티 체크박스 = {}", item.getRegions());
        log.info("상품 타입 = {}", item.getItemType());
        log.info("배송 종류 = {}", item.getDeliveryCode());

        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

}