package com.beedigital.beestore.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    // Méthode factice : sert uniquement à fabriquer un MethodParameter
    // pour construire une vraie MethodArgumentNotValidException.
    @SuppressWarnings("unused")
    private void methodeFactice(String valeur) {
    }

    @Test
    @DisplayName("handleValidation() doit retourner 400 avec le détail des erreurs par champ")
    void handleValidation() throws Exception {
        // GIVEN : une exception de validation portant deux erreurs de champ
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "produit");
        bindingResult.addError(new FieldError("produit", "nom",
                "Le nom du produit est obligatoire"));
        bindingResult.addError(new FieldError("produit", "prix",
                "Le prix est obligatoire"));

        MethodParameter parametre = new MethodParameter(
                GlobalExceptionHandlerTest.class
                        .getDeclaredMethod("methodeFactice", String.class), 0);
        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(parametre, bindingResult);

        // WHEN
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> reponse =
                handler.handleValidation(ex);

        // THEN
        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        GlobalExceptionHandler.ErrorResponse corps = reponse.getBody();
        assertThat(corps).isNotNull();
        assertThat(corps.status()).isEqualTo(400);
        assertThat(corps.message()).isEqualTo("Données invalides");
        assertThat(corps.timestamp()).isNotNull();
        assertThat(corps.details()).isEqualTo(Map.of(
                "nom", "Le nom du produit est obligatoire",
                "prix", "Le prix est obligatoire"));
    }

    @Test
    @DisplayName("handleNotFound() doit retourner 404 avec le message de l'exception")
    void handleNotFound() {
        // GIVEN
        ProduitNotFoundException ex =
                new ProduitNotFoundException("Produit non trouvé avec l'ID : 99");

        // WHEN
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> reponse =
                handler.handleNotFound(ex);

        // THEN
        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        GlobalExceptionHandler.ErrorResponse corps = reponse.getBody();
        assertThat(corps).isNotNull();
        assertThat(corps.status()).isEqualTo(404);
        assertThat(corps.message()).isEqualTo("Produit non trouvé avec l'ID : 99");
        assertThat(corps.timestamp()).isNotNull();
        assertThat(corps.details()).isNull();
    }

    @Test
    @DisplayName("handleGeneric() doit retourner 500 avec un message générique, sans fuite du détail technique")
    void handleGeneric() {
        // GIVEN
        Exception ex = new RuntimeException("boom : détail technique interne");

        // WHEN
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> reponse =
                handler.handleGeneric(ex);

        // THEN
        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        GlobalExceptionHandler.ErrorResponse corps = reponse.getBody();
        assertThat(corps).isNotNull();
        assertThat(corps.status()).isEqualTo(500);
        assertThat(corps.message())
                .isEqualTo("Une erreur interne est survenue. Contactez l'administrateur.")
                .doesNotContain("boom");
        assertThat(corps.timestamp()).isNotNull();
        assertThat(corps.details()).isNull();
    }
}