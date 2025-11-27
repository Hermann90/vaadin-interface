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

    // Méthode pour colorier les lignes sans executeJs
    public void setRowColorProvider(Function<T, String> colorProvider) {
        this.rowColorProvider = colorProvider;
        applyRowColors();
    }

    // Nouvelle méthode sans executeJs
    private void applyRowColors() {
        if (rowColorProvider == null) return;

        // Utiliser un renderer personnalisé pour chaque colonne
        grid.getColumns().forEach(column -> {
            column.setRenderer(new ComponentRenderer<>(item -> {
                Div div = new Div();
                
                // Appliquer la couleur de fond
                String backgroundColor = rowColorProvider.apply(item);
                if (backgroundColor != null) {
                    div.getStyle().set("background-color", backgroundColor);
                    div.getStyle().set("width", "100%");
                    div.getStyle().set("height", "100%");
                    div.getStyle().set("padding", "var(--lumo-space-s)");
                }
                
                // Afficher la valeur
                String value = getValueAsString(item, column);
                div.setText(value != null ? value : "");
                
                return div;
            }));
        });
    }

    // Helper method pour obtenir la valeur d'une colonne
    private String getValueAsString(T item, Grid.Column<T> column) {
        try {
            // Méthode simple pour obtenir la valeur affichée
            if (item instanceof Person) {
                Person person = (Person) item;
                if (column.getKey().equals("id")) return String.valueOf(person.getId());
                if (column.getKey().equals("name")) return person.getName();
                if (column.getKey().equals("email")) return person.getEmail();
                if (column.getKey().equals("status")) return person.getStatus();
                if (column.getKey().equals("department")) return person.getDepartment();
            }
            return String.valueOf(item);
        } catch (Exception e) {
            return "";
        }
    }

    // Méthode alternative plus simple
    public void colorizeRowsSimple(Function<T, String> colorProvider) {
        this.rowColorProvider = colorProvider;
        
        // Recréer la grid avec les nouvelles couleurs
        Component oldGrid = grid;
        Grid<T> newGrid = new Grid<>(grid.getBeanType());
        newGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        
        // Recopier les colonnes
        grid.getColumns().forEach(column -> {
            Grid.Column<T> newColumn = newGrid.addColumn(item -> {
                // Retourner la valeur normale
                return getValueAsString(item, column);
            }).setHeader(column.getHeaderText())
              .setWidth(column.getWidth())
              .setFlexGrow(column.getFlexGrow());
            
            // Appliquer le style de fond
            newColumn.setClassNameGenerator(item -> {
                String color = colorProvider.apply(item);
                return color != null ? "colored-row" : null;
            });
        });
        
        // Remplacer la grid
        replace(oldGrid, newGrid);
        grid = newGrid;
        
        // Réappliquer les données et la sélection
        if (grid.getDataProvider() != null) {
            newGrid.setItems(grid.getDataProvider());
        }
        if (currentSelection != null) {
            newGrid.select(currentSelection);
        }
        
        // Réappliquer les écouteurs
        setupSelectionHandling();
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
        applyRowColors();
    }

    public void setItems(Collection<T> items) {
        grid.setItems(items);
        applyRowColors();
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
//€€€

public class PersonGrid extends SelectableGrid<Person> {

    public PersonGrid() {
        super(Person.class);
        configureGrid();
        setupDefaultColors();
        loadSampleData();
    }

    private void configureGrid() {
        Grid<Person> grid = getGrid();
        
        grid.removeAllColumns();
        grid.addColumn(Person::getId).setHeader("ID").setWidth("80px").setKey("id");
        grid.addColumn(Person::getName).setHeader("Nom").setAutoWidth(true).setKey("name");
        grid.addColumn(Person::getEmail).setHeader("Email").setAutoWidth(true).setKey("email");
        grid.addColumn(Person::getStatus).setHeader("Statut").setWidth("120px").setKey("status");
        grid.addColumn(Person::getDepartment).setHeader("Département").setWidth("150px").setKey("department");
        grid.addColumn(Person::getAge).setHeader("Âge").setWidth("100px").setKey("age");
        grid.addColumn(Person::getFormattedSalary).setHeader("Salaire").setWidth("120px").setKey("salary");
    }

    private void setupDefaultColors() {
        // Utiliser la méthode simple sans executeJs
        colorizeRowsSimple(person -> {
            if (person == null) return null;
            
            switch (person.getStatus()) {
                case "ACTIVE":
                    return "lightgreen";
                case "INACTIVE":
                    return "lightgray";
                case "PENDING":
                    return "lightyellow";
                case "VIP":
                    return "lightblue";
                case "URGENT":
                    return "lightcoral";
                default:
                    return "white";
            }
        });
    }

    // Méthode pour couleurs par département
    public void setDepartmentColors() {
        colorizeRowsSimple(person -> {
            if (person == null) return null;
            
            switch (person.getDepartment()) {
                case "Ventes":
                    return "lightgreen";
                case "Marketing":
                    return "plum";
                case "IT":
                    return "lightblue";
                case "Direction":
                    return "gold";
                case "RH":
                    return "paleturquoise";
                case "Support":
                    return "pink";
                case "Finance":
                    return "orange";
                default:
                    return "white";
            }
        });
    }

    // Méthode pour couleurs par performance
    public void setPerformanceColors() {
        colorizeRowsSimple(person -> {
            if (person == null) return null;
            
            int score = person.getPerformanceScore();
            if (score >= 9) return "limegreen";
            if (score >= 7) return "lightgreen";
            if (score >= 5) return "yellow";
            return "tomato";
        });
    }

    // Méthode pour couleurs alternées
    public void setAlternatingColors() {
        colorizeRowsSimple(person -> {
            if (person == null) return null;
            return person.getId() % 2 == 0 ? "whitesmoke" : "white";
        });
    }

    private void loadSampleData() {
        List<Person> sampleData = PersonDataGenerator.generateSampleData(15);
        setItems(sampleData);
    }
}

//)))
public class SelectableGrid<T> extends VerticalLayout {
    private Grid<T> grid;
    // ... autres variables

    // Méthode avec TemplateRenderer (sans executeJs)
    public void colorizeWithTemplateRenderer(Function<T, String> colorProvider) {
        this.rowColorProvider = colorProvider;
        
        // Recréer toutes les colonnes avec TemplateRenderer
        grid.getColumns().forEach(column -> {
            String property = column.getKey();
            
            column.setRenderer(new TemplateRenderer<T>(
                "<div style='background-color: [[item.color]]; padding: var(--lumo-space-s); width: 100%; height: 100%;'>[[item.value]]</div>"
            ).withProperty("color", item -> colorProvider.apply(item))
             .withProperty("value", item -> getValueAsString(item, column)));
        });
    }

    // Méthode avec ComponentRenderer simple
    public void colorizeWithComponentRenderer(Function<T, String> colorProvider) {
        this.rowColorProvider = colorProvider;
        
        grid.getColumns().forEach(column -> {
            column.setRenderer(new ComponentRenderer<>(item -> {
                Span span = new Span();
                String value = getValueAsString(item, column);
                span.setText(value != null ? value : "");
                
                String color = colorProvider.apply(item);
                if (color != null) {
                    span.getStyle()
                        .set("background-color", color)
                        .set("display", "block")
                        .set("padding", "var(--lumo-space-s)")
                        .set("width", "100%")
                        .set("height", "100%");
                }
                
                return span;
            }));
        });
    }
}

///)))
public class PersonGrid extends SelectableGrid<Person> {

    public PersonGrid() {
        super(Person.class);
        configureGrid();
        setupDefaultColors();
        loadSampleData();
    }

    private void configureGrid() {
        Grid<Person> grid = getGrid();
        
        grid.removeAllColumns();
        grid.addColumn(Person::getId).setHeader("ID").setWidth("80px").setKey("id");
        grid.addColumn(Person::getName).setHeader("Nom").setAutoWidth(true).setKey("name");
        grid.addColumn(Person::getEmail).setHeader("Email").setAutoWidth(true).setKey("email");
        grid.addColumn(Person::getStatus).setHeader("Statut").setWidth("120px").setKey("status");
        grid.addColumn(Person::getDepartment).setHeader("Département").setWidth("150px").setKey("department");
    }

    private void setupDefaultColors() {
        // Utiliser TemplateRenderer
        colorizeWithTemplateRenderer(person -> {
            if (person == null) return "white";
            
            switch (person.getStatus()) {
                case "ACTIVE": return "lightgreen";
                case "INACTIVE": return "lightgray";
                case "PENDING": return "lightyellow";
                case "VIP": return "lightblue";
                case "URGENT": return "lightcoral";
                default: return "white";
            }
        });
    }

    private void loadSampleData() {
        List<Person> sampleData = PersonDataGenerator.generateSampleData(15);
        setItems(sampleData);
    }
}
