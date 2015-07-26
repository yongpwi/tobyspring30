package springbook.user.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.UserService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;

/**
 * Created by yong on 2015-07-06.
 *
 * @author yong
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserDaoTest {
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
    public void addAndGet() throws ClassNotFoundException, SQLException {
        setUp();

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User userget1 = dao.get(user.getId());
        checkSameUser(userget1, user);

        User userget2 = dao.get(user2.getId());
        checkSameUser(userget2, user2);
    }

    @Test
    public void count() throws ClassNotFoundException, SQLException {
        setUp();
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException{
        setUp();
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknow_id");
    }

    @Test
    public void getAll(){
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertThat(users0.size(), is(0));

        dao.add(user);
        List<User> users = dao.getAll();
        assertThat(users.size(), is(1));
        checkSameUser(user, users.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size(), is(2));
        checkSameUser(user, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size(), is(3));
        checkSameUser(user, users3.get(0));
        checkSameUser(user2, users3.get(1));
        checkSameUser(user3, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
    }

    @Test(expected = DuplicateKeyException.class)
    public void duplicateKey(){
        dao.deleteAll();

        dao.add(user);
        dao.add(user);
    }

    @Test
    public void sqlExceptionTranslate(){
        dao.deleteAll();

        try{
            dao.add(user);
            dao.add(user);
        } catch (DuplicateKeyException ex){
            SQLException sqlEx = (SQLException)ex.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            assertThat(set.translate(null, null, sqlEx), is(DuplicateKeyException.class));
        }
    }

    @Test
    public void update(){
        dao.deleteAll();

        dao.add(user);
        dao.add(user2);

        user.setName("용퓌");
        user.setPassword("spring");
        user.setLevel(Level.GOLD);
        user.setLogin(1);
        user.setRecommend(11);
        dao.update(user);

        User userUpdate = dao.get(user.getId());
        checkSameUser(user, userUpdate);
        User user2same = dao.get(user2.getId());
        checkSameUser(user2, user2same);
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
            fail("TestUserServiceException expected");
        } catch(TestUserService.TestUserServiceException e){

        }

        checkLevelUpgrade(users.get(1), false);
    }
}
