package spring.core.singleton;

/*상태를 유지해서 나타나는 싱글톤의 문제*/
public class StatefulService {
    /*상태를 유지하는 필드*/
    private int price;

    public void orderStateful(String name, int price) {
        System.out.println("name = " + name + ", price = " + price);
        this.price = price; /*여기가 문제!! 공유되는 필드를 조작 해버림 */
    }

    /*해결방법: 공유되지 않는 지역변수 사용*/
    public int orderStateless(String name, int price) {
        System.out.println("name = " + name + ", price = " + price);
        return price; /*반환형을 void -> int로 하고, 넘겨버리면 됨(price가 지역 변수 라서 상태유지 x)*/
    }

    public int getPrice() {
        return price;
    }
}
