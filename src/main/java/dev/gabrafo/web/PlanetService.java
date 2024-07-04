package dev.gabrafo.web;

import dev.gabrafo.domain.Planet;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlanetService {

    private final PlanetRepository planetRepository;

    // Injeção de dependência por construtor
    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    public Planet create(Planet planet) {
        return planetRepository.save(planet);
    }

    public Optional<Planet> get(Long id) {
        return planetRepository.findById(id);
    }
}
