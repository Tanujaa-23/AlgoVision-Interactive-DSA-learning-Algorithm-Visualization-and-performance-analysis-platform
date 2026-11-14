package com.algovision.controller;

import com.algovision.dao.ProgressDAO;
import com.algovision.datastructure.*;
import com.algovision.model.User;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class DataStructureServlet extends HttpServlet {

    private final ProgressDAO progressDAO = new ProgressDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ds = request.getParameter("ds");
        if (ds == null) ds = "stack";
        request.setAttribute("selectedDS", ds);
        request.getRequestDispatcher("/visualization/dsVisualizer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loggedUser");
        int userId = 2; // Default to Student (user 2)
        if (user != null) {
            if (user.getId() <= 0) {
                User dbUser = new com.algovision.dao.UserDAO().getUserByEmail(user.getEmail());
                if (dbUser != null && dbUser.getId() > 0) {
                    user.setId(dbUser.getId());
                    session.setAttribute("loggedUser", user);
                    session.setAttribute("userId", user.getId());
                }
            }
            if (user.getId() > 0) {
                userId = user.getId();
            }
        } else if (session.getAttribute("userId") != null) {
            int sessId = (Integer) session.getAttribute("userId");
            if (sessId > 0) {
                userId = sessId;
            }
        }

        String ds = request.getParameter("ds");
        String op = request.getParameter("operation");
        String valStr = request.getParameter("value");

        int val = 0;
        try {
            if (valStr != null) val = Integer.parseInt(valStr.trim());
        } catch (NumberFormatException ignored) {}

        Map<String, Object> respMap = new HashMap<>();

        if ("stack".equalsIgnoreCase(ds)) {
            StackVisualization stack = (StackVisualization) session.getAttribute("ds_stack");
            if (stack == null) {
                stack = new StackVisualization();
                session.setAttribute("ds_stack", stack);
            }
            if ("push".equalsIgnoreCase(op)) respMap.put("message", stack.push(val));
            else if ("pop".equalsIgnoreCase(op)) respMap.put("message", stack.pop());
            else respMap.put("message", stack.peek());
            respMap.put("elements", stack.getElements());

        } else if ("queue".equalsIgnoreCase(ds)) {
            QueueVisualization queue = (QueueVisualization) session.getAttribute("ds_queue");
            if (queue == null) {
                queue = new QueueVisualization();
                session.setAttribute("ds_queue", queue);
            }
            if ("enqueue".equalsIgnoreCase(op)) respMap.put("message", queue.enqueue(val));
            else if ("dequeue".equalsIgnoreCase(op)) respMap.put("message", queue.dequeue());
            else respMap.put("message", queue.peek());
            respMap.put("elements", queue.getElements());

        } else if ("linkedlist".equalsIgnoreCase(ds)) {
            LinkedListVisualization ll = (LinkedListVisualization) session.getAttribute("ds_ll");
            if (ll == null) {
                ll = new LinkedListVisualization();
                session.setAttribute("ds_ll", ll);
            }
            if ("insert".equalsIgnoreCase(op)) respMap.put("message", ll.insert(val));
            else if ("delete".equalsIgnoreCase(op)) respMap.put("message", ll.delete(val));
            else if ("search".equalsIgnoreCase(op)) respMap.put("message", ll.search(val));
            else respMap.put("message", ll.reverse());
            respMap.put("elements", ll.getElements());

        } else if ("heap".equalsIgnoreCase(ds)) {
            HeapVisualization heap = (HeapVisualization) session.getAttribute("ds_heap");
            if (heap == null) {
                heap = new HeapVisualization();
                session.setAttribute("ds_heap", heap);
            }
            if ("insert".equalsIgnoreCase(op)) respMap.put("message", heap.insert(val));
            else respMap.put("message", heap.extractMax());
            respMap.put("elements", heap.getElements());
        }

        // Increment & refresh user progress accurately for Data Structures
        progressDAO.recordAlgorithmProgress(userId, "Data Structures", ds);

        out.print(gson.toJson(respMap));
        out.flush();
    }
}
