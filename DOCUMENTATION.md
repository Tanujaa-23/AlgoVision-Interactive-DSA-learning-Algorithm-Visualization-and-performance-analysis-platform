# AlgoVision Pro - Project Documentation & System Architecture Report

---

## 1. ABSTRACT
**AlgoVision Pro** is an interactive, web-based Data Structures and Algorithms (DSA) learning, step-by-step visualization, and empirical performance analysis platform. Designed specifically as a final-year Bachelor of Information Technology project, AlgoVision Pro addresses the difficulty computer science students face when learning abstract algorithmic processes. By combining step-by-step Java-driven animation engines, live metric tracking, interactive data structure operations, DSA quizzes, coding challenge evaluation, AI/rule-based algorithm recommendation, and downloadable PDF execution reports, AlgoVision Pro transforms abstract theoretical concepts into visual intuition.

---

## 2. INTRODUCTION
Data Structures and Algorithms form the foundation of computer science and software engineering. Traditional educational approaches rely on static textbook pseudocode and manual trace tables, which fail to convey the dynamic behavior of array partitioning, pointer manipulation, and graph edge relaxation. AlgoVision Pro provides a digital sandbox where students can experiment with custom inputs, observe micro-steps (comparisons, swaps, pivots, node visits), compare Big-O efficiency under empirical workloads, and evaluate their understanding through interactive assessments.

---

## 3. PROBLEM STATEMENT
Students frequently encounter learning bottlenecks when studying DSA due to:
1. **Lack of Visual Feedback**: Inability to visualize memory state transitions during recursive sorts or graph traversals.
2. **Abstract Complexity Analysis**: Difficulty relating Big-O theoretical bounds to practical runtime behavior across varying input distributions.
3. **Disjointed Learning Tools**: Switching between separate tools for visualization, quizzes, and code evaluation.
4. **Static Study Materials**: Passive reading of textbook code without interactive experimentation.

---

## 4. OBJECTIVES
- Build a production-grade Java EE web application following traditional Eclipse Dynamic Web Project guidelines.
- Implement strict **MVC + DAO** architecture cleanly separating controller servlets, data access objects, core Java DSA implementations, step engines, and JSP/JS view layers.
- Provide step-by-step visual animation engines for 14+ core algorithms across Sorting, Searching, and Graph domains.
- Deliver interactive visualization modules for Stack, Queue, Linked List, BST, and Binary Max Heap.
- Incorporate empirical performance benchmarking (Input Size vs. Execution Time) powered by Chart.js.
- Feature an intelligent rule-based Algorithm Recommendation Engine and PDF execution report generator.

---

## 5. EXISTING SYSTEM VS PROPOSED SYSTEM

| Feature | Existing System (Traditional Learning) | Proposed System (AlgoVision Pro) |
| :--- | :--- | :--- |
| **Learning Mode** | Passive pseudocode reading & paper tracing | Interactive step-by-step animated visualizer |
| **Custom Inputs** | Fixed textbook array examples | User-defined inputs, size tuning & custom graphs |
| **Performance Analysis** | Theoretical Big-O formulas only | Empirical benchmark tables & live Chart.js graphs |
| **Assessment & Feedback** | Manual paper grading | Instant automated DSA quiz & challenge evaluation |
| **Execution Reports** | None | Downloadable formatted PDF execution reports |

---

## 6. SYSTEM ARCHITECTURE

```
                               +----------------------------------+
                               |     Browser (HTML5/CSS3/JS)      |
                               +----------------------------------+
                                                |
                                                | HTTP Request / AJAX
                                                v
                               +----------------------------------+
                               |   Jakarta Servlet Controller     |
                               | (Login, Visualizer, Quiz, etc.)  |
                               +----------------------------------+
                                                |
                                                v
                               +----------------------------------+
                               |     Service & Algorithm Layer     |
                               | (BubbleSort, Dijkstra, StepEngine)|
                               +----------------------------------+
                                                |
                                                v
                               +----------------------------------+
                               |            DAO Layer             |
                               | (UserDAO, AlgorithmDAO, QuizDAO) |
                               +----------------------------------+
                                                |
                                                v
                               +----------------------------------+
                               |  JDBC / PostgreSQL Database      |
                               |        (algovision_db)           |
                               +----------------------------------+
```

