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

        Planet planet = planetRepository.save(PLANET);

        Planet sut = testEntityManager.find(Planet.class, planet.getId());

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

    @Test
    public void createPlanet_WithExistingName_ThrowsException() {

        Planet planet = testEntityManager.persistFlushFind(PLANET); // Vai ter um ID!
        testEntityManager.detach(planet); // Faz com que o JPA "esqueça" que esse planeta já existe no BD
        planet.setId(null); // Temos que setar como nulo para que o save(PLANET) não atualize o planeta existente no BD
        // E sim que ele crie um novo planeta! Para saber se vai atualizar ou criar um recurso, o ORM checa se ele tem ID
        // Por isso estamos tirando o ID

        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }
}