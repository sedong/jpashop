package jpabook.jpashop;

import jpabook.jpashop.domain.Book;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import org.hibernate.mapping.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.List;

import static jpabook.jpashop.domain.OrderStatus.CANCEL;
import static jpabook.jpashop.domain.OrderStatus.ORDER;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{


            Order orderA = new Order();
            orderA.setStatus(ORDER);
            em.persist(orderA);
            Order orderB = new Order();
            orderB.setStatus(CANCEL);
            em.persist(orderB);
            Order orderC = new Order();
            orderC.setStatus(ORDER);
            em.persist(orderC);

            OrderItem orderItemA = new OrderItem();
            orderItemA.setOrderPrice(2000);
            orderItemA.setOrder(orderA);
            em.persist(orderItemA);

            OrderItem orderItemB = new OrderItem();
            orderItemB.setOrderPrice(1000);
            orderItemB.setOrder(orderA);
            em.persist(orderItemB);

            OrderItem orderItemC = new OrderItem();
            orderItemC.setOrderPrice(1000);
            orderItemC.setOrder(orderB);
            em.persist(orderItemC);

            em.flush();
            em.clear();

            String query = "select o from OrderItem o join fetch o.order";

            List<OrderItem> result = em.createQuery(query,OrderItem.class).getResultList();
            //List<Member> result = em.createQuery("select m from Member m where m.name like '%k%' ",Member.class).getResultList();
            for(OrderItem orderItem : result){
                System.out.println("orderItem = "+orderItem.getId());
            }
           // String query = "select oi from Order o join o.orderItems oi";

//            List<Order> result = em.createQuery(query,Order.class).getResultList();
//            //List<Member> result = em.createQuery("select m from Member m where m.name like '%k%' ",Member.class).getResultList();
//            for(Order order : result){
//                System.out.println("orderItem = "+order.getId());
//            }
            System.out.println("result="+result);
            tx.commit();
        }catch (Exception e) {
            System.out.println(e.toString());
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
