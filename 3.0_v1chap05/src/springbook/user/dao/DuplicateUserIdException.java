package springbook.user.dao;

/**
 * Created by yong on 2015-07-17.
 *
 * @author yong
 */
public class DuplicateUserIdException extends RuntimeException{
    public DuplicateUserIdException(Throwable cause){
        super(cause);
    }
}
