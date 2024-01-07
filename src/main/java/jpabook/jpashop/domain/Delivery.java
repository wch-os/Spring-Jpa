package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY) //거울
    private Order order;

    @Embedded
    private Address address;

    /**
     * 중요!. EnumType.ORDINAL가 default 값이다.
     * 중간에 enum이 추가되면 순서가 밀리게 된다.
     */
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //READY, COMP
}
