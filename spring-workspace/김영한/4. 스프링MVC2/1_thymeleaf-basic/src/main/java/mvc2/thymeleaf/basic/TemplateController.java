package mvc2.thymeleaf.basic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 템플릿 조각 - 공통 레이아웃(타일즈 느낌)을 재사용
 */

@Controller
@RequestMapping("/template")
public class TemplateController {

    @GetMapping("fragment")
    public String template() {
        return "template/fragment/fragmentMain";
    }

    /**
     * 공통 레이아웃 + 각 페이지마다 필요한 정보를 더 추가해서 사용
     */
    @GetMapping("layout")
    public String layout() {
        return "template/layout/layoutMain";
    }

    /**
     * <html> 전체에 적용
     */
    @GetMapping("/layoutExtend")
    public String layoutExtends() {
        return "template/layoutExtend/layoutExtendMain";
    }

}