public class SelectableGrid<T> extends VerticalLayout {
    private Grid<T> grid;
    private List<SelectionListener<T>> selectionListeners = new ArrayList<>();
    private T currentSelection;
    private boolean allowDeselection = false;
    private Function<T, String> rowStyleGenerator;

    public SelectableGrid(Class<T> beanType) {
        grid = new Grid<>(beanType);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        
        setupSelectionHandling();
        add(grid);
    }

    private void setupSelectionHandling() {
        grid.addItemClickListener(event -> {
            T clickedItem = event.getItem();
            
            if (clickedItem.equals(currentSelection)) {
                if (allowDeselection) {
                    clearSelection();
                } else {
                    refreshSelection();
                }
            } else {
                setSelectedItem(clickedItem);
            }
        });
        
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null && !allowDeselection && currentSelection != null) {
                grid.asSingleSelect().setValue(currentSelection);
            }
        });
    }

    // Méthode pour définir le générateur de styles avec couleurs HTML
    public void setRowStyleGenerator(Function<T, String> styleGenerator) {
        this.rowStyleGenerator = styleGenerator;
        grid.setClassNameGenerator(styleGenerator);
    }

    // Méthode pour appliquer des styles inline directement
    public void setRowStyleProvider(Function<T, String> styleProvider) {
        grid.setClassNameGenerator(person -> {
            String style = styleProvider.apply(person);
            if (style != null && !style.isEmpty()) {
                // Appliquer le style inline via JavaScript
                getElement().executeJs(
                    "setTimeout(() => {" +
                    "  const grid = $0;" +
                    "  const items = grid.querySelectorAll('tr');" +
                    "  items.forEach((row, index) => {" +
                    "    const item = grid.get('items')[index];" +
                    "    if (item && $1(item)) {" +
                    "      row.style = $1(item);" +
                    "    }" +
                    "  });" +
                    "}, 100);",
                    grid.getElement(), styleProvider
                );
            }
            return style;
        });
    }

    // ... (les autres méthodes restent les mêmes)
}
//@@

public class PersonGrid extends SelectableGrid<Person> {

    public PersonGrid() {
        super(Person.class);
        configureGrid();
        setupRowColors();
        loadSampleData();
    }

