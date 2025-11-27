public class SelectableGrid<T> extends VerticalLayout {
    private Grid<T> grid;
    private List<SelectionListener<T>> selectionListeners = new ArrayList<>();
    private T currentSelection;

    public SelectableGrid(Class<T> beanType) {
        grid = new Grid<>(beanType);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        
        // Gestion personnalisée de la sélection
        grid.addItemClickListener(event -> {
            T clickedItem = event.getItem();
            handleItemClick(clickedItem);
        });
        
        add(grid);
    }

    private void handleItemClick(T clickedItem) {
        T oldSelection = currentSelection;
        
        if (clickedItem.equals(currentSelection)) {
            // Même item cliqué : on garde la sélection
            // Ne rien faire ou forcer la ré-selection si nécessaire
            grid.select(clickedItem); // S'assurer qu'il reste sélectionné
            currentSelection = clickedItem;
        } else {
            // Nouvel item cliqué : changement de sélection
            currentSelection = clickedItem;
            grid.select(clickedItem);
            fireSelectionChanged(oldSelection, currentSelection);
        }
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

    // Méthodes utilitaires
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
        if (!item.equals(oldSelection)) {
            fireSelectionChanged(oldSelection, currentSelection);
        }
    }

    public void clearSelection() {
        T oldSelection = currentSelection;
        currentSelection = null;
        grid.deselectAll();
        if (oldSelection != null) {
            fireSelectionChanged(oldSelection, null);
        }
    }

    public Grid<T> getGrid() {
        return grid;
    }
}

///&&&
public class SelectableGrid<T> extends VerticalLayout {
    private Grid<T> grid;
    private List<SelectionListener<T>> selectionListeners = new ArrayList<>();
    private T currentSelection;

    public SelectableGrid(Class<T> beanType) {
        grid = new Grid<>(beanType);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        
        // Utiliser un ValueChangeListener mais avec gestion personnalisée
        grid.asSingleSelect().addValueChangeListener(event -> {
            T newValue = event.getValue();
            T oldValue = currentSelection;
            
            if (newValue == null && oldValue != null) {
                // Tentative de désélection - l'empêcher
                grid.asSingleSelect().setValue(oldValue);
            } else if (newValue != null) {
                // Nouvelle sélection ou même item
                currentSelection = newValue;
                if (!newValue.equals(oldValue)) {
                    fireSelectionChanged(oldValue, newValue);
                }
            }
        });
        
        add(grid);
    }

    // ... (le reste des méthodes reste identique)
}

/// !!! meileur 
public class SelectableGrid<T> extends VerticalLayout {
    private Grid<T> grid;
    private List<SelectionListener<T>> selectionListeners = new ArrayList<>();
    private T currentSelection;
    private boolean allowDeselection = false; // Contrôle si on permet la désélection

    public SelectableGrid(Class<T> beanType) {
        grid = new Grid<>(beanType);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        
        setupSelectionHandling();
        add(grid);
    }

    private void setupSelectionHandling() {
        // Approche avec ItemClickListener pour un contrôle total
        grid.addItemClickListener(event -> {
            T clickedItem = event.getItem();
            
            if (clickedItem.equals(currentSelection)) {
                // Même item cliqué : soit on garde sélectionné, soit on désélectionne selon le paramètre
                if (allowDeselection) {
                    clearSelection();
                } else {
                    // Forcer la sélection à rester
                    refreshSelection();
                }
            } else {
                // Nouvel item : changement normal
                setSelectedItem(clickedItem);
            }
        });
        
        // Backup avec ValueChangeListener
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null && !allowDeselection && currentSelection != null) {
                // Rétablir la sélection si désélection non autorisée
                grid.asSingleSelect().setValue(currentSelection);
            }
        });
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

    public Grid<T> getGrid() {
        return grid;
    }
}

/// bb

@Route("")
public class MainView extends VerticalLayout {
    private PersonGrid personGrid;
    private Div detailsPanel;
    private Button actionButton;
    private Checkbox allowDeselectCheckbox;

    public MainView() {
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        personGrid = new PersonGrid();
        personGrid.setAllowDeselection(false); // ← DÉSACTIVER LA DÉSÉLECTION
        
        detailsPanel = new Div();
        detailsPanel.setWidthFull();
        detailsPanel.getStyle()
            .set("border", "1px solid var(--lumo-contrast-20pct)")
            .set("padding", "1rem")
            .set("border-radius", "0.5rem")
            .set("background", "var(--lumo-contrast-5pct)");
        
        actionButton = new Button("Action sur la sélection");
        actionButton.setEnabled(false);
        
        allowDeselectCheckbox = new Checkbox("Permettre la désélection");
        allowDeselectCheckbox.setValue(false);
    }

    private void setupLayout() {
        setPadding(true);
        setSpacing(true);
        
        H2 title = new H2("Grid Sélectionnable");
        HorizontalLayout options = new HorizontalLayout(allowDeselectCheckbox);
        options.setAlignItems(Alignment.CENTER);
        
        add(title, options, personGrid, actionButton, detailsPanel);
    }

    private void setupListeners() {
        // Écouteur de sélection
        personGrid.addSelectionListener((oldValue, newValue) -> {
            if (newValue != null) {
                actionButton.setEnabled(true);
                updateDetailsPanel(newValue);
                
                Notification.show(
                    "Sélection changée", 
                    "Nouvelle sélection: " + newValue.getName(), 
                    2000, 
                    Notification.Position.BOTTOM_START
                );
            } else {
                actionButton.setEnabled(false);
                detailsPanel.removeAll();
                detailsPanel.setText("Aucune personne sélectionnée");
            }
        });

        // Action sur le bouton
        actionButton.addClickListener(event -> {
            Person selected = personGrid.getSelectedItem();
            if (selected != null) {
                Notification.show("Action sur: " + selected.getName());
            }
        });

        // Contrôle de la désélection
        allowDeselectCheckbox.addValueChangeListener(event -> {
            personGrid.setAllowDeselection(event.getValue());
        });
    }

    private void updateDetailsPanel(Person person) {
        detailsPanel.removeAll();
        
        VerticalLayout details = new VerticalLayout();
        details.setSpacing(false);
        details.setPadding(false);
        
        H3 name = new H3(person.getName());
        name.getStyle().set("margin-top", "0");
        
        Div email = new Div(new Text("Email: " + person.getEmail()));
        Div id = new Div(new Text("ID: " + person.getId()));
        
        details.add(name, email, id);
        detailsPanel.add(details);
    }
}

