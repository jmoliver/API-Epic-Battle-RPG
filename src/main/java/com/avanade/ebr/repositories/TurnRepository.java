package com.avanade.ebr.repositories;

import com.avanade.ebr.entities.Game;
import com.avanade.ebr.entities.Turn;
import com.avanade.ebr.enums.TurnState;
import com.avanade.ebr.enums.TurnType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurnRepository extends JpaRepository<Turn, Long> {
    Turn findTop1ByGameOrderByCreatedDesc(Game game);

    Turn findTop1ByGame_IdAndTypeAndStateOrderByCreatedDesc(Long gameId, TurnType type, TurnState state);

    List<Turn> findByGame_IdOrderByCreatedAsc(Long gameId);

    List<Turn> findByGame_IdAndTypeOrderByCreatedAsc(Long gameId, TurnType type);
}