    private void configureGrid() {
        Grid<Person> grid = getGrid();
        
        grid.removeAllColumns();
        grid.addColumn(Person::getId).setHeader("ID").setWidth("80px");
        grid.addColumn(Person::getName).setHeader("Nom").setAutoWidth(true);
        grid.addColumn(Person::getEmail).setHeader("Email").setAutoWidth(true);
        grid.addColumn(Person::getStatus).setHeader("Statut").setWidth("120px");
        grid.addColumn(Person::getDepartment).setHeader("Département").setWidth("150px");
        
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    private void setupRowColors() {
        // Appliquer les couleurs directement via les styles inline
        setRowStyleGenerator(person -> {
            if (person == null) return null;
            
            // Styles basés sur le statut avec couleurs HTML
            switch (person.getStatus()) {
                case "ACTIVE":
                    return "active-row";
                case "INACTIVE":
                    return "inactive-row";
                case "PENDING":
                    return "pending-row";
                case "VIP":
                    return "vip-row";
                case "URGENT":
                    return "urgent-row";
                default:
                    return null;
            }
        });

        // Appliquer les styles inline après le rendu
        getElement().executeJs(
            "setTimeout(() => {" +
            "  const grid = $0;" +
            "  const rows = grid.querySelectorAll('tr');" +
            "  rows.forEach(row => {" +
            "    if (row.classList.contains('active-row')) {" +
            "      row.style.backgroundColor = '#e8f5e8';" +
            "      row.style.borderLeft = '4px solid #4CAF50';" +
            "    } else if (row.classList.contains('inactive-row')) {" +
            "      row.style.backgroundColor = '#f5f5f5';" +
            "      row.style.borderLeft = '4px solid #9E9E9E';" +
            "      row.style.color = '#757575';" +
            "    } else if (row.classList.contains('pending-row')) {" +
            "      row.style.backgroundColor = '#fff3e0';" +
            "      row.style.borderLeft = '4px solid #FF9800';" +
            "    } else if (row.classList.contains('vip-row')) {" +
            "      row.style.backgroundColor = '#e3f2fd';" +
            "      row.style.borderLeft = '4px solid #2196F3';" +
            "      row.style.fontWeight = 'bold';" +
            "    } else if (row.classList.contains('urgent-row')) {" +
            "      row.style.backgroundColor = '#ffebee';" +
            "      row.style.borderLeft = '4px solid #f44336';" +
            "      row.style.fontWeight = 'bold';" +
            "    }" +
            "  });" +
            "}, 100);",
            getGrid().getElement()
        );
    }

    // Alternative : méthode avec styles dynamiques basés sur les données
    public void applyDynamicColors() {
        getGrid().setClassNameGenerator(person -> {
            if (person == null) return "default-row";
            
            String style = "data-row ";
            
            // Couleur basée sur le statut
            switch (person.getStatus()) {
                case "ACTIVE":
                    style += "active-status";
                    break;
                case "INACTIVE":
                    style += "inactive-status";
                    break;
                case "PENDING":
                    style += "pending-status";
                    break;
                case "VIP":
                    style += "vip-status";
                    break;
                case "URGENT":
                    style += "urgent-status";
                    break;
                default:
                    style += "default-status";
            }
            
            return style;
        });

        // Appliquer les couleurs
        applyColorStyles();
    }

    private void applyColorStyles() {
        getElement().executeJs(
            "setTimeout(() => {" +
            "  const grid = $0;" +
            "  const rows = grid.querySelectorAll('tr[class*=\"status\"]');" +
            "  rows.forEach(row => {" +
            "    if (row.classList.contains('active-status')) {" +
            "      row.style.background = 'linear-gradient(90deg, #E8F5E8 0%, #FFFFFF 100%)';" +
            "      row.style.borderLeft = '4px solid #4CAF50';" +
            "    } else if (row.classList.contains('inactive-status')) {" +
            "      row.style.background = 'linear-gradient(90deg, #F5F5F5 0%, #FFFFFF 100%)';" +
            "      row.style.borderLeft = '4px solid #9E9E9E';" +
            "      row.style.color = '#616161';" +
            "    } else if (row.classList.contains('pending-status')) {" +
            "      row.style.background = 'linear-gradient(90deg, #FFF3E0 0%, #FFFFFF 100%)';" +
            "      row.style.borderLeft = '4px solid #FF9800';" +
            "    } else if (row.classList.contains('vip-status')) {" +
            "      row.style.background = 'linear-gradient(90deg, #E3F2FD 0%, #FFFFFF 100%)';" +
            "      row.style.borderLeft = '4px solid #2196F3';" +
            "      row.style.fontWeight = '600';" +
            "    } else if (row.classList.contains('urgent-status')) {" +
            "      row.style.background = 'linear-gradient(90deg, #FFEBEE 0%, #FFFFFF 100%)';" +
            "      row.style.borderLeft = '4px solid #F44336';" +
            "      row.style.fontWeight = '600';" +
            "      row.style.color = '#C62828';" +
            "    }" +
            "  });" +
            "}, 100);",
            getGrid().getElement()
        );
    }

    private void loadSampleData() {
        List<Person> sampleData = Arrays.asList(
            new Person(1, "Jean Dupont", "jean.dupont@email.com", "ACTIVE", "Ventes"),
            new Person(2, "Marie Martin", "marie.martin@email.com", "INACTIVE", "Marketing"),
            new Person(3, "Pierre Durand", "pierre.durand@email.com", "PENDING", "IT"),
            new Person(4, "Sophie Lambert", "sophie.lambert@email.com", "VIP", "Direction"),
            new Person(5, "Thomas Moreau", "thomas.moreau@email.com", "ACTIVE", "RH"),
            new Person(6, "Laura Petit", "laura.petit@email.com", "URGENT", "Support"),
            new Person(7, "Marc Dubois", "marc.dubois@email.com", "INACTIVE", "IT")
        );
        
        setItems(sampleData);
    }
}

//@@
@Route("")
public class MainView extends VerticalLayout {
    private PersonGrid personGrid;
    private Div detailsPanel;
    private Select<String> colorSchemeSelector;
    private Map<String, String> colorSchemes = new HashMap<>();

