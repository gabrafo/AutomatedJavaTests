package dev.gabrafo.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class QueryBuilder {

    private QueryBuilder(){} // Deixando private para que ele não seja contado no Jacoco, já que não é instanciado

    public static Example<Planet> makeQuery(Planet planet) {

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();
        return Example.of(planet, exampleMatcher);
    }
}