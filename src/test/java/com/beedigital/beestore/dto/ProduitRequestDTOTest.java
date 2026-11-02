package com.beedigital.beestore.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

class ProduitRequestDTOTest {

    // Chaque élément change UN champ d'un DTO complet (pour tester equals/hashCode)
    private static final List<Consumer<ProduitRequestDTO>> MODIFICATIONS = List.of(
            d -> d.setNom("Autre nom"),
            d -> d.setDescription("Autre description"),
            d -> d.setPrix(1.0),
            d -> d.setStock(99),
            d -> d.setCategorie("Autre catégorie"),
            d -> d.setActif(false));

    // Chaque élément met UN champ à null (pour couvrir les branches null de equals/hashCode)
    private static final List<Consumer<ProduitRequestDTO>> MISES_A_NULL = List.of(
            d -> d.setNom(null),
            d -> d.setDescription(null),
            d -> d.setPrix(null),
            d -> d.setStock(null),
            d -> d.setCategorie(null),
            d -> d.setActif(null));

    private static ProduitRequestDTO dtoComplet() {
        ProduitRequestDTO dto = new ProduitRequestDTO();
        dto.setNom("Clavier");
        dto.setDescription("Mécanique rétroéclairé");
        dto.setPrix(89.90);
        dto.setStock(25);
        dto.setCategorie("Informatique");
        dto.setActif(true);
        return dto;
    }

    // =========================================================
    // Getters
    // =========================================================
    @Test
    void getNom() {
        assertThat(dtoComplet().getNom()).isEqualTo("Clavier");
    }

    @Test
    void getDescription() {
        assertThat(dtoComplet().getDescription()).isEqualTo("Mécanique rétroéclairé");
    }

    @Test
    void getPrix() {
        assertThat(dtoComplet().getPrix()).isEqualTo(89.90);
    }

    @Test
    void getStock() {
        assertThat(dtoComplet().getStock()).isEqualTo(25);
    }

    @Test
    void getCategorie() {
        assertThat(dtoComplet().getCategorie()).isEqualTo("Informatique");
    }

    @Test
    void getActif() {
        assertThat(dtoComplet().getActif()).isTrue();
        // Valeur par défaut du champ : actif = true
        assertThat(new ProduitRequestDTO().getActif()).isTrue();
    }

    // =========================================================
    // Setters
    // =========================================================
    @Test
    void setNom() {
        ProduitRequestDTO dto = new ProduitRequestDTO();
        dto.setNom("Souris");
        assertThat(dto.getNom()).isEqualTo("Souris");
    }

    @Test
    void setDescription() {
        ProduitRequestDTO dto = new ProduitRequestDTO();
        dto.setDescription("Sans fil");
        assertThat(dto.getDescription()).isEqualTo("Sans fil");
    }

    @Test
    void setPrix() {
        ProduitRequestDTO dto = new ProduitRequestDTO();
        dto.setPrix(19.90);
        assertThat(dto.getPrix()).isEqualTo(19.90);
    }

    @Test
    void setStock() {
        ProduitRequestDTO dto = new ProduitRequestDTO();
        dto.setStock(50);
        assertThat(dto.getStock()).isEqualTo(50);
    }

    @Test
    void setCategorie() {
        ProduitRequestDTO dto = new ProduitRequestDTO();
        dto.setCategorie("Accessoires");
        assertThat(dto.getCategorie()).isEqualTo("Accessoires");
    }

    @Test
    void setActif() {
        ProduitRequestDTO dto = new ProduitRequestDTO();
        dto.setActif(false);
        assertThat(dto.getActif()).isFalse();
    }

