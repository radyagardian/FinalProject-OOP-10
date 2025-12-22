package com.finpro.kel10.Backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "scores")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "score_id")
    private UUID scoreId;

    @Column(name = "player_id", nullable = false)
    private UUID playerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", insertable = false, updatable = false)
    private Player player;

    @Column(nullable = false)
    private Integer value;

    @Column(name = "zombies_killed")
    private Integer zombiesKilled = 0;

    // --- BAGIAN WAVE REACHED DIHAPUS DARI SINI ---

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Score() {}

    public Score(UUID playerId, Integer value, Integer zombiesKilled) {
        this.playerId = playerId;
        this.value = value;
        this.zombiesKilled = zombiesKilled;
    }

    // Getters and Setters
    public UUID getScoreId() { return scoreId; }
    public void setScoreId(UUID scoreId) { this.scoreId = scoreId; }

    public UUID getPlayerId() { return playerId; }
    public void setPlayerId(UUID playerId) { this.playerId = playerId; }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }

    public Integer getValue() { return value; }
    public void setValue(Integer value) { this.value = value; }

    public Integer getZombiesKilled() { return zombiesKilled; }
    public void setZombiesKilled(Integer zombiesKilled) { this.zombiesKilled = zombiesKilled; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}