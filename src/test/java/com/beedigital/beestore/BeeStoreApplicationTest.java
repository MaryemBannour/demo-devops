package com.beedigital.beestore;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

class BeeStoreApplicationTest {

    @Test
    void main() {
        // SpringApplication.run est remplacé par un faux : on vérifie l'appel
        // sans démarrer l'application (pas de port, pas de base de données).
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            String[] args = {"--spring.profiles.active=test"};

            BeeStoreApplication.main(args);

            springApplication.verify(() -> SpringApplication.run(BeeStoreApplication.class, args));
        }
    }

    @Test
    void constructeur() {
        // Couvre le constructeur implicite de la classe
        assertThat(new BeeStoreApplication()).isNotNull();
    }
}