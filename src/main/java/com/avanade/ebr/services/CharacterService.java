package com.avanade.ebr.services;

import com.avanade.ebr.entities.Character;
import com.avanade.ebr.enums.CharacterType;
import com.avanade.ebr.exceptions.ResourceNotFoundException;
import com.avanade.ebr.repositories.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    public List<Character> getAll() {
        return characterRepository.findAll();
    }

    public Character findById(Long id) {
        return characterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Character not found"));
    }

    public Character findCharacterTypeById(CharacterType characterType, Long id) {
        return characterRepository.findByCharacterTypeAndId(characterType, id);
    }

    public Character getRandomMonster() {
        var monsters = characterRepository.findByCharacterType(CharacterType.monster);
        var amount = monsters.size();
        var index = (int) (Math.random() * amount);
        return monsters.get(index);
    }

    public Character create(Character character) {
        if (characterRepository.existsById(character.getId()))
            throw new ResourceNotFoundException("The character already exists");

        return characterRepository.save(character);
    }

    public Character update(Character character) {
        characterRepository.findById(character.getId()).orElseThrow(() -> new ResourceNotFoundException("Character not found"));
        return characterRepository.save(character);
    }

    public void delete(Long characterId) {
        characterRepository.findById(characterId).orElseThrow(() -> new ResourceNotFoundException("Character not found"));
        characterRepository.deleteById(characterId);
    }
}
