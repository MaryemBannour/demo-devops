package com.beedigital.beestore.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

class ProduitTest {

    private static final LocalDateTime DATE = LocalDateTime.of(2026, 1, 15, 10, 30);

    // Chaque élément change UN champ d'un produit complet (pour tester equals/hashCode)
    private static final List<Consumer<Produit>> MODIFICATIONS = List.of(
            p -> p.setId(99L),
            p -> p.setNom("Autre nom"),
            p -> p.setDescription("Autre description"),
            p -> p.setPrix(1.0),
            p -> p.setStock(99),
            p -> p.setCategorie("Autre catégorie"),
            p -> p.setActif(false),
            p -> p.setDateCreation(DATE.plusDays(1)));

    // Chaque élément met UN champ à null (pour couvrir les branches null de equals/hashCode)
    private static final List<Consumer<Produit>> MISES_A_NULL = List.of(
            p -> p.setId(null),
            p -> p.setNom(null),
            p -> p.setDescription(null),
            p -> p.setPrix(null),
            p -> p.setStock(null),
            p -> p.setCategorie(null),
            p -> p.setActif(null),
            p -> p.setDateCreation(null));

    private static Produit produitComplet() {
        return Produit.builder()
                .id(1L).nom("Laptop Pro").description("Ultrabook 14 pouces")
                .prix(1299.99).stock(10).categorie("Informatique")
                .actif(true).dateCreation(DATE)
                .build();
    }

    // =========================================================
    // @PrePersist
    // =========================================================
    @Test
    void onCreate() {
        // Le builder ignore le "= true" du champ : actif vaut null tant qu'on n'a pas persisté
        Produit produit = Produit.builder().nom("Souris").build();
        assertThat(produit.getActif()).isNull();
        assertThat(produit.getDateCreation()).isNull();

        LocalDateTime avant = LocalDateTime.now();
        produit.onCreate();
        LocalDateTime apres = LocalDateTime.now();

        assertThat(produit.getActif()).isTrue();
        assertThat(produit.getDateCreation()).isBetween(avant, apres);
    }

    @Test
    void onCreate_actifDejaRenseigne_nEstPasModifie() {
        Produit inactif = Produit.builder().actif(false).build();
        inactif.onCreate();
        assertThat(inactif.getActif()).isFalse();

        Produit actif = Produit.builder().actif(true).build();
        actif.onCreate();
        assertThat(actif.getActif()).isTrue();
    }

    // =========================================================
    // Getters
    // =========================================================
    @Test
    void getId() {
        assertThat(produitComplet().getId()).isEqualTo(1L);
    }

    @Test
    void getNom() {
        assertThat(produitComplet().getNom()).isEqualTo("Laptop Pro");
    }

    @Test
    void getDescription() {
        assertThat(produitComplet().getDescription()).isEqualTo("Ultrabook 14 pouces");
    }

    @Test
    void getPrix() {
        assertThat(produitComplet().getPrix()).isEqualTo(1299.99);
    }

    @Test
    void getStock() {
        assertThat(produitComplet().getStock()).isEqualTo(10);
    }

    @Test
    void getCategorie() {
        assertThat(produitComplet().getCategorie()).isEqualTo("Informatique");
    }

    @Test
    void getActif() {
        assertThat(produitComplet().getActif()).isTrue();
        // Avec le constructeur vide, l'initialiseur "= true" s'applique
        assertThat(new Produit().getActif()).isTrue();
    }

    @Test
    void getDateCreation() {
        assertThat(produitComplet().getDateCreation()).isEqualTo(DATE);
    }

    // =========================================================
    // Setters
    // =========================================================
    @Test
    void setId() {
        Produit produit = new Produit();
        produit.setId(5L);
        assertThat(produit.getId()).isEqualTo(5L);
    }

    @Test
    void setNom() {
        Produit produit = new Produit();
        produit.setNom("Clavier");
        assertThat(produit.getNom()).isEqualTo("Clavier");
    }

    @Test
    void setDescription() {
        Produit produit = new Produit();
        produit.setDescription("Mécanique rétroéclairé");
        assertThat(produit.getDescription()).isEqualTo("Mécanique rétroéclairé");
    }

    @Test
    void setPrix() {
        Produit produit = new Produit();
        produit.setPrix(89.90);
        assertThat(produit.getPrix()).isEqualTo(89.90);
    }

    @Test
    void setStock() {
        Produit produit = new Produit();
        produit.setStock(25);
        assertThat(produit.getStock()).isEqualTo(25);
    }

