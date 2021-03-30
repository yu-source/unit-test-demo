package com.arjun.subjective.demo.matcher;

import com.arjun.subjective.demo.entity.Node;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * 检查输入节点是否是给定节点的同级节点.
 *
 * @author arjun
 * @date 2021/01/08
 */
public class IsSibling extends TypeSafeDiagnosingMatcher<Node> {

    /**
     * 同级兄弟节点
     */
    private final Node sibling;

    public IsSibling(Node sibling) {
        this.sibling = sibling;
    }

    @Override
    protected boolean matchesSafely(Node node, Description description) {
        if (sibling.parent() == null) {
            description.appendText("input root node can not be tested for siblings");
            return false;
        }
        if (sibling.equals(node)) {
            description.appendText("input node is the same as the test node");
            return false;
        }
        if (node.parent() != null && node.parent().equals(sibling.parent())) {
            return true;
        }
        if (node.parent() == null) {
            description.appendText("a root node with no siblings");
        } else {
            description.appendText("a node with parent ").appendValue(node.parent());
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        if (sibling != null) {
            if (sibling.parent() == null) {
                description.appendText("a sibling of a root node");
            } else {
                description.appendText("two unequal nodes with parent ").appendValue(sibling.parent());
            }
        } else {
            description.appendText("the sibling node cannot be empty");
        }
    }

    public static IsSibling isSibling(Node sibling) {
        return new IsSibling(sibling);
    }
}
