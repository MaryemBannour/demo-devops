package com.beedigital.beestore.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

class ProduitResponseDTOTest {

    private static final LocalDateTime DATE = LocalDateTime.of(2026, 1, 15, 10, 30);

    // Chaque élément change UN champ d'un DTO complet (pour tester equals/hashCode)
    private static final List<Consumer<ProduitResponseDTO>> MODIFICATIONS = List.of(
            d -> d.setId(99L),
            d -> d.setNom("Autre nom"),
            d -> d.setDescription("Autre description"),
            d -> d.setPrix(1.0),
            d -> d.setStock(99),
            d -> d.setCategorie("Autre catégorie"),
            d -> d.setActif(false),
            d -> d.setDateCreation(DATE.plusDays(1)));

    // Chaque élément met UN champ à null (pour couvrir les branches null de equals/hashCode)
    private static final List<Consumer<ProduitResponseDTO>> MISES_A_NULL = List.of(
            d -> d.setId(null),
            d -> d.setNom(null),
            d -> d.setDescription(null),
            d -> d.setPrix(null),
            d -> d.setStock(null),
            d -> d.setCategorie(null),
            d -> d.setActif(null),
            d -> d.setDateCreation(null));

    private static ProduitResponseDTO dtoComplet() {
        return ProduitResponseDTO.builder()
                .id(1L).nom("Laptop Pro").description("Ultrabook 14 pouces")
                .prix(1299.99).stock(10).categorie("Informatique")
                .actif(true).dateCreation(DATE)
                .build();
    }

    // =========================================================
    // Getters
    // =========================================================
    @Test
    void getId() {
        assertThat(dtoComplet().getId()).isEqualTo(1L);
    }

    @Test
    void getNom() {
        assertThat(dtoComplet().getNom()).isEqualTo("Laptop Pro");
    }

    @Test
    void getDescription() {
        assertThat(dtoComplet().getDescription()).isEqualTo("Ultrabook 14 pouces");
    }

    @Test
    void getPrix() {
        assertThat(dtoComplet().getPrix()).isEqualTo(1299.99);
    }

    @Test
    void getStock() {
        assertThat(dtoComplet().getStock()).isEqualTo(10);
    }

    @Test
    void getCategorie() {
        assertThat(dtoComplet().getCategorie()).isEqualTo("Informatique");
    }

    @Test
    void getActif() {
        assertThat(dtoComplet().getActif()).isTrue();
    }

    @Test
    void getDateCreation() {
        assertThat(dtoComplet().getDateCreation()).isEqualTo(DATE);
    }

    // =========================================================
    // Setters
    // =========================================================
    @Test
    void setId() {
        ProduitResponseDTO dto = new ProduitResponseDTO();
        dto.setId(5L);
        assertThat(dto.getId()).isEqualTo(5L);
    }

    @Test
    void setNom() {
        ProduitResponseDTO dto = new ProduitResponseDTO();
        dto.setNom("Clavier");
        assertThat(dto.getNom()).isEqualTo("Clavier");
    }

    @Test
    void setDescription() {
        ProduitResponseDTO dto = new ProduitResponseDTO();
        dto.setDescription("Mécanique rétroéclairé");
        assertThat(dto.getDescription()).isEqualTo("Mécanique rétroéclairé");
    }

    @Test
    void setPrix() {
        ProduitResponseDTO dto = new ProduitResponseDTO();
        dto.setPrix(89.90);
        assertThat(dto.getPrix()).isEqualTo(89.90);
    }

    @Test
    void setStock() {
        ProduitResponseDTO dto = new ProduitResponseDTO();
        dto.setStock(25);
        assertThat(dto.getStock()).isEqualTo(25);
    }

    @Test
    void setCategorie() {
        ProduitResponseDTO dto = new ProduitResponseDTO();
        dto.setCategorie("Accessoires");
        assertThat(dto.getCategorie()).isEqualTo("Accessoires");
    }

    @Test
    void setActif() {
        ProduitResponseDTO dto = new ProduitResponseDTO();
        dto.setActif(false);
        assertThat(dto.getActif()).isFalse();
    }

    @Test
    void setDateCreation() {
        ProduitResponseDTO dto = new ProduitResponseDTO();
        dto.setDateCreation(DATE);
        assertThat(dto.getDateCreation()).isEqualTo(DATE);
    }

