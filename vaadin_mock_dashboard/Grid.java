public class SingleSelectGrid<T> extends VerticalLayout {
    private Grid<T> grid;
    private List<T> dataItems;
    private T selectedItem;

    public SingleSelectGrid(Class<T> beanType) {
        this.dataItems = new ArrayList<>();
        initializeGrid(beanType);
        add(grid);
    }

    private void initializeGrid(Class<T> beanType) {
        grid = new Grid<>(beanType);
        
        // Configuration de la sélection simple
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        
        // Désactiver l'édition et autres interactions non souhaitées
        grid.setEnabled(true);
        
        // Gérer la sélection
        grid.asSingleSelect().addValueChangeListener(event -> {
            T previousSelection = selectedItem;
            selectedItem = event.getValue();
            
            // Notifier du changement de sélection
            onSelectionChanged(previousSelection, selectedItem);
        });
    }

    protected void onSelectionChanged(T previousSelection, T newSelection) {
        // Méthode à surcharger pour réagir aux changements de sélection
        System.out.println("Sélection changée : " + 
            (previousSelection != null ? previousSelection.toString() : "null") + 
            " -> " + 
            (newSelection != null ? newSelection.toString() : "null"));
    }

    // Méthodes utilitaires
    public void setItems(List<T> items) {
        this.dataItems = new ArrayList<>(items);
        grid.setItems(dataItems);
    }

    public void addItem(T item) {
        dataItems.add(item);
        refreshGrid();
    }

    public void removeItem(T item) {
        dataItems.remove(item);
        refreshGrid();
    }

    public T getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(T item) {
        grid.asSingleSelect().setValue(item);
    }

    public void clearSelection() {
        grid.asSingleSelect().clear();
    }

    private void refreshGrid() {
        grid.setItems(dataItems);
    }

    public Grid<T> getGrid() {
        return grid;
    }
}

/// aaaaa

public class PersonGrid extends SingleSelectGrid<Person> {

    public PersonGrid() {
        super(Person.class);
        
        // Configuration des colonnes
        getGrid().removeAllColumns();
        getGrid().addColumn(Person::getId).setHeader("ID");
        getGrid().addColumn(Person::getName).setHeader("Nom");
        getGrid().addColumn(Person::getEmail).setHeader("Email");
        
        // Charger des données d'exemple
        setItems(createSampleData());
    }

    @Override
    protected void onSelectionChanged(Person previousSelection, Person newSelection) {
        // Implémentation spécifique
        if (newSelection != null) {
            Notification.show("Personne sélectionnée : " + newSelection.getName());
        } else {
            Notification.show("Aucune personne sélectionnée");
        }
    }

    private List<Person> createSampleData() {
        return Arrays.asList(
            new Person(1, "Jean Dupont", "jean.dupont@email.com"),
            new Person(2, "Marie Martin", "marie.martin@email.com"),
            new Person(3, "Pierre Durand", "pierre.durand@email.com")
        );
    }
}

/// bbb

public class Person {
    private int id;
    private String name;
    private String email;

    public Person() {}

    public Person(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}

/// ccc

public class SelectableGrid<T> extends VerticalLayout {
    private Grid<T> grid;
    private List<SelectionListener<T>> selectionListeners = new ArrayList<>();

    public SelectableGrid(Class<T> beanType) {
        grid = new Grid<>(beanType);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        
        grid.asSingleSelect().addValueChangeListener(event -> {
            fireSelectionChanged(event.getOldValue(), event.getValue());
        });
        
        add(grid);
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

    public T getSelectedItem() {
        return grid.asSingleSelect().getValue();
    }

    public void setSelectedItem(T item) {
        grid.asSingleSelect().setValue(item);
    }

    public Grid<T> getGrid() {
        return grid;
    }
}

/// ddd
public class MainView extends VerticalLayout {
    
    public MainView() {
        PersonGrid personGrid = new PersonGrid();
        
        // Ajouter un écouteur externe
        personGrid.addSelectionListener((oldValue, newValue) -> {
            if (newValue != null) {
                // Mettre à jour d'autres composants en fonction de la sélection
                updateDetailsPanel(newValue);
            }
        });
        
        add(personGrid);
    }
    
    private void updateDetailsPanel(Person person) {
        // Mettre à jour un panneau de détails
    }
}

