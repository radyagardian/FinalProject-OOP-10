package com.finpro.kel10.Backend.controller;

import com.finpro.kel10.Backend.model.Score;
import com.finpro.kel10.Backend.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PostMapping
    public ResponseEntity<?> createScore(@RequestBody Score score){
        try{
            Score createdScore = scoreService.createScore(score);
            return new ResponseEntity<>(createdScore, HttpStatus.CREATED);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Score>> getAllScores() {
        List<Score> scores = scoreService.getAllScores();
        return ResponseEntity.ok(scores);
    }

    // BUG FIX: Method ini sebelumnya melakukan DELETE, sekarang sudah benar (GET)
    @GetMapping("/{scoreId}")
    public ResponseEntity<?> getScoreById(@PathVariable UUID scoreId){
        Optional<Score> score = scoreService.getScoreById(scoreId);
        if (score.isPresent()) {
            return ResponseEntity.ok(score.get());
        } else {
            return new ResponseEntity<>("Score not found", HttpStatus.NOT_FOUND);
        }
    }

    // Method baru untuk menghapus Score berdasarkan ID
    @DeleteMapping("/{scoreId}")
    public ResponseEntity<?> deleteScore(@PathVariable UUID scoreId) {
        try {
            scoreService.deleteScore(scoreId);
            return ResponseEntity.ok("Score with ID " + scoreId + " deleted successfully.");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<Score>> getScoresByPlayerId(@PathVariable UUID playerId) {
        List<Score> scores = scoreService.getScoresByPlayerId(playerId);
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/player/{playerId}/ordered")
    public ResponseEntity<List<Score>> getScoresByPlayerIdOrdered(@PathVariable UUID playerId) {
        List<Score> scores = scoreService.getScoresByPlayerIdOrderByValue(playerId);
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<Score>> getLeaderboard(@RequestParam(defaultValue = "10") int limit) {
        List<Score> leaderboard = scoreService.getLeaderboard(limit);
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("/above/{minValue}")
    public ResponseEntity<List<Score>> getScoresAboveValue(@PathVariable Integer minValue) {
        List<Score> scores = scoreService.getScoresAboveValue(minValue);
        return ResponseEntity.ok(scores);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Score>> getRecentScores() {
        List<Score> scores = scoreService.getRecentScores();
        return ResponseEntity.ok(scores);
    }

    // --- ENDPOINT STATISTIK (ZOMBIE & WAVE) ---

    @GetMapping("/player/{playerId}/total-zombies")
    public ResponseEntity<?> getTotalZombiesByPlayerId(@PathVariable UUID playerId) {
        Integer total = scoreService.getTotalZombiesKilledByPlayerId(playerId);
        Map<String, Integer> response = Collections.singletonMap("totalZombiesKilled", total);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/player/{playerId}")
    public ResponseEntity<?> deleteScoresByPlayerId(@PathVariable UUID playerId) {
        scoreService.deleteScoresByPlayerId(playerId);
        return ResponseEntity.ok("All scores for player " + playerId + " deleted successfully.");
    }
}