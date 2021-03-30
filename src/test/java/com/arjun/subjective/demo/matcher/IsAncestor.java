package com.arjun.subjective.demo.matcher;

import com.arjun.subjective.demo.entity.Node;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * 检查给定的节点是否是输入节点的祖先.
 *
 * @author arjun
 * @date 2021/01/08
 */
public class IsAncestor extends TypeSafeDiagnosingMatcher<Node> {

    /**
     * 子节点
     */
    private final Node descendant;

    public IsAncestor(Node descendant) {
        this.descendant = descendant;
    }

    @Override
    protected boolean matchesSafely(Node node, Description description) {
        Node descendantExpected = descendant;
        while (descendantExpected.parent() != null) {
            if (descendantExpected.parent().equals(node)) {
                return true;
            }
            descendantExpected = descendantExpected.parent();
        }
        description.appendText("this node which was not an ancestor of ").appendValue(descendant);
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("an ancestor node of ").appendValue(description);
    }

    public static IsAncestor isAncestor(Node descendant) {
        return new IsAncestor(descendant);
    }
}
