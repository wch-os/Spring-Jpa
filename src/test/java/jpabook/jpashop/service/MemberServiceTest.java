package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("sun");

        //when
        Long saveId = memberService.join(member);

        //then
        Assertions.assertThat(memberRepository.findOne(saveId)).isEqualTo(member);
    }

    @Test(expected = IllegalStateException.class)
    public void 중복회원예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setUsername("sun");

        Member member2 = new Member();
        member2.setUsername("sun");

        //when
        memberService.join(member1);
        memberService.join(member2); //예외가 발생해야 한다!

        //then
        Assert.fail("예외가 발생해야 한다.");
    }
}