package com.arjun.subjective.demo.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * 整除判断.
 *
 * @author arjun
 * @date 2021/01/08
 */
public class DivisibleBy extends TypeSafeDiagnosingMatcher<Integer> {

    /**
     * 除数
     */
    private final Integer divisor;

    public DivisibleBy(Integer divisor) {
        this.divisor = divisor;
    }


    @Override
    protected boolean matchesSafely(Integer integer, Description description) {
        // 余数
        int remainder = integer % divisor;
        description.appendText("was ").appendValue(integer).appendText(" which left a remainder of ").appendValue(remainder);
        return remainder == 0;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("A number divisible by ").appendValue(divisor);
    }

    public static DivisibleBy divisibleBy(Integer divisor) {
        return new DivisibleBy(divisor);
    }
}
