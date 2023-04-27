package com.avanade.ebr.services;

import com.avanade.ebr.dto.InputPlayer;
import com.avanade.ebr.entities.Game;
import com.avanade.ebr.entities.Move;
import com.avanade.ebr.entities.Player;
import com.avanade.ebr.entities.Turn;
import com.avanade.ebr.enums.CharacterType;
import com.avanade.ebr.exceptions.ResourceNotFoundException;
import com.avanade.ebr.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private CharacterService characterService;


    public Player getById(Long playerId) {
        return playerRepository.findById(playerId).orElseThrow(() -> new ResourceNotFoundException("Player not found"));
    }

    public Player getInitiativeWinner(List<Move> moves) {
        var diceValue1 = moves.get(0);
        var diceValue2 = moves.get(1);

        if (diceValue1 == diceValue2) return null;

        return moves.stream().max(Comparator.comparing(Move::getDiceValue)).get().getPlayer();
    }

    public Player createPlayer(Game game, InputPlayer inputPlayer) {
        var character = characterService.findById(inputPlayer.getChosenCharacterId());
        var player = new Player(game, character);
        player.setName(inputPlayer.getName());
        return playerRepository.save(player);
    }

    public Player createOpponent(Game game, InputPlayer inputPlayer) {
        var character = inputPlayer.getEnemyCharacterId() == null
                ? characterService.getRandomMonster()
                : characterService.findCharacterTypeById(CharacterType.monster, inputPlayer.getEnemyCharacterId());
        var player = new Player(game, character);
        return playerRepository.save(player);
    }

    public byte applyDamage(Turn turn, Player attacker, Player opponent) {
        var damage = attacker.calculateDamage();
        opponent.takeDamage(damage);
        playerRepository.save(opponent);
        return damage;
    }
}
