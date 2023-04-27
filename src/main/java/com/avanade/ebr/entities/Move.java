package com.avanade.ebr.entities;

import com.avanade.ebr.enums.MoveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "move")
@Entity
public class Move implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private byte diceValue;
    @ManyToOne(cascade = CascadeType.ALL)
    private Turn turn;
    @ManyToOne(cascade = CascadeType.ALL)
    private Player player;
    private MoveType moveType;
    private LocalDateTime created;
}
