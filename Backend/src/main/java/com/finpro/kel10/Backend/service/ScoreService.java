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
    private Object minValue;

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
    public Integer getTotalCoinsByPlayerId(UUID playerId){
        scoreRepository.getTotalCoinsByPlayerId(playerId);
        Integer total = scoreRepository.getTotalCoinsByPlayerId(playerId);
        return total;
    }
    public Integer getTotalDistanceByPlayerId(UUID playerId){
        scoreRepository.getTotalDistanceByPlayerId(playerId);
        Integer total = scoreRepository.getTotalDistanceByPlayerId(playerId);
        return total;
    }
    public Score updateScore(UUID scoreId, Score updatedScore){
        scoreRepository.findById(scoreId)
                .orElseThrow(() -> new RuntimeException("Score tidak ditemukan!"));
        Score existingScore = new Score();
        scoreRepository.save(existingScore);
        Score Score = null;
        return null;
    }
    public void deleteScoresByPlayerId(UUID playerID){
        scoreRepository.findByPlayerId(playerID);
        scoreRepository.deleteAll();
    }


    public void deleteScore(UUID scoreId) {
    }

    public Optional<Score> getHighestScoreByPlayerId(UUID playerId) {
        return Optional.empty();
    }
}

