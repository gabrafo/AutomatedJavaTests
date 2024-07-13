package dev.gabrafo.jacoco;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Configuração p/ ignorar métodos no relatório de cobertura do Jacoco

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExcludeFromJacocoGeneratedReport {
}