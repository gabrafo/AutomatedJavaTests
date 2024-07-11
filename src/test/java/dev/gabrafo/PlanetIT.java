package dev.gabrafo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Simula um ambiente Web com o TomCat
public class PlanetIT {

    @Test
    public void contextLoads(){} // Testa o carregamento do contexto de aplicação
}