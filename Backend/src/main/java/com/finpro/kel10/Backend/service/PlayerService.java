package com.finpro.kel10.Backend.service;

import com.finpro.kel10.Backend.model.Player;
import com.finpro.kel10.Backend.model.Score;
import com.finpro.kel10.Backend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Player createPlayer(Player player) {
        if (playerRepository.existsByUsername(player.getUsername())) {
            throw new RuntimeException("Username already exists: " + player.getUsername());
        }
        return playerRepository.save(player);
    }

    public Optional<Player> getPlayerById(UUID playerId) {
        return playerRepository.findById(playerId);
    }

    public Optional<Player> getPlayerByUsername(String username) {
        return playerRepository.findByUsername(username);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Transactional
    public void updatePlayerStats(Score savedScore) {
        Player player = playerRepository.findById(savedScore.getPlayerId())
                .orElseThrow(() -> new RuntimeException("Player not found"));

        player.updateHighScore(savedScore.getValue());
        int kills = savedScore.getZombiesKilled() != null ? savedScore.getZombiesKilled() : 0;
        player.addZombiesKilled(kills);


        playerRepository.save(player);
    }

    public Player updatePlayer(UUID playerId, Player updatedPlayer) {
        Player existingPlayer = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with ID: " + playerId));

        if (updatedPlayer.getUsername() != null &&
                !updatedPlayer.getUsername().equals(existingPlayer.getUsername())) {
            existingPlayer.setUsername(updatedPlayer.getUsername());
        }
        return playerRepository.save(existingPlayer);
    }

    public void deletePlayer(UUID playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new RuntimeException("Player not found with ID: " + playerId);
        }
        playerRepository.deleteById(playerId);
    }

    public void deletePlayerByUsername(String username) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        playerRepository.delete(player);
    }

    public boolean isUsernameExist(String username) {
        return playerRepository.existsByUsername(username);
    }

    // Leaderboard Methods
    public List<Player> getLeaderboardByHighScore(int limit) {
        List<Player> allTop = playerRepository.findTopPlayersByHighScore(limit);
        if (allTop.size() > limit) {
            return allTop.subList(0, limit);
        }
        return allTop;
    }

    public List<Player> getLeaderboardByTotalZombiesKilled() {
        return playerRepository.findAllByOrderByTotalZombiesKilledDesc();
    }

}