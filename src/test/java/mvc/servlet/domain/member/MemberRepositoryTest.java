package mvc.servlet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance(); //싱글톤이기 때문에 new 안됨

    @AfterEach
    void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    void store(){
        //given
        Member sun = new Member("sun", 20);

        //when
        Member save = memberRepository.save(sun);

        //then
        Member findMember = memberRepository.findById(save.getId());
        assertThat(findMember).isEqualTo(save);
    }

    @Test
    void findAll(){
        //given
        Member sun = new Member("sun", 20);
        Member sie = new Member("sie", 18);

        memberRepository.save(sun);
        memberRepository.save(sie);

        //when
        List<Member> all = memberRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(2);
        assertThat(all).contains(sun, sie);
    }
}