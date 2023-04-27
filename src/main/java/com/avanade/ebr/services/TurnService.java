package com.avanade.ebr.services;

import com.avanade.ebr.entities.Game;
import com.avanade.ebr.entities.Move;
import com.avanade.ebr.entities.Player;
import com.avanade.ebr.entities.Turn;
import com.avanade.ebr.enums.GameState;
import com.avanade.ebr.enums.TurnState;
import com.avanade.ebr.enums.TurnType;
import com.avanade.ebr.exceptions.InvalidInputException;
import com.avanade.ebr.exceptions.ResourceNotFoundException;
import com.avanade.ebr.repositories.GameRepository;
import com.avanade.ebr.repositories.MoveRepository;
import com.avanade.ebr.repositories.TurnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnService {
    @Autowired
    private TurnRepository turnRepository;
    @Autowired
    private MoveRepository moveRepository;
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerService playerService;

    public Turn lastOpenTurn(Game game) {
        var turn = turnRepository.findTop1ByGameOrderByCreatedDesc(game);

        if (turn == null) {
            turn = new Turn(game);
            return turnRepository.save(turn);
        }

        if (turn.getState() == TurnState.finished) {
            var nextNumberTurn = turn.getNextNumber();
            var newTurn = new Turn(game);
            newTurn.setNumber(nextNumberTurn);
            turnRepository.save(newTurn);
        }

        return turn;
    }

    public Turn lastFinishedTurnInitiative(Long gameId) {
        return turnRepository.findTop1ByGame_IdAndTypeAndStateOrderByCreatedDesc(gameId, TurnType.initiative, TurnState.finished);
    }

    public List<Turn> lastTurnInitiative(Long gameId) {
        return turnRepository.findByGame_IdAndTypeOrderByCreatedAsc(gameId, TurnType.initiative);
    }

    public Turn lastOpenTurn(Long gameId) {
        var game = gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game not found"));
        return lastOpenTurn(game);
    }

    public Turn create(Game game, Player player) {
        if (game.getState() == GameState.finished)
            throw new InvalidInputException("Unable to create a turn for a finished game");

        var lastTurn = turnRepository.findTop1ByGameOrderByCreatedDesc(game);

        if (lastTurn == null) {
            var turn = new Turn(game);
            return turnRepository.save(turn);
        }

        if (lastTurn.getState() == TurnState.started) throw new InvalidInputException("There is still an open turn");

        var newTurn = new Turn(game);
        if (player != null) newTurn.setWhoStarts(player);
        newTurn.setNumber(lastTurn.getNextNumber());

        return turnRepository.save(newTurn);
    }

    public Turn update(Turn turn) {
        return turnRepository.save(turn);
    }

    public void evaluateInitiatives(Turn turn, List<Move> moves) {
        turn.setState(TurnState.finished);
        turnRepository.save(turn);

        var winner = playerService.getInitiativeWinner(moves);

        if (winner == null) {
            create(turn.getGame(), null);
            return;
        }

        var game = gameRepository.findById(turn.getId()).get();
        game.setState(GameState.started);
        gameRepository.save(game);

        create(game, winner);
    }

    public void saveDamage(Turn turn, byte damage) {
        turn.setDamage(damage);
        turn.setState(TurnState.finished);
        turnRepository.save(turn);
    }

    public void saveDamage(Turn turn, Player player, byte damage) {
        saveDamage(turn, damage);
        create(turn.getGame(), player);
    }

    public List<Turn> findByGameId(Long gameId) {
        return turnRepository.findByGame_IdOrderByCreatedAsc(gameId);
    }
}
