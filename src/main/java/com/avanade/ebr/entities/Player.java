package com.avanade.ebr.entities;

import com.avanade.ebr.utils.Dice;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "player")
@Entity
public class Player implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private byte life;
    @ManyToOne(cascade = CascadeType.ALL)
    private Character character;
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    private Game game;
    private LocalDateTime created;

    public Player(Game game, Character character) {
        this.game = game;
        this.character = character;
        name = "Opponent";
        life = character.getLife();
        created = LocalDateTime.now();
    }

    @JsonIgnore
    public byte calculateDamage() {
        var valueDice = Dice.Random(character);
        return (byte) (valueDice + character.getStrength());
    }

    @JsonIgnore
    public byte takeDamage(byte damage) {
        life -= damage;
        return life;
    }

    @JsonIgnore
    public boolean isDead() {
        return life <= 0;
    }
}
