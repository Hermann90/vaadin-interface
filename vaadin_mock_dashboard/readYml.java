import java.util.List;
import java.util.Map;

public class SparkConfig {
    private SparkParameters parameters;
    private String extraArgRegex;
    private CsvConfig csv;
    private PeriodFilenameConf periodFilenameConf;
    private PartitionConfig partition;
    private List<AddColumn> addColumns;

    // Getters and setters
    public SparkParameters getParameters() { return parameters; }
    public void setParameters(SparkParameters parameters) { this.parameters = parameters; }
    
    public String getExtraArgRegex() { return extraArgRegex; }
    public void setExtraArgRegex(String extraArgRegex) { this.extraArgRegex = extraArgRegex; }
    
    public CsvConfig getCsv() { return csv; }
    public void setCsv(CsvConfig csv) { this.csv = csv; }
    
    public PeriodFilenameConf getPeriodFilenameConf() { return periodFilenameConf; }
    public void setPeriodFilenameConf(PeriodFilenameConf periodFilenameConf) { this.periodFilenameConf = periodFilenameConf; }
    
    public PartitionConfig getPartition() { return partition; }
    public void setPartition(PartitionConfig partition) { this.partition = partition; }
    
    public List<AddColumn> getAddColumns() { return addColumns; }
    public void setAddColumns(List<AddColumn> addColumns) { this.addColumns = addColumns; }
}

public class SparkParameters {
    private String sparkSerializer;
    private String driverCores;
    private String driverMemory;
    private String executorInstances;
    private String executorCores;
    private String executorMemory;

    // Getters and setters with @JsonProperty for YAML mapping
    @JsonProperty("spark.serializer")
    public String getSparkSerializer() { return sparkSerializer; }
    public void setSparkSerializer(String sparkSerializer) { this.sparkSerializer = sparkSerializer; }
    
    @JsonProperty("spark.driver.cores")
    public String getDriverCores() { return driverCores; }
    public void setDriverCores(String driverCores) { this.driverCores = driverCores; }
    
    @JsonProperty("spark.driver.memory")
    public String getDriverMemory() { return driverMemory; }
    public void setDriverMemory(String driverMemory) { this.driverMemory = driverMemory; }
    
    @JsonProperty("spark.executor.instances")
    public String getExecutorInstances() { return executorInstances; }
    public void setExecutorInstances(String executorInstances) { this.executorInstances = executorInstances; }
    
    @JsonProperty("spark.executor.cores")
    public String getExecutorCores() { return executorCores; }
    public void setExecutorCores(String executorCores) { this.executorCores = executorCores; }
    
    @JsonProperty("spark.executor.memory")
    public String getExecutorMemory() { return executorMemory; }
    public void setExecutorMemory(String executorMemory) { this.executorMemory = executorMemory; }
}

public class CsvConfig {
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
    
    // ... other getters and setters for all fields
}

public class PeriodFilenameConf {
    private String regexRule;
    private String regexReplacement;

    // Getters and setters
    public String getRegexRule() { return regexRule; }
    public void setRegexRule(String regexRule) { this.regexRule = regexRule; }
    
    public String getRegexReplacement() { return regexReplacement; }
    public void setRegexReplacement(String regexReplacement) { this.regexReplacement = regexReplacement; }
}

public class PartitionConfig {
    private String mode;
    private String overwriteSaveMode;
    private String periodPartitionRule;
    private List<PartitionColumn> columns;

    // Getters and setters
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    
    public String getOverwriteSaveMode() { return overwriteSaveMode; }
    public void setOverwriteSaveMode(String overwriteSaveMode) { this.overwriteSaveMode = overwriteSaveMode; }
    
    public String getPeriodPartitionRule() { return periodPartitionRule; }
    public void setPeriodPartitionRule(String periodPartitionRule) { this.periodPartitionRule = periodPartitionRule; }
    
    public List<PartitionColumn> getColumns() { return columns; }
    public void setColumns(List<PartitionColumn> columns) { this.columns = columns; }
}

public class PartitionColumn {
    private String name;
    private String type;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

public class AddColumn {
    private String columnName;
    private String columnValue;
    private String type;

    // Getters and setters
    public String getColumnName() { return columnName; }
    public void setColumnName(String columnName) { this.columnName = columnName; }
    
    public String getColumnValue() { return columnValue; }
    public void setColumnValue(String columnValue) { this.columnValue = columnValue; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
