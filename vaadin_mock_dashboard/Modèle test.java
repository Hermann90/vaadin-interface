public class DataModel {
    private String code;
    private String typeEchantillon;
    
    public DataModel() {}
    
    public DataModel(String code, String typeEchantillon) {
        this.code = code;
        this.typeEchantillon = typeEchantillon;
    }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getTypeEchantillon() { return typeEchantillon; }
    public void setTypeEchantillon(String typeEchantillon) { 
        this.typeEchantillon = typeEchantillon; 
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataModel that = (DataModel) o;
        return Objects.equals(code, that.code) && 
               Objects.equals(typeEchantillon, that.typeEchantillon);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(code, typeEchantillon);
    }
}
//))
public class CustomGrid extends VerticalLayout {
    private Grid<DataModel> grid;
    private DataModel selectedItem = null;
    private ListDataProvider<DataModel> dataProvider;
    
    // Définition des couleurs
    private final String COULEUR_SELECTION = "#e3f2fd"; // Bleu clair
    private final String COULEUR_ECHANTILLI = "#e8f5e8"; // Vert clair
    private final String COULEUR_SANSDONNEE = "#f5f5f5"; // Gris clair
    private final String COULEUR_DEFAUT = "#ffffff"; // Blanc
    
    public CustomGrid() {
        setPadding(false);
        setSpacing(false);
        setSizeFull();
        
        initializeGrid();
        populateGrid();
        add(grid);
    }
    
    private void initializeGrid() {
        grid = new Grid<>(DataModel.class);
        grid.setSizeFull();
        
        // Supprimer les colonnes par défaut
        grid.removeAllColumns();
        
        // Colonne CODE avec renderer personnalisé
        grid.addComponentColumn(item -> createStyledCell(item, item.getCode()))
            .setHeader("CODE")
            .setAutoWidth(true);
        
        // Colonne TYPE_ECHANTILLON avec renderer personnalisé
        grid.addComponentColumn(item -> createStyledCell(item, item.getTypeEchantillon()))
            .setHeader("TYPE_ECHANTILLON")
            .setAutoWidth(true);
        
        // Gestion du clic sur les items
        grid.addItemClickListener(event -> {
            DataModel clickedItem = event.getItem();
            
            // Si c'est le même item, on maintient la sélection
            if (clickedItem.equals(selectedItem)) {
                return;
            }
            
            // Mise à jour de la sélection
            selectedItem = clickedItem;
            
            // Rafraîchissement pour appliquer les nouveaux styles
            dataProvider.refreshAll();
        });
        
        // Configuration du DataProvider
        dataProvider = new ListDataProvider<>(new ArrayList<>());
        grid.setDataProvider(dataProvider);
    }
    
    private Div createStyledCell(DataModel item, String content) {
        Div cell = new Div();
        cell.setText(content);
        cell.getStyle()
            .set("padding", "8px 12px")
            .set("height", "100%")
            .set("display", "flex")
            .set("align-items", "center");
        
        // Appliquer la couleur de fond selon le type et la sélection
        String backgroundColor = getBackgroundColor(item);
        cell.getStyle().set("background-color", backgroundColor);
        
        // Style supplémentaire pour l'élément sélectionné
        if (item.equals(selectedItem)) {
            cell.getStyle()
                .set("font-weight", "500")
                .set("border", "2px solid #1976d2");
        } else {
            cell.getStyle().set("border", "none");
        }
        
        return cell;
    }
    
    private String getBackgroundColor(DataModel item) {
        // Si l'item est sélectionné, retourner la couleur de sélection
        if (item.equals(selectedItem)) {
            return COULEUR_SELECTION;
        }
        
        // Sinon, retourner la couleur selon le type d'échantillon
        if (item.getTypeEchantillon() == null) {
            return COULEUR_DEFAUT;
        }
        
        switch (item.getTypeEchantillon().toUpperCase()) {
            case "ECHANTILLI":
                return COULEUR_ECHANTILLI;
            case "SANSDONNEE":
                return COULEUR_SANSDONNEE;
            default:
                return COULEUR_DEFAUT;
        }
    }
    
    private void populateGrid() {
        List<DataModel> data = Arrays.asList(
            new DataModel("CTA_TITRES_LCL", "ECHANTILLI"),
            new DataModel("CRD_BNQ_POOL", "SANSDONNEE"),
            new DataModel("PRS_TYPE_STR", "ECHANTILLI"),
            new DataModel("CRD_LIG_TIR_RSU_MSL", "SANSDONNEE"),
            new DataModel("DSE_DEC_MDT_GES_PMS", "ECHANTILLI"),
            new DataModel("OPE_VIR_EUR_EMI", "SANSDONNEE"),
            new DataModel("MVM_SRC_CLA", "ECHANTILLI"),
            new DataModel("RBQ_PAG_RAD", "SANSDONNEE"),
            new DataModel("DSE_MDT_GES_PMS_DTL", "ECHANTILLI")
        );
        
        dataProvider.getItems().clear();
        dataProvider.getItems().addAll(data);
        dataProvider.refreshAll();
    }
    
    public DataModel getSelectedItem() {
        return selectedItem;
    }
    
    public void setItems(List<DataModel> items) {
        dataProvider.getItems().clear();
        dataProvider.getItems().addAll(items);
        dataProvider.refreshAll();
    }
}

