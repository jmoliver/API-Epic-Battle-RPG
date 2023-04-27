package com.avanade.ebr.utils;

import com.avanade.ebr.entities.Character;

import java.util.Random;

public class Dice {
    public static final byte d12 = 12;
    public static final byte d20 = 20;

    public static byte Random(byte sidesDie) {
        var random = new Random();
        return (byte) (random.nextInt(sidesDie) + 1);
    }

    public static byte Random(Character character) {
        byte values = 0;

        for (var i = 0; i < character.getAmountDice(); i++) {
            values += Random(character.getSidesDice());
        }

        return values;
    }
}
