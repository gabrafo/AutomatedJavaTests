package dev.gabrafo.domain;

import static dev.gabrafo.common.PlanetConstants.PLANET; // Acesso estático à constante
import static dev.gabrafo.common.PlanetConstants.INVALID_PLANET;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Para testes de unidade é melhor usar o Mockito puro a usar Spring, para melhorar a otimização

// @SpringBootTest(classes = PlanetService.class) Monta o contexto de aplicação do Spring, não será usado aqui
@ExtendWith(MockitoExtension.class) // Habilita o Mockito
public class PlanetServiceTest {

    // @Autowired (Caso estivesse usando Spring)
    @InjectMocks
    private PlanetService planetService;

    // @MockBean (Caso estivesse usando Spring)
    // Dublê de testes da dependẽncia da unidade (classe PlanetService)
    @Mock
    private PlanetRepository planetRepository;

    @Test
    // Padrão de nomenclatura do método: operação_estado_retorno
    public void createPlanet_WithValidData_ReturnsPlanet(){
        // AAA

        // Arrange: Arruma os dados pro teste
        when(planetRepository.save(PLANET)).thenReturn(PLANET);

        // Act: Executar a operação a ser testada
        // sut = System Under Test
        Planet sut = planetService.create(PLANET);

        // Assert: Aferir se o resultado foi o esperado
        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException(){

        // Arrange
        when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        // Act & Assert
        assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_WithValidId_ReturnsPlanet(){

        // Arrange
        when(planetRepository.findById(anyLong())).thenReturn(Optional.of(PLANET));

        // Act
        Optional<Planet> sut = planetService.get(1L);

        // Assert
        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_WithInvalidId_ReturnsNull(){

        // Arrange
        when(planetRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Planet> sut = planetService.get(1L);

        // Assert
        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanetByName_WithValidName_ReturnsPlanet(){

        // Arrange
        when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        // Act
        Optional<Planet> sut = planetService.getByName(PLANET.getName());

        // Assert
        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanetByName_WithInvalidName_ReturnsNull(){

        // Arrange
        final String name = "Unexisting name";
        when(planetRepository.findByName(name)).thenReturn(Optional.empty());

        // Act
        Optional<Planet> sut = planetService.getByName(name);

        // Assert
        assertThat(sut).isEmpty();
    }

    @Test
    public void listPlanets_ReturnsAllPlanets(){

        // Arrange
        List<Planet> planets = new ArrayList<>() {
            {
                add(PLANET);
            }
        };

        Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerrain()));

        when(planetRepository.findAll(query)).thenReturn(planets);

        // Act
        List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

        // Assert
        assertThat(sut).containsExactly(PLANET);
    }

    @Test
    public void listPlanets_ReturnsEmptyList(){

        // Arrange
        when(planetRepository.findAll(any())).thenReturn(List.of());

        // Act
        List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

        // Assert
        assertThat(sut).isEmpty();
    }

    @Test
    public void removePlanet_WithValidId_DoestNotThrowException(){

        // AAA
        assertThatCode(() -> planetService.remove(1L)).doesNotThrowAnyException();
    }

    @Test
    public void removePlanet_WithInvalidId_ThrowsException(){

        // Arrange
        doThrow(RuntimeException.class).when(planetRepository).deleteById(anyLong());

        // Act & Assert
        assertThatThrownBy(() -> planetService.remove(1L)).isInstanceOf(RuntimeException.class);
    }
}