package data_structure;

/**
 * ArrayList 동작 방식 직접 구현
 */
class ArrayList {
    // 실제 데이터를 저장할 Object 배열
    private Object[] data;
    // ArrayList의 전체 크기
    private int size;
    // 현재 데이터가 들어있는 마지막 위치를 가리키는 인덱스
    private int index;

    // ArrayList 생성자
    public ArrayList() {
        // 초기 크기를 1로 설정
        this.size = 1;
        // size 크기만큼의 Object 배열 생성
        this.data = new Object[this.size];
        // 현재 위치 인덱스를 0으로 초기화
        this.index = 0;
    }

    // 데이터를 추가하는 메서드
    public void add(Object obj) {
        System.out.println("index: " + this.index + ", size: " + this.size + ", data size: " + this.data.length);
        // 현재 인덱스가 배열의 크기-1과 같다면 (배열이 거의 다 찼다면)
        // 예: 크기가 4인 배열의 경우, index가 3일 때 doubling 필요
        if (this.index == this.size - 1) {
            // 배열의 크기를 두 배로 늘리는 메서드 호출
            doubling();
        }
        // 현재 인덱스 위치에 새로운 데이터 삽입
        data[this.index] = obj;
        // 다음 삽입을 위해 인덱스 1 증가
        this.index++;
    }

    // 배열의 크기를 두 배로 늘리는 private 메서드
    private void doubling() {
        // size를 두 배로 증가
        this.size = this.size * 2;
        // 새로운 크기의 배열 생성
        Object[] newData = new Object[this.size];
        // 기존 데이터를 새 배열로 복사
        for (int i = 0; i < data.length; i++) {
            newData[i] = data[i];
        }
        // 새로운 배열을 기존 data 참조 변수에 할당
        this.data = newData;
        System.out.println("*** index: " + this.index + ", size: " + this.size + ", data size: " + this.data.length);
    }

    // 특정 인덱스의 데이터를 반환하는 메서드
    public Object get(int i) throws Exception {
        // 요청한 인덱스가 현재 저장된 데이터 개수보다 크면 예외 발생
        if (i > this.index - 1) {
            throw new Exception("Array Index Out of Bound");
        }
        // 음수 인덱스 요청 시 예외 발생
        else if (i < 0) {
            throw new Exception("Negative Value");
        }
        // 해당 인덱스의 데이터 반환
        return this.data[i];
    }

    // 특정 인덱스의 데이터를 삭제하는 메서드
    public void remove(int i) throws Exception {
        // 삭제하려는 인덱스가 현재 저장된 데이터 개수보다 크면 예외 발생
        if (i > this.index - 1) {
            throw new Exception("Array Index Out of Bound");
        }
        // 음수 인덱스 삭제 요청 시 예외 발생
        else if (i < 0) {
            throw new Exception("Negative Value");
        }
        System.out.println("data removed: " + this.data[i]);

        // 삭제할 인덱스 이후의 모든 데이터를 한 칸씩 앞으로 이동
        // 예: [1,2,3,4,5] 에서 2를 삭제하면 [1,3,4,5] 가 되도록 이동
        for (int x = i; x < this.data.length - 1; x++) {
            data[x] = data[x + 1];
        }
        // 데이터 개수 1 감소
        this.index--;
    }

    // 테스트를 위한 main 메서드
    public static void main(String[] args) throws Exception {
        ArrayList arrayList = new ArrayList();
        // 0부터 9까지의 문자열을 추가
        arrayList.add("0");
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        arrayList.add("4");
        arrayList.add("5");
        arrayList.add("6");
        arrayList.add("7");
        arrayList.add("8");
        arrayList.add("9");
        // 인덱스 5의 값을 출력 (5)
        System.out.println(arrayList.get(5));
        // 인덱스 5의 값을 삭제
        arrayList.remove(5);
        // 다시 인덱스 5의 값을 출력 (6)
        System.out.println(arrayList.get(5));
    }
}