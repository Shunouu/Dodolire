# DodoLire - Application de génération d'histoires pour crèches

## 📱 Présentation

DodoLire est une application Android conçue pour les crèches, permettant de générer des histoires et des comptines personnalisées pour les enfants. L'application utilise l'API OpenAI pour créer des contenus adaptés à l'âge des enfants, avec des personnages et des thèmes personnalisables.

## 🌟 Fonctionnalités

- *Génération d'histoires et de comptines* adaptées aux enfants de 1 à 3 ans
- *Personnalisation* du contenu (âge, thème, personnage, mot-clé)
- *Archivage* des histoires générées pour une utilisation ultérieure
- *Lecture audio* des histoires (Text-to-Speech)
- *Interface intuitive* adaptée aux professionnels de la petite enfance

## 🔧 Stack technique

- *Langage principal*: Java
- *Environnement de développement*: Android Studio
- *API*: OpenAI GPT (pour la génération de contenu)
- *Stockage local*: SharedPreferences
- *Design*: Interface utilisateur intuitive avec des couleurs douces et une typographie adaptée à l'enfance

## 👥 Équipe de développement

Notre application a été développée par une équipe de 5 personnes, chacune responsable de différentes parties du projet:

•⁠  ⁠Yanis: Développement de la page d'accueil
•⁠  ⁠Jean-Jacques: Développement des pages d'inscription, de connexion et faire la gestion de l'API
•⁠  ⁠John: Développement de la page des histoires
•⁠  ⁠Lonny: Développement de la page d'archives
•⁠  ⁠Shun: Développement du formulaire de génération, de la navigation (menu burger), et de la page profil

## 📋 Structure de l'application

L'application est composée de plusieurs écrans:

1. *Écran d'accueil*: Présentation de l'application avec le logo DodoLire
2. *Écran d'inscription/connexion*: Création de compte ou connexion pour les crèches
3. *Formulaire de génération*: Sélection des paramètres pour l'histoire ou la comptine
4. *Vue de l'histoire*: Affichage du contenu généré avec options de lecture audio et d'archivage
5. *Archives*: Consultation des histoires précédemment générées et archivées

## 🚀 Installation

1. Clonez ce dépôt
2. Ouvrez le projet dans Android Studio
3. Configurez votre clé API OpenAI dans le fichier OpenAIService.java
4. Compilez et exécutez l'application sur un émulateur ou un appareil Android

## 🔑 Configuration de l'API OpenAI

Pour utiliser l'application, vous devez disposer d'une clé API OpenAI:

1. Créez un compte sur [OpenAI](https://openai.com/)
2. Générez une clé API dans votre tableau de bord
3. Remplacez la valeur de API_KEY dans le fichier OpenAIService.java

## 📝 Utilisation

1. *Inscription*: Créez un compte pour votre crèche
2. *Formulaire*: Remplissez les champs pour personnaliser l'histoire
   - Type: Histoire ou Comptine
   - Âge: 1, 2 ou 3 ans
   - Thème: Aventure, Animaux, Nature, Héros
   - Genre du personnage: Garçon, Fille, Animal
   - Prénom du personnage
   - Mot-clé pour l'histoire
3. *Génération*: Appuyez sur le bouton "Générer" pour créer l'histoire
4. *Lecture*: Lisez l'histoire ou écoutez-la avec la fonction audio
5. *Archivage*: Sauvegardez les histoires que vous aimez pour les retrouver plus tard

## 👥 Public cible

- Professionnels de la petite enfance
- Crèches et garderies
- Éducateurs travaillant avec des enfants de 1 à 3 ans

## 🔮 Perspectives d'évolution

- Ajout d'illustrations générées par IA pour accompagner les histoires
- Fonctionnalité de partage des histoires entre crèches
- Version pour les parents
- Exportation des histoires en PDF
- Création d'un catalogue d'histoires communautaires

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier LICENSE pour plus de détails.

## 🙏 Remerciements

- OpenAI pour l'API de génération de texte
- L'équipe de développement pour leur travail sur ce projet
- Les crèches partenaires pour leurs retours précieux
\\\`

```java file="main/java/com/example/dodolire/api/OpenAIService.java"
package com.example.dodolire.api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Service pour interagir avec l'API OpenAI
 * Développé par Shun pour le projet DodoLire
 */
public class OpenAIService {
    private static final String TAG = "OpenAIService";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "sk-votre-clé-api"; // Remplacez par votre clé API

    public interface OpenAICallback {
        void onSuccess(String response);
        void onError(String error);
    }

    /**
     * Génère une histoire ou une comptine en utilisant l'API OpenAI
     * @param type Type de contenu (Histoire ou Comptine)
     * @param age Âge de l'enfant
     * @param theme Thème de l'histoire
     * @param genre Genre du personnage principal
     * @param prenom Prénom du personnage principal
     * @param motCle Mot-clé à inclure dans l'histoire
     * @param callback Callback pour gérer la réponse
     */
    public static void generateStory(String type, String age, String theme, String genre, 
                                    String prenom, String motCle, OpenAICallback callback) {
        new GenerateStoryTask(callback).execute(type, age, theme, genre, prenom, motCle);
    }

    private static class GenerateStoryTask extends AsyncTask<String, Void, String> {
        private final OpenAICallback callback;
        private String errorMessage = null;

        GenerateStoryTask(OpenAICallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String age = params[1];
            String theme = params[2];
            String genre = params[3];
            String prenom = params[4];
            String motCle = params[5];

            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
                connection.setDoOutput(true);

                // Construire le prompt pour l'API
                String prompt = buildPrompt(type, age, theme, genre, prenom, motCle);

                // Créer le corps de la requête JSON
                String jsonInputString = buildRequestBody(prompt);

                // Envoyer la requête
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                // Lire la réponse
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        return extractContentFromResponse(response.toString());
                    }
                } else {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        errorMessage = "Erreur API: " + response.toString();
                        return null;
                    }
                }
            } catch (IOException e) {
                errorMessage = "Erreur réseau: " + e.getMessage();
                Log.e(TAG, "Erreur réseau", e);
                return null;
            } catch (JSONException e) {
                errorMessage = "Erreur JSON: " + e.getMessage();
                Log.e(TAG, "Erreur JSON", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                callback.onSuccess(result);
            } else {
                callback.onError(errorMessage != null ? errorMessage : "Erreur inconnue");
            }
        }

        /**
         * Construit le prompt pour l'API OpenAI
         */
        private String buildPrompt(String type, String age, String theme, String genre, String prenom, String motCle) {
            return "Tu es un générateur d'histoires et de comptines pour enfants de crèche. " +
                   "Génère une " + type + " adaptée pour un enfant de " + age + " sur le thème " + theme + ". " +
                   "Le personnage principal est un " + genre + " qui s'appelle " + prenom + ". " +
                   "Utilise le mot-clé suivant dans l'histoire: " + motCle + ". " +
                   "L'histoire doit être courte, adaptée à l'âge de l'enfant, et avoir une fin positive. " +
                   "Si c'est une comptine, elle doit être rythmée et facile à mémoriser. " +
                   "Commence par un titre accrocheur.";
        }

        /**
         * Construit le corps de la requête JSON pour l'API OpenAI
         */
        private String buildRequestBody(String prompt) throws JSONException {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", "gpt-3.5-turbo");
            
            JSONArray messagesArray = new JSONArray();
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", prompt);
            messagesArray.put(message);
            
            jsonBody.put("messages", messagesArray);
            jsonBody.put("temperature", 0.7);
            jsonBody.put("max_tokens", 1000);
            
            return jsonBody.toString();
        }

        /**
         * Extrait le contenu de la réponse JSON de l'API OpenAI
         */
        private String extractContentFromResponse(String jsonResponse) throws JSONException {
            JSONObject responseJson = new JSONObject(jsonResponse);
            JSONArray choices = responseJson.getJSONArray("choices");
            if (choices.length() > 0) {
                JSONObject firstChoice = choices.getJSONObject(0);
                JSONObject message = firstChoice.getJSONObject("message");
                return message.getString("content");
            }
            return "Aucun contenu généré";
        }
    }
}
