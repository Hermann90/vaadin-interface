public class SelectableGrid<T> extends VerticalLayout {
    private Grid<T> grid;
    private List<SelectionListener<T>> selectionListeners = new ArrayList<>();
    private T currentSelection;
    private boolean allowDeselection = false;
    private Function<T, String> rowColorProvider;
    private ListDataProvider<T> dataProvider; // ✅ Stocker le dataProvider

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

    // Méthode avec ComponentRenderer
    private void applyRowColors() {
        if (rowColorProvider == null) return;

        // Appliquer un renderer à chaque colonne
        grid.getColumns().forEach(column -> {
            column.setRenderer(new ComponentRenderer<>(item -> {
                Div div = new Div();
                
                // Appliquer la couleur de fond
                String backgroundColor = rowColorProvider.apply(item);
                if (backgroundColor != null) {
                    div.getStyle()
                        .set("background-color", backgroundColor)
                        .set("width", "100%")
                        .set("height", "100%")
                        .set("padding", "var(--lumo-space-s)");
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
            if (item instanceof Person) {
                Person person = (Person) item;
                // Utiliser le header comme identifiant
                String header = column.getHeaderText();
                if ("ID".equals(header)) return String.valueOf(person.getId());
                if ("Nom".equals(header)) return person.getName();
                if ("Email".equals(header)) return person.getEmail();
                if ("Statut".equals(header)) return person.getStatus();
                if ("Département".equals(header)) return person.getDepartment();
                if ("Âge".equals(header)) return String.valueOf(person.getAge());
                if ("Salaire".equals(header)) return person.getFormattedSalary();
            }
            return String.valueOf(item);
        } catch (Exception e) {
            return "";
        }
    }

    // Méthode alternative plus simple - SANS recréer la grid
    public void colorizeRowsSimple(Function<T, String> colorProvider) {
        this.rowColorProvider = colorProvider;
        
        // Utiliser ClassNameGenerator sur chaque colonne
        grid.getColumns().forEach(column -> {
            column.setClassNameGenerator(item -> {
                String color = colorProvider.apply(item);
                return color != null ? "colored-cell" : null;
            });
        });

        // Appliquer les styles via un style inline
        getElement().executeJs(
            "const style = document.createElement('style');" +
            "style.textContent = `" +
            "  .colored-cell { background-color: inherit !important; }" +
            "`;" +
            "document.head.appendChild(style);"
        );
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
        this.dataProvider = new ListDataProvider<>(items); // ✅ Stocker le dataProvider
        grid.setItems(dataProvider);
        applyRowColors();
    }

    public void setItems(Collection<T> items) {
        this.dataProvider = new ListDataProvider<>(new ArrayList<>(items)); // ✅ Stocker le dataProvider
        grid.setItems(dataProvider);
        applyRowColors();
    }

    // ✅ Méthode pour obtenir les données actuelles
    public List<T> getItems() {
        if (dataProvider != null) {
            return new ArrayList<>(((ListDataProvider<T>) dataProvider).getItems());
        }
        return new ArrayList<>();
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
        grid.addColumn(Person::getId).setHeader("ID").setWidth("80px");
        grid.addColumn(Person::getName).setHeader("Nom").setAutoWidth(true);
        grid.addColumn(Person::getEmail).setHeader("Email").setAutoWidth(true);
        grid.addColumn(Person::getStatus).setHeader("Statut").setWidth("120px");
        grid.addColumn(Person::getDepartment).setHeader("Département").setWidth("150px");
        grid.addColumn(Person::getAge).setHeader("Âge").setWidth("100px");
        grid.addColumn(Person::getFormattedSalary).setHeader("Salaire").setWidth("120px");
    }

    private void setupDefaultColors() {
        // Utiliser la méthode principale avec ComponentRenderer
        setRowColorProvider(person -> {
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
        setRowColorProvider(person -> {
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
        setRowColorProvider(person -> {
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
        setRowColorProvider(person -> {
            if (person == null) return null;
            return person.getId() % 2 == 0 ? "whitesmoke" : "white";
        });
    }

    private void loadSampleData() {
        List<Person> sampleData = PersonDataGenerator.generateSampleData(15);
        setItems(sampleData);
    }
}

//&&&
// Dans SelectableGrid - méthode principale à utiliser
public void setRowColorProvider(Function<T, String> colorProvider) {
    this.rowColorProvider = colorProvider;
    
    // Réappliquer simplement les renderers
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
                    .set("padding", "8px")
                    .set("margin", "-8px")
                    .set("border-left", "4px solid " + darkenColor(color));
            }
            
            return span;
        }));
    });
}

private String darkenColor(String color) {
    // Simplification - retourner une couleur plus foncée
    switch (color) {
        case "lightgreen": return "green";
        case "lightgray": return "gray";
        case "lightyellow": return "orange";
        case "lightblue": return "blue";
        case "lightcoral": return "red";
        case "plum": return "purple";
        case "gold": return "darkorange";
        case "paleturquoise": return "teal";
        case "pink": return "deeppink";
        case "orange": return "darkorange";
        case "limegreen": return "darkgreen";
        case "yellow": return "goldenrod";
        case "tomato": return "darkred";
        case "whitesmoke": return "lightgray";
        default: return "darkgray";
    }
}
