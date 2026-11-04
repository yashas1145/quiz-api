package in.ybuilds.quiz.service;

import in.ybuilds.quiz.dao.QuestionDao;
import in.ybuilds.quiz.model.Question;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionDao questionDao;

    public List<Question> getQuestions() {
        return questionDao.findAll();
    }

    public List<Question> getQuestionsByTopic(String topic) {
        return questionDao.findQuestionByTopic(topic);
    }

    public Question getQuestionById(int id) {
        return questionDao.findById(id).orElse(null);
    }

    public boolean questionExistsById(int id) {
        return !questionDao.existsById(id);
    }

    public Question addQuestion(Question question) {
        return questionDao.save(question);
    }

    @Transactional
    public List<Question> addQuestions(List<Question> questionList) {
        return questionDao.saveAll(questionList);
    }

    @Transactional
    public Question updateQuestion(int id, Question question) {
        Question dbQuestion = questionDao.findById(id).orElse(null);

        if(dbQuestion == null) {
            return null;
        }

        dbQuestion.setQuestion(question.getQuestion() == null ? dbQuestion.getQuestion() : question.getQuestion());
        dbQuestion.setTopic(question.getTopic() == null ? dbQuestion.getTopic() : question.getTopic());
        dbQuestion.setOption1(question.getOption1() == null ? dbQuestion.getOption1() : question.getOption1());
        dbQuestion.setOption2(question.getOption2() == null ? dbQuestion.getOption2() : question.getOption2());
        dbQuestion.setOption3(question.getOption3() == null ? dbQuestion.getOption3() : question.getOption3());
        dbQuestion.setOption4(question.getOption4() == null ? dbQuestion.getOption4() : question.getOption4());
        dbQuestion.setAnswer(question.getAnswer() == null ? dbQuestion.getAnswer() : question.getAnswer());

        questionDao.save(dbQuestion);
        return dbQuestion;
    }

    @Transactional
    public boolean deleteQuestion(int id) {
        Question question = questionDao.findById(id).orElse(null);

        if(question == null) {
            return false;
        }

        questionDao.delete(question);
        return true;
    }
}
