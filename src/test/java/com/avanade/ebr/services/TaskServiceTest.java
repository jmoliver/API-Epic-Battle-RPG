package com.avanade.ebr.services;

import com.avanade.ebr.entities.Character;
import com.avanade.ebr.repositories.CharacterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

class TaskServiceTest {

    @InjectMocks
    private CharacterService taskService;

    @Mock
    private CharacterRepository taskRepository;

    private List<Character> taskList;

    @BeforeEach
    void setUp() {
    }

    @Test
    void create() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }
}