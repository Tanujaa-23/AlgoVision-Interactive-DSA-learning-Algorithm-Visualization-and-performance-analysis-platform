package com.algovision.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for managing PostgreSQL JDBC Database Connections.
 * Implements thread-safe singleton connection pattern with automatic table initialization fallback.
 */
public class DBConnection {

    // Default PostgreSQL database credentials
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/algovision_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres"; // User can change as per local Postgres setup
    private static final String JDBC_DRIVER = "org.postgresql.Driver";

    private static boolean driverLoaded = false;
    private static boolean tablesInitialized = false;

    static {
        try {
            Class.forName(JDBC_DRIVER);
            driverLoaded = true;
            System.out.println("[DBConnection] PostgreSQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("[DBConnection] PostgreSQL JDBC Driver not found in classpath: " + e.getMessage());
        }
    }

    /**
     * Obtains a new database connection to PostgreSQL.
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (!driverLoaded) {
            try {
                Class.forName(JDBC_DRIVER);
                driverLoaded = true;
            } catch (ClassNotFoundException e) {
                throw new SQLException("PostgreSQL JDBC Driver missing from classpath.", e);
            }
        }
        
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        if (!tablesInitialized) {
            initializeDatabaseTables(conn);
            tablesInitialized = true;
        }
        return conn;
    }

    /**
     * Ensures all required tables and default admin user are present in PostgreSQL database.
     */
    private static synchronized void initializeDatabaseTables(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // Create Users Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(150) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "role VARCHAR(20) DEFAULT 'STUDENT', " +
                    "streak_days INT DEFAULT 1, " +
                    "last_login_date DATE DEFAULT CURRENT_DATE, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Create Algorithms Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS algorithms (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL UNIQUE, " +
                    "category VARCHAR(50) NOT NULL, " +
                    "description TEXT NOT NULL, " +
                    "how_it_works TEXT, " +
                    "complexity_best VARCHAR(50), " +
                    "complexity_avg VARCHAR(50), " +
                    "complexity_worst VARCHAR(50), " +
                    "space_complexity VARCHAR(50), " +
                    "is_stable BOOLEAN DEFAULT FALSE, " +
                    "is_in_place BOOLEAN DEFAULT TRUE, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Create Executions Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS algorithm_executions (" +
                    "id SERIAL PRIMARY KEY, " +
                    "user_id INT REFERENCES users(id) ON DELETE CASCADE, " +
                    "algorithm_id INT REFERENCES algorithms(id) ON DELETE SET NULL, " +
                    "algorithm_name VARCHAR(100), " +
                    "input_data TEXT NOT NULL, " +
                    "step_count INT DEFAULT 0, " +
                    "comparison_count INT DEFAULT 0, " +
                    "swap_count INT DEFAULT 0, " +
                    "execution_time_ms DOUBLE PRECISION DEFAULT 0.0, " +
                    "executed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Create Quiz Questions Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS quiz_questions (" +
                    "id SERIAL PRIMARY KEY, " +
                    "question TEXT NOT NULL, " +
                    "option_a TEXT NOT NULL, " +
                    "option_b TEXT NOT NULL, " +
                    "option_c TEXT NOT NULL, " +
                    "option_d TEXT NOT NULL, " +
                    "correct_option CHAR(1) NOT NULL, " +
                    "explanation TEXT, " +
                    "topic VARCHAR(50) NOT NULL, " +
                    "difficulty VARCHAR(20) DEFAULT 'Easy', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Create Quiz Results Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS quiz_results (" +
                    "id SERIAL PRIMARY KEY, " +
                    "user_id INT REFERENCES users(id) ON DELETE CASCADE, " +
                    "topic VARCHAR(50) NOT NULL, " +
                    "total_questions INT NOT NULL, " +
                    "correct_answers INT NOT NULL, " +
                    "wrong_answers INT NOT NULL, " +
                    "percentage DOUBLE PRECISION NOT NULL, " +
                    "time_taken_seconds INT DEFAULT 0, " +
                    "taken_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Create Challenges Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS challenges (" +
                    "id SERIAL PRIMARY KEY, " +
                    "title VARCHAR(200) NOT NULL, " +
                    "description TEXT NOT NULL, " +
                    "difficulty VARCHAR(20) NOT NULL, " +
                    "topic VARCHAR(50) NOT NULL, " +
                    "input_format TEXT, " +
                    "output_format TEXT, " +
                    "constraints TEXT, " +
                    "sample_input TEXT NOT NULL, " +
                    "sample_output TEXT NOT NULL, " +
                    "expected_output TEXT NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Create Submissions Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS submissions (" +
                    "id SERIAL PRIMARY KEY, " +
                    "user_id INT REFERENCES users(id) ON DELETE CASCADE, " +
                    "challenge_id INT REFERENCES challenges(id) ON DELETE CASCADE, " +
                    "user_code TEXT NOT NULL, " +
                    "status VARCHAR(30) NOT NULL, " +
                    "execution_time_ms DOUBLE PRECISION DEFAULT 0.0, " +
                    "submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Create User Progress Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS user_progress (" +
                    "id SERIAL PRIMARY KEY, " +
                    "user_id INT REFERENCES users(id) ON DELETE CASCADE, " +
                    "topic VARCHAR(50) NOT NULL, " +
                    "completed_count INT DEFAULT 0, " +
                    "total_count INT DEFAULT 10, " +
                    "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "CONSTRAINT unique_user_topic UNIQUE(user_id, topic))");

            // Create Achievements Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS achievements (" +
                    "id SERIAL PRIMARY KEY, " +
                    "badge_key VARCHAR(50) UNIQUE NOT NULL, " +
                    "title VARCHAR(100) NOT NULL, " +
                    "description TEXT NOT NULL, " +
                    "icon_class VARCHAR(50) NOT NULL)");

            // Create User Achievements Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS user_achievements (" +
                    "id SERIAL PRIMARY KEY, " +
                    "user_id INT REFERENCES users(id) ON DELETE CASCADE, " +
                    "achievement_id INT REFERENCES achievements(id) ON DELETE CASCADE, " +
                    "earned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "CONSTRAINT unique_user_badge UNIQUE(user_id, achievement_id))");

            System.out.println("[DBConnection] Database schema verified/initialized successfully.");
        } catch (SQLException e) {
            System.err.println("[DBConnection] Schema initialization error: " + e.getMessage());
        }
    }
}
