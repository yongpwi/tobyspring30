package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by yong on 2015-07-11.
 *
 * @author yong
 */
public interface StatementStarategy {
    PreparedStatement makeStatement(Connection c) throws SQLException;
}
