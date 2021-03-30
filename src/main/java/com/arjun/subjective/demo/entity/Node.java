package com.arjun.subjective.demo.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author arjun
 * @date 2021/01/08
 */
public class Node {

    private final int value;

    private Node parent;

    private final Set<Node> children;

    /**
     * Create a new Node with the input value
     */
    public Node(int value) {
        this.value = value;
        children = new HashSet<>();
    }

    /**
     * @return The value of this Node
     */
    public int value() {
        return value;
    }

    /**
     * @return The parent of this Node
     */
    public Node parent() {
        return parent;
    }

    /**
     * @return A copy of the Set of children of this Node
     */
    public Set<Node> children() {
        return new HashSet<>(children);
    }

    /**
     * Add a child to this Node
     *
     * @return this Node
     */
    public Node add(Node child) {
        if (child != null) {
            children.add(child);
            child.parent = this;
        }
        return this;
    }

    /**
     * Remove a child from this Node
     *
     * @return this Node
     */
    public Node remove(Node child) {
        if (child != null && children.contains(child)) {
            children.remove(child);
            child.parent = null;
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Node {")
                .append("value = ").append(value).append(", ")
                .append("parent = ").append(parent != null ? parent.value : "null").append(", ")
                .append("children = ").append("[")
                .append(children.stream().map(n -> Integer.toString(n.value)).collect(Collectors.joining(", ")))
                .append("]}");

        return builder.toString();
    }

    public static void main(String... args) {
        Node root = createTree();
        printNode(root);
    }

    private static Node createTree() {
       /*
                        1
                    /       \
                  2          3
                /   \      /   \
              4       5   6     7
            /   \       |
          8      9    10
       */
        Node root = new Node(1);
        root.add(
                new Node(2).add(
                        new Node(4).add(
                                new Node(8)
                        ).add(
                                new Node(9)
                        )

                ).add(
                        new Node(5).add(
                                new Node(10)
                        )
                )
        ).add(
                new Node(3).add(
                        new Node(6)
                ).add(
                        new Node(7)
                )
        );
        return root;
    }

    private static void printNode(Node node) {
        System.out.println(node);
        for (Node child : node.children()) {
            printNode(child);
        }
    }
}
