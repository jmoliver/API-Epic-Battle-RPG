package com.avanade.ebr.controllers;

import com.avanade.ebr.dto.InputMove;
import com.avanade.ebr.dto.InputPlayer;
import com.avanade.ebr.dto.ResponseWhoPlaysNow;
import com.avanade.ebr.entities.Game;
import com.avanade.ebr.entities.Move;
import com.avanade.ebr.entities.Player;
import com.avanade.ebr.entities.Turn;
import com.avanade.ebr.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping("/game")
    public ResponseEntity<List<Game>> get() {
        return new ResponseEntity<>(gameService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/game")
    public ResponseEntity<Game> post(@RequestBody InputPlayer inputPlayer) {
        return new ResponseEntity<>(gameService.create(inputPlayer), HttpStatus.OK);
    }

    @GetMapping("/game/{gameId}/initiative")
    public ResponseEntity<List<Move>> initiative(@PathVariable(name = "gameId") Long gameId) {
        return new ResponseEntity<>(gameService.getInitiatives(gameId), HttpStatus.OK);
    }

    @PostMapping("/game/{gameId}/initiative")
    public ResponseEntity<List<Move>> initiative(@PathVariable(name = "gameId") Long gameId, @RequestBody InputMove inputMove) {
        return new ResponseEntity<>(gameService.disputeInitiative(gameId, inputMove.getPlayerId()), HttpStatus.OK);
    }

    @GetMapping("/game/{gameId}/initiative/winner")
    public ResponseEntity<Player> initiativeWinner(@PathVariable(name = "gameId") Long gameId) {
        return new ResponseEntity<>(gameService.getInitiativeWinner(gameId), HttpStatus.OK);
    }

    @GetMapping("/game/{gameId}/whoplaysnow")
    public ResponseEntity<ResponseWhoPlaysNow> whoplaysnow(@PathVariable(name = "gameId") Long gameId) {
        return new ResponseEntity<>(gameService.whoPlaysNow(gameId), HttpStatus.OK);
    }

    @PostMapping("/game/{gameId}/attack")
    public ResponseEntity<Move> attack(@PathVariable(name = "gameId") Long gameId, @RequestBody InputMove inputMove) {
        return new ResponseEntity<>(gameService.attack(gameId, inputMove.getPlayerId()), HttpStatus.OK);
    }

    @PostMapping("/game/{gameId}/defense")
    public ResponseEntity<Move> defense(@PathVariable(name = "gameId") Long gameId, @RequestBody InputMove inputMove) {
        return new ResponseEntity<>(gameService.defense(gameId, inputMove.getPlayerId()), HttpStatus.OK);
    }

    @PostMapping("/game/{gameId}/applydamage")
    public ResponseEntity<Game> damage(@PathVariable(name = "gameId") Long gameId) {
        return new ResponseEntity<>(gameService.applydamage(gameId), HttpStatus.OK);
    }

    @PostMapping("/game/{gameId}/turns")
    public ResponseEntity<List<Turn>> turns(@PathVariable(name = "gameId") Long gameId) {
        return new ResponseEntity<>(gameService.turns(gameId), HttpStatus.OK);
    }

    @PostMapping("/game/{gameId}/moves")
    public ResponseEntity<List<Move>> moves(@PathVariable(name = "gameId") Long gameId) {
        return new ResponseEntity<>(gameService.moves(gameId), HttpStatus.OK);
    }
}
