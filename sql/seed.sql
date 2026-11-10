-- Initial Seed Data for AlgoVision Pro 

-- Standard Admin and Demo Student Users (Passwords hashed using SHA-256 for 'admin123' and 'student123')
INSERT INTO users (name, email, password, role, streak_days) VALUES
('Alex Student', 'student@algovision.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'STUDENT', 7)
ON CONFLICT (email) DO NOTHING;

-- Achievements / Badges
INSERT INTO achievements (badge_key, title, description, icon_class) VALUES
('first_algo', 'First Algorithm', 'Visualized your first algorithm on AlgoVision Pro', 'bi bi-lightning-charge-fill'),
('sorting_master', 'Sorting Master', 'Executed all 6 sorting algorithms step-by-step', 'bi bi-sort-numeric-down'),
('graph_explorer', 'Graph Explorer', 'Ran graph traversals and shortest path algorithms', 'bi bi-diagram-3-fill'),
('quiz_master', 'Quiz Master', 'Scored 100% on a DSA topic quiz', 'bi bi-patch-check-fill'),
('challenge_solver', 'Problem Solver', 'Solved 10 coding challenges successfully', 'bi bi-code-slash'),
('streak_7', '7 Day Streak', 'Logged in and practiced DSA for 7 consecutive days', 'bi bi-fire')
ON CONFLICT (badge_key) DO NOTHING;

-- Algorithms Information Seed
INSERT INTO algorithms (name, category, description, how_it_works, complexity_best, complexity_avg, complexity_worst, space_complexity, is_stable, is_in_place) VALUES
('Bubble Sort', 'Sorting', 'Bubble Sort repeatedly steps through the list, compares adjacent elements and swaps them if they are in the wrong order.', 'Repeatedly passes through the array, comparing adjacent elements and swapping them if arr[j] > arr[j+1]. After each pass, the largest unsorted element bubbles up to its correct position.', 'O(n)', 'O(n²)', 'O(n²)', 'O(1)', TRUE, TRUE),
('Selection Sort', 'Sorting', 'Selection Sort divides the array into a sorted and an unsorted region. It repeatedly selects the smallest element from the unsorted region.', 'Iterates through the unsorted portion of the array to find the minimum element, then swaps it with the first element of the unsorted portion.', 'O(n²)', 'O(n²)', 'O(n²)', 'O(1)', FALSE, TRUE),
('Insertion Sort', 'Sorting', 'Insertion Sort builds the final sorted array one item at a time by placing each item into its correct position.', 'Iterates through elements and inserts each element into its proper sorted sub-array position by shifting larger elements to the right.', 'O(n)', 'O(n²)', 'O(n²)', 'O(1)', TRUE, TRUE),
('Merge Sort', 'Sorting', 'Merge Sort is an efficient, comparison-based, divide-and-conquer sorting algorithm.', 'Recursively divides the array into two halves, sorts each half, and then merges the two sorted halves using an auxiliary array.', 'O(n log n)', 'O(n log n)', 'O(n log n)', 'O(n)', TRUE, FALSE),
('Quick Sort', 'Sorting', 'Quick Sort picks a element as pivot and partitions the given array around the picked pivot.', 'Chooses a pivot element, partitions elements smaller than pivot to left and greater to right, then recursively sorts the sub-arrays.', 'O(n log n)', 'O(n log n)', 'O(n²)', 'O(log n)', FALSE, TRUE),
('Heap Sort', 'Sorting', 'Heap Sort converts the array into a Binary Max Heap and repeatedly extracts the maximum element.', 'Builds a max heap from the array, swaps the root (max element) with the last element, reduces heap size, and re-heapifies.', 'O(n log n)', 'O(n log n)', 'O(n log n)', 'O(1)', FALSE, TRUE),
('Linear Search', 'Searching', 'Linear Search sequentially checks each element of the list until a match is found or the end is reached.', 'Iterates sequentially from index 0 to n-1 comparing target with each array element.', 'O(1)', 'O(n)', 'O(n)', 'O(1)', TRUE, TRUE),
('Binary Search', 'Searching', 'Binary Search finds the position of a target value within a sorted array using divide-and-conquer.', 'Compares target with the middle element. If smaller, searches left half; if larger, searches right half.', 'O(1)', 'O(log n)', 'O(log n)', 'O(1)', TRUE, TRUE),
('Jump Search', 'Searching', 'Jump Search checks fewer elements than linear search by jumping ahead by fixed steps in a sorted array.', 'Jumps ahead by step size m = √n. Once an element larger than target is reached, performs linear search backwards.', 'O(1)', 'O(√n)', 'O(√n)', 'O(1)', TRUE, TRUE),
('Breadth-First Search (BFS)', 'Graph', 'BFS traverses or searches tree/graph data structures level by level using a FIFO Queue.', 'Starts at source node, visits all immediate neighbors, enqueues them, and continues level by level.', 'O(V + E)', 'O(V + E)', 'O(V + E)', 'O(V)', TRUE, FALSE),
('Depth-First Search (DFS)', 'Graph', 'DFS explores as far as possible along each branch before backtracking using a Stack / Recursion.', 'Starts at root/source, marks as visited, and recursively explores each unvisited adjacent node.', 'O(V + E)', 'O(V + E)', 'O(V + E)', 'O(V)', TRUE, FALSE),
('Dijkstra Shortest Path', 'Graph', 'Dijkstra finds the shortest path between nodes in a weighted graph with non-negative edge weights.', 'Maintains a distance array initialized to infinity. Uses a priority queue to extract the unvisited node with minimum distance and relaxes its neighbors.', 'O((V + E) log V)', 'O((V + E) log V)', 'O((V + E) log V)', 'O(V)', TRUE, FALSE),
('Prim Minimum Spanning Tree', 'Graph', 'Prim algorithm builds a Minimum Spanning Tree for a weighted undirected graph starting from an arbitrary vertex.', 'Grows MST one vertex at a time by picking the minimum weight edge connecting a visited node to an unvisited node.', 'O(E log V)', 'O(E log V)', 'O(E log V)', 'O(V)', TRUE, FALSE),
('Kruskal MST', 'Graph', 'Kruskal algorithm finds MST by sorting all edges by weight and adding non-cycle edges using Disjoint Set (Union-Find).', 'Sorts all edges in ascending order of weight. Adds edges one by one to MST if they do not form a cycle (using Union-Find).', 'O(E log E)', 'O(E log E)', 'O(E log E)', 'O(V + E)', TRUE, FALSE)
ON CONFLICT (name) DO NOTHING;

-- Quiz Questions Seed
INSERT INTO quiz_questions (question, option_a, option_b, option_c, option_d, correct_option, explanation, topic, difficulty) VALUES
('What is the worst-case time complexity of Quick Sort?', 'O(n log n)', 'O(n)', 'O(n²)', 'O(2ⁿ)', 'C', 'Quick Sort has a worst-case complexity of O(n²) when the pivot chosen is consistently the smallest or largest element (e.g. sorted input with poor pivot choice).', 'Sorting', 'Easy'),
('Which sorting algorithm guarantees O(n log n) worst-case time complexity and is stable?', 'Quick Sort', 'Merge Sort', 'Heap Sort', 'Bubble Sort', 'B', 'Merge Sort always runs in O(n log n) time in best, average, and worst cases, and preserves relative order of equal elements (stable).', 'Sorting', 'Medium'),
('What data structure is primarily used to implement Breadth-First Search (BFS)?', 'Stack', 'Queue', 'Priority Queue', 'Binary Search Tree', 'B', 'BFS uses a FIFO Queue to process graph vertices level by level.', 'Graph', 'Easy'),
('What is the tight space complexity of Heap Sort?', 'O(n)', 'O(n log n)', 'O(1)', 'O(log n)', 'C', 'Heap Sort sorts in-place using the original array memory, resulting in O(1) auxiliary space complexity.', 'Sorting', 'Medium'),
('In Dijkstra algorithm, what requirement must be satisfied for edge weights?', 'All edge weights must be negative', 'Edge weights must be non-negative', 'Graph must be unweighted', 'Graph must be bipartite', 'B', 'Dijkstra algorithm requires non-negative edge weights because greedy edge relaxation assumes paths can only increase in weight.', 'Graph', 'Medium'),
('What is the average time complexity of searching in a balanced Binary Search Tree (BST)?', 'O(1)', 'O(n)', 'O(log n)', 'O(n log n)', 'C', 'A balanced BST has a height of log n, so lookup operation takes O(log n) comparisons.', 'Trees', 'Easy');

-- Coding Challenges Seed
INSERT INTO challenges (title, description, difficulty, topic, input_format, output_format, constraints, sample_input, sample_output, expected_output) VALUES
('Find Maximum Element', 'Given an array of integers, write a Java program to find and return the maximum element in the array.', 'Easy', 'Arrays', 'Space-separated integers representing array elements', 'Single integer representing maximum value', '1 <= N <= 1000', '5 2 9 1 7', '9', '9'),
('Check Array Sorted', 'Write a program to determine if a given array of integers is sorted in non-decreasing (ascending) order.', 'Easy', 'Arrays', 'Space-separated integers', 'true if sorted, false otherwise', '1 <= N <= 1000', '1 3 5 8 10', 'true', 'true'),
('Binary Search Implementation', 'Given a sorted array of distinct integers and a target value, return the 0-based index of target if found, or -1 if target is not present.', 'Medium', 'Searching', 'First line: array elements space-separated. Second line: target integer.', 'Integer index', '1 <= N <= 10^5', '2 5 8 12 16 23\n12', '3', '3'),
('Reverse Linked List', 'Given head of a singly linked list values, return the reversed values.', 'Medium', 'LinkedList', 'Space-separated node values', 'Space-separated reversed values', '1 <= N <= 1000', '1 2 3 4 5', '5 4 3 2 1', '5 4 3 2 1');
