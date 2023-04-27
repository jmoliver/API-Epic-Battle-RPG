package com.avanade.ebr.controllers;

import com.avanade.ebr.entities.Character;
import com.avanade.ebr.services.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
public class CharacterController {
    @Autowired
    private CharacterService characterService;

    @GetMapping("/character")
    public ResponseEntity<List<Character>> getAll() {
        return new ResponseEntity<>(characterService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/character/{characterId}")
    public ResponseEntity<Character> getAll(@PathVariable(name = "characterId") Long id) {
        return new ResponseEntity<>(characterService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/character")
    public ResponseEntity<Character> post(@RequestBody Character character) {
        return new ResponseEntity<>(characterService.create(character), HttpStatus.OK);
    }

    @PutMapping("/character")
    public ResponseEntity<Character> put(@RequestBody Character character) {
        return new ResponseEntity<>(characterService.update(character), HttpStatus.OK);
    }

    @DeleteMapping("/character/{characterId}")
    public ResponseEntity<Void> delete(@PathVariable(name = "characterId") Long characterId) {
        characterService.delete(characterId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
