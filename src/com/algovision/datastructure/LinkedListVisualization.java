package com.algovision.datastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LinkedListVisualization {

    private final List<Integer> list = new ArrayList<>();

    public String insert(int val) {
        list.add(val);
        return "Inserted node " + val + " at tail of Singly Linked List.";
    }

    public String delete(int val) {
        if (list.remove(Integer.valueOf(val))) {
            return "Deleted node with value " + val + " from Linked List.";
        }
        return "Node " + val + " not found in Linked List.";
    }

    public String search(int val) {
        int idx = list.indexOf(val);
        if (idx != -1) {
            return "Node " + val + " FOUND at node index " + idx;
        }
        return "Node " + val + " NOT FOUND in Linked List.";
    }

    public String reverse() {
        Collections.reverse(list);
        return "Reversed entire Singly Linked List in O(N) time.";
    }

    public List<Integer> getElements() {
        return new ArrayList<>(list);
    }
}
