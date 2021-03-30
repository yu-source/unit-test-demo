package com.arjun.subjective.demo.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * 对数字进行实际检查以查看其是否为偶数.
 *
 * @author arjun
 * @date 2021/01/08
 */
public class IsEven extends TypeSafeDiagnosingMatcher<Integer> {

    @Override
    protected boolean matchesSafely(Integer integer, Description description) {
        description.appendText("was ").appendValue(integer).appendText(", which is an Odd number");
        return integer % 2 == 0;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("An Even number");
    }

    public static IsEven isEven() {
        return new IsEven();
    }
}