//&&
public class CustomGrid extends VerticalLayout {
    private Grid<DataModel> grid;
    private DataModel selectedItem = null;
    private ListDataProvider<DataModel> dataProvider;
    
    private final String COULEUR_SELECTION = "#e3f2fd";
    private final String COULEUR_ECHANTILLI = "#e8f5e8";
    private final String COULEUR_SANSDONNEE = "#f5f5f5";
    private final String COULEUR_DEFAUT = "#ffffff";
    
    public CustomGrid() {
        setPadding(false);
        setSpacing(false);
        setSizeFull();
        
        initializeGrid();
        populateGrid();
        add(grid);
    }
    
    private void initializeGrid() {
        grid = new Grid<>();
        grid.setSizeFull();
        
        // Créer les colonnes avec renderer personnalisé pour toute la ligne
        Grid.Column<DataModel> codeColumn = grid.addComponentColumn(item -> 
            createCell(item, item.getCode())
        ).setHeader("CODE").setAutoWidth(true);
        
        Grid.Column<DataModel> typeColumn = grid.addComponentColumn(item -> 
            createCell(item, item.getTypeEchantillon())
        ).setHeader("TYPE_ECHANTILLON").setAutoWidth(true);
        
        // Appliquer un style cohérent à toutes les cellules de la même ligne
        codeColumn.setFlexGrow(0);
        typeColumn.setFlexGrow(0);
        
        // Gestion du clic
        grid.addItemClickListener(event -> {
            DataModel clickedItem = event.getItem();
            
            if (clickedItem.equals(selectedItem)) {
                return; // Maintient la sélection
            }
            
            selectedItem = clickedItem;
            dataProvider.refreshAll();
        });
        
        dataProvider = new ListDataProvider<>(new ArrayList<>());
        grid.setDataProvider(dataProvider);
        
        // Style global de la grid
        grid.getStyle()
            .set("border", "1px solid #ddd")
            .set("border-radius", "4px");
    }
    
    private Div createCell(DataModel item, String content) {
        Div cell = new Div();
        cell.setText(content);
        
        // Style de base pour toutes les cellules
        cell.getStyle()
            .set("padding", "var(--lumo-space-s) var(--lumo-space-m)")
            .set("height", "100%")
            .set("display", "flex")
            .set("align-items", "center")
            .set("box-sizing", "border-box");
        
        // Appliquer la couleur de fond
        String backgroundColor = getBackgroundColor(item);
        cell.getStyle().set("background-color", backgroundColor);
        
        // Style pour l'élément sélectionné
        if (item.equals(selectedItem)) {
            cell.getStyle()
                .set("font-weight", "600")
                .set("color", "#1976d2")
                .set("border-left", "4px solid #1976d2");
        } else {
            cell.getStyle()
                .set("font-weight", "400")
                .set("color", "inherit")
                .set("border-left", "none");
        }
        
        return cell;
    }
    
    private String getBackgroundColor(DataModel item) {
        if (item.equals(selectedItem)) {
            return COULEUR_SELECTION;
        }
        
        if (item.getTypeEchantillon() == null) {
            return COULEUR_DEFAUT;
        }
        
        switch (item.getTypeEchantillon().toUpperCase()) {
            case "ECHANTILLI":
                return COULEUR_ECHANTILLI;
            case "SANSDONNEE":
                return COULEUR_SANSDONNEE;
            default:
                return COULEUR_DEFAUT;
        }
    }
    
    private void populateGrid() {
        List<DataModel> data = Arrays.asList(
            new DataModel("CTA_TITRES_LCL", "ECHANTILLI"),
            new DataModel("CRD_BNQ_POOL", "SANSDONNEE"),
            new DataModel("PRS_TYPE_STR", "ECHANTILLI"),
            new DataModel("CRD_LIG_TIR_RSU_MSL", "SANSDONNEE"),
            new DataModel("DSE_DEC_MDT_GES_PMS", "ECHANTILLI"),
            new DataModel("OPE_VIR_EUR_EMI", "SANSDONNEE"),
            new DataModel("MVM_SRC_CLA", "ECHANTILLI"),
            new DataModel("RBQ_PAG_RAD", "SANSDONNEE"),
            new DataModel("DSE_MDT_GES_PMS_DTL", "ECHANTILLI")
        );
        
        dataProvider.getItems().clear();
        dataProvider.getItems().addAll(data);
        dataProvider.refreshAll();
    }
}
//€&
@Route("custom-grid-view")
@PageTitle("Grid Personnalisée")
public class MainView extends VerticalLayout {
    
    public MainView() {
        setPadding(true);
        setSpacing(true);
        setSizeFull();
        
        H1 title = new H1("Grid avec Couleurs Dynamiques");
        title.getStyle().set("margin-bottom", "20px");
        
        CustomGrid customGrid = new CustomGrid();
        customGrid.setWidth("100%");
        customGrid.setHeight("500px");
        
        // Information sur la sélection
        Div infoPanel = new Div();
        infoPanel.getStyle()
            .set("padding", "15px")
            .set("background-color", "#f8f9fa")
            .set("border", "1px solid #dee2e6")
            .set("border-radius", "4px")
            .set("margin-bottom", "15px");
        
        infoPanel.setText("✓ ECHANTILLI = Vert clair | ✓ SANSDONNEE = Gris clair | ✓ Sélectionné = Bleu clair");
        
        add(title, infoPanel, customGrid);
    }
}
