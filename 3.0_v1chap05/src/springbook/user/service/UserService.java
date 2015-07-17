package springbook.user.service;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.List;

/**
 * Created by yong on 2015-07-17.
 *
 * @author yong
 */
public class UserService {
    UserDao userDao;

    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    public void updateLevels(){
        List<User> users = userDao.getAll();
        for(User user : users){
            Boolean changed = null;
            if(user.getLevel() == Level.BASIC && user.getLogin() >= 1){
                user.setLevel(Level.SILVER);
                changed = true;
            } else if(user.getLevel() == Level.SILVER && user.getRecommend() >= 10){
                user.setLevel(Level.GOLD);
                changed = true;
            } else if(user.getLevel() == Level.GOLD){
                changed = false;
            } else{
                changed = false;
            }

            if(changed){
                userDao.update(user);
            }
        }
    }
}
