// ✅ CHANGER LA COULEUR DE SÉLECTION EN NOIR
public void setBlackSelection() {
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  /* COULEUR DE SÉLECTION NOIRE */" +
        "  vaadin-grid [part~='row'][selected] {" +
        "    background-color: #000000 !important;" +
        "    color: white !important;" +
        "  }" +
        "  vaadin-grid [part~='row'][selected] [part~='cell'] {" +
        "    background-color: #000000 !important;" +
        "    color: white !important;" +
        "  }" +
        "  /* SURVOL */" +
        "  vaadin-grid [part~='row']:hover {" +
        "    background-color: #333333 !important;" +
        "    color: white !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

// ✅ METHODE POUR APPLIQUER LA SÉLECTION NOIRE APRÈS CLIC
public void enableBlackSelection() {
    setBlackSelection();
    
    // S'assurer que la sélection est visible après chaque clic
    grid.addItemClickListener(event -> {
        getElement().executeJs(
            "setTimeout(() => {" +
            "  const grid = $0;" +
            "  const selectedRows = grid.querySelectorAll('[part~=\"row\"][selected]');" +
            "  selectedRows.forEach(row => {" +
            "    row.style.backgroundColor = '#000000';" +
            "    row.style.color = 'white';" +
            "    const cells = row.querySelectorAll('[part~=\"cell\"]');" +
            "    cells.forEach(cell => {" +
            "      cell.style.backgroundColor = '#000000';" +
            "      cell.style.color = 'white';" +
            "    });" +
            "  });" +
            "}, 10);",
            grid.getElement()
        );
    });
}

// ✅ METHODE FORCÉE POUR SÉLECTION NOIRE
public void forceBlackSelection() {
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    
    // Écouteur pour appliquer le noir après chaque sélection
    grid.asSingleSelect().addValueChangeListener(event -> {
        if (event.getValue() != null) {
            currentSelection = event.getValue();
            applyBlackSelectionStyle();
        }
    });
    
    applyBlackSelectionCSS();
}

private void applyBlackSelectionCSS() {
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  .black-selection [part~='selected'] {" +
        "    background: #000000 !important;" +
        "    color: white !important;" +
        "  }" +
        "  .black-selection [part~='selected'] [part~='cell'] {" +
        "    background: #000000 !important;" +
        "    color: white !important;" +
        "  }" +
        "  .black-selection [part~='row']:hover {" +
        "    background: #333333 !important;" +
        "    color: white !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
    
    // Ajouter la classe à la grid
    grid.addClassName("black-selection");
}

private void applyBlackSelectionStyle() {
    getElement().executeJs(
        "setTimeout(() => {" +
        "  const grid = $0;" +
        "  // Nettoyer toutes les lignes" +
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
        "  // Appliquer le noir sur la ligne sélectionnée" +
        "  const selectedRow = grid.querySelector('[part~=\"row\"][selected]');" +
        "  if (selectedRow) {" +
        "    selectedRow.style.backgroundColor = '#000000';" +
        "    selectedRow.style.color = 'white';" +
        "    const cells = selectedRow.querySelectorAll('[part~=\"cell\"]');" +
        "    cells.forEach(cell => {" +
        "      cell.style.backgroundColor = '#000000';" +
        "      cell.style.color = 'white';" +
        "    });" +
        "  }" +
        "}, 50);",
        grid.getElement()
    );
}


//€€
public PersonGrid() {
    super(Person.class);
    configureGrid();
    setupRowColors();
    loadSampleData();
    
    // ✅ APPELER CETTE MÉTHODE POUR LA SÉLECTION NOIRE
    enableBlackSelection();
    
    // OU pour une méthode plus robuste :
    // forceBlackSelection();
}

// ✅ MÉTHODE POUR TESTER
public void testBlackSelection() {
    if (!getItems().isEmpty()) {
        setSelectedItem(getItems().get(0));
        // Forcer l'application du style noir
        applyBlackSelectionStyle();
    }
}

//€€
public MainView() {
    initializeComponents();
    setupLayout();
    setupListeners();
    
    // ✅ TESTER LA SÉLECTION NOIRE
    Button testButton = new Button("Sélectionner première ligne", event -> {
        personGrid.testBlackSelection();
    });
    add(testButton);
}

private void initializeComponents() {
    personGrid = new PersonGrid();
    
    // ✅ S'ASSURER QUE LA SÉLECTION NOIRE EST ACTIVÉE
    personGrid.enableBlackSelection();
}

//@&
// ✅ MÉTHODE ULTIME POUR SÉLECTION NOIRE
public void ultimateBlackSelection() {
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    
    // Appliquer les styles directement via JavaScript
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  /* FORCER LA SÉLECTION NOIRE */" +
        "  vaadin-grid-row[selected] {" +
        "    background: black !important;" +
        "    color: white !important;" +
        "  }" +
        "  vaadin-grid-row[selected]::before {" +
        "    background: black !important;" +
        "  }" +
        "  vaadin-grid-cell-content[selected] {" +
        "    background: black !important;" +
        "    color: white !important;" +
        "  }" +
        "  td[selected] {" +
        "    background: black !important;" +
        "    color: white !important;" +
        "  }" +
        "  tr[selected] {" +
        "    background: black !important;" +
        "    color: white !important;" +
        "  }" +
        "  [selected] {" +
        "    background: black !important;" +
        "    color: white !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
    
    // Forcer le rafraîchissement après chaque clic
    grid.addItemClickListener(event -> {
        getElement().executeJs(
            "setTimeout(() => {" +
            "  document.querySelectorAll('[selected]').forEach(el => {" +
            "    el.style.backgroundColor = 'black';" +
            "    el.style.color = 'white';" +
            "  });" +
            "}, 10);"
        );
    });
}
