package springbook.user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

/**
 * Created by yong on 2015-07-06.
 *
 * @author yong
 */
public class UserDaoTest {
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

        ConnectionMaker connectionMaker = new DconnectionMaker();

        UserDao dao = new DaoFactory().userDao();

        UserDao dao3 = context.getBean("userDao", UserDao.class);
        UserDao dao4 = context.getBean("userDao", UserDao.class);

        System.out.println(dao3);
        System.out.println(dao4);

//        User user = new User();
//        user.setId("yongpwi");
//        user.setName("용퓌");
//        user.setPassword("yong");
//
//        dao.add(user);
//
//        System.out.println(user.getId() + " 등록 성공");
//
//        User user2 = dao.get(user.getId());
//        System.out.println(user2.getName());
//        System.out.println(user2.getPassword());
//
//        System.out.println(user2.getId() + " 조회 성공");
    }
}
