package hello.core.beanfind;

import hello.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextInfoTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean(){
        String[] beanDefinitionNames = ac.getBeanDefinitionNames(); //스프링 컨테이너에 등록된 모든 'Bean 이름' 들을 get 해온다.
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName); //'Bean 이름'을 key 하여, 매칭되는 'Bean 객체' value 가져온다.
            System.out.println("name = " + beanDefinitionName + " object = " + bean);
        }
    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean(){
        String[] beanDefinitionNames = ac.getBeanDefinitionNames(); //스프링 컨테이너에 등록된 모든 'Bean 이름' 들을 get 해온다.
        for (String beanDefinitionName : beanDefinitionNames) {

            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);//Bean 각각에 대한 메타데이터

            /**
             ROLE_APPLICATION : 일반적으로 사용자가 정의한 빈
             ROLE_INFRASTRUCTURE : 스프링이 내부에서 사용하는 빈
             */
            if(beanDefinition.getRole() == BeanDefinition.ROLE_INFRASTRUCTURE) {
                Object bean = ac.getBean(beanDefinitionName); //'Bean 이름'을 key 하여, 매칭되는 'Bean 객체' value 가져온다.
                System.out.println("name = " + beanDefinitionName + " object = " + bean);
            }
        }
    }


}
