package com.cntest.su.demo.matcher;

import com.cntest.su.demo.entity.Node;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * 检查给定的节点是否为根节点.
 *
 * @author arjun
 * @date 2021/01/08
 */
public class IsRoot extends TypeSafeDiagnosingMatcher<Node> {

    @Override
    protected boolean matchesSafely(Node node, Description description) {
        if (node.parent() != null) {
            description.appendValue(node).appendText(" this node with parent ").appendValue(node.parent());
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a root node with no parent");
    }

    public static IsRoot isRoot() {
        return new IsRoot();
    }
}
