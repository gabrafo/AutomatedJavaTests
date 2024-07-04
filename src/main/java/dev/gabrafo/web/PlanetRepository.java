package dev.gabrafo.web;

import dev.gabrafo.domain.Planet;
import org.springframework.data.repository.CrudRepository;

public interface PlanetRepository extends CrudRepository<Planet, Long> {
}
