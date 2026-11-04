package in.ybuilds.quiz.service;

import in.ybuilds.quiz.dao.QuizDao;
import in.ybuilds.quiz.dto.QuestionDto;
import in.ybuilds.quiz.dto.QuizRequestDto;
import in.ybuilds.quiz.dto.QuizResponseDto;
import in.ybuilds.quiz.model.Question;
import in.ybuilds.quiz.model.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuestionService questionService;
    private final QuizDao quizDao;

    public Quiz createQuiz(String topic, int count, String title) {
        List<Question> questionList = questionService.getQuestionsByTopic(topic);
        List<Question> questions = new ArrayList<>();
        for(int i=0; i<count; i++) {
            questions.add(questionList.get((int) (Math.random() * questionList.size())));
        }

        Quiz quiz = Quiz.builder()
                .title(title)
                .questionList(questions)
                .build();

        quizDao.save(quiz);
        return quiz;
    }

    public QuizResponseDto getQuizById(int id) {
        Quiz quiz = quizDao.findById(id).orElse(null);

        if(quiz == null) {
            return null;
        }

        List<QuestionDto> questionList = quiz.getQuestionList()
                .stream()
                .map(q -> QuestionDto.builder()
                        .id(q.getId())
                        .option1(q.getOption1())
                        .option2(q.getOption2())
                        .option3(q.getOption3())
                        .option4(q.getOption4())
                        .build()).toList();

        return QuizResponseDto.builder()
                .title(quiz.getTitle())
                .questionList(questionList)
                .build();
    }

    public int computeResult(QuizRequestDto quizRequestDto) {
        try {
            return quizRequestDto.getAnswerList().stream()
                    .filter(q -> questionService.getQuestionById(q.getId()).getAnswer().equals(q.getAnswer()))
                    .toList().size();
        } catch (Exception e) {
            return -1;
        }
    }
}
