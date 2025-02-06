# 📌 MODIFICATIONS ET AMÉLIORATIONS DU PROJET

## 📝 Introduction
Ce fichier récapitule **toutes les modifications** apportées au projet initial, ainsi que **les corrections et améliorations** effectuées.

---

## 📌 1. Correction des DAO (`GenreDao.java` et `MovieDao.java`)

### ✅ **Solution**
- **Modification des requêtes SQL** pour utiliser `idgenre` et `idmovie`.
- **Utilisation des jointures (`JOIN genre ON movie.genre_id = genre.idgenre`)** pour récupérer le genre dans `MovieDao`.
- **Ajout de `Statement.RETURN_GENERATED_KEYS`** pour récupérer l'ID des films ajoutés.

