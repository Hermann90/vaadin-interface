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

    // ✅ MODIFIÉ - Appliquer les couleurs sur les LIGNES entières
    public void setRowColorProvider(Function<T, String> colorProvider) {
        this.rowColorProvider = colorProvider;
        applyRowColors();
    }

    // ✅ NOUVELLE MÉTHODE - Colorier les LIGNES entières
    private void applyRowColors() {
        if (rowColorProvider == null) return;

        // Utiliser setClassNameGenerator sur la GRID pour les lignes
        grid.setClassNameGenerator(item -> {
            String color = rowColorProvider.apply(item);
            if (color != null) {
                return "colored-row-" + color.replace("#", "").toLowerCase();
            }
            return null;
        });

        // Appliquer les styles pour les LIGNES
        applyRowStyles();
    }

    // ✅ NOUVELLE MÉTHODE - Styles pour les lignes
    private void applyRowStyles() {
        getElement().executeJs(
            "const style = document.createElement('style');" +
            "style.textContent = `" +
            "  .colored-row-lightgreen { background-color: lightgreen !important; }" +
            "  .colored-row-lightgray { background-color: lightgray !important; }" +
            "  .colored-row-lightyellow { background-color: lightyellow !important; }" +
            "  .colored-row-lightblue { background-color: lightblue !important; }" +
            "  .colored-row-lightcoral { background-color: lightcoral !important; }" +
            "  .colored-row-plum { background-color: plum !important; }" +
            "  .colored-row-gold { background-color: gold !important; }" +
            "  .colored-row-paleturquoise { background-color: paleturquoise !important; }" +
            "  .colored-row-pink { background-color: pink !important; }" +
            "  .colored-row-orange { background-color: orange !important; }" +
            "  .colored-row-limegreen { background-color: limegreen !important; }" +
            "  .colored-row-yellow { background-color: yellow !important; }" +
            "  .colored-row-tomato { background-color: tomato !important; }" +
            "  .colored-row-whitesmoke { background-color: whitesmoke !important; }" +
            "  .colored-row-white { background-color: white !important; }" +
            "  .colored-row-aliceblue { background-color: aliceblue !important; }" +
            "  .colored-row-lightcyan { background-color: lightcyan !important; }" +
            "  .colored-row-turquoise { background-color: turquoise !important; }" +
            "  .colored-row-darkturquoise { background-color: darkturquoise !important; }" +
            "`;" +
            "document.head.appendChild(style);"
        );
    }

    // ✅ MÉTHODE ALTERNATIVE - Utiliser setPartNameGenerator (recommandée)
    public void setUniformRowColors(Function<T, String> colorProvider) {
        this.rowColorProvider = colorProvider;
        
        // Utiliser setPartNameGenerator pour styliser les LIGNES
        grid.setPartNameGenerator(item -> {
            String color = colorProvider.apply(item);
            if (color != null) {
                return "colored-row";
            }
            return null;
        });

        // Appliquer les couleurs dynamiquement via JavaScript
        applyDynamicRowColors();
    }

    // ✅ APPLIQUER LES COULEURS DYNAMIQUEMENT
    private void applyDynamicRowColors() {
        getElement().executeJs(
            "setTimeout(() => {" +
            "  const grid = $0;" +
            "  const items = grid.items;" +
            "  " +
            "  items.forEach((item, index) => {" +
            "    const row = grid.$.table.querySelector(`tr[part~=\"row\"]:nth-child(${index + 1})`);" +
            "    if (row) {" +
            "      const color = $1(item);" +
            "      if (color) {" +
            "        row.style.backgroundColor = color;" +
            "        // Colorier aussi toutes les cellules de la ligne" +
            "        const cells = row.querySelectorAll('td');" +
            "        cells.forEach(cell => {" +
            "          cell.style.backgroundColor = color;" +
            "        });" +
            "      }" +
            "    }" +
            "  });" +
            "}, 100);",
            grid.getElement(),
            (SerializableFunction<T, String>) rowColorProvider::apply
        );
    }

    // ✅ MÉTHODE SIMPLE - Sans JavaScript complexe
    public void setSimpleRowColors(Function<T, String> colorProvider) {
        this.rowColorProvider = colorProvider;
        
        // Utiliser ClassNameGenerator sur la grid (pas sur les colonnes)
        grid.setClassNameGenerator(item -> {
            String color = colorProvider.apply(item);
            if (color != null) {
                return "row-colored";
            }
            return null;
        });

        // Style simple pour toutes les lignes colorées
        getElement().executeJs(
            "const style = document.createElement('style');" +
            "style.textContent = `" +
            "  .row-colored { background-color: inherit !important; }" +
            "  .row-colored td { background-color: inherit !important; }" +
            "`;" +
            "document.head.appendChild(style);"
        );

        // Forcer l'application des couleurs
        refreshRowColors();
    }

    // ✅ RAFRAÎCHIR LES COULEURS DES LIGNES
    private void refreshRowColors() {
        if (rowColorProvider == null) return;

        getElement().executeJs(
            "setTimeout(() => {" +
            "  const grid = $0;" +
            "  const items = grid.items;" +
            "  " +
            "  if (items) {" +
            "    items.forEach((item, index) => {" +
            "      const row = grid.querySelector(`tr[role=\"row\"]:nth-child(${index + 2})`);" +
            "      if (row) {" +
            "        const color = $1(item);" +
            "        if (color) {" +
            "          row.style.backgroundColor = color;" +
            "          // Appliquer à toutes les cellules" +
            "          const cells = row.querySelectorAll('[part~=\"cell\"]');" +
            "          cells.forEach(cell => {" +
            "            cell.style.backgroundColor = color;" +
            "          });" +
            "        }" +
            "      }" +
            "    });" +
            "  }" +
            "}, 50);",
            grid.getElement(),
            (SerializableFunction<T, String>) rowColorProvider::apply
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
        grid.setItems(items);
        // Rafraîchir les couleurs des LIGNES après chargement
        getUI().ifPresent(ui -> ui.access(() -> {
            refreshRowColors();
        }));
    }

    public void setItems(Collection<T> items) {
        grid.setItems(items);
        getUI().ifPresent(ui -> ui.access(() -> {
            refreshRowColors();
        }));
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

//&&

public class PersonGrid extends SelectableGrid<Person> {

    public PersonGrid() {
        super(Person.class);
        configureGrid();
        setupRowColors(); // ✅ MODIFIÉ - pour les LIGNES
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

    private void setupRowColors() {
        // ✅ UTILISER LES NOUVELLES MÉTHODES POUR LES LIGNES
        setUniformRowColors(person -> {
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

    // Méthode pour couleurs par département (LIGNES)
    public void setDepartmentRowColors() {
        setUniformRowColors(person -> {
            if (person == null) return null;
            
            switch (person.getDepartment()) {
                case "Ventes": return "lightgreen";
                case "Marketing": return "plum";
                case "IT": return "lightblue";
                case "Direction": return "gold";
                case "RH": return "paleturquoise";
                case "Support": return "pink";
                case "Finance": return "orange";
                default: return "white";
            }
        });
    }

    // Méthode simple pour couleurs (LIGNES)
    public void setSimpleRowColors() {
        setSimpleRowColors(person -> {
            if (person == null) return null;
            
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
