package com.avanade.ebr.services;

import com.avanade.ebr.dto.InputPlayer;
import com.avanade.ebr.dto.ResponseWhoPlaysNow;
import com.avanade.ebr.entities.Game;
import com.avanade.ebr.entities.Move;
import com.avanade.ebr.entities.Player;
import com.avanade.ebr.entities.Turn;
import com.avanade.ebr.enums.GameState;
import com.avanade.ebr.enums.MoveType;
import com.avanade.ebr.exceptions.InvalidInputException;
import com.avanade.ebr.exceptions.ResourceNotFoundException;
import com.avanade.ebr.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerService playerService;
    @Autowired
    private TurnService turnService;
    @Autowired
    private MoveService moveService;

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public Game findById(Long id) {
        return gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Game not found"));
    }

    public List<Move> disputeInitiative(Long gameId, Long playerId) {
        var game = findById(gameId);

        if (game.getState() != GameState.waitingToStart)
            throw new InvalidInputException("This game no longer accepts initiative. The game is already: " + game.getState());

        var player = game.getPlayer(playerId);
        var turn = turnService.lastOpenTurn(game);
        return moveService.initiative(turn, player);
    }

    public Player getInitiativeWinner(Long gameId) {
        var game = findById(gameId);
        if (game == null) throw new ResourceNotFoundException("Game not found");

        var turn = turnService.lastFinishedTurnInitiative(gameId);
        if (turn == null) return null;
        var moves = moveService.getMoves(turn);
        return playerService.getInitiativeWinner(moves);
    }

    public List<Move> getInitiatives(Long gameId) {
        var game = findById(gameId);
        if (game == null) throw new ResourceNotFoundException("Game not found");

        var turns = turnService.lastTurnInitiative(gameId);
        if (turns.size() == 0) return null;
        var turnsId = turns.stream().map(Turn::getId).collect(Collectors.toList());
        return moveService.getMoves(turnsId);
    }

    @Transactional
    public Game create(InputPlayer inputPlayer) {
        var game = new Game();

        game.setState(GameState.waitingToStart);
        game.setCreated(LocalDateTime.now());
        game = gameRepository.save(game);

        var player = playerService.createPlayer(game, inputPlayer);
        var enemy = playerService.createOpponent(game, inputPlayer);

        game.setPlayers(new ArrayList<>() {
            {
                add(player);
                add(enemy);
            }
        });

        turnService.create(game, null);
        return game;
    }

    public Move attack(Long gameId, Long playerId) {
        var game = findById(gameId);

        if (game.getState() != GameState.started)
            throw new InvalidInputException("This game does not accept attack. The game is already: " + game.getState());

        var turn = turnService.lastOpenTurn(gameId);
        var player = playerService.getById(playerId);
        return moveService.create(turn, player, MoveType.attack);
    }

    public Move defense(Long gameId, Long playerId) {
        var game = findById(gameId);

        if (game.getState() != GameState.started)
            throw new InvalidInputException("This game does not accept defense. The game is already: " + game.getState());

        var turn = turnService.lastOpenTurn(gameId);
        var player = playerService.getById(playerId);
        return moveService.create(turn, player, MoveType.defense);
    }

    public Game applydamage(Long gameId) {
        var game = findById(gameId);

        if (game.getState() != GameState.started)
            throw new InvalidInputException("This game does not accept damage application. The game is already: " + game.getState());

        var turn = turnService.lastOpenTurn(game);
        var moves = moveService.getMoves(turn);

        if (moves.size() < 2)
            throw new InvalidInputException("Waiting for all players to make their moves");

        var attack = moves.stream().filter(m -> m.getMoveType() == MoveType.attack).findFirst().get();
        var defense = moves.stream().filter(m -> m.getMoveType() == MoveType.defense).findFirst().get();

        var attacker = turn.getWhoStarts();
        var opponent = game.getPlayers().stream().filter(p -> !p.getId().equals(attacker.getId())).findFirst().get();
        var damageSuffered = (byte) 0;

        if (attack.getDiceValue() > defense.getDiceValue()) {
            damageSuffered = playerService.applyDamage(turn, attacker, opponent);
            if (opponent.isDead()) return finishGame(game, turn, damageSuffered);
        }

        turnService.saveDamage(turn, opponent, damageSuffered);

        return game;
    }

    public Game finishGame(Game game, Turn turn, byte damage) {
        turnService.saveDamage(turn, damage);
        game.setFinished(LocalDateTime.now());
        game.setState(GameState.finished);
        return gameRepository.save(game);
    }

    public ResponseWhoPlaysNow whoPlaysNow(Long gameId) {
        var game = findById(gameId);

        if (game.getState() == GameState.finished) throw new InvalidInputException("This game is already finished");
        if (game.getState() == GameState.waitingToStart) return new ResponseWhoPlaysNow(null, MoveType.initiative);

        var turn = turnService.lastOpenTurn(game);
        var moves = moveService.getMoves(turn);

        if (moves.size() == 0) {
            return new ResponseWhoPlaysNow(turn.getWhoStarts(), MoveType.attack);
        }

        if (moves.size() == 1) {
            var player = game.getPlayers().stream().filter(p -> !p.getId().equals(turn.getWhoStarts().getId())).findFirst().get();
            return new ResponseWhoPlaysNow(player, MoveType.defense);
        }

        return new ResponseWhoPlaysNow(null, MoveType.damage);
    }

    public List<Turn> turns(Long gameId) {
        findById(gameId);
        return turnService.findByGameId(gameId);
    }

    public List<Move> moves(Long gameId) {
        findById(gameId);
        return moveService.findByGameId(gameId);
    }
}
