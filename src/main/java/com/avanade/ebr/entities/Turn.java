package com.avanade.ebr.entities;

import com.avanade.ebr.enums.GameState;
import com.avanade.ebr.enums.TurnState;
import com.avanade.ebr.enums.TurnType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "turn")
@Entity
public class Turn implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter(AccessLevel.NONE)
    private byte number = 1;
    @ManyToOne(cascade = CascadeType.ALL)
    private Game game;
    private byte damage;
    private TurnType type;
    private TurnState state;
    private LocalDateTime created;
    @ManyToOne(cascade = CascadeType.ALL)
    private Player whoStarts;

    public Turn(Game game) {
        this.game = game;
        this.type = game.getState() == GameState.waitingToStart ? TurnType.initiative : TurnType.inGame;
        this.created = LocalDateTime.now();
        this.state = TurnState.started;
    }

    public byte getNumber() {
        return (byte) (number == 0 ? 1 : number);
    }

    public byte getNextNumber() {
        return (byte) (getNumber() + 1);
    }
}
