package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        // 루트 Order 조회
        List<OrderQueryDto> result = findOrders(); 
        
        // 루프를 돌면서 컬렉션 OrderItem 추가(
        // 1+N 문제 : Order query 1번 -> OrderItemQuery N개
        result.forEach(orderQueryDto -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(orderQueryDto.getOrderId()); // quern N번
            orderQueryDto.setOrderItems(orderItems);
        });

        return result;
    }

    // toOne 관계 한 번에 조회
    private List<OrderQueryDto> findOrders() {
        return em.createQuery("select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.username, o.orderDate, o.status, d.address)" +
                        " from Order o " +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }
    
    // 1:N 관계인 orderItem 조회
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" + // OrderItem 과 Item 은 toOne 관계이므로 row 수에 영향을 끼치지 않으므로, join 해도 된다.
                                " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }


    public List<OrderQueryDto> findAllByDto_optimization() {
        // 1. 루트 Order 조회
        List<OrderQueryDto> result = findOrders();

        // 2. orderId 리스트화
        List<Long> orderIds = toOrderIds(result);

        // 3. Order와 관련된 OrderItem을 한 번에 쿼리
        List<OrderItemQueryDto> orderItems = findOrderItemMap(orderIds);

        // 4. <orderId, orderItem>, orderId를 기준으로 OrderItem Map 화
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));

        // 5. 루프를 돌면서 컬렉션 OrderItem 추가 (이전과 달리 추가 쿼리 실행 X)
        // result.forEach(o -> o.setOrderItems(orderItems));
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));


        return result;
    }
    private static List<Long> toOrderIds(List<OrderQueryDto> result) {
        List<Long> orderIds = result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
        return orderIds;
    }
    private List<OrderItemQueryDto> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" + // OrderItem 과 Item 은 toOne 관계이므로 row 수에 영향을 끼치지 않으므로, join 해도 된다.
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class) // 1개씩 가지고 오는 것이 아닌 in 절로 한 번에 가지고 온다.
                .setParameter("orderIds", orderIds)
                .getResultList();

        return orderItems;
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                "select new " +
                        " jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.username, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d" +
                        " join o.orderItems oi" +
                        " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}

