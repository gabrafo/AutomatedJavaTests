package dev.gabrafo;

import static dev.gabrafo.common.PlanetConstants.PLANET;
import static dev.gabrafo.common.PlanetConstants.TATOOINE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import dev.gabrafo.domain.Planet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("it")
@Sql(scripts = {"/import_planets.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // Para o Get
@Sql(scripts = {"/remove_planets.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) // Para apagar registros do Post
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Simula um ambiente Web com o TomCat
public class PlanetIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createPlanet_ReturnsCreated(){

        ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets", PLANET, Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(sut.getBody().getId()).isNotNull();
        assertThat((sut.getBody()).getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test
    public void getPlanetById_ReturnsPlanet(){
        ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/1", Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATOOINE);
    }

}