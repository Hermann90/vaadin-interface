public class SelectableGrid<T> extends VerticalLayout {
    private Grid<T> grid;
    private List<SelectionListener<T>> selectionListeners = new ArrayList<>();
    private T currentSelection;
    private boolean allowDeselection = false;
    private Function<T, String> rowColorProvider;

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

    // Méthode pour définir les couleurs des lignes
    public void setRowColorProvider(Function<T, String> colorProvider) {
        this.rowColorProvider = colorProvider;
        applyRowColors();
    }

    // Appliquer les couleurs aux lignes
    private void applyRowColors() {
        if (rowColorProvider == null) return;

        getElement().executeJs(
            "setTimeout(() => {" +
            "  const grid = $0;" +
            "  const items = grid.items;" +
            "  const rows = grid.querySelectorAll('tr');" +
            "  " +
            "  rows.forEach((row, index) => {" +
            "    if (index < items.length) {" +
            "      const item = items[index];" +
            "      const color = $1(item);" +
            "      if (color) {" +
            "        row.style.backgroundColor = color;" +
            "        row.style.borderLeft = '5px solid ' + color;" +
            "        row.style.transition = 'all 0.3s ease';" +
            "      }" +
            "    }" +
            "  });" +
            "}, 50);", 
            grid.getElement(), 
            (SerializableFunction<T, String>) rowColorProvider::apply
        );
    }

    // Rafraîchir les couleurs quand les données changent
    public void refreshRowColors() {
        applyRowColors();
    }

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
        // Rafraîchir les couleurs après le chargement des données
        getElement().executeJs("setTimeout(() => $0.refreshRowColors(), 100);");
    }

    public void setItems(Collection<T> items) {
        grid.setItems(items);
        getElement().executeJs("setTimeout(() => $0.refreshRowColors(), 100);");
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

///???
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
        grid.addColumn(Person::getAge).setHeader("Âge").setWidth("100px");
        grid.addColumn(Person::getFormattedSalary).setHeader("Salaire").setWidth("120px");
        
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    private void setupRowColors() {
        // Définir les couleurs basées sur le statut
        setRowColorProvider(person -> {
            if (person == null) return null;
            
            switch (person.getStatus()) {
                case "ACTIVE":
                    return "#E8F5E8"; // Vert clair
                case "INACTIVE":
                    return "#F5F5F5"; // Gris clair
                case "PENDING":
                    return "#FFF3E0"; // Orange clair
                case "VIP":
                    return "#E3F2FD"; // Bleu clair
                case "URGENT":
                    return "#FFEBEE"; // Rouge clair
                default:
                    return "#FFFFFF"; // Blanc
            }
        });
    }

    // Méthode pour changer les couleurs basées sur le département
    public void setDepartmentBasedColors() {
        setRowColorProvider(person -> {
            if (person == null) return null;
            
            switch (person.getDepartment()) {
                case "Ventes":
                    return "#E8F5E8"; // Vert
                case "Marketing":
                    return "#F3E5F5"; // Violet
                case "IT":
                    return "#E3F2FD"; // Bleu
                case "Direction":
                    return "#FFF8E1"; // Jaune
                case "RH":
                    return "#E0F2F1"; // Turquoise
                case "Support":
                    return "#FCE4EC"; // Rose
                case "Finance":
                    return "#FFF3E0"; // Orange
                default:
                    return "#FAFAFA"; // Gris très clair
            }
        });
    }

    // Méthode pour changer les couleurs basées sur le score de performance
    public void setPerformanceBasedColors() {
        setRowColorProvider(person -> {
            if (person == null) return null;
            
            int score = person.getPerformanceScore();
            if (score >= 9) return "#C8E6C9"; // Vert foncé - Excellent
            if (score >= 7) return "#E8F5E8"; // Vert clair - Bon
            if (score >= 5) return "#FFF9C4"; // Jaune - Moyen
            return "#FFCDD2"; // Rouge - À améliorer
        });
    }

    // Méthode pour les couleurs alternées simples
    public void setAlternatingColors() {
        setRowColorProvider(person -> {
            if (person == null) return null;
            
            int id = person.getId();
            return id % 2 == 0 ? "#F5F5F5" : "#FFFFFF";
        });
    }

    private void loadSampleData() {
        List<Person> sampleData = PersonDataGenerator.generateSampleData(15);
        setItems(sampleData);
    }
}

///&&&
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
        colorSchemeSelector.setItems(
            "Par statut", 
            "Par département", 
            "Par performance", 
            "Couleurs alternées",
            "Dégradé d'âge"
        );
        colorSchemeSelector.setValue("Par statut");
    }

    private void setupLayout() {
        setPadding(true);
        setSpacing(true);
        
        H2 title = new H2("Grid avec Lignes Colorées");
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
        switch (scheme) {
            case "Par statut":
                personGrid.setupRowColors(); // Utilise la méthode par défaut
                break;
                
            case "Par département":
                personGrid.setDepartmentBasedColors();
                break;
                
            case "Par performance":
                personGrid.setPerformanceBasedColors();
                break;
                
            case "Couleurs alternées":
                personGrid.setAlternatingColors();
                break;
                
            case "Dégradé d'âge":
                applyAgeGradientColors();
                break;
        }
    }

    private void applyAgeGradientColors() {
        personGrid.setRowColorProvider(person -> {
            if (person == null) return null;
            
            int age = person.getAge();
            // Dégradé du bleu clair (jeune) au vert (âgé)
            if (age <= 30) return "#E3F2FD"; // Bleu très clair
            if (age <= 40) return "#B3E5FC"; // Bleu clair
            if (age <= 50) return "#81D4FA"; // Bleu moyen
            if (age <= 60) return "#4FC3F7"; // Bleu foncé
            return "#29B6F6"; // Bleu très foncé
        });
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
        Div age = new Div(new Text("Âge: " + person.getAge()));
        Div salary = new Div(new Text("Salaire: " + person.getFormattedSalary()));
        Div performance = new Div(new Text("Performance: " + person.getPerformanceLevel()));
        
        // Appliquer la couleur du statut dans les détails
        String statusColor = getStatusColor(person.getStatus());
        status.getStyle()
            .set("color", statusColor)
            .set("font-weight", "bold")
            .set("padding", "4px 8px")
            .set("background", getStatusBackgroundColor(person.getStatus()))
            .set("border-radius", "4px")
            .set("display", "inline-block");
        
        details.add(name, email, id, status, department, age, salary, performance);
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


