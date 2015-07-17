package springbook.learningtest;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by yong on 2015-07-14.
 *
 * @author yong
 */
public interface BufferedReaderCallback {
    Integer doSoumeThingWithReader(BufferedReader br) throws IOException;
}
