package springbook.learningtest;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by yong on 2015-07-14.
 *
 * @author yong
 */
public class CalcSumTest {
    Calculator calculator;
    String numFiepath;

    @Before
    public void setUp(){
        this.calculator = new Calculator();
        this.numFiepath = getClass().getResource("numbers.txt").getPath();
    }

    @Test
    public void sumObNumbers() throws IOException{
        Calculator calculator = new Calculator();
        int sum = calculator.calcSum(getClass().getResource("numbers.txt").getPath());
        assertThat(sum, is(10));
    }

    @Test
    public void multiplyOfNumbers() throws IOException{
        assertThat(calculator.calcMultiply(this.numFiepath), is(24));
    }
}
