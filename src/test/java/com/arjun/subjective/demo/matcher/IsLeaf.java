package com.arjun.subjective.demo.matcher;

import com.arjun.subjective.demo.entity.Node;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * 检查给定的节点是否为叶子节点.
 *
 * @author arjun
 * @date 2021/01/08
 */
public class IsLeaf extends TypeSafeDiagnosingMatcher<Node> {

    @Override
    protected boolean matchesSafely(Node node, Description description) {
        if (!node.children().isEmpty()) {
            description.appendValue(node).appendText(" this node with ").appendValue(node.children().size()).appendText(" children");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a leaf node with no children");
    }

    public static IsLeaf isLeaf() {
        return new IsLeaf();
    }
}
