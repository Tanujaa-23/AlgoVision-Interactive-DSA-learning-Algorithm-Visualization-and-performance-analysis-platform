package com.algovision.service;

import com.algovision.dao.QuizDAO;
import com.algovision.model.Question;
import com.algovision.model.QuizResult;

import java.util.List;
import java.util.Map;

public class QuizService {

    private final QuizDAO quizDAO = new QuizDAO();

    public List<Question> getQuizQuestions(String topic) {
        return quizDAO.getQuestionsByTopic(topic);
    }

    public QuizResult evaluateQuiz(int userId, String topic, Map<Integer, String> userAnswers, int timeTakenSeconds) {
        List<Question> questions = quizDAO.getQuestionsByTopic(topic);
        int correct = 0;

        for (Question q : questions) {
            String ans = userAnswers.get(q.getId());
            if (ans != null && ans.equalsIgnoreCase(q.getCorrectOption())) {
                correct++;
            }
        }

        int total = questions.size();
        int wrong = total - correct;
        double pct = total > 0 ? (double) correct / total * 100.0 : 0.0;

        QuizResult res = new QuizResult();
        res.setUserId(userId);
        res.setTopic(topic);
        res.setTotalQuestions(total);
        res.setCorrectAnswers(correct);
        res.setWrongAnswers(wrong);
        res.setPercentage(pct);
        res.setTimeTakenSeconds(timeTakenSeconds);

        quizDAO.saveQuizResult(res);
        return res;
    }

    public List<QuizResult> getUserQuizHistory(int userId) {
        return quizDAO.getResultsByUser(userId);
    }
}
