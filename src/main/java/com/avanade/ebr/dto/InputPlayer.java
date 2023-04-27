package com.avanade.ebr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputPlayer {
    private String name;
    private Long chosenCharacterId;
    private Long enemyCharacterId;
}
