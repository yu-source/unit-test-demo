package com.arjun.subjective.demo.service;

import com.arjun.subjective.demo.entity.Node;
import com.arjun.subjective.demo.matcher.IsAncestor;
import com.arjun.subjective.demo.matcher.IsDescendant;
import com.arjun.subjective.demo.matcher.IsLeaf;
import com.arjun.subjective.demo.matcher.IsRoot;
import com.arjun.subjective.demo.matcher.IsSibling;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author arjun
 * @date 2021/01/08
 */
public class NodeTestFixture {

    private static Node one = new Node(1);
    private static Node two = new Node(2);
    private static Node three = new Node(3);
    private static Node four = new Node(4);
    private static Node five = new Node(5);
    private static Node six = new Node(6);
    private static Node seven = new Node(7);
    private static Node eight = new Node(8);
    private static Node nine = new Node(9);
    private static Node ten = new Node(10);

    /*
                        1
                    /       \
                  2          3
                /   \      /   \
              4       5   6     7
            /   \       |
          8      9    10
     */
    static {
        one.add(two);
        one.add(three);

        two.add(four);
        two.add(five);

        three.add(six);
        three.add(seven);

        four.add(eight);
        four.add(nine);

        five.add(ten);
    }

    @Test
    public void nodeTest() {
        // 自定义节点测试

        // 检查给定的节点是否为叶子节点
        Assert.assertThat(six, IsLeaf.isLeaf());
        // 检查给定的节点是否为根节点.
        Assert.assertThat(one, IsRoot.isRoot());
        // 检查给定的节点是否为输入Node的后代.
        Assert.assertThat(three, IsDescendant.isDescendant(one));
        // 检查给定的节点是否是输入节点的祖先.
        Assert.assertThat(one, IsAncestor.isAncestor(two));
        // 检查输入节点是否是给定节点的同级节点.
        Assert.assertThat(three, IsSibling.isSibling(two));
        // 自定义匹配器
//        Assert.assertThat(null, IsSibling.isSibling(null));
//        Assert.assertThat(one, IsSibling.isSibling(one));
//        Assert.assertThat(two, IsSibling.isSibling(one));
//        Assert.assertThat(one, IsSibling.isSibling(two));
//        Assert.assertThat(five, IsSibling.isSibling(six));
    }
}
