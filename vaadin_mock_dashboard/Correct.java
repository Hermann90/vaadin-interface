public class SelectableGrid<T> extends VerticalLayout {
    private Grid<T> grid;
    private List<SelectionListener<T>> selectionListeners = new ArrayList<>();

    public SelectableGrid(Class<T> beanType) {
        grid = new Grid<>(beanType);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        
        // Gestion de la sélection
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

    public void setItems(Collection<T> items) {
        grid.setItems(items);
    }

    public T getSelectedItem() {
        return grid.asSingleSelect().getValue();
    }

    public void setSelectedItem(T item) {
        grid.asSingleSelect().setValue(item);
    }

    public void clearSelection() {
        grid.asSingleSelect().clear();
    }

    public Grid<T> getGrid() {
        return grid;
    }
}

///???

public class PersonGrid extends SelectableGrid<Person> {

    public PersonGrid() {
        super(Person.class);
        configureGrid();
        loadSampleData();
    }

    private void configureGrid() {
        Grid<Person> grid = getGrid();
        
        // Configuration des colonnes
        grid.removeAllColumns();
        grid.addColumn(Person::getId).setHeader("ID").setWidth("80px");
        grid.addColumn(Person::getName).setHeader("Nom").setAutoWidth(true);
        grid.addColumn(Person::getEmail).setHeader("Email").setAutoWidth(true);
        
        // Style optionnel
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    private void loadSampleData() {
        List<Person> sampleData = Arrays.asList(
            new Person(1, "Jean Dupont", "jean.dupont@email.com"),
            new Person(2, "Marie Martin", "marie.martin@email.com"),
            new Person(3, "Pierre Durand", "pierre.durand@email.com"),
            new Person(4, "Sophie Lambert", "sophie.lambert@email.com"),
            new Person(5, "Thomas Moreau", "thomas.moreau@email.com")
        );
        
        setItems(sampleData);
    }
}

///???

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

///???
@Route("")
public class MainView extends VerticalLayout {
    private PersonGrid personGrid;
    private Div detailsPanel;
    private Button actionButton;

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
            .set("border", "1px solid var(--lumo-contrast-20pct)")
            .set("padding", "1rem")
            .set("border-radius", "0.5rem")
            .set("background", "var(--lumo-contrast-5pct)");
        
        actionButton = new Button("Action sur la sélection");
        actionButton.setEnabled(false);
    }

    private void setupLayout() {
        setPadding(true);
        setSpacing(true);
        
        H2 title = new H2("Grid Sélectionnable");
        
        add(title, personGrid, actionButton, detailsPanel);
    }

    private void setupListeners() {
        // ✅ MAINTENANT FONCTIONNEL - addSelectionListener est disponible
        personGrid.addSelectionListener((oldValue, newValue) -> {
            if (newValue != null) {
                // Activer le bouton et afficher les détails
                actionButton.setEnabled(true);
                updateDetailsPanel(newValue);
                
                Notification.show(
                    "Sélection changée", 
                    "De: " + (oldValue != null ? oldValue.getName() : "aucun") + 
                    " → Vers: " + newValue.getName(), 
                    3000, 
                    Notification.Position.BOTTOM_START
                );
            } else {
                // Désactiver le bouton et vider les détails
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

