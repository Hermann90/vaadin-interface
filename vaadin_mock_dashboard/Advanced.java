public class DataModel {
    private String code;
    private String typeEchantillon;
    
    public DataModel() {}
    
    public DataModel(String code, String typeEchantillon) {
        this.code = code;
        this.typeEchantillon = typeEchantillon;
    }
    
    // Getters et Setters
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
//??

@CssImport("./styles/grid-styles.css")
public class CustomGrid extends VerticalLayout {
    private Grid<DataModel> grid;
    private DataModel selectedItem = null;
    private ListDataProvider<DataModel> dataProvider;
    
    public CustomGrid() {
        setPadding(false);
        setSpacing(false);
        setSizeFull();
        
        initializeGrid();
        populateGrid();
        add(grid);
    }
    
    private void initializeGrid() {
        // Création de la Grid avec le type de modèle
        grid = new Grid<>(DataModel.class);
        grid.setSizeFull();
        
        // Configuration des colonnes
        grid.removeAllColumns();
        grid.addColumn(DataModel::getCode)
            .setHeader("CODE")
            .setAutoWidth(true)
            .setFlexGrow(0);
        
        grid.addColumn(DataModel::getTypeEchantillon)
            .setHeader("TYPE_ECHANTILLON")
            .setAutoWidth(true)
            .setFlexGrow(0);
        
        // Style personnalisé basé sur typeEchantillon
        grid.setClassNameGenerator(item -> {
            if (item.equals(selectedItem)) {
                return "selected-row";
            }
            return getRowClassByType(item.getTypeEchantillon());
        });
        
        // Gestion de la sélection avec sélection unique
        grid.addSelectionListener(event -> {
            // Dans Vaadin 24, la sélection est gérée automatiquement
            // On utilise plutôt le click pour notre logique personnalisée
        });
        
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
        
        // Mise à jour du DataProvider
        dataProvider.getItems().clear();
        dataProvider.getItems().addAll(data);
        dataProvider.refreshAll();
    }
    
    private String getRowClassByType(String typeEchantillon) {
        if (typeEchantillon == null) return "default-row";
        
        switch (typeEchantillon.toUpperCase()) {
            case "ECHANTILLI":
                return "echantilli-row";
            case "SANSDONNEE":
                return "sansdonnee-row";
            default:
                return "default-row";
        }
    }
    
    // Méthode pour obtenir l'élément sélectionné
    public DataModel getSelectedItem() {
        return selectedItem;
    }
    
    // Méthode pour mettre à jour les données
    public void setItems(List<DataModel> items) {
        dataProvider.getItems().clear();
        dataProvider.getItems().addAll(items);
        dataProvider.refreshAll();
    }
}

//€€
/* Styles pour la Grid Vaadin 24 */
:host([theme~="custom-grid"]) {
    --lumo-border-radius: 4px;
}

/* Couleur de sélection */
.selected-row {
    background-color: #e3f2fd !important;
    font-weight: 500;
}

/* Couleurs selon le type d'échantillon */
.echantilli-row {
    background-color: #e8f5e8 !important;
}

.sansdonnee-row {
    background-color: #f5f5f5 !important;
}

.default-row {
    background-color: #ffffff !important;
}

/* Style au survol */
.echantilli-row:hover, 
.sansdonnee-row:hover, 
.default-row:hover {
    background-color: #d6eaff !important;
}

.selected-row:hover {
    background-color: #c5e0ff !important;
}

/* Amélioration de l'apparence de la grid */
vaadin-grid {
    border: 1px solid var(--lumo-contrast-10pct);
    border-radius: 4px;
}

vaadin-grid-cell-content {
    padding: 8px 12px;
}

/* Header styling */
vaadin-grid::part(header-cell) {
    background-color: var(--lumo-primary-color-10pct);
    font-weight: 600;
    border-bottom: 2px solid var(--lumo-primary-color-50pct);
}

//tav
@Route("custom-grid-view")
@PageTitle("Grid Personnalisée")
@CssImport("./styles/grid-styles.css")
public class MainView extends VerticalLayout {
    
    public MainView() {
        setPadding(true);
        setSpacing(true);
        setSizeFull();
        
        // Titre
        H1 title = new H1("Grid avec Sélection et Couleurs");
        title.getStyle().set("margin-bottom", "20px");
        
        // Création de la grid personnalisée
        CustomGrid customGrid = new CustomGrid();
        customGrid.setWidth("100%");
        customGrid.setHeight("500px");
        
        // Ajout des composants à la vue
        add(title, customGrid);
        
        // Optionnel: Affichage de la sélection
        Div selectionInfo = new Div();
        selectionInfo.getStyle()
            .set("padding", "10px")
            .set("background-color", "var(--lumo-primary-color-10pct)")
            .set("border-radius", "4px");
        
        // Mise à jour de l'info de sélection
        customGrid.addAttachListener(event -> {
            updateSelectionInfo(selectionInfo, customGrid);
        });
        
        add(selectionInfo);
    }
    
    private void updateSelectionInfo(Div info, CustomGrid grid) {
        // Cette méthode pourrait être étendue pour réagir aux changements de sélection
        info.setText("Cliquez sur une ligne pour la sélectionner. Double-clic maintient la sélection.");
    }
}
//€€

public class CustomGrid extends VerticalLayout {
    // ... autres méthodes ...
    
    private void initializeGrid() {
        grid = new Grid<>(DataModel.class);
        grid.setSizeFull();
        
        // Configuration des colonnes
        grid.removeAllColumns();
        grid.addColumn(DataModel::getCode).setHeader("CODE").setAutoWidth(true);
        grid.addColumn(DataModel::getTypeEchantillon).setHeader("TYPE_ECHANTILLON").setAutoWidth(true);
        
        // Utilisation de part API pour les styles
        grid.getElement().getThemeList().add("custom-grid");
        
        // Style generator
        grid.setClassNameGenerator(item -> {
            if (item.equals(selectedItem)) {
                return "selected-row";
            }
            return getRowClassByType(item.getTypeEchantillon());
        });
        
        // Gestion des clics
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
    }
}
