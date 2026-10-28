package com.edumania.controller;

import com.edumania.documents.Student;
import com.edumania.documents.Faculty;
import com.edumania.documents.Question;
import com.edumania.documents.Quiz;
import com.edumania.documents.QuizAttempt;
import com.edumania.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class QuizController {

    @Autowired
    private QuizService quizService;

    
    
    
    
    @GetMapping("/student/quizzes")
    public String studentQuizzes(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("student");
        
        if (student == null) {
            return "redirect:/student-login";
        }
        
        
        List<Quiz> quizzes = quizService.getAllActiveQuizzes();
        
        model.addAttribute("student", student);
        model.addAttribute("quizzes", quizzes);
        
        return "quiz-list";
    }

    
    
    
    
    @GetMapping("/student/quiz/take/{quizId}")
    public String takeQuiz(
            @PathVariable String quizId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        Student student = (Student) session.getAttribute("student");
        
        if (student == null) {
            return "redirect:/student-login";
        }
        
        
        if (quizService.hasAttemptedQuiz(student.getId(), quizId)) {
            redirectAttributes.addFlashAttribute("error", "You have already attempted this quiz!");
            return "redirect:/student/quizzes";
        }
        
        
        Map<String, Object> quizData = quizService.getQuizWithQuestions(quizId);
        
        if (quizData == null) {
            redirectAttributes.addFlashAttribute("error", "Quiz not found!");
            return "redirect:/student/quizzes";
        }
        
        model.addAttribute("student", student);
        model.addAttribute("quiz", quizData.get("quiz"));
        model.addAttribute("questions", quizData.get("questions"));
        
        return "take-quiz";
    }

    
    
    
    
    @PostMapping("/student/quiz/submit")
    public String submitQuiz(
            @RequestParam String quizId,
            @RequestParam Map<String, String> allParams,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Student student = (Student) session.getAttribute("student");
        
        if (student == null) {
            return "redirect:/student-login";
        }
        
        try {
            
            Map<String, Integer> answers = new java.util.HashMap<>();
            
            for (Map.Entry<String, String> entry : allParams.entrySet()) {
                if (entry.getKey().startsWith("question_")) {
                    String questionId = entry.getKey().substring(9); 
                    try {
                        int selectedOption = Integer.parseInt(entry.getValue());
                        answers.put(questionId, selectedOption);
                    } catch (NumberFormatException e) {
                        
                    }
                }
            }
            
            
            QuizAttempt attempt = quizService.submitQuiz(student.getId(), quizId, answers);
            
            redirectAttributes.addFlashAttribute("attemptId", attempt.getId());
            return "redirect:/student/quiz/result/" + attempt.getId();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to submit quiz: " + e.getMessage());
            return "redirect:/student/quizzes";
        }
    }

    
    
    
    
    @GetMapping("/student/quiz/result/{attemptId}")
    public String quizResult(
            @PathVariable @NonNull String attemptId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        Student student = (Student) session.getAttribute("student");
        
        if (student == null) {
            return "redirect:/student-login";
        }
        
        
        Optional<QuizAttempt> attemptOptional = quizService.getQuizAttemptById(attemptId);
        
        if (attemptOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Result not found!");
            return "redirect:/student/quizzes";
        }
        
        QuizAttempt attempt = attemptOptional.get();
        
        
        if (!attempt.getStudentId().equals(student.getId())) {
            return "redirect:/student/quizzes";
        }
        
        model.addAttribute("student", student);
        model.addAttribute("attempt", attempt);
        
        return "quiz-result";
    }
    




@GetMapping("/faculty/quizzes")
public String facultyQuizzes(HttpSession session, Model model) {
    Faculty faculty = (Faculty) session.getAttribute("faculty");
    
    if (faculty == null) {
        return "redirect:/faculty-login";
    }
    
    List<Quiz> quizzes = quizService.getAllActiveQuizzes();
    model.addAttribute("faculty", faculty);
    model.addAttribute("quizzes", quizzes);
    
    return "faculty-quizzes";
}


@GetMapping("/faculty/quiz/create")
public String createQuizForm(HttpSession session, Model model) {
    Faculty faculty = (Faculty) session.getAttribute("faculty");
    
    if (faculty == null) {
        return "redirect:/faculty-login";
    }
    
    model.addAttribute("faculty", faculty);
    model.addAttribute("quiz", new Quiz());
    
    return "faculty-create-quiz";
}


@PostMapping("/faculty/quiz/save")
public String saveQuiz(
        @RequestParam String title,
        @RequestParam String description,
        @RequestParam String courseId,
        @RequestParam int duration,
        @RequestParam int passingScore,
        @RequestParam String difficulty,
        HttpSession session,
        RedirectAttributes redirectAttributes) {
    
    Faculty faculty = (Faculty) session.getAttribute("faculty");
    
    if (faculty == null) {
        return "redirect:/faculty-login";
    }
    
    try {
        
        long count = quizService.getAllActiveQuizzes().size() + 1;
        String quizId = "QUIZ-" + String.format("%03d", count);
        
        Quiz quiz = new Quiz();
        quiz.setQuizId(quizId);
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setCourseId(courseId);
        quiz.setDuration(duration);
        quiz.setPassingScore(passingScore);
        quiz.setDifficulty(difficulty);
        quiz.setTotalQuestions(0);
        quiz.setActive(true);
        quiz.setStartDate(LocalDateTime.now());
        quiz.setEndDate(LocalDateTime.now().plusDays(30));
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUpdatedAt(LocalDateTime.now());
        quiz.setQuestionIds(new ArrayList<>());
        
        Quiz savedQuiz = quizService.saveQuiz(quiz);
        
        redirectAttributes.addFlashAttribute("success", "Quiz created successfully! Quiz ID: " + quizId);
        return "redirect:/faculty/quiz/add-questions/" + savedQuiz.getId();
        
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Failed to create quiz: " + e.getMessage());
        return "redirect:/faculty/quiz/create";
    }
}


@GetMapping("/faculty/quiz/add-questions/{quizId}")
public String addQuestionsForm(
        @PathVariable @NonNull String quizId,
        HttpSession session,
        Model model,
        RedirectAttributes redirectAttributes) {
    
    Faculty faculty = (Faculty) session.getAttribute("faculty");
    
    if (faculty == null) {
        return "redirect:/faculty-login";
    }
    
    Optional<Quiz> quizOptional = quizService.getQuizById(quizId);
    if (quizOptional.isEmpty()) {
        redirectAttributes.addFlashAttribute("error", "Quiz not found!");
        return "redirect:/faculty/quizzes";
    }
    
    Quiz quiz = quizOptional.get();
    List<Question> questions = quizService.getQuestionsForQuiz(quiz.getQuizId());
    
    model.addAttribute("faculty", faculty);
    model.addAttribute("quiz", quiz);
    model.addAttribute("questions", questions);
    
    return "faculty-add-questions";
}


@PostMapping("/faculty/quiz/add-question")
public String addQuestion(
        @RequestParam @NonNull String quizId,
        @RequestParam String questionText,
        @RequestParam List<String> options,
        @RequestParam int correctOptionIndex,
        @RequestParam int marks,
        @RequestParam(required = false) String explanation,
        HttpSession session,
        RedirectAttributes redirectAttributes) {
    
    Faculty faculty = (Faculty) session.getAttribute("faculty");
    
    if (faculty == null) {
        return "redirect:/faculty-login";
    }
    
    try {
        
        Optional<Quiz> quizOptional = quizService.getQuizById(quizId);
        if (quizOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Quiz not found!");
            return "redirect:/faculty/quizzes";
        }
        
        Quiz quiz = quizOptional.get();
        
        
        long questionCount = quizService.getQuestionsForQuiz(quiz.getQuizId()).size() + 1;
        String questionId = "Q-" + String.format("%03d", questionCount);
        
        Question question = new Question();
        question.setQuestionId(questionId);
        question.setQuizId(quiz.getQuizId());
        question.setQuestionText(questionText);
        question.setOptions(options);
        question.setCorrectOptionIndex(correctOptionIndex);
        question.setMarks(marks);
        question.setExplanation(explanation);
        question.setDifficulty(quiz.getDifficulty());
        question.setCategory("MCQ");
        
        Question savedQuestion = quizService.saveQuestion(question);
        
        
        List<String> questionIds = quiz.getQuestionIds();
        if (questionIds == null) {
            questionIds = new ArrayList<>();
        }
        questionIds.add(savedQuestion.getId());
        quiz.setQuestionIds(questionIds);
        quiz.setTotalQuestions(questionIds.size());
        quiz.setUpdatedAt(LocalDateTime.now());
        quizService.updateQuiz(quiz);
        
        redirectAttributes.addFlashAttribute("success", "Question added successfully!");
        
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Failed to add question: " + e.getMessage());
    }
    
    return "redirect:/faculty/quiz/add-questions/" + quizId;
}


@PostMapping("/faculty/quiz/delete-question")
public String deleteQuestion(
        @RequestParam @NonNull String questionId,
        @RequestParam @NonNull String quizId,
        HttpSession session,
        RedirectAttributes redirectAttributes) {
    
    Faculty faculty = (Faculty) session.getAttribute("faculty");
    
    if (faculty == null) {
        return "redirect:/faculty-login";
    }
    
    try {
        
        Optional<Quiz> quizOptional = quizService.getQuizById(quizId);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            List<String> questionIds = quiz.getQuestionIds();
            if (questionIds != null) {
                questionIds.remove(questionId);
                quiz.setQuestionIds(questionIds);
                quiz.setTotalQuestions(questionIds.size());
                quiz.setUpdatedAt(LocalDateTime.now());
                quizService.updateQuiz(quiz);
            }
        }
        
        
        quizService.deleteQuestion(questionId);
        
        redirectAttributes.addFlashAttribute("success", "Question deleted successfully!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Failed to delete question: " + e.getMessage());
    }
    
    return "redirect:/faculty/quiz/add-questions/" + quizId;
}


@PostMapping("/faculty/quiz/delete")
public String deleteQuiz(
        @RequestParam @NonNull String quizId,
        HttpSession session,
        RedirectAttributes redirectAttributes) {
    
    Faculty faculty = (Faculty) session.getAttribute("faculty");
    
    if (faculty == null) {
        return "redirect:/faculty-login";
    }
    
    try {
        
        List<Question> questions = quizService.getQuestionsForQuiz(quizId);
        for (Question q : questions) {
            quizService.deleteQuestion(java.util.Objects.requireNonNull(
                    q.getId(), "Saved quiz question must have an ID"));
        }
        
        
        quizService.deleteQuiz(quizId);
        
        redirectAttributes.addFlashAttribute("success", "Quiz deleted successfully!");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Failed to delete quiz: " + e.getMessage());
    }
    
    return "redirect:/faculty/quizzes";
}

    
    
    
    
    @GetMapping("/student/quiz/history")
    public String quizHistory(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("student");
        
        if (student == null) {
            return "redirect:/student-login";
        }
        
        
        List<QuizAttempt> attempts = quizService.getStudentQuizAttempts(student.getId());
        
        model.addAttribute("student", student);
        model.addAttribute("attempts", attempts);
        
        return "quiz-history";
    }
}