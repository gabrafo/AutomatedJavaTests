package dev.gabrafo.testes;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

public class CalculatorTest {

    @Test // JUnit
    public void testSum(){
        Calculator calculator = new Calculator();
        assertThat(calculator.sum(1,1)).isEqualTo(2);// AssertJ
    }
}
