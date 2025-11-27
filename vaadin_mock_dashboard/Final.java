public class SelectableGrid<T> extends VerticalLayout {
    private Grid<T> grid;
    private List<SelectionListener<T>> selectionListeners = new ArrayList<>();
    private T currentSelection;
    private boolean allowDeselection = false;

    public SelectableGrid(Class<T> beanType) {
        grid = new Grid<>(beanType);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        
        setupSelectionHandling();
        add(grid);
    }

    private void setupSelectionHandling() {
        grid.addItemClickListener(event -> {
            T clickedItem = event.getItem();
            
            if (clickedItem != null && clickedItem.equals(currentSelection)) {
                if (allowDeselection) {
                    clearSelection();
                } else {
                    refreshSelection();
                }
            } else if (clickedItem != null) {
                setSelectedItem(clickedItem);
            }
        });
        
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null && !allowDeselection && currentSelection != null) {
                grid.asSingleSelect().setValue(currentSelection);
            }
        });
    }

    // ✅ AJOUTER CETTE MÉTHODE
    public Grid<T> getGrid() {
        return grid;
    }

    private void refreshSelection() {
        if (currentSelection != null) {
            grid.select(currentSelection);
        }
    }

    public void setAllowDeselection(boolean allowDeselection) {
        this.allowDeselection = allowDeselection;
    }

    public boolean isAllowDeselection() {
        return allowDeselection;
    }

    public interface SelectionListener<T> {
        void selectionChanged(T oldValue, T newValue);
    }

    public void addSelectionListener(SelectionListener<T> listener) {
        selectionListeners.add(listener);
    }

    public void removeSelectionListener(SelectionListener<T> listener) {
        selectionListeners.remove(listener);
    }

    private void fireSelectionChanged(T oldValue, T newValue) {
        for (SelectionListener<T> listener : selectionListeners) {
            listener.selectionChanged(oldValue, newValue);
        }
    }

    public void setItems(List<T> items) {
        grid.setItems(items);
    }

    public void setItems(Collection<T> items) {
        grid.setItems(items);
    }

    public T getSelectedItem() {
        return currentSelection;
    }

    public void setSelectedItem(T item) {
        T oldSelection = currentSelection;
        currentSelection = item;
        grid.select(item);
        if (oldSelection == null || !item.equals(oldSelection)) {
            fireSelectionChanged(oldSelection, currentSelection);
        }
    }

    public void clearSelection() {
        if (!allowDeselection) return;
        
        T oldSelection = currentSelection;
        currentSelection = null;
        grid.deselectAll();
        if (oldSelection != null) {
            fireSelectionChanged(oldSelection, null);
        }
    }
}

// ggg
public class PersonGrid extends SelectableGrid<Person> {

    public PersonGrid() {
        super(Person.class);
        configureGrid();
        loadSampleData();
    }

    private void configureGrid() {
        Grid<Person> grid = getGrid(); // ✅ MAINTENANT FONCTIONNEL
        
        grid.removeAllColumns();
        grid.addColumn(Person::getId).setHeader("ID").setWidth("80px");
        grid.addColumn(Person::getName).setHeader("Nom").setAutoWidth(true);
        grid.addColumn(Person::getEmail).setHeader("Email").setAutoWidth(true);
        grid.addColumn(Person::getStatus).setHeader("Statut").setWidth("120px");
        grid.addColumn(Person::getDepartment).setHeader("Département").setWidth("150px");
        grid.addColumn(Person::getAge).setHeader("Âge").setWidth("100px");
        grid.addColumn(Person::getFormattedSalary).setHeader("Salaire").setWidth("120px");
        grid.addColumn(Person::getRemoteStatus).setHeader("Mode travail").setWidth("130px");
        grid.addColumn(Person::getPerformanceLevel).setHeader("Performance").setWidth("140px");
        
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        
        // Appliquer les couleurs après configuration
        setupRowColors();
    }

    private void setupRowColors() {
        Grid<Person> grid = getGrid(); // ✅ MAINTENANT FONCTIONNEL
        
        grid.setClassNameGenerator(person -> {
            if (person == null) return null;
            
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

        // Appliquer les styles inline
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
            grid.getElement()
        );
    }

    private void loadSampleData() {
        List<Person> sampleData = PersonDataGenerator.generateSampleData(15);
        setItems(sampleData);
    }
}

//€€
@Route("")
public class MainView extends VerticalLayout {
    private PersonGrid personGrid;
    private Div detailsPanel;
    private Select<String> colorSchemeSelector;

