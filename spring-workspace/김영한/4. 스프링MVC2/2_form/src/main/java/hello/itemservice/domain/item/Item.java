package hello.itemservice.domain.item;

import lombok.Data;

import java.util.List;

@Data
public class Item {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    private Boolean open; // 판매 여부: 단일 체크 박스 - 히든 필드 사용해서 null 값 처리
    private List<String> regions; // 등록 지역: 다중 체크 박스
    private ItemType itemType; // 상품 종류: 라디오 버튼(1개만 선택)
    private String deliveryCode; // 배송 방식: 셀렉트 박스

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}