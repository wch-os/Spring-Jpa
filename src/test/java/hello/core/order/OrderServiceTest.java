package hello.core.order;

import hello.core.AppConfig;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class OrderServiceTest {

    MemberService memberService;
    OrderService orderService;

    @BeforeEach
    void beforeEach(){
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
        orderService = appConfig.orderService();
    }

    @Test
    void createOrder(){
        Member member = new Member(10L, "sieun", Grade.VIP);
        memberService.join(member);

        Order chicken = orderService.createOrder(member.getId(), "chicken", 20000);
        assertThat(chicken.getDiscountPrice()).isEqualTo(1000);
        assertThat(chicken.calculatePrice()).isEqualTo(chicken.getItemPrice() - chicken.getDiscountPrice());
    }
}
