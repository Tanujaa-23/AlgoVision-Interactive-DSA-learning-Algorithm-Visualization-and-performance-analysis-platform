package com.algovision.datastructure;

import java.util.ArrayList;
import java.util.List;

public class QueueVisualization {

    private final List<Integer> queue = new ArrayList<>();
    private final int capacity = 8;

    public String enqueue(int val) {
        if (queue.size() >= capacity) {
            return "Queue Overflow! Maximum capacity is " + capacity;
        }
        queue.add(val);
        return "Enqueued value " + val + " to Queue rear.";
    }

    public String dequeue() {
        if (queue.isEmpty()) {
            return "Queue Underflow! Queue is empty.";
        }
        int val = queue.remove(0);
        return "Dequeued element " + val + " from Queue front.";
    }

    public String peek() {
        if (queue.isEmpty()) return "Queue is empty.";
        return "Front element is " + queue.get(0);
    }

    public List<Integer> getElements() {
        return new ArrayList<>(queue);
    }
}
