// ✅ CORRIGER LA VISUALISATION DE LA SÉLECTION
public void fixSelectionVisibility() {
    // Réinitialiser le mode de sélection
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    
    // Appliquer les styles CSS pour la sélection
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  /* COULEUR DE SÉLECTION PRINCIPALE */" +
        "  vaadin-grid::part(selected-row) {" +
        "    background-color: #2196F3 !important;" +
        "  }" +
        "  vaadin-grid::part(selected-row-cell) {" +
        "    background-color: #2196F3 !important;" +
        "    color: white !important;" +
        "  }" +
        "  /* SURVOL */" +
        "  vaadin-grid::part(row):hover {" +
        "    background-color: #f5f5f5 !important;" +
        "  }" +
        "  /* CURSEUR */" +
        "  vaadin-grid::part(row) {" +
        "    cursor: pointer !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

// ✅ METHODE ALTERNATIVE AVEC JAVASCRIPT DIRECT
public void forceSelectionStyle() {
    grid.addSelectionListener(event -> {
        event.getFirstSelectedItem().ifPresent(item -> {
            // Forcer le style après la sélection
            getElement().executeJs(
                "setTimeout(() => {" +
                "  const grid = $0;" +
                "  const selectedRows = grid.shadowRoot.querySelectorAll('[part~=\"row\"][selected]');" +
                "  selectedRows.forEach(row => {" +
                "    row.style.backgroundColor = '#2196F3';" +
                "    row.style.color = 'white';" +
                "    const cells = row.querySelectorAll('[part~=\"cell\"]');" +
                "    cells.forEach(cell => {" +
                "      cell.style.backgroundColor = '#2196F3';" +
                "      cell.style.color = 'white';" +
                "    });" +
                "  });" +
                "}, 50);",
                grid.getElement()
            );
        });
    });
}

// ✅ SOLUTION COMPLÈTE POUR LA SÉLECTION VISIBLE
public void enableVisibleSelection() {
    // 1. Mode de sélection
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    
    // 2. Écouteur pour appliquer les styles après sélection
    grid.asSingleSelect().addValueChangeListener(event -> {
        if (event.getValue() != null) {
            currentSelection = event.getValue();
            applySelectionStyle();
        }
    });
    
    // 3. Styles CSS
    applySelectionCSS();
}

private void applySelectionCSS() {
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  vaadin-grid [part~='row'][selected] {" +
        "    background-color: #2196F3 !important;" +
        "    color: white !important;" +
        "  }" +
        "  vaadin-grid [part~='row'][selected] [part~='cell'] {" +
        "    background-color: #2196F3 !important;" +
        "    color: white !important;" +
        "  }" +
        "  vaadin-grid [part~='row']:hover {" +
        "    background-color: #e3f2fd !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

private void applySelectionStyle() {
    getElement().executeJs(
        "setTimeout(() => {" +
        "  const grid = $0;" +
        "  // Nettoyer les anciennes sélections" +
        "  const allRows = grid.querySelectorAll('[part~=\"row\"]');" +
        "  allRows.forEach(row => {" +
        "    row.style.backgroundColor = '';" +
        "    row.style.color = '';" +
        "    const cells = row.querySelectorAll('[part~=\"cell\"]');" +
        "    cells.forEach(cell => {" +
        "      cell.style.backgroundColor = '';" +
        "      cell.style.color = '';" +
        "    });" +
        "  });" +
        "  " +
        "  // Appliquer sur la ligne sélectionnée" +
        "  const selectedRow = grid.querySelector('[part~=\"row\"][selected]');" +
        "  if (selectedRow) {" +
        "    selectedRow.style.backgroundColor = '#2196F3';" +
        "    selectedRow.style.color = 'white';" +
        "    const cells = selectedRow.querySelectorAll('[part~=\"cell\"]');" +
        "    cells.forEach(cell => {" +
        "      cell.style.backgroundColor = '#2196F3';" +
        "      cell.style.color = 'white';" +
        "    });" +
        "  }" +
        "}, 100);",
        grid.getElement()
    );
}

// ✅ METHODE SIMPLE ET EFFICACE
public void makeSelectionVisible() {
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    
    // Utiliser les variants de thème Vaadin
    grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  .vaadin-grid-container [part~='body-cell']:not([part~='details-cell']):not([part~='selected']) {" +
        "    background: var(--lumo-contrast-5pct);" +
        "  }" +
        "  .vaadin-grid-container [part~='body-cell'][selected] {" +
        "    background: #2196F3 !important;" +
        "    color: white !important;" +
        "  }" +
        "  [part~='selected'] {" +
        "    background: #2196F3 !important;" +
        "  }" +
        "  [part~='selected'] [part~='cell'] {" +
        "    background: #2196F3 !important;" +
        "    color: white !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

///mmm
public PersonGrid() {
    super(Person.class);
    configureGrid();
    setupRowColors();
    loadSampleData();
    
    // ✅ APPELER CETTE MÉTHODE POUR RENDRE LA SÉLECTION VISIBLE
    makeSelectionVisible();
    
    // OU
    // enableVisibleSelection();
}

//€€
public MainView() {
    initializeComponents();
    setupLayout();
    setupListeners();
    
    // ✅ TESTER LA SÉLECTION
    Button testButton = new Button("Sélectionner première ligne", event -> {
        if (!personGrid.getItems().isEmpty()) {
            personGrid.setSelectedItem(personGrid.getItems().get(0));
        }
    });
    add(testButton);
}

