package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by yong on 2015-07-07.
 *
 * @author yong
 */
public class CountingConnectionMaker implements ConnectionMaker {
    int counter = 0;
    private ConnectionMaker realConnectionMaker;

    public CountingConnectionMaker(ConnectionMaker realConnectionMaker){
        this.realConnectionMaker = realConnectionMaker;
    }

    public Connection markConnection() throws ClassNotFoundException, SQLException {
        this.counter++;
        return realConnectionMaker.markConnection();
    }

    public int getCounter(){
        return this.counter;
    }
}