    public MainView() {
        initializeComponents();
        setupLayout();
        setupListeners();
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
        colorSchemeSelector.setItems("Standard", "Pastel", "Vibrant", "Monochrome");
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

    private void applyColorScheme(String scheme) {
        Grid<Person> grid = personGrid.getGrid(); // ✅ MAINTENANT FONCTIONNEL
        
        String jsCode = "setTimeout(() => {" +
            "  const grid = $0;" +
            "  const rows = grid.querySelectorAll('tr');" +
            "  rows.forEach(row => {" +
            "    " + getColorMappingJS(scheme) +
            "  });" +
            "}, 100);";
        
        grid.getElement().executeJs(jsCode);
    }

    private String getColorMappingJS(String scheme) {
        switch (scheme) {
            case "pastel":
                return "if (row.classList.contains('active-row')) { row.style.backgroundColor = '#F0F8FF'; row.style.borderLeft = '4px solid #87CEEB'; }" +
                       "else if (row.classList.contains('inactive-row')) { row.style.backgroundColor = '#FFF0F5'; row.style.borderLeft = '4px solid #DDA0DD'; }" +
                       "else if (row.classList.contains('pending-row')) { row.style.backgroundColor = '#FFFACD'; row.style.borderLeft = '4px solid #FFD700'; }" +
                       "else if (row.classList.contains('vip-row')) { row.style.backgroundColor = '#F0FFF0'; row.style.borderLeft = '4px solid #98FB98'; }" +
                       "else if (row.classList.contains('urgent-row')) { row.style.backgroundColor = '#FFE4E1'; row.style.borderLeft = '4px solid #FFB6C1'; }";
                
            case "vibrant":
                return "if (row.classList.contains('active-row')) { row.style.backgroundColor = '#00FF00'; row.style.color = '#000000'; }" +
                       "else if (row.classList.contains('inactive-row')) { row.style.backgroundColor = '#FF0000'; row.style.color = '#FFFFFF'; }" +
                       "else if (row.classList.contains('pending-row')) { row.style.backgroundColor = '#FFFF00'; row.style.color = '#000000'; }" +
                       "else if (row.classList.contains('vip-row')) { row.style.backgroundColor = '#0000FF'; row.style.color = '#FFFFFF'; }" +
                       "else if (row.classList.contains('urgent-row')) { row.style.backgroundColor = '#FF00FF'; row.style.color = '#FFFFFF'; }";
                
            case "monochrome":
                return "if (row.classList.contains('active-row')) { row.style.backgroundColor = '#FFFFFF'; row.style.borderLeft = '4px solid #000000'; }" +
                       "else if (row.classList.contains('inactive-row')) { row.style.backgroundColor = '#F0F0F0'; row.style.borderLeft = '4px solid #666666'; }" +
                       "else if (row.classList.contains('pending-row')) { row.style.backgroundColor = '#E0E0E0'; row.style.borderLeft = '4px solid #333333'; }" +
                       "else if (row.classList.contains('vip-row')) { row.style.backgroundColor = '#D0D0D0'; row.style.borderLeft = '4px solid #000000'; row.style.fontWeight = 'bold'; }" +
                       "else if (row.classList.contains('urgent-row')) { row.style.backgroundColor = '#C0C0C0'; row.style.borderLeft = '4px solid #000000'; row.style.fontWeight = 'bold'; }";
                
            default: // standard
                return "if (row.classList.contains('active-row')) { row.style.backgroundColor = '#E8F5E8'; row.style.borderLeft = '4px solid #4CAF50'; }" +
                       "else if (row.classList.contains('inactive-row')) { row.style.backgroundColor = '#F5F5F5'; row.style.borderLeft = '4px solid #9E9E9E'; }" +
                       "else if (row.classList.contains('pending-row')) { row.style.backgroundColor = '#FFF3E0'; row.style.borderLeft = '4px solid #FF9800'; }" +
                       "else if (row.classList.contains('vip-row')) { row.style.backgroundColor = '#E3F2FD'; row.style.borderLeft = '4px solid #2196F3'; row.style.fontWeight = 'bold'; }" +
                       "else if (row.classList.contains('urgent-row')) { row.style.backgroundColor = '#FFEBEE'; row.style.borderLeft = '4px solid #F44336'; row.style.fontWeight = 'bold'; }";
        }
    }

    private void updateDetailsPanel(Person person) {
        detailsPanel.removeAll();
        
        VerticalLayout details = new VerticalLayout();
        details.setSpacing(false);
        details.setPadding(false);
        
        H3 name = new H3(person.getName());
        name.getStyle().set("margin-top", "0").set("color", "#1976D2");
        
        Div email = new Div(new Text("Email: " + person.getEmail()));
        Div id = new Div(new Text("ID: " + person.getId()));
        Div status = new Div(new Text("Statut: " + person.getStatus()));
        Div department = new Div(new Text("Département: " + person.getDepartment()));
        Div salary = new Div(new Text("Salaire: " + person.getFormattedSalary()));
        Div performance = new Div(new Text("Performance: " + person.getPerformanceLevel()));
        
        status.getStyle()
            .set("color", getStatusColor(person.getStatus()))
            .set("font-weight", "bold")
            .set("padding", "4px 8px")
            .set("background", getStatusBackgroundColor(person.getStatus()))
            .set("border-radius", "4px")
            .set("display", "inline-block");
        
        details.add(name, email, id, status, department, salary, performance);
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

//&&


