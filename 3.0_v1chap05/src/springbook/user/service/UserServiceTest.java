package springbook.user.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;

/**
 * Created by yong on 2015-07-26.
 *
 * @author yong
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
    @Autowired
    UserDao dao;

    @Autowired
    DataSource dataSource;

    @Autowired
    UserService userService;

    private User user;
    private User user2;
    private User user3;

    List<User> users;

    @Before
    public void setUp(){
//        this.user = new User("pear", "피어", "jr", Level.BASIC, 1, 10);
//        this.user2 = new User("pear2", "피어2", "jr2", Level.SILVER, 2, 20);
//        this.user3 = new User("pear3", "피어3", "jr3", Level.GOLD, 3, 30);
//
        users = Arrays.asList(
                new User("pear", "피어", "jr", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 10),
                new User("pear2", "피어2", "jr2", Level.SILVER, MIN_LOGCOUNT_FOR_SILVER, 20),
                new User("pear3", "피어3", "jr3", Level.GOLD, 3, MIN_RECCOMEND_FOR_GOLD - 1),
                new User("pear4", "피어4", "jr4", Level.BASIC, 4, MIN_RECCOMEND_FOR_GOLD),
                new User("pear5", "피어5", "jr5", Level.GOLD, 5, Integer.MAX_VALUE)
        );
//        user = new User();
    }

    @Test
    public void upgradeLevels(){
        dao.deleteAll();
        for(User user : users) {
            dao.add(user);
        }
        try {
            userService.updateLevels();
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkLevelUpgrade(users.get(0), false);
        checkLevelUpgrade(users.get(1), true);
        checkLevelUpgrade(users.get(2), false);
        checkLevelUpgrade(users.get(3), true);
        checkLevelUpgrade(users.get(4), false);
    }

    private void checkLevelUpgrade(User user, boolean upgraded) {
        User userUpdate = dao.get(user.getId());
        if(upgraded){
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }


    @Test(expected = IllegalStateException.class)
    public void cannotUpgradeLevel(){
        Level[] levels = Level.values();
        for(Level level : levels){
            if(level.nextLevel() != null){
                continue;
            }
            user.setLevel(level);
            user.upgradeLevel();
        }
    }


    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = dao.get(user.getId());
        assertThat(userUpdate.getLevel(), is(expectedLevel));
    }

    @Test
    public void add(){
        dao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelReal = dao.get(userWithLevel.getId());
        User userWithoutLevelRead = dao.get(userWithoutLevel.getId());

        assertThat(userWithLevelReal.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
    }

    static class TestUserService extends UserService{
        private String id;

        private TestUserService(String id){
            this.id = id;
        }

        protected void upgradeLevel(User user){
            if(user.getId().equals(this.id))
                throw new TestUserServiceException();
            super.upgradeLevel(user);
        }

        private class TestUserServiceException extends RuntimeException {
        }
    }

    @Test
    public void upgradeAllOrNotining() throws Exception{
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.dao);
        testUserService.setDataSource(this.dataSource);

        dao.deleteAll();

        for(User user : users){
            dao.add(user);
        }

        try{
            testUserService.updateLevels();
//            fail("TestUserServiceException expected");
        } catch(TestUserService.TestUserServiceException e){

        }

        checkLevelUpgrade(users.get(1), false);
    }
}
