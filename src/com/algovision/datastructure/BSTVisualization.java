package com.algovision.datastructure;

import java.util.ArrayList;
import java.util.List;

public class BSTVisualization {

    public static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node(int val) { this.val = val; }
    }

    private Node root;

    public String insert(int val) {
        root = insertRec(root, val);
        return "Inserted value " + val + " into Binary Search Tree.";
    }

    private Node insertRec(Node node, int val) {
        if (node == null) return new Node(val);
        if (val < node.val) node.left = insertRec(node.left, val);
        else if (val > node.val) node.right = insertRec(node.right, val);
        return node;
    }

    public String search(int val) {
        return searchRec(root, val) ? "Value " + val + " FOUND in BST." : "Value " + val + " NOT FOUND in BST.";
    }

    private boolean searchRec(Node node, int val) {
        if (node == null) return false;
        if (node.val == val) return true;
        return val < node.val ? searchRec(node.left, val) : searchRec(node.right, val);
    }

    public List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorderRec(root, result);
        return result;
    }

    private void inorderRec(Node node, List<Integer> result) {
        if (node != null) {
            inorderRec(node.left, result);
            result.add(node.val);
            inorderRec(node.right, result);
        }
    }

    public List<Integer> preorder() {
        List<Integer> result = new ArrayList<>();
        preorderRec(root, result);
        return result;
    }

    private void preorderRec(Node node, List<Integer> result) {
        if (node != null) {
            result.add(node.val);
            preorderRec(node.left, result);
            preorderRec(node.right, result);
        }
    }

    public List<Integer> postorder() {
        List<Integer> result = new ArrayList<>();
        postorderRec(root, result);
        return result;
    }

    private void postorderRec(Node node, List<Integer> result) {
        if (node != null) {
            postorderRec(node.left, result);
            postorderRec(node.right, result);
            result.add(node.val);
        }
    }

    public Node getRoot() { return root; }
}
