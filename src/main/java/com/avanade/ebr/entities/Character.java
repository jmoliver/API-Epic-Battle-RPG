package com.avanade.ebr.entities;

import com.avanade.ebr.enums.CharacterType;
import com.avanade.ebr.utils.Dice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "character")
@Entity
public class Character implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private CharacterType characterType;
    private String characterTypeName;
    private byte life;
    private byte strength;
    private byte defense;
    private byte agility;
    private byte sidesDice;
    private byte amountDice;

    public byte getAttack() {
        var valueDie = Dice.Random(Dice.d12);
        return (byte) (valueDie + strength + agility);
    }

    public byte getDefense() {
        var valueDie = Dice.Random(Dice.d12);
        return (byte) (valueDie + defense + agility);
    }
}
