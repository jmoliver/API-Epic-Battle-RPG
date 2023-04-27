package com.avanade.ebr.repositories;

import com.avanade.ebr.entities.Move;
import com.avanade.ebr.entities.Player;
import com.avanade.ebr.entities.Turn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {
    List<Move> findByTurn(Turn turn);

    List<Move> findByTurnIdIn(List<Long> turn);

    List<Move> findByTurnAndPlayer(Turn turn, Player player);
}
