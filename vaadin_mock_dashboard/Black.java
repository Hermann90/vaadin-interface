// ✅ COULEUR DE SÉLECTION NOIRE POUR LES LIGNES
public void setBlackRowSelection() {
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  /* LIGNE SÉLECTIONNÉE EN NOIR */" +
        "  [part~='row'][selected] {" +
        "    background-color: #000000 !important;" +
        "    color: #FFFFFF !important;" +
        "  }" +
        "  /* CELLULES DE LA LIGNE SÉLECTIONNÉE */" +
        "  [part~='row'][selected] [part~='cell'] {" +
        "    background-color: #000000 !important;" +
        "    color: #FFFFFF !important;" +
        "  }" +
        "  /* CONTENU DES CELLULES */" +
        "  [part~='row'][selected] [part~='cell'] [part~='content'] {" +
        "    background-color: #000000 !important;" +
        "    color: #FFFFFF !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

// ✅ MÉTHODE RENFORCÉE POUR LES LIGNES
public void enableBlackRowSelection() {
    setBlackRowSelection();
    
    // S'assurer que la couleur est appliquée après chaque sélection
    grid.asSingleSelect().addValueChangeListener(event -> {
        if (event.getValue() != null) {
            applyBlackToSelectedRow();
        }
    });
}

// ✅ APPLIQUER LE NOIR À LA LIGNE SÉLECTIONNÉE
private void applyBlackToSelectedRow() {
    getElement().executeJs(
        "setTimeout(() => {" +
        "  const grid = $0;" +
        "  const selectedRow = grid.querySelector('[part~=\"row\"][selected]');" +
        "  " +
        "  if (selectedRow) {" +
        "    // Appliquer le noir à la ligne" +
        "    selectedRow.style.backgroundColor = '#000000';" +
        "    selectedRow.style.color = '#FFFFFF';" +
        "    " +
        "    // Appliquer le noir à toutes les cellules de la ligne" +
        "    const cells = selectedRow.querySelectorAll('[part~=\"cell\"]');" +
        "    cells.forEach(cell => {" +
        "      cell.style.backgroundColor = '#000000';" +
        "      cell.style.color = '#FFFFFF';" +
        "    });" +
        "  }" +
        "}, 50);",
        grid.getElement()
    );
}

// ✅ FORCER LA SÉLECTION NOIRE AU DÉMARRAGE
public void initializeBlackSelection() {
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    setBlackRowSelection();
    
    // Réappliquer après le chargement des données
    grid.addDataProviderListener(event -> {
        getUI().ifPresent(ui -> ui.access(() -> {
            applyBlackToSelectedRow();
        }));
    });
}

//@@
public PersonGrid() {
    super(Person.class);
    configureGrid();
    setupRowColors();
    loadSampleData();
    
    // ✅ ACTIVER LA SÉLECTION NOIRE DES LIGNES
    enableBlackRowSelection();
}
//&&&

// ✅ SOLUTION ULTIME POUR LIGNES NOIRES
public void ultimateBlackRowSelection() {
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    
    // Écouteur sur les clics de ligne
    grid.addItemClickListener(event -> {
        applyUltimateBlackSelection();
    });
    
    // Écouteur sur les changements de sélection
    grid.asSingleSelect().addValueChangeListener(event -> {
        applyUltimateBlackSelection();
    });
    
    // Styles CSS complets
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  /* RÉINITIALISER TOUTES LES LIGNES */" +
        "  [part~='row'] {" +
        "    background-color: inherit;" +
        "    color: inherit;" +
        "  }" +
        "  [part~='cell'] {" +
        "    background-color: inherit;" +
        "    color: inherit;" +
        "  }" +
        "  " +
        "  /* LIGNE SÉLECTIONNÉE - NOIR */" +
        "  [part~='row'][selected] {" +
        "    background-color: #000000 !important;" +
        "    color: #FFFFFF !important;" +
        "  }" +
        "  " +
        "  /* CELLULES DE LA LIGNE SÉLECTIONNÉE */" +
        "  [part~='row'][selected] [part~='cell'] {" +
        "    background-color: #000000 !important;" +
        "    color: #FFFFFF !important;" +
        "  }" +
        "  " +
        "  /* CONTENU DES CELLULES */" +
        "  [part~='row'][selected] [part~='cell'] > * {" +
        "    background-color: #000000 !important;" +
        "    color: #FFFFFF !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

private void applyUltimateBlackSelection() {
    getElement().executeJs(
        "setTimeout(() => {" +
        "  const grid = $0;" +
        "  " +
        "  // 1. Réinitialiser toutes les lignes" +
        "  const allRows = grid.querySelectorAll('[part~=\"row\"]');" +
        "  allRows.forEach(row => {" +
        "    row.style.backgroundColor = '';" +
        "    row.style.color = '';" +
        "    " +
        "    const cells = row.querySelectorAll('[part~=\"cell\"]');" +
        "    cells.forEach(cell => {" +
        "      cell.style.backgroundColor = '';" +
        "      cell.style.color = '';" +
        "    });" +
        "  });" +
        "  " +
        "  // 2. Appliquer le noir à la ligne sélectionnée" +
        "  const selectedRow = grid.querySelector('[part~=\"row\"][selected]');" +
        "  if (selectedRow) {" +
        "    selectedRow.style.backgroundColor = '#000000';" +
        "    selectedRow.style.color = '#FFFFFF';" +
        "    " +
        "    const cells = selectedRow.querySelectorAll('[part~=\"cell\"]');" +
        "    cells.forEach(cell => {" +
        "      cell.style.backgroundColor = '#000000';" +
        "      cell.style.color = '#FFFFFF';" +
        "    });" +
        "  }" +
        "}, 100);",
        grid.getElement()
    );
}

//gg
public PersonGrid() {
    super(Person.class);
    configureGrid();
    setupRowColors();
    loadSampleData();
    
    // ✅ UTILISER LA SOLUTION ULTIME
    ultimateBlackRowSelection();
}

