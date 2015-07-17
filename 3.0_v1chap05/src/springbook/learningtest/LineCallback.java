package springbook.learningtest;

/**
 * Created by yong on 2015-07-14.
 *
 * @author yong
 */
public interface LineCallback<T> {
    T doSomeThingWithLine(String line, T value);
}
