package spring.core.order;

public interface OrderService {
    /*주문할때 아이디, 주문물건, 물건 가격 필요*/
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
