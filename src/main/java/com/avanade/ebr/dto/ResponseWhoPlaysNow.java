package com.avanade.ebr.dto;

import com.avanade.ebr.entities.Player;
import com.avanade.ebr.enums.MoveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWhoPlaysNow {
    private Player player;
    private MoveType move;
}