    public MainView() {
        initializeColorSchemes();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeColorSchemes() {
        // Définir différents schémas de couleurs
        colorSchemes.put("Standard", "standard");
        colorSchemes.put("Pastel", "pastel");
        colorSchemes.put("Vibrant", "vibrant");
        colorSchemes.put("Monochrome", "monochrome");
    }

    private void initializeComponents() {
        personGrid = new PersonGrid();
        
        detailsPanel = new Div();
        detailsPanel.setWidthFull();
        detailsPanel.getStyle()
            .set("border", "1px solid #E0E0E0")
            .set("padding", "1rem")
            .set("border-radius", "8px")
            .set("background", "#FAFAFA");
        
        colorSchemeSelector = new Select<>();
        colorSchemeSelector.setLabel("Schéma de couleurs");
        colorSchemeSelector.setItems(colorSchemes.keySet());
        colorSchemeSelector.setValue("Standard");
    }

    private void setupLayout() {
        setPadding(true);
        setSpacing(true);
        
        H2 title = new H2("Grid avec Couleurs HTML");
        title.getStyle().set("color", "#1976D2");
        
        HorizontalLayout controls = new HorizontalLayout(colorSchemeSelector);
        controls.setAlignItems(Alignment.END);
        
        add(title, controls, personGrid, detailsPanel);
    }

    private void setupListeners() {
        personGrid.addSelectionListener((oldValue, newValue) -> {
            if (newValue != null) {
                updateDetailsPanel(newValue);
            } else {
                detailsPanel.removeAll();
                detailsPanel.setText("Aucune personne sélectionnée");
            }
        });

        colorSchemeSelector.addValueChangeListener(event -> {
            applyColorScheme(event.getValue());
        });
    }

    private void applyColorScheme(String schemeName) {
        String scheme = colorSchemes.get(schemeName);
        
        String jsCode = "setTimeout(() => {" +
            "  const grid = $0;" +
            "  const rows = grid.querySelectorAll('tr');" +
            "  rows.forEach(row => {" +
            "    const statusCell = row.querySelector('[slot=\"vaadin-grid-cell-content\"]:nth-child(4)');" +
            "    if (statusCell) {" +
            "      const status = statusCell.textContent.trim();" +
            "      " + getColorMappingJS(scheme) +
            "    }" +
            "  });" +
            "}, 100);";
        
        personGrid.getElement().executeJs(jsCode, personGrid.getGrid().getElement());
    }

    private String getColorMappingJS(String scheme) {
        switch (scheme) {
            case "pastel":
                return "if (status === 'ACTIVE') { row.style.backgroundColor = '#F0F8FF'; row.style.borderLeft = '4px solid #87CEEB'; }" +
                       "else if (status === 'INACTIVE') { row.style.backgroundColor = '#FFF0F5'; row.style.borderLeft = '4px solid #DDA0DD'; }" +
                       "else if (status === 'PENDING') { row.style.backgroundColor = '#FFFACD'; row.style.borderLeft = '4px solid #FFD700'; }" +
                       "else if (status === 'VIP') { row.style.backgroundColor = '#F0FFF0'; row.style.borderLeft = '4px solid #98FB98'; }" +
                       "else if (status === 'URGENT') { row.style.backgroundColor = '#FFE4E1'; row.style.borderLeft = '4px solid #FFB6C1'; }";
                
            case "vibrant":
                return "if (status === 'ACTIVE') { row.style.backgroundColor = '#00FF00'; row.style.color = '#000000'; }" +
                       "else if (status === 'INACTIVE') { row.style.backgroundColor = '#FF0000'; row.style.color = '#FFFFFF'; }" +
                       "else if (status === 'PENDING') { row.style.backgroundColor = '#FFFF00'; row.style.color = '#000000'; }" +
                       "else if (status === 'VIP') { row.style.backgroundColor = '#0000FF'; row.style.color = '#FFFFFF'; }" +
                       "else if (status === 'URGENT') { row.style.backgroundColor = '#FF00FF'; row.style.color = '#FFFFFF'; }";
                
            case "monochrome":
                return "if (status === 'ACTIVE') { row.style.backgroundColor = '#FFFFFF'; row.style.borderLeft = '4px solid #000000'; }" +
                       "else if (status === 'INACTIVE') { row.style.backgroundColor = '#F0F0F0'; row.style.borderLeft = '4px solid #666666'; }" +
                       "else if (status === 'PENDING') { row.style.backgroundColor = '#E0E0E0'; row.style.borderLeft = '4px solid #333333'; }" +
                       "else if (status === 'VIP') { row.style.backgroundColor = '#D0D0D0'; row.style.borderLeft = '4px solid #000000'; font-weight: bold; }" +
                       "else if (status === 'URGENT') { row.style.backgroundColor = '#C0C0C0'; row.style.borderLeft = '4px solid #000000'; font-weight: bold; }";
                
            default: // standard
                return "if (status === 'ACTIVE') { row.style.backgroundColor = '#E8F5E8'; row.style.borderLeft = '4px solid #4CAF50'; }" +
                       "else if (status === 'INACTIVE') { row.style.backgroundColor = '#F5F5F5'; row.style.borderLeft = '4px solid #9E9E9E'; }" +
                       "else if (status === 'PENDING') { row.style.backgroundColor = '#FFF3E0'; row.style.borderLeft = '4px solid #FF9800'; }" +
                       "else if (status === 'VIP') { row.style.backgroundColor = '#E3F2FD'; row.style.borderLeft = '4px solid #2196F3'; font-weight: bold; }" +
                       "else if (status === 'URGENT') { row.style.backgroundColor = '#FFEBEE'; row.style.borderLeft = '4px solid #F44336'; font-weight: bold; }";
        }
    }

    private void updateDetailsPanel(Person person) {
        detailsPanel.removeAll();
        
        VerticalLayout details = new VerticalLayout();
        details.setSpacing(false);
        details.setPadding(false);
        
        H3 name = new H3(person.getName());
        name.getStyle().set("margin-top", "0").set("color", "#1976D2");
        
        // Appliquer la couleur du statut dans les détails
        String statusColor = getStatusColor(person.getStatus());
        
        Div email = new Div(new Text("Email: " + person.getEmail()));
        Div id = new Div(new Text("ID: " + person.getId()));
        Div status = new Div(new Text("Statut: " + person.getStatus()));
        Div department = new Div(new Text("Département: " + person.getDepartment()));
        
        status.getStyle()
            .set("color", statusColor)
            .set("font-weight", "bold")
            .set("padding", "4px 8px")
            .set("background", getStatusBackgroundColor(person.getStatus()))
            .set("border-radius", "4px")
            .set("display", "inline-block");
        
        details.add(name, email, id, status, department);
        detailsPanel.add(details);
    }

    private String getStatusColor(String status) {
        switch (status) {
            case "ACTIVE": return "#2E7D32";
            case "INACTIVE": return "#616161";
            case "PENDING": return "#EF6C00";
            case "VIP": return "#1565C0";
            case "URGENT": return "#C62828";
            default: return "#000000";
        }
    }

    private String getStatusBackgroundColor(String status) {
        switch (status) {
            case "ACTIVE": return "#E8F5E8";
            case "INACTIVE": return "#F5F5F5";
            case "PENDING": return "#FFF3E0";
            case "VIP": return "#E3F2FD";
            case "URGENT": return "#FFEBEE";
            default: return "#FFFFFF";
        }
    }
}
