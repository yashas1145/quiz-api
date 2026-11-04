package in.ybuilds.quiz.controller;

import in.ybuilds.quiz.dto.QuizRequestDto;
import in.ybuilds.quiz.dto.QuizResponseDto;
import in.ybuilds.quiz.model.Quiz;
import in.ybuilds.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes")
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/create")
    public ResponseEntity<?> createQuiz(@RequestParam String topic, @RequestParam int count, @RequestParam String title) {
        Quiz quiz = quizService.createQuiz(topic, count, title);

        if(quiz == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Failed to create quiz"
            ));
        }

        if(quiz.getQuestionList().isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Failed to add questions to quiz"
            ));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "quiz", quiz
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizById(@PathVariable int id) {
        QuizResponseDto quizDto = quizService.getQuizById(id);

        if(quizDto == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Failed to get quiz"
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "quiz", quizDto
        ));
    }

    @PostMapping("/compute")
    public ResponseEntity<?> computeResults(@RequestBody QuizRequestDto quizRequestDto) {
        int score = quizService.computeResult(quizRequestDto);

        if(score == -1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Failed to compute score"
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "score", score
        ));
    }
}
