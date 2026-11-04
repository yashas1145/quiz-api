package in.ybuilds.quiz.controller;

import in.ybuilds.quiz.model.Question;
import in.ybuilds.quiz.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<?> getQuestions(@RequestParam(required = false) String topic) {
        List<Question> questionList = topic == null || topic.isBlank()
                ? questionService.getQuestions()
                : questionService.getQuestionsByTopic(topic);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "count", questionList.size(),
                "questions", questionList
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable int id) {
        if(questionService.questionExistsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", String.format("Question with ID %d not found", id)
            ));
        }

        Question question = questionService.getQuestionById(id);

        if(question == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Failed to fetch question"
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "question", question
        ));
    }

    @PostMapping
    public ResponseEntity<?> addQuestion(@RequestBody Question question) {
        Question savedQuestion = questionService.addQuestion(question);

        if(savedQuestion == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Failed to save question"
            ));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "question", savedQuestion
        ));
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> addQuestions(@RequestBody List<Question> questionList) {
        List<Question> savedQuestionList = questionService.addQuestions(questionList);

        if(savedQuestionList == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Failed to save questions"
            ));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "count", savedQuestionList.size(),
                "questions", savedQuestionList
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable int id, @RequestBody Question question) {
        if(questionService.questionExistsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", String.format("Question with ID %d not found", id)
            ));
        }

        Question updatedQuestion = questionService.updateQuestion(id, question);

        if(updatedQuestion == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Failed to update question"
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "question", updatedQuestion
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestionById(@PathVariable int id) {
        if(questionService.questionExistsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", String.format("Question with ID %d not found", id)
            ));
        }

        if(!questionService.deleteQuestion(id)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Failed to delete question"
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message", String.format("Deleted question with ID %d", id)
        ));
    }
}
