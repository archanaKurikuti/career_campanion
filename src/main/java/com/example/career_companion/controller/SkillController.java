package com.example.career_companion.controller;

import com.example.career_companion.entity.Skill;
import com.example.career_companion.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@Tag(name = "Skills", description = "Skill lookup and management endpoints")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    @Operation(summary = "Get list of all technical skills")
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    @PostMapping
    @Operation(summary = "Add or get skill by name")
    public ResponseEntity<Skill> createSkill(@RequestParam String name) {
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.createSkill(name));
    }
}
