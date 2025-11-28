public class CustomGrid extends VerticalLayout {
    private Grid<DataModel> grid;
    private DataModel selectedItem = null;
    private ListDataProvider<DataModel> dataProvider;
    private TextField codeFilter;
    private ComboBox<String> typeFilter;
    
    private final String COULEUR_SELECTION = "#e3f2fd";
    private final String COULEUR_ECHANTILLI = "#e8f5e8";
    private final String COULEUR_SANSDONNEE = "#f5f5f5";
    private final String COULEUR_DEFAUT = "#ffffff";
    
    public CustomGrid() {
        setPadding(false);
        setSpacing(true);
        setSizeFull();
        
        initializeFilters();
        initializeGrid();
        populateGrid();
        
        add(createFilterHeader(), grid);
    }
    
    private void initializeFilters() {
        codeFilter = new TextField();
        codeFilter.setPlaceholder("Filtrer...");
        codeFilter.setClearButtonVisible(true);
        codeFilter.setWidth("100%");
        
        typeFilter = new ComboBox<>();
        typeFilter.setPlaceholder("Filtrer...");
        typeFilter.setItems("ECHANTILLI", "SANSDONNEE");
        typeFilter.setClearButtonVisible(true);
        typeFilter.setWidth("100%");
        
        codeFilter.addValueChangeListener(e -> applyFilter());
        typeFilter.addValueChangeListener(e -> applyFilter());
    }
    
    private Component createFilterHeader() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth("100%");
        headerLayout.setPadding(true);
        headerLayout.setSpacing(true);
        headerLayout.getStyle()
            .set("border-bottom", "1px solid #e0e0e0")
            .set("background-color", "#f8f9fa");
        
        // Bouton réinitialiser
        Button resetButton = new Button("Réinitialiser", VaadinIcon.REFRESH.create());
        resetButton.addClickListener(e -> resetFilters());
        
        headerLayout.add(resetButton);
        headerLayout.setFlexGrow(1, resetButton);
        headerLayout.setJustifyContentMode(JustifyContentMode.END);
        
        return headerLayout;
    }
    
    private void initializeGrid() {
        grid = new Grid<>();
        grid.setSizeFull();
        
        // Colonne CODE avec header personnalisé
        Grid.Column<DataModel> codeColumn = grid.addComponentColumn(item -> 
            createCell(item, item.getCode())
        ).setHeader(createColumnHeader("CODE", codeFilter))
         .setAutoWidth(true)
         .setResizable(true);
        
        // Colonne TYPE_ECHANTILLON avec header personnalisé
        Grid.Column<DataModel> typeColumn = grid.addComponentColumn(item -> 
            createCell(item, item.getTypeEchantillon())
        ).setHeader(createColumnHeader("TYPE_ECHANTILLON", typeFilter))
         .setAutoWidth(true)
         .setResizable(true);
        
        codeColumn.setFlexGrow(0);
        typeColumn.setFlexGrow(0);
        
        // Gestion du clic
        grid.addItemClickListener(event -> {
            DataModel clickedItem = event.getItem();
            
            if (clickedItem.equals(selectedItem)) {
                return;
            }
            
            selectedItem = clickedItem;
            dataProvider.refreshAll();
        });
        
        dataProvider = new ListDataProvider<>(new ArrayList<>());
        grid.setDataProvider(dataProvider);
        
        grid.getStyle()
            .set("border", "1px solid #ddd")
            .set("border-radius", "4px");
    }
    
    private Component createColumnHeader(String title, Component filterComponent) {
        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.setPadding(false);
        headerLayout.setSpacing(false);
        headerLayout.setWidth("100%");
        
        // Layout pour le titre et le chevron
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setWidth("100%");
        titleLayout.setPadding(false);
        titleLayout.setSpacing(true);
        titleLayout.setAlignItems(Alignment.CENTER);
        
        // Titre de la colonne
        Span titleSpan = new Span(title);
        titleSpan.getStyle()
            .set("font-weight", "600")
            .set("font-size", "var(--lumo-font-size-s)");
        
        // Chevron pour le filtre (icône flèche vers le bas)
        Icon chevronIcon = VaadinIcon.CHEVRON_DOWN.create();
        chevronIcon.setSize("var(--lumo-icon-size-s)");
        chevronIcon.getStyle()
            .set("color", "var(--lumo-contrast-60pct)")
            .set("cursor", "pointer");
        
        // Au clic sur le chevron, on focus le champ de filtre
        chevronIcon.addClickListener(e -> {
            filterComponent.getElement().callJsFunction("focus");
        });
        
        titleLayout.add(titleSpan, chevronIcon);
        titleLayout.setFlexGrow(1, titleSpan);
        titleLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        
        // Champ de filtre
        filterComponent.getStyle()
            .set("font-size", "var(--lumo-font-size-xs)")
            .set("margin-top", "4px");
        
        headerLayout.add(titleLayout, filterComponent);
        
        return headerLayout;
    }
    
    private Div createCell(DataModel item, String content) {
        Div cell = new Div();
        cell.setText(content);
        
        cell.getStyle()
            .set("padding", "var(--lumo-space-s) var(--lumo-space-m)")
            .set("height", "100%")
            .set("display", "flex")
            .set("align-items", "center")
            .set("box-sizing", "border-box");
        
        String backgroundColor = getBackgroundColor(item);
        cell.getStyle().set("background-color", backgroundColor);
        
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
    
    private void applyFilter() {
        String codeFilterValue = codeFilter.getValue();
        String typeFilterValue = typeFilter.getValue();
        
        dataProvider.clearFilters();
        
        if ((codeFilterValue != null && !codeFilterValue.isEmpty()) || typeFilterValue != null) {
            dataProvider.addFilter(item -> {
                boolean matchesCode = true;
                boolean matchesType = true;
                
                if (codeFilterValue != null && !codeFilterValue.isEmpty()) {
                    matchesCode = item.getCode() != null && 
                                 item.getCode().toLowerCase()
                                     .contains(codeFilterValue.toLowerCase());
                }
                
                if (typeFilterValue != null) {
                    matchesType = typeFilterValue.equals(item.getTypeEchantillon());
                }
                
                return matchesCode && matchesType;
            });
        }
    }
    
    private void resetFilters() {
        codeFilter.clear();
        typeFilter.clear();
        dataProvider.clearFilters();
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

//))
private Component createColumnHeader(String title, Component filterComponent) {
    VerticalLayout headerLayout = new VerticalLayout();
    headerLayout.setPadding(false);
    headerLayout.setSpacing(false);
    headerLayout.setWidth("100%");
    
    // Container cliquable pour le titre
    HorizontalLayout titleContainer = new HorizontalLayout();
    titleContainer.setWidth("100%");
    titleContainer.setPadding(false);
    titleContainer.setSpacing(true);
    titleContainer.setAlignItems(Alignment.CENTER);
    titleContainer.getStyle()
        .set("cursor", "pointer")
        .set("padding", "2px 4px")
        .set("border-radius", "3px");
    
    // Effet hover sur le titre
    titleContainer.addMouseEnterListener(e -> {
        titleContainer.getStyle().set("background-color", "var(--lumo-contrast-5pct)");
    });
    titleContainer.addMouseLeaveListener(e -> {
        titleContainer.getStyle().remove("background-color");
    });
    
    // Titre de la colonne
    Span titleSpan = new Span(title);
    titleSpan.getStyle()
        .set("font-weight", "600")
        .set("font-size", "var(--lumo-font-size-s)")
        .set("user-select", "none");
    
    // Chevron pour le filtre
    Icon chevronIcon = VaadinIcon.CHEVRON_DOWN.create();
    chevronIcon.setSize("var(--lumo-icon-size-s)");
    chevronIcon.getStyle()
        .set("color", "var(--lumo-contrast-60pct)")
        .set("transition", "transform 0.2s");
    
    // Au clic sur le titre ou le chevron, on focus le champ de filtre
    titleContainer.addClickListener(e -> {
        filterComponent.getElement().callJsFunction("focus");
        
        // Animation du chevron (optionnel)
        chevronIcon.getElement().executeJs(
            "this.animate([{ transform: 'rotate(0deg)' }, { transform: 'rotate(180deg)' }], { duration: 200 });"
        );
    });
    
    titleContainer.add(titleSpan, chevronIcon);
    titleContainer.setFlexGrow(1, titleSpan);
    titleContainer.setJustifyContentMode(JustifyContentMode.BETWEEN);
    
    // Champ de filtre - caché par défaut et affiché au survol
    filterComponent.getStyle()
        .set("font-size", "var(--lumo-font-size-xs)")
        .set("margin-top", "4px")
        .set("max-height", "0")
        .set("opacity", "0")
        .set("transition", "all 0.3s ease")
        .set("overflow", "hidden");
    
    // Afficher le filtre au survol de l'en-tête
    headerLayout.addMouseEnterListener(e -> {
        filterComponent.getStyle()
            .set("max-height", "40px")
            .set("opacity", "1");
    });
    
    headerLayout.addMouseLeaveListener(e -> {
        // Ne cacher le filtre que s'il n'est pas focus
        UI.getCurrent().getPage().executeJs(
            "return document.activeElement !== $0", 
            filterComponent.getElement()
        ).then(result -> {
            if (result.asBoolean()) {
                filterComponent.getStyle()
                    .set("max-height", "0")
                    .set("opacity", "0");
            }
        });
    });
    
    // Cacher le filtre quand il perd le focus
    filterComponent.getElement().addEventListener("blur", e -> {
        filterComponent.getStyle()
            .set("max-height", "0")
            .set("opacity", "0");
    }).addEventData("event.stopPropagation()");
    
    headerLayout.add(titleContainer, filterComponent);
    
    return headerLayout;
}
//jh
@Route("custom-grid-view")
@PageTitle("Grid avec Filtres Intégrés")
public class MainView extends VerticalLayout {
    
    public MainView() {
        setPadding(true);
        setSpacing(true);
        setSizeFull();
        
        H1 title = new H1("Grid avec Filtres Intégrés dans les En-têtes");
        title.getStyle().set("margin-bottom", "10px");
        
        Div description = new Div();
        description.setText("Cliquez sur les chevrons ▼ dans les en-têtes de colonnes pour accéder aux filtres.");
        description.getStyle()
            .set("color", "var(--lumo-secondary-text-color)")
            .set("margin-bottom", "20px");
        
        CustomGrid customGrid = new CustomGrid();
        customGrid.setWidth("100%");
        customGrid.setHeight("600px");
        
        add(title, description, customGrid);
    }
}

//€€