---

## 7. TECHNOLOGY STACK

- **Language & Runtime**: Java 17+, JDK 23
- **Web Application Standard**: Jakarta EE Servlet API 6.0, JSP 3.1
- **Deployment Platform**: Apache Tomcat 10+ / Eclipse Dynamic Web Project
- **Database & Persistence**: PostgreSQL 16 / JDBC with `PreparedStatement`
- **Frontend Architecture**: HTML5, CSS3, JavaScript (ES6+), Bootstrap 5, Bootstrap Icons
- **Data Visualization**: Chart.js, HTML5 Canvas API
- **PDF Generation**: OpenPDF / iText Java Library

---

## 8. DATABASE DESIGN (ER DIAGRAM & TABLES)

### 8.1 PostgreSQL Database Schema (`algovision_db`)

1. **`users`**: Stores user authentication records, roles (`STUDENT`, `ADMIN`), streak counters, and timestamps.
2. **`algorithms`**: Stores algorithm metadata, Big-O complexities (Best, Average, Worst, Space), stability, and in-place attributes.
3. **`algorithm_executions`**: Logs user execution runs, input arrays, step counts, comparisons, swaps, and runtime in milliseconds.
4. **`quiz_questions`**: Stores multi-topic multiple-choice questions, options, correct choices, explanations, and difficulty levels.
5. **`quiz_results`**: Tracks user quiz scores, percentage, wrong answers, and completion time.
6. **`challenges`**: Holds DSA coding problems, constraints, sample inputs, and expected outputs.
7. **`submissions`**: Records code submissions, evaluation status (`Accepted`, `Wrong Answer`), and runtime metrics.
8. **`user_progress`**: Tracks topic completion progress per student.
9. **`achievements` & `user_achievements`**: Badge keys, title descriptions, and user badge unlocking timestamps.

---

## 9. MODULE DESCRIPTIONS

1. **Authentication Module**: Secure login, registration, session management (`HttpSession`), role authorization, and SHA-256 password hashing.
2. **Student Dashboard Module**: Overview of completed algorithms, solved challenges, streak counter, topic progress doughnut charts, and recent activity log.
3. **Algorithm Visualization Engine**: Step-generator backend (`AlgorithmStep.java`) paired with JavaScript canvas/bar renderer supporting custom input, speed control, play/pause/step operations.
4. **Data Structure Visualizer**: Interactive sandbox for Stack (push/pop/peek), Queue (enqueue/dequeue), Linked List (insert/delete/search/reverse), BST, and Max Heap.
5. **Performance Analyzer**: Empirical benchmarking engine generating live comparison tables and Chart.js line graphs across array sizes 100 to 5,000.
6. **Quiz & Challenge Modules**: Instant scoring, solution explanations, timer counters, and automated Java output evaluation.
7. **Recommendation Engine**: Rule-based matching system recommending optimal algorithms (e.g. Dijkstra, BFS, Quick Sort) for given problem statements.
8. **PDF Report Generator**: Produces downloadable PDF execution reports using OpenPDF.
9. **Admin Dashboard**: Control panel for managing users, adding algorithms, and monitoring platform execution stats.

---

## 10. ADVANTAGES & FUTURE SCOPE

### Advantages:
- Built strictly without Maven/Spring/Hibernate frameworks as a true Eclipse Dynamic Web Project.
- Pure Java algorithm implementations avoiding built-in utility shortcuts.
- High-performance, responsive dark-themed Bootstrap UI with interactive canvas components.
- Zero SQL injection vulnerability guaranteed by exclusive use of JDBC `PreparedStatement`.

### Future Scope:
- Machine Learning-based personalized learning path recommendations.
- Multi-language online code compiler (Python, C++, Java, Rust).
- Gamified multiplayer DSA challenge battles and global student leaderboards.
