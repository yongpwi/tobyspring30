package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by yong on 2015-07-06.
 *
 * @author yong
 */
public interface ConnectionMaker {
    public Connection markConnection() throws ClassNotFoundException, SQLException;
}
