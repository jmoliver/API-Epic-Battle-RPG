package com.avanade.ebr.entities;

import com.avanade.ebr.enums.GameState;
import com.avanade.ebr.exceptions.InvalidInputException;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game")
@Entity
public class Game implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private GameState state;
    @JsonManagedReference
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Player> players;
    private LocalDateTime created;
    private LocalDateTime finished;

    public Player getPlayer(Long playerId) {
        return players.stream().filter(p -> p.getId().equals(playerId)).findFirst().orElseThrow(() -> new InvalidInputException("Player not found"));
    }
}
