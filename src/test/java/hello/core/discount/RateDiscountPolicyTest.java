package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RateDiscountPolicyTest {

    RateDiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 할인이 되어야한다.")
    void vip_discount() {
        //given
        Member member = new Member(10L, "sun", Grade.VIP);

        //when
        int discount = rateDiscountPolicy.discount(member, 4000);

        //then
        assertThat(discount).isEqualTo(400);
    }

    @Test
    @DisplayName("VIP는 10% 할인이 되지않아야 한다.")
    void basic_discount() {
        //given
        Member member = new Member(10L, "sie", Grade.BASIC);

        //when
        int discount = rateDiscountPolicy.discount(member, 4000);

        //then
        assertThat(discount).isEqualTo(0);
    }
}