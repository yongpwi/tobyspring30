package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yong on 2015-07-06.
 *
 * @author yong
 */
@Configuration
public class DaoFactory {
//    @Bean
//    public UserDao userDao(){
//        UserDao userDao = new UserDao();
//        userDao.setConnectionMaker(connectionMaker());
//        return userDao;
//    }

    @Bean
    public UserDao userDao(){
        return new UserDao(connectionMaker());
    }

//    @Bean
//    public AccountDao accountDao(){
//        return new AccountDao(connectionMaker());
//    }
//
//    @Bean
//    public MessageDao accountDao(){
//        return new MessageDao(connectionMaker());
//    }

    @Bean
    public ConnectionMaker connectionMaker(){
        return new DconnectionMaker();
    }
}
