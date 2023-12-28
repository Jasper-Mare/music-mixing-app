package src.util;

import java.util.ArrayList;

public class Tree<T> {

    Node rootNode;

    public class Node {
        public T data;
        public ArrayList<Node> childNodes;

        public Node(T data) {
            this.data = data;
        }

        public boolean isLeaf() {
            return (childNodes.size() == 0);
        }

        public void addChildNode(T data) {
            childNodes.add(new Node(data));
        }
    }
}
