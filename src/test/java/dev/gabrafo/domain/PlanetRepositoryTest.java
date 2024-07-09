package dev.gabrafo.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static dev.gabrafo.common.PlanetConstants.PLANET;
import static dev.gabrafo.common.PlanetConstants.TATOOINE;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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

    @Sql(scripts = "/import_planets.sql")
    @Test
    public void listPlanets_ReturnsFilteredPlanets(){

    Example<Planet> queryWithoutFilters = QueryBuilder.makeQuery(new Planet());
    Example<Planet> filteredQuery = QueryBuilder.makeQuery((new Planet(TATOOINE.getClimate(), TATOOINE.getTerrain())));

    List<Planet> responseWithoutFilters = planetRepository.findAll(queryWithoutFilters);
    List<Planet> filteredResponse = planetRepository.findAll(filteredQuery);

    assertThat(responseWithoutFilters).isNotEmpty();
    assertThat(responseWithoutFilters).hasSize(3);

    assertThat(filteredResponse).isNotEmpty();
    assertThat(filteredResponse).hasSize(1);
    assertThat(filteredResponse.get(0)).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanets_ReturnsEmpty(){

        Example<Planet> query= QueryBuilder.makeQuery(new Planet());

        List<Planet> response = planetRepository.findAll(query);

        assertThat(response).isEmpty();
    }

    @Test
    public void removePlanet_WithValidId_RemovesFromDb(){

        Planet planet = testEntityManager.persistFlushFind(PLANET);

        planetRepository.deleteById(planet.getId());

        Planet removedPlanet = testEntityManager.find(Planet.class, planet.getId());
        assertThat(removedPlanet).isNull();
    }

    @Sql(scripts = "/import_planets.sql")
    @Test
    public void removePlanet_WithInvalidId_DoesNotChangeDb(){
        // Verifica que o planeta com ID 4L não existe antes da exclusão
        assertThat(testEntityManager.find(Planet.class, 4L)).isNull();

        planetRepository.deleteById(4L);

        // Verifica que os planetas com IDs 1L, 2L e 3L ainda existem
        assertThat(testEntityManager.find(Planet.class, 1L)).isInstanceOf(Planet.class);
        assertThat(testEntityManager.find(Planet.class, 2L)).isInstanceOf(Planet.class);
        assertThat(testEntityManager.find(Planet.class, 3L)).isInstanceOf(Planet.class);

        // Verifica que o planeta com ID 4L ainda não existe após a tentativa de exclusão
        assertThat(testEntityManager.find(Planet.class, 4L)).isNull();
    }
}