    // =========================================================
    // equals / canEqual / hashCode / toString
    // =========================================================
    @Test
    void testEquals() {
        ProduitRequestDTO a = dtoComplet();
        ProduitRequestDTO b = dtoComplet();

        assertThat(a.equals(a)).isTrue();                 // même instance
        assertThat(a.equals(b)).isTrue();                 // mêmes valeurs
        assertThat(a.equals(null)).isFalse();             // null
        assertThat(a.equals("pas un DTO")).isFalse();     // autre type

        // Un seul champ différent suffit à rendre les DTO différents
        for (Consumer<ProduitRequestDTO> modification : MODIFICATIONS) {
            ProduitRequestDTO different = dtoComplet();
            modification.accept(different);
            assertThat(a.equals(different)).isFalse();
            assertThat(different.equals(a)).isFalse();
        }

        // Gestion des champs null : rempli/null, null/rempli, null/null
        for (Consumer<ProduitRequestDTO> miseANull : MISES_A_NULL) {
            ProduitRequestDTO avecNull = dtoComplet();
            miseANull.accept(avecNull);
            ProduitRequestDTO autreAvecNull = dtoComplet();
            miseANull.accept(autreAvecNull);

            assertThat(a.equals(avecNull)).isFalse();
            assertThat(avecNull.equals(a)).isFalse();
            assertThat(avecNull.equals(autreAvecNull)).isTrue();
        }
    }

    @Test
    void canEqual() {
        ProduitRequestDTO dto = dtoComplet();

        assertThat(dto.canEqual(dtoComplet())).isTrue();
        assertThat(dto.canEqual("pas un DTO")).isFalse();
    }

    @Test
    void testHashCode() {
        ProduitRequestDTO a = dtoComplet();

        // Cohérent avec equals : mêmes valeurs => même hashCode
        assertThat(a.hashCode()).isEqualTo(dtoComplet().hashCode());

        // Un champ différent => hashCode différent
        for (Consumer<ProduitRequestDTO> modification : MODIFICATIONS) {
            ProduitRequestDTO different = dtoComplet();
            modification.accept(different);
            assertThat(different.hashCode()).isNotEqualTo(a.hashCode());
        }

        // Les champs null ne provoquent pas d'erreur et restent cohérents
        for (Consumer<ProduitRequestDTO> miseANull : MISES_A_NULL) {
            ProduitRequestDTO avecNull = dtoComplet();
            miseANull.accept(avecNull);
            ProduitRequestDTO autreAvecNull = dtoComplet();
            miseANull.accept(autreAvecNull);
            assertThat(avecNull.hashCode()).isEqualTo(autreAvecNull.hashCode());
        }
    }

    @Test
    void testToString() {
        String texte = dtoComplet().toString();

        assertThat(texte).startsWith("ProduitRequestDTO(")
                .contains("nom=Clavier", "description=Mécanique rétroéclairé",
                        "prix=89.9", "stock=25", "categorie=Informatique", "actif=true");
    }

    // =========================================================
    // Bonus : les contraintes de validation Jakarta du DTO
    // =========================================================
    private static final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validation_donneesValides_aucuneViolation() {
        assertThat(validator.validate(dtoComplet())).isEmpty();

        // Le stock à 0 est autorisé (@PositiveOrZero)
        ProduitRequestDTO stockZero = dtoComplet();
        stockZero.setStock(0);
        assertThat(validator.validate(stockZero)).isEmpty();
    }

    @Test
    void validation_donneesInvalides_uneViolationParChampEnErreur() {
        ProduitRequestDTO dto = new ProduitRequestDTO();
        dto.setNom("");                        // @NotBlank + @Size(min = 2)
        dto.setDescription("x".repeat(501));   // @Size(max = 500)
        dto.setPrix(-5.0);                     // @Positive
        dto.setStock(-1);                      // @PositiveOrZero
        dto.setCategorie("x".repeat(101));     // @Size(max = 100)

        Set<ConstraintViolation<ProduitRequestDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .containsOnly("nom", "description", "prix", "stock", "categorie");
    }

    @Test
    void validation_prixEtStockAbsents_sontRefuses() {
        ProduitRequestDTO dto = dtoComplet();
        dto.setPrix(null);   // @NotNull
        dto.setStock(null);  // @NotNull

        Set<ConstraintViolation<ProduitRequestDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactlyInAnyOrder("prix", "stock");
    }
}