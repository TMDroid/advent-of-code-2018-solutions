package com.adventofcode;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Node> children;
    private List<Integer> metadata;

    public Node(int children, int metadata) {
        this.children = new ArrayList<>(children);
        this.metadata = new ArrayList<>(metadata);
    }

    public List<Node> getChildren() {
        return children;
    }

    public List<Integer> getMetadata() {
        return metadata;
    }
}
