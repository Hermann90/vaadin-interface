package com.example.application.views.upload;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Vue d'upload avec validation : n'autorise que .csv/.txt et lignes de la forme key:value
 */
@Route("upload-validate")
public class UploadValidateView extends VerticalLayout {

    public UploadValidateView() {
        setPadding(true);
        setSpacing(true);

        Text title = new Text("Uploader un fichier (.csv / .txt) contenant des lignes key:value");

        // Buffer en mémoire (adapter si fichier volumineux -> FileBuffer)
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setMaxFiles(1);
        upload.setAcceptedFileTypes("text/plain", "text/csv"); // aide côté navigateur mais pas infaillible
        upload.setDropLabel(new Text("Glisser / déposer un .csv ou .txt"));

        // Affichage du contenu validé
        Grid<Map.Entry<String, String>> grid = new Grid<>();
        grid.addColumn(Map.Entry::getKey).setHeader("Key");
        grid.addColumn(Map.Entry::getValue).setHeader("Value");
        grid.setVisible(false);

        // Pré pour afficher erreurs brutes si besoin
        Pre errorsPre = new Pre();
        errorsPre.setVisible(false);

        // Pattern : autorise espaces autour, capture key (tout avant le premier ':') et value (tout après)
        Pattern linePattern = Pattern.compile("^\\s*([^:]+)\\s*:\\s*(.+)\\s*$");

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            String mimeType = event.getMIMEType(); // parfois vide ou inexact
            long size = event.getContentLength();

            // Vérifier extension côté serveur (plus fiable que le MIME)
            String lower = fileName == null ? "" : fileName.toLowerCase(Locale.ROOT);
            boolean extOk = lower.endsWith(".txt") || lower.endsWith(".csv");
            boolean mimeOk = mimeType == null || mimeType.isEmpty() || mimeType.startsWith("text/");

            if (!extOk || !mimeOk) {
                Notification.show("Fichier refusé : extension ou type incorrect (attendu .txt ou .csv).");
                return;
            }

            // Lire et valider le contenu
            List<String> errorLines = new ArrayList<>();
            Map<String, String> map = new LinkedHashMap<>();
            int maxErrorsToShow = 20;
            int lineNumber = 0;

            try (InputStream in = buffer.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    if (line.trim().isEmpty()) {
                        // Ignorer lignes vides (si tu veux les interdire, ajoute une erreur ici)
                        continue;
                    }

                    Matcher m = linePattern.matcher(line);
                    if (!m.matches()) {
                        if (errorLines.size() < maxErrorsToShow) {
                            errorLines.add("Ligne " + lineNumber + " : \"" + line + "\"");
                        } else if (errorLines.size() == maxErrorsToShow) {
                            errorLines.add("... et plus d'erreurs non affichées ...");
                        }
                    } else {
                        String key = m.group(1).trim();
                        String value = m.group(2).trim();

                        // Optionnel : contrôle plus strict sur key/value (ex : pas d'espaces, etc.)
                        if (key.isEmpty()) {
                            if (errorLines.size() < maxErrorsToShow) {
                                errorLines.add("Ligne " + lineNumber + " : clé vide.");
                            }
                        } else {
                            // gestion des doublons : ici, on remplace la valeur existante
                            map.put(key, value);
                        }
                    }
                }
            } catch (IOException e) {
                Notification.show("Erreur lecture du fichier : " + e.getMessage());
                return;
            }

            if (!errorLines.isEmpty()) {
                // Afficher les erreurs à l'utilisateur
                StringBuilder sb = new StringBuilder();
                sb.append("Format incorrect. Erreurs trouvées (exemples) :\n");
                for (String err : errorLines) {
                    sb.append(err).append("\n");
                }
                errorsPre.setText(sb.toString());
                errorsPre.setVisible(true);
                grid.setVisible(false);
                Notification.show("Le fichier n'est pas au bon format. Voir détails.", 5000, Notification.Position.MIDDLE);
                return;
            }

            // Succès : afficher le contenu dans la grille
            errorsPre.setVisible(false);

            List<Map.Entry<String, String>> entries = new ArrayList<>(map.entrySet());
            grid.setItems(entries);
            grid.setVisible(true);

            Notification.show("Fichier validé (" + map.size() + " paires). Taille : " + size + " octets");
        });

        upload.addFileRejectedListener(event ->
                Notification.show("Fichier rejeté : " + event.getErrorMessage())
        );
        upload.addFailedListener(event ->
                Notification.show("Upload échoué : " + event.getReason().getMessage())
        );

        // Button pour télécharger un exemple (optionnel)
        Button sampleBtn = new Button("Télécharger un exemple", e -> {
            String example = "key1:value1\nkey2:value2\nkey3:value3\n";
            StreamResource resource = new StreamResource("example.txt", () -> new java.io.ByteArrayInputStream(example.getBytes(StandardCharsets.UTF_8)));
            com.vaadin.flow.component.html.Anchor a = new com.vaadin.flow.component.html.Anchor(resource, "Télécharger example.txt");
            a.getElement().setAttribute("download", true);
            add(a);
            a.getElement().callJsFunction("click");
            remove(a);
        });

        add(title, upload, sampleBtn, errorsPre, grid);
    }
}
