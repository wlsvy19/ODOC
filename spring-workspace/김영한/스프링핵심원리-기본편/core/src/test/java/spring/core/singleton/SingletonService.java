package spring.core.singleton;

public class SingletonService {
    /*1. 런타임시 스태틱 메로리 영역에 딱 1개만 존재하는 자기 자신(객체)을 미리 만들어 놓음*/
    private static final SingletonService instance = new SingletonService();
    
    /*2. public 으로 열어서 객체 인스턴스가 필요하면 이 static 메서드를 통해서만 조회하도록 허용*/
    public static SingletonService getInstance() {
        return instance;
    }

    /*3. 생성자를 private으로 하여 외부에서 new로 객체 생성못하게 막음*/
    private SingletonService() {
    }
    
    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }

//    public static void main(String[] args) {
//        /*private은 여기 클래스 안에서 사용 가능 하므로 다른 클래스에서 사용 해보도록*/
//        // SingletonService singletonService = new SingletonService();
//    }
}
