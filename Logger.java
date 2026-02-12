import java.io.IOException;
import java.util.logging.*;

public class CustomLogger {

    public static Logger getLogger() throws IOException {
        Logger logger = Logger.getLogger("ImportantLogger");
        logger.setUseParentHandlers(false); // désactive la console par défaut
        logger.setLevel(Level.WARNING);     // ne garde que WARNING et plus

        // Supprime les anciens handlers pour éviter les doublons
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        // Handler vers le fichier important.log
        FileHandler fileHandler = new FileHandler("important.log", true);
        fileHandler.setLevel(Level.WARNING);
        fileHandler.setFormatter(new CustomFormatter());

        logger.addHandler(fileHandler);
        return logger;
    }

    // Formatter pour le format demandé
    static class CustomFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            // Niveau sur 7 caractères, justifié à gauche, espaces à droite
            String level = String.format("%-7s", record.getLevel().getName());

            // Date au format yyyy-mm-dd hh:mm:ss
            String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                              .format(new java.util.Date(record.getMillis()));

            String message = record.getMessage();

            return String.format("%s ; %s ; %s%n", level, date, message);
        }
    }
}
