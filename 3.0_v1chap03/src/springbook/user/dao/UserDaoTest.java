package springbook.user.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by yong on 2015-07-06.
 *
 * @author yong
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserDaoTest {
    UserDao dao;

    private User user;
    private User user2;
    private User user3;

    @Before
    public void setUp(){
        dao = new UserDao();
        DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost/toby", "toby", "toby2015", true);
        dao.setDataSource(dataSource);

        this.user = new User("pear", "피어", "jr");
        this.user2 = new User("pear2", "피어2", "jr");
        this.user3 = new User("pear3", "피어3", "jr3");
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
        assertThat(userget1.getName(), is(user.getName()));
        assertThat(userget1.getPassword(), is(user.getPassword()));

        User userget2 = dao.get(user2.getId());
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPassword(), is(user2.getPassword()));
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
    }
}
