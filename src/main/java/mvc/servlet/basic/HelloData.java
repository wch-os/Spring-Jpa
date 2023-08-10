package mvc.servlet.basic;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter //Getter, Setter 자동으로 생성해주는 애노테이션
public class HelloData {
    private String username;
    private int age;
}