    @Test
    void setCategorie() {
        Produit produit = new Produit();
        produit.setCategorie("Accessoires");
        assertThat(produit.getCategorie()).isEqualTo("Accessoires");
    }

    @Test
    void setActif() {
        Produit produit = new Produit();
        produit.setActif(false);
        assertThat(produit.getActif()).isFalse();
    }

    @Test
    void setDateCreation() {
        Produit produit = new Produit();
        produit.setDateCreation(DATE);
        assertThat(produit.getDateCreation()).isEqualTo(DATE);
    }

    // =========================================================
    // equals / canEqual / hashCode / toString
    // =========================================================
    @Test
    void testEquals() {
        Produit a = produitComplet();
        Produit b = produitComplet();

        assertThat(a.equals(a)).isTrue();                 // même instance
        assertThat(a.equals(b)).isTrue();                 // mêmes valeurs
        assertThat(a.equals(null)).isFalse();             // null
        assertThat(a.equals("pas un produit")).isFalse(); // autre type

        // Un seul champ différent suffit à rendre les produits différents
        for (Consumer<Produit> modification : MODIFICATIONS) {
            Produit different = produitComplet();
            modification.accept(different);
            assertThat(a.equals(different)).isFalse();
            assertThat(different.equals(a)).isFalse();
        }

        // Gestion des champs null : rempli/null, null/rempli, null/null
        for (Consumer<Produit> miseANull : MISES_A_NULL) {
            Produit avecNull = produitComplet();
            miseANull.accept(avecNull);
            Produit autreAvecNull = produitComplet();
            miseANull.accept(autreAvecNull);

            assertThat(a.equals(avecNull)).isFalse();
            assertThat(avecNull.equals(a)).isFalse();
            assertThat(avecNull.equals(autreAvecNull)).isTrue();
        }
    }

    @Test
    void canEqual() {
        Produit produit = produitComplet();

        assertThat(produit.canEqual(produitComplet())).isTrue();
        assertThat(produit.canEqual("pas un produit")).isFalse();
    }

    @Test
    void testHashCode() {
        Produit a = produitComplet();

        // Cohérent avec equals : mêmes valeurs => même hashCode
        assertThat(a.hashCode()).isEqualTo(produitComplet().hashCode());

        // Un champ différent => hashCode différent
        for (Consumer<Produit> modification : MODIFICATIONS) {
            Produit different = produitComplet();
            modification.accept(different);
            assertThat(different.hashCode()).isNotEqualTo(a.hashCode());
        }

        // Les champs null ne provoquent pas d'erreur et restent cohérents
        for (Consumer<Produit> miseANull : MISES_A_NULL) {
            Produit avecNull = produitComplet();
            miseANull.accept(avecNull);
            Produit autreAvecNull = produitComplet();
            miseANull.accept(autreAvecNull);
            assertThat(avecNull.hashCode()).isEqualTo(autreAvecNull.hashCode());
        }
    }

    @Test
    void testToString() {
        String texte = produitComplet().toString();

        assertThat(texte).startsWith("Produit(")
                .contains("id=1", "nom=Laptop Pro", "description=Ultrabook 14 pouces",
                        "prix=1299.99", "stock=10", "categorie=Informatique",
                        "actif=true", "dateCreation=" + DATE);
    }

    // =========================================================
    // Builder et constructeurs
    // =========================================================
    @Test
    void builder() {
        Produit produit = produitComplet();

        assertThat(produit.getId()).isEqualTo(1L);
        assertThat(produit.getNom()).isEqualTo("Laptop Pro");
        assertThat(produit.getDescription()).isEqualTo("Ultrabook 14 pouces");
        assertThat(produit.getPrix()).isEqualTo(1299.99);
        assertThat(produit.getStock()).isEqualTo(10);
        assertThat(produit.getCategorie()).isEqualTo("Informatique");
        assertThat(produit.getActif()).isTrue();
        assertThat(produit.getDateCreation()).isEqualTo(DATE);

        // Le constructeur avec tous les champs donne le même résultat que le builder
        Produit viaConstructeur = new Produit(1L, "Laptop Pro", "Ultrabook 14 pouces",
                1299.99, 10, "Informatique", true, DATE);
        assertThat(viaConstructeur).isEqualTo(produit);

        // toString du builder lui-même
        assertThat(Produit.builder().id(1L).toString()).contains("ProduitBuilder", "id=1");
    }
}