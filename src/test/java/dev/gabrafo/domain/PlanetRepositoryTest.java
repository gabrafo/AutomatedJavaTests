package dev.gabrafo.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static dev.gabrafo.common.PlanetConstants.PLANET;

// INTEGRAÇÃO COM BD
// Utiliza um banco H2 em memória pra testar (já configura automaticamente)
@DataJpaTest
public class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager; // Interage com o banco de dados sem usar o repository

    @AfterEach
    public void afterEach(){
        PLANET.setId(null);
    }

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

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet(){

        Planet planet = testEntityManager.persistFlushFind(PLANET);

        Optional<Planet> sut = planetRepository.findById(planet.getId());

        assertThat(sut).isNotEmpty();
        assertThat(sut).isEqualTo(Optional.of(planet));
    }

    @Test
    public void getPlanet_WithInvalidId_ReturnsEmpty() {
        Optional<Planet> sut = planetRepository.findById(1L);

        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet(){

        Planet planet = testEntityManager.persistFlushFind(PLANET);

        Optional<Planet> sut = planetRepository.findByName(planet.getName());

        assertThat(sut).isNotEmpty();
        assertThat(sut).isEqualTo(Optional.of(planet));
    }

    @Test
    public void getPlanet_WithInvalidName_ReturnsEmpty() {
        Optional<Planet> sut = planetRepository.findByName("name");

        assertThat(sut).isEmpty();
    }
}