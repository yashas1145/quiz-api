package in.ybuilds.quiz.dao;

import in.ybuilds.quiz.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionDao extends JpaRepository<Question, Integer> {
    List<Question> findQuestionByTopic(String topic);
    boolean existsById(int id);
}
