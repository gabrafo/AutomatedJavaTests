package dev.gabrafo.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static dev.gabrafo.common.PlanetConstants.PLANET;

// INTEGRAÇÃO COM BD
// Utiliza um banco H2 em memória pra testar (já configura automaticamente)
@DataJpaTest
public class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager; // Interage com o banco de dados sem usar o repository

    @Test
    public void createPlanet_withValidData_ReturnsPlanet(){

        // Arrange
        Planet planet = planetRepository.save(PLANET);

        // Act
        Planet sut = testEntityManager.find(Planet.class, planet.getId());

        // Assert
        assertThat(sut).isNotNull();
        // Precisamos validar cada propriedade porque o planet não tem ID, enquanto o sut tem
        assertThat(sut.getName()).isEqualTo(planet.getName());
        assertThat(sut.getClimate()).isEqualTo(planet.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(planet.getTerrain());
    }

    @Test
    public void createPlanet_WIthInvalidData_ThrowsException() {
        Planet emptyPlanet = new Planet();
        Planet invalidPlanet = new Planet("", "", "");

        assertThatThrownBy(() -> planetRepository.save(emptyPlanet)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> planetRepository.save(invalidPlanet)).isInstanceOf(RuntimeException.class);
    }
}
