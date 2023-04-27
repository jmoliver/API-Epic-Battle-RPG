package com.avanade.ebr.repositories;

import com.avanade.ebr.entities.Character;
import com.avanade.ebr.enums.CharacterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    List<Character> findByCharacterType(CharacterType characterType);

    Character findByCharacterTypeAndId(CharacterType characterType, Long id);
}
