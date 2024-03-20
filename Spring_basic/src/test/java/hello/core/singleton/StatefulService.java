package hello.core.singleton;

public class StatefulService {

//    private int price; //상태를 유지하는 필드 (Stateful)

    public int order(String name, int price){
        System.out.println("name = " + name + " price = " + price );
//        this.price = price; //멀티스레드로 요청해 올 경우, 문제 발생!
        return price;
    }

//    public int getPrice(){
//        return price;
//    }
}