    // =========================================================
    // equals / canEqual / hashCode / toString
    // =========================================================
    @Test
    void testEquals() {
        ProduitResponseDTO a = dtoComplet();
        ProduitResponseDTO b = dtoComplet();

        assertThat(a.equals(a)).isTrue();                 // même instance
        assertThat(a.equals(b)).isTrue();                 // mêmes valeurs
        assertThat(a.equals(null)).isFalse();             // null
        assertThat(a.equals("pas un DTO")).isFalse();     // autre type

        // Un seul champ différent suffit à rendre les DTO différents
        for (Consumer<ProduitResponseDTO> modification : MODIFICATIONS) {
            ProduitResponseDTO different = dtoComplet();
            modification.accept(different);
            assertThat(a.equals(different)).isFalse();
            assertThat(different.equals(a)).isFalse();
        }

        // Gestion des champs null : rempli/null, null/rempli, null/null
        for (Consumer<ProduitResponseDTO> miseANull : MISES_A_NULL) {
            ProduitResponseDTO avecNull = dtoComplet();
            miseANull.accept(avecNull);
            ProduitResponseDTO autreAvecNull = dtoComplet();
            miseANull.accept(autreAvecNull);

            assertThat(a.equals(avecNull)).isFalse();
            assertThat(avecNull.equals(a)).isFalse();
            assertThat(avecNull.equals(autreAvecNull)).isTrue();
        }
    }

    @Test
    void canEqual() {
        ProduitResponseDTO dto = dtoComplet();

        assertThat(dto.canEqual(dtoComplet())).isTrue();
        assertThat(dto.canEqual("pas un DTO")).isFalse();
    }

    @Test
    void testHashCode() {
        ProduitResponseDTO a = dtoComplet();

        // Cohérent avec equals : mêmes valeurs => même hashCode
        assertThat(a.hashCode()).isEqualTo(dtoComplet().hashCode());

        // Un champ différent => hashCode différent
        for (Consumer<ProduitResponseDTO> modification : MODIFICATIONS) {
            ProduitResponseDTO different = dtoComplet();
            modification.accept(different);
            assertThat(different.hashCode()).isNotEqualTo(a.hashCode());
        }

        // Les champs null ne provoquent pas d'erreur et restent cohérents
        for (Consumer<ProduitResponseDTO> miseANull : MISES_A_NULL) {
            ProduitResponseDTO avecNull = dtoComplet();
            miseANull.accept(avecNull);
            ProduitResponseDTO autreAvecNull = dtoComplet();
            miseANull.accept(autreAvecNull);
            assertThat(avecNull.hashCode()).isEqualTo(autreAvecNull.hashCode());
        }
    }

    @Test
    void testToString() {
        String texte = dtoComplet().toString();

        assertThat(texte).startsWith("ProduitResponseDTO(")
                .contains("id=1", "nom=Laptop Pro", "description=Ultrabook 14 pouces",
                        "prix=1299.99", "stock=10", "categorie=Informatique",
                        "actif=true", "dateCreation=" + DATE);
    }

    // =========================================================
    // Builder et constructeurs
    // =========================================================
    @Test
    void builder() {
        ProduitResponseDTO dto = dtoComplet();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNom()).isEqualTo("Laptop Pro");
        assertThat(dto.getDescription()).isEqualTo("Ultrabook 14 pouces");
        assertThat(dto.getPrix()).isEqualTo(1299.99);
        assertThat(dto.getStock()).isEqualTo(10);
        assertThat(dto.getCategorie()).isEqualTo("Informatique");
        assertThat(dto.getActif()).isTrue();
        assertThat(dto.getDateCreation()).isEqualTo(DATE);

        // Le constructeur avec tous les champs donne le même résultat que le builder
        ProduitResponseDTO viaConstructeur = new ProduitResponseDTO(1L, "Laptop Pro",
                "Ultrabook 14 pouces", 1299.99, 10, "Informatique", true, DATE);
        assertThat(viaConstructeur).isEqualTo(dto);

        // Le constructeur vide laisse tous les champs à null
        assertThat(new ProduitResponseDTO()).hasAllNullFieldsOrProperties();

        // toString du builder lui-même
        assertThat(ProduitResponseDTO.builder().id(1L).toString())
                .contains("ProduitResponseDTOBuilder", "id=1");
    }
}