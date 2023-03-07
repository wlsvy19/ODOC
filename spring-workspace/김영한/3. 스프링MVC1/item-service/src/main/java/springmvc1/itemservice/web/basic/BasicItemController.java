package springmvc1.itemservice.web.basic;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springmvc1.itemservice.domain.item.Item;
import springmvc1.itemservice.domain.item.ItemRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // final붙은 멤버변수로 생성자를 자동으로 만들어 줌
public class BasicItemController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ItemRepository itemRepository;

    /**
     * 테스트용 데이터 미리 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 5000, 33));
        itemRepository.save(new Item("itemC", 35000, 7));
        itemRepository.save(new Item("itemD", 50000, 5));
    }

    /**
     * 모든 상품 보여주기
     *
     * @param model
     * @return
     */
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    /**
     * 클릭한 상품 보여주기
     *
     * @param itemId
     * @param model
     * @return
     */
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    /**
     * 상품 등록 버튼을 최초로 눌렀을 경우
     *
     * @return
     */
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    /**
     * 실제 상품 등록 하기
     *
     * @return
     */
    // @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName, @RequestParam int price, @RequestParam Integer quantity, Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    /**
     * @RequestParam 여러개 불편해서 @ModelAttribute로 개선
     */
    // @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {
        itemRepository.save(item);

        // @ModelAttiribute가 자동으로 추가 해줘서 생략 가능
        // model.addAttribute("item", item);

        return "basic/item";
    }

    /**
     * @ModelAttribute 에서 () 안에 들어가는 "item" 생략 가능
     */
    // @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

    /**
     * @ModelAttribute 아예 생략 가능
     */
    // @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        // 등록 후 새로고침 하면 계속 POST로 요청 보내서 등록 됨 (새로고침은 마지막 행위가 다시 실행)
        return "basic/item";
    }

    /**
     * PGR(Post Redirect Get) 패턴 적용
     */
    // @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        // 리다이렉트로 해결(302 코드): 웹브라우저가 다시 요청, V6에서 개선
        return "redirect:/basic/items/" + item.getId();
    }

    /**
     * 리다이렉트 할 때 설정
     */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        
        // 쿼리 파라미터로 URL에 넘어감
        redirectAttributes.addAttribute("status", true);

        // RedirectAttributes에 넣은 값이 {itemId} 로 자동 치환되고 인코딩도 자동으로 해줌
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 상품 수정 화면 이동
     */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    /**
     * 실제 상품 수정 실제 처리
     */
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        // 수정 후 해당 아이템으로 리다이렉트 (스프링에서 지원)
        return "redirect:/basic/items/{itemId}";
    }

}