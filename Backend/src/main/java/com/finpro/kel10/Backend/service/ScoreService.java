package com.finpro.kel10.Backend.service;

import com.finpro.kel10.Backend.model.Score;
import com.finpro.kel10.Backend.repository.ScoreRepository;
import com.finpro.kel10.Backend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    @Transactional
    public Score createScore(Score score) {
        if (!playerRepository.existsById(score.getPlayerId())) {
            throw new RuntimeException("Player not found with ID: " + score.getPlayerId());
        }

        Score savedScore = scoreRepository.save(score);
        playerService.updatePlayerStats(savedScore);

        return savedScore;
    }

    public Optional<Score> getScoreById(UUID scoreId) {
        return scoreRepository.findById(scoreId);
    }

    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    public List<Score> getScoresByPlayerId(UUID playerId) {
        return scoreRepository.findByPlayerId(playerId);
    }

    public List<Score> getScoresByPlayerIdOrderByValue(UUID playerId) {
        return scoreRepository.findByPlayerIdOrderByValueDesc(playerId);
    }

    public List<Score> getLeaderboard(int limit) {
        List<Score> topScores = scoreRepository.findTopScores();
        if (limit > 0 && topScores.size() > limit) {
            return topScores.subList(0, limit);
        }
        return topScores;
    }

    public List<Score> getHighScoreByPlayerId(UUID playerId){
        return scoreRepository.findHighestScoreByPlayerId(playerId);
    }

    public List<Score> getScoresAboveValue(Integer minValue){
        return scoreRepository.findByValueGreaterThan(minValue);
    }

    public List<Score> getRecentScores(){
        return scoreRepository.findAllByOrderByCreatedAtDesc();
    }

    public Integer getTotalZombiesKilledByPlayerId(UUID playerId){
        Integer total = scoreRepository.getTotalZombiesKilledByPlayerId(playerId);
        return total != null ? total : 0;
    }


    public Score updateScore(UUID scoreId, Score updatedScore){
        Score existingScore = scoreRepository.findById(scoreId)
                .orElseThrow(() -> new RuntimeException("Score tidak ditemukan!"));

        if(updatedScore.getValue() != null) existingScore.setValue(updatedScore.getValue());
        if(updatedScore.getZombiesKilled() != null) existingScore.setZombiesKilled(updatedScore.getZombiesKilled());

        // Bagian Wave Dihapus

        return scoreRepository.save(existingScore);
    }

    public void deleteScoresByPlayerId(UUID playerID){
        List<Score> scores = scoreRepository.findByPlayerId(playerID);
        scoreRepository.deleteAll(scores);
    }

    public void deleteScore(UUID scoreId) {
        if(scoreRepository.existsById(scoreId)) {
            scoreRepository.deleteById(scoreId);
        } else {
            throw new RuntimeException("Score ID not found");
        }
    }
}