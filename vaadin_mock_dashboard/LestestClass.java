import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlConfigReader {
    public static SparkConfig readConfig(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        return mapper.readValue(new File(filePath), SparkConfig.class);
    }
    
    public static void main(String[] args) {
        try {
            SparkConfig config = YamlConfigReader.readConfig("spark-config.yml");
            
            // Print configuration values
            System.out.println("=== SPARK CONFIGURATION ===");
            Map<String, String> sparkParams = config.getSpark().getParameters();
            if (sparkParams != null) {
                for (Map.Entry<String, String> entry : sparkParams.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            } else {
                System.out.println("Spark parameters are null!");
            }
            
        } catch (IOException e) {
            System.err.println("Error reading YAML configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

class SparkConfig {
    private SparkSection spark;
    private String extraArgRegex;
    private CsvConfig csv;
    private List<AddColumn> addColumns;
    private PeriodFilenameConf periodFilenameConf;
    private PartitionConfig partition;

    public SparkSection getSpark() { return spark; }
    public void setSpark(SparkSection spark) { this.spark = spark; }
    
    public String getExtraArgRegex() { return extraArgRegex; }
    public void setExtraArgRegex(String extraArgRegex) { this.extraArgRegex = extraArgRegex; }
    
    public CsvConfig getCsv() { return csv; }
    public void setCsv(CsvConfig csv) { this.csv = csv; }
    
    public List<AddColumn> getAddColumns() { return addColumns; }
    public void setAddColumns(List<AddColumn> addColumns) { this.addColumns = addColumns; }
    
    public PeriodFilenameConf getPeriodFilenameConf() { return periodFilenameConf; }
    public void setPeriodFilenameConf(PeriodFilenameConf periodFilenameConf) { this.periodFilenameConf = periodFilenameConf; }
    
    public PartitionConfig getPartition() { return partition; }
    public void setPartition(PartitionConfig partition) { this.partition = partition; }
}

class SparkSection {
    private Map<String, String> parameters = new HashMap<>();
    
    // Use @JsonAnySetter to capture all properties under "spark:"
    @JsonAnySetter
    public void setParameter(String key, Object value) {
        if (value instanceof Map) {
            // This handles the nested parameters section
            Map<?, ?> paramsMap = (Map<?, ?>) value;
            for (Map.Entry<?, ?> entry : paramsMap.entrySet()) {
                parameters.put(entry.getKey().toString(), entry.getValue().toString());
            }
        } else {
            parameters.put(key, value.toString());
        }
    }
    
    public Map<String, String> getParameters() { 
        return parameters; 
    }
    
    // Convenience methods
    public String getSparkSerializer() { return parameters.get("spark.serializer"); }
    public String getDriverCores() { return parameters.get("spark.driver.cores"); }
    public String getDriverMemory() { return parameters.get("spark.driver.memory"); }
    public String getExecutorInstances() { return parameters.get("spark.executor.instances"); }
    public String getExecutorCores() { return parameters.get("spark.executor.cores"); }
    public String getExecutorMemory() { return parameters.get("spark.executor.memory"); }
}

// Alternative approach if the above doesn't work:
class SparkSectionAlternative {
    private Parameters parameters;

    public Parameters getParameters() { return parameters; }
    public void setParameters(Parameters parameters) { this.parameters = parameters; }
    
    // Inner class to represent the parameters section
    public static class Parameters {
        @JsonProperty("spark.serializer")
        private String sparkSerializer;
        
        @JsonProperty("spark.driver.cores")
        private String driverCores;
        
        @JsonProperty("spark.driver.memory")
        private String driverMemory;
        
        @JsonProperty("spark.executor.instances")
        private String executorInstances;
        
        @JsonProperty("spark.executor.cores")
        private String executorCores;
        
        @JsonProperty("spark.executor.memory")
        private String executorMemory;

        // Getters and setters
        public String getSparkSerializer() { return sparkSerializer; }
        public void setSparkSerializer(String sparkSerializer) { this.sparkSerializer = sparkSerializer; }
        
        public String getDriverCores() { return driverCores; }
        public void setDriverCores(String driverCores) { this.driverCores = driverCores; }
        
        public String getDriverMemory() { return driverMemory; }
        public void setDriverMemory(String driverMemory) { this.driverMemory = driverMemory; }
        
        public String getExecutorInstances() { return executorInstances; }
        public void setExecutorInstances(String executorInstances) { this.executorInstances = executorInstances; }
        
        public String getExecutorCores() { return executorCores; }
        public void setExecutorCores(String executorCores) { this.executorCores = executorCores; }
        
        public String getExecutorMemory() { return executorMemory; }
        public void setExecutorMemory(String executorMemory) { this.executorMemory = executorMemory; }
    }
}

// The rest of the classes remain the same as previous implementation
class CsvConfig {
    private String inputDirPath;
    private String errorDirPath;
    private String histoDirPath;
    private String outputDirPath;
    private String delimiter;
    private boolean inferSchema;
    private boolean havingHeader;
    private String comment;
    private String nullValue;
    private String encoding;
    private boolean checkExtension;
    private boolean allowNoInputFile;
    private boolean purgeOutputDirPathBeforeExecution;
    private boolean ignoreLeadingWhiteSpace;
    private boolean ignoreTrailingWhiteSpace;
    private boolean allowMissingOrExtraColumn;
    private String saveFormat;
    private String saveMode;
    private Map<String, String> readerExtraOptions;
    private Map<String, String> writerExtraOptions;
    private Map<String, String> schema;

    // Getters and setters
    public String getInputDirPath() { return inputDirPath; }
    public void setInputDirPath(String inputDirPath) { this.inputDirPath = inputDirPath; }
    // ... other getters and setters
}

class PeriodFilenameConf {
    private String regexRule;
    private String regexReplacement;
    // ... getters and setters
}

class PartitionConfig {
    private String mode;
    private String overwriteSaveMode;
    private String periodPartitionRule;
    private List<PartitionColumn> columns;
    // ... getters and setters
}

class PartitionColumn {
    private String name;
    private String type;
    // ... getters and setters
}

class AddColumn {
    private String columnName;
    private String columnValue;
    private String type;
    // ... getters and setters
}


public class DebugYamlReader {
    public static void debugYamlStructure(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Map<String, Object> rawConfig = mapper.readValue(new File(filePath), Map.class);
        
        System.out.println("=== RAW YAML STRUCTURE ===");
        printMap("", rawConfig);
    }
    
    private static void printMap(String indent, Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(indent + entry.getKey() + ": " + 
                (entry.getValue() instanceof Map ? "" : entry.getValue()));
            
            if (entry.getValue() instanceof Map) {
                printMap(indent + "  ", (Map<String, Object>) entry.getValue());
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        debugYamlStructure("spark-config.yml");
    }
}
