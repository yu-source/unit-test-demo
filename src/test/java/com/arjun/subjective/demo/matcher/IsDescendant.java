package com.arjun.subjective.demo.matcher;

import com.arjun.subjective.demo.entity.Node;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * 检查给定的节点是否为输入Node的后代.
 * @author arjun
 * @date 2021/01/08
 */
public class IsDescendant extends TypeSafeDiagnosingMatcher<Node> {

    /**
     * 父节点
     */
    private final Node ancestor;

    public IsDescendant(Node ancestor) {
        this.ancestor = ancestor;
    }

    @Override
    protected boolean matchesSafely(Node node, Description description) {
        while (node.parent() != null) {
            if (node.parent().equals(ancestor)) {
                return true;
            }
            node = node.parent();
        }
        description.appendText("this node which was not a descendant of ").appendValue(ancestor);
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a descendant node of ").appendValue(ancestor);
    }

    public static IsDescendant isDescendant(Node ancestor) {
        return new IsDescendant(ancestor);
    }
}
