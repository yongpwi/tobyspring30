package springbook.user.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by yong on 2015-07-11.
 *
 * @author yong
 */
public class JdbcContext {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStarategy(StatementStarategy stmt) throws SQLException{
        Connection c = null;
        PreparedStatement ps = null;

        try{
            c = this.dataSource.getConnection();
            ps = stmt.makeStatement(c);

            ps.executeUpdate();
        } catch (SQLException e){
            throw e;
        } finally {
            if(ps != null){
                try{
                    ps.close();
                } catch (SQLException e){

                }
            }
            if(c != null){
                try{
                    c.close();
                } catch (SQLException e){

                }
            }
        }
    }

    public void executeSql(final String query) throws SQLException{
        workWithStatementStarategy(
            new StatementStarategy(){
                public PreparedStatement makeStatement(Connection c) throws SQLException {
                    return c.prepareStatement(query);
                }
            });
    }
}
