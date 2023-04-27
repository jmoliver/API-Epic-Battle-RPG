package com.avanade.ebr.services;

import com.avanade.ebr.entities.Move;
import com.avanade.ebr.entities.Player;
import com.avanade.ebr.entities.Turn;
import com.avanade.ebr.enums.MoveType;
import com.avanade.ebr.exceptions.InvalidInputException;
import com.avanade.ebr.repositories.MoveRepository;
import com.avanade.ebr.utils.Dice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.avanade.ebr.utils.Dice.d12;
import static com.avanade.ebr.utils.Dice.d20;

@Service
public class MoveService {
    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private TurnService turnService;

    public List<Move> getMoves(Turn turn) {
        return moveRepository.findByTurn(turn);
    }

    public List<Move> getMoves(List<Long> turnsId) {
        return moveRepository.findByTurnIdIn(turnsId);
    }

    public List<Move> initiative(Turn turn, Player player) {
        var moves = moveRepository.findByTurn(turn);
        var playerMove = moves.stream().anyMatch(m -> m.getPlayer().equals(player));
        if (playerMove) throw new InvalidInputException("You have already made your move on this initiative turn.");

        var diceValue = Dice.Random(d20);
        var move = new Move();
        move.setTurn(turn);
        move.setPlayer(player);
        move.setMoveType(MoveType.initiative);
        move.setCreated(LocalDateTime.now());
        move.setDiceValue(diceValue);
        move = moveRepository.save(move);
        moves.add(move);

        if (moves.size() == 2) turnService.evaluateInitiatives(turn, moves);

        return moves;
    }

    public Move create(Turn turn, Player player, MoveType moveType) {
        var moves = moveRepository.findByTurn(turn);

        if (moves.size() == 0) {
            if (!turn.getWhoStarts().getId().equals(player.getId()))
                throw new InvalidInputException("It's not your turn to attack");

            if (moveType == MoveType.defense)
                throw new InvalidInputException("It's your turn to attack");

        } else {
            var playerMove = moves.stream().anyMatch(m -> m.getPlayer().getId().equals(player.getId()));
            if (playerMove) throw new InvalidInputException("You have already made your move on this turn.");

            if (moveType == MoveType.attack)
                throw new InvalidInputException("It's not your turn to attack");
        }

        var move = new Move();
        move.setTurn(turn);
        move.setPlayer(player);
        move.setMoveType(moveType);
        move.setCreated(LocalDateTime.now());
        move.setDiceValue(Dice.Random(d12));
        return moveRepository.save(move);
    }

    public List<Move> findByGameId(Long gameId) {
        var turns = turnService.findByGameId(gameId);
        var turnsId = turns.stream().map(Turn::getId).collect(Collectors.toList());
        return moveRepository.findByTurnIdIn(turnsId);
    }
}
