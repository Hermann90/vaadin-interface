import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class YamlSchemaReplacer {

    public static void main(String[] args) {
        String filePath = "config.yaml";
        
        try {
            // Nouveau schéma à mettre en place
            Map<String, String> newSchema = new LinkedHashMap<>();
            newSchema.put("ID", "integer");
            newSchema.put("NOM", "string");
            newSchema.put("PRENOM", "string");
            newSchema.put("AGE", "integer");
            newSchema.put("EMAIL", "string");
            newSchema.put("DATE_CREATION", "timestamp");
            
            // Remplacer le schéma
            replaceSchema(filePath, newSchema);
            
            System.out.println("Schéma remplacé avec succès !");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void replaceSchema(String filePath, Map<String, String> newSchema) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filePath));
        List<String> newLines = new ArrayList<>();
        
        boolean inSchemaSection = false;
        boolean schemaWritten = false;
        
        for (String line : lines) {
            // Détecter le début de la section schema
            if (line.trim().equals("schema:")) {
                inSchemaSection = true;
                schemaWritten = true;
                
                // Écrire la ligne "schema:" et le NOUVEAU schéma
                newLines.add(line);
                writeNewSchema(newLines, newSchema, getIndentLevel(line));
                continue;
            }
            
            // Si on est dans la section schema, sauter les anciennes lignes
            if (inSchemaSection) {
                // Sortir de la section quand on trouve une ligne avec moins d'indentation
                if (getIndentLevel(line) <= 2 && !line.trim().isEmpty() && !line.trim().startsWith(" ")) {
                    inSchemaSection = false;
                    newLines.add(line);
                }
                // Ignorer les anciennes lignes du schéma
            } else {
                newLines.add(line);
            }
        }
        
        // Si le schéma n'existait pas, l'ajouter à la fin
        if (!schemaWritten) {
            addSchemaToEnd(newLines, newSchema);
        }
        
        // Réécrire le fichier
        Files.write(Path.of(filePath), newLines);
    }

    private static void writeNewSchema(List<String> newLines, Map<String, String> newSchema, int indentLevel) {
        String indent = " ".repeat(indentLevel + 2);
        
        for (Map.Entry<String, String> entry : newSchema.entrySet()) {
            newLines.add(indent + entry.getKey() + ":  " + entry.getValue());
        }
    }

    private static void addSchemaToEnd(List<String> newLines, Map<String, String> newSchema) {
        // Trouver la fin de la section csv
        int csvEndIndex = -1;
        for (int i = 0; i < newLines.size(); i++) {
            if (newLines.get(i).trim().equals("csv:")) {
                // Chercher la prochaine section de même niveau
                int csvIndent = getIndentLevel(newLines.get(i));
                for (int j = i + 1; j < newLines.size(); j++) {
                    String currentLine = newLines.get(j);
                    if (!currentLine.trim().isEmpty() && getIndentLevel(currentLine) <= csvIndent) {
                        csvEndIndex = j;
                        break;
                    }
                }
                break;
            }
        }
        
        if (csvEndIndex == -1) {
            // Ajouter à la fin du fichier
            newLines.add("  schema:");
            for (Map.Entry<String, String> entry : newSchema.entrySet()) {
                newLines.add("    " + entry.getKey() + ":  " + entry.getValue());
            }
        } else {
            // Insérer avant la section suivante
            newLines.add(csvEndIndex, "  schema:");
            for (Map.Entry<String, String> entry : newSchema.entrySet()) {
                newLines.add(csvEndIndex + 1, "    " + entry.getKey() + ":  " + entry.getValue());
            }
        }
    }

    private static int getIndentLevel(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') count++;
            else break;
        }
        return count;
    }
}
