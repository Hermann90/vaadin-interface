public class CustomGrid extends VerticalLayout {
    private Grid<YourDataModel> grid;
    private YourDataModel selectedItem = null;
    
    // Définition des couleurs
    private final String COULEUR_SELECTION = "#e3f2fd"; // Bleu clair pour la sélection
    private final String COULEUR_ECHANTILLI = "#e8f5e8"; // Vert clair
    private final String COULEUR_SANSDONNEE = "#f5f5f5"; // Gris clair
    
    public CustomGrid() {
        initializeGrid();
        add(grid);
    }
    
    private void initializeGrid() {
        grid = new Grid<>(YourDataModel.class);
        
        // Configuration des colonnes
        grid.setColumns("code", "typeEchantillon");
        
        // Style personnalisé basé sur typeEchantillon
        grid.setClassNameGenerator(item -> {
            if (item.equals(selectedItem)) {
                return "selected-row";
            }
            return getRowClassByType(item.getTypeEchantillon());
        });
        
        // Gestion de la sélection
        grid.addItemClickListener(event -> {
            YourDataModel clickedItem = event.getItem();
            
            if (clickedItem.equals(selectedItem)) {
                // Deuxième clic sur la même ligne - maintient la sélection
                return;
            }
            
            // Désélection de l'élément précédent
            selectedItem = clickedItem;
            
            // Rafraîchissement de la grid pour appliquer les nouveaux styles
            grid.getDataProvider().refreshAll();
        });
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
}
//&&
public class YourDataModel {
    private String code;
    private String typeEchantillon;
    
    // Constructeurs
    public YourDataModel() {}
    
    public YourDataModel(String code, String typeEchantillon) {
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
        YourDataModel that = (YourDataModel) o;
        return Objects.equals(code, that.code) && 
               Objects.equals(typeEchantillon, that.typeEchantillon);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(code, typeEchantillon);
    }
}

//€€

/* Couleur de sélection */
.selected-row {
    background-color: #e3f2fd !important;
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

//&&
private void initializeGrid() {
    grid = new Grid<>(YourDataModel.class);
    grid.setColumns("code", "typeEchantillon");
    
    // Style dynamique basé sur le type et la sélection
    grid.setStyleGenerator(item -> {
        if (item.equals(selectedItem)) {
            return "selected";
        }
        return item.getTypeEchantillon().toLowerCase();
    });
    
    grid.addItemClickListener(event -> {
        YourDataModel clickedItem = event.getItem();
        
        // Si c'est le même item, on ne fait rien (maintient la sélection)
        if (clickedItem.equals(selectedItem)) {
            return;
        }
        
        selectedItem = clickedItem;
        grid.getDataProvider().refreshAll();
    });
}
//@@
/* Styles pour la sélection et les types */
.selected {
    background-color: #e3f2fd !important;
}

.echantilli {
    background-color: #e8f5e8 !important;
}

.sansdonnee {
    background-color: #f5f5f5 !important;
}

//€€
private void populateGrid() {
    List<YourDataModel> data = Arrays.asList(
        new YourDataModel("CTA_TITRES_LCL", "ECHANTILLI"),
        new YourDataModel("CRD_BNQ_POOL", "SANSDONNEE"),
        new YourDataModel("PRS_TYPE_STR", "ECHANTILLI"),
        new YourDataModel("CRD_LIG_TIR_RSU_MSL", "SANSDONNEE"),
        new YourDataModel("DSE_DEC_MDT_GES_PMS", "ECHANTILLI"),
        new YourDataModel("OPE_VIR_EUR_EMI", "SANSDONNEE"),
        new YourDataModel("MVM_SRC_CLA", "ECHANTILLI"),
        new YourDataModel("RBQ_PAG_RAD", "SANSDONNEE"),
        new YourDataModel("DSE_MDT_GES_PMS_DTL", "ECHANTILLI")
    );
    
    grid.setItems(data);
}
