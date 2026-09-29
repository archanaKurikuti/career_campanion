package com.example.career_companion.service;

import com.example.career_companion.entity.Skill;
import com.example.career_companion.exception.ResourceNotFoundException;
import com.example.career_companion.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Skill getSkillById(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));
    }

    public Skill createSkill(String name) {
        return skillRepository.findByNameIgnoreCase(name.trim())
                .orElseGet(() -> {
                    Skill s = new Skill();
                    s.setName(name.trim());
                    return skillRepository.save(s);
                });
    }
}
