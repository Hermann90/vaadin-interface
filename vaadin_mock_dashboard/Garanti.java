// ✅ SOLUTION GARANTIE POUR LA SÉLECTION DES LIGNES
public void guaranteeRowSelectionColor() {
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    
    // 1. Écouteur sur les clics de ligne
    grid.addItemClickListener(event -> {
        T clickedItem = event.getItem();
        if (clickedItem != null) {
            setSelectedItem(clickedItem);
            forceRowSelectionColor();
        }
    });
    
    // 2. Écouteur sur les changements de sélection
    grid.asSingleSelect().addValueChangeListener(event -> {
        if (event.getValue() != null) {
            currentSelection = event.getValue();
            forceRowSelectionColor();
        }
    });
    
    // 3. Appliquer les styles CSS
    applyGuaranteedSelectionStyles();
}

// ✅ APPLIQUER LES STYLES CSS
private void applyGuaranteedSelectionStyles() {
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  /* RÉINITIALISATION */" +
        "  vaadin-grid-cell-content {" +
        "    padding: var(--lumo-space-s);" +
        "  }" +
        "  " +
        "  /* LIGNE SÉLECTIONNÉE - NOIR */" +
        "  vaadin-grid-row[selected] {" +
        "    background: black !important;" +
        "    color: white !important;" +
        "  }" +
        "  " +
        "  /* CELLULES DE LA LIGNE SÉLECTIONNÉE */" +
        "  vaadin-grid-row[selected] vaadin-grid-cell-content {" +
        "    background: black !important;" +
        "    color: white !important;" +
        "  }" +
        "  " +
        "  /* TOUTES LES CELLULES DE LA LIGNE SÉLECTIONNÉE */" +
        "  vaadin-grid-row[selected] ::slotted(*) {" +
        "    background: black !important;" +
        "    color: white !important;" +
        "  }" +
        "  " +
        "  /* STYLE DIRECT SUR LES ÉLÉMENTS */" +
        "  [selected] {" +
        "    background: black !important;" +
        "    color: white !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

// ✅ FORCER LA COULEUR DE SÉLECTION
private void forceRowSelectionColor() {
    getElement().executeJs(
        "setTimeout(() => {" +
        "  const grid = $0;" +
        "  " +
        "  // 1. RETIRER TOUTES LES ANCIENNES SÉLECTIONS" +
        "  const allRows = grid.querySelectorAll('tr');" +
        "  allRows.forEach(row => {" +
        "    row.style.backgroundColor = '';" +
        "    row.style.color = '';" +
        "    " +
        "    // Réinitialiser toutes les cellules" +
        "    const cells = row.querySelectorAll('td');" +
        "    cells.forEach(cell => {" +
        "      cell.style.backgroundColor = '';" +
        "      cell.style.color = '';" +
        "      " +
        "      // Réinitialiser le contenu des cellules" +
        "      const content = cell.querySelector('div');" +
        "      if (content) {" +
        "        content.style.backgroundColor = '';" +
        "        content.style.color = '';" +
        "      }" +
        "    });" +
        "  });" +
        "  " +
        "  // 2. APPLIQUER LE NOIR À LA LIGNE SÉLECTIONNÉE" +
        "  const selectedRow = grid.querySelector('tr[selected]');" +
        "  if (!selectedRow) return;" +
        "  " +
        "  // Colorier la ligne entière" +
        "  selectedRow.style.backgroundColor = 'black';" +
        "  selectedRow.style.color = 'white';" +
        "  " +
        "  // Colorier toutes les cellules de la ligne" +
        "  const selectedCells = selectedRow.querySelectorAll('td');" +
        "  selectedCells.forEach(cell => {" +
        "    cell.style.backgroundColor = 'black';" +
        "    cell.style.color = 'white';" +
        "    " +
        "    // Colorier le contenu des cellules" +
        "    const content = cell.querySelector('div');" +
        "    if (content) {" +
        "      content.style.backgroundColor = 'black';" +
        "      content.style.color = 'white';" +
        "    }" +
        "  });" +
        "  " +
        "}, 100);",
        grid.getElement()
    );
}

// ✅ SURCHARGER setSelectedItem POUR GARANTIR LA COULEUR
@Override
public void setSelectedItem(T item) {
    T oldSelection = currentSelection;
    currentSelection = item;
    
    // Sélectionner dans la grid
    grid.select(item);
    
    // FORCER la couleur de sélection
    forceRowSelectionColor();
    
    if (oldSelection == null || !item.equals(oldSelection)) {
        fireSelectionChanged(oldSelection, currentSelection);
    }
}

// ✅ MÉTHODE POUR TESTER
public void testSelectionWithColor() {
    if (!getItems().isEmpty()) {
        setSelectedItem(getItems().get(0));
    }
}

// hh
public PersonGrid() {
    super(Person.class);
    configureGrid();
    setupRowColors();
    loadSampleData();
    
    // ✅ APPELER LA SOLUTION GARANTIE
    guaranteeRowSelectionColor();
}

// ✅ OPTIONNEL : DÉSACTIVER LES COULEURS PAR STATUT POUR ÉVITER LES CONFLITS
private void setupRowColors() {
    // Ne rien faire ou utiliser une méthode différente
    // pour éviter les conflits avec la sélection
}

//€€
public MainView() {
    initializeComponents();
    setupLayout();
    setupListeners();
    
    // ✅ TESTER IMMÉDIATEMENT
    getUI().ifPresent(ui -> ui.access(() -> {
        // Attendre que la grid soit chargée puis tester
        ui.setPollInterval(500);
        ui.addPollListener(event -> {
            if (!personGrid.getItems().isEmpty()) {
                personGrid.testSelectionWithColor();
                ui.setPollInterval(-1); // Arrêter le polling
            }
        });
    }));
}

//88
// ✅ VERSION EXTRÊME - TOUT REDÉFINIR
public void extremeRowSelection() {
    grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    
    // Supprimer tous les anciens écouteurs
    grid.removeAll();
    
    // Nouvel écouteur simple et direct
    grid.addItemClickListener(event -> {
        // Désélectionner tout
        grid.deselectAll();
        
        // Sélectionner l'item cliqué
        grid.select(event.getItem());
        
        // Appliquer manuellement la couleur
        getElement().executeJs(
            "setTimeout(() => {" +
            "  const grid = $0;" +
            "  " +
            "  // Trouver toutes les lignes" +
            "  const rows = grid.$.table.querySelectorAll('tr');" +
            "  " +
            "  rows.forEach(row => {" +
            "    if (row.getAttribute('selected') !== null) {" +
            "      // Ligne sélectionnée - NOIR" +
            "      row.style.background = 'black';" +
            "      row.style.color = 'white';" +
            "      " +
            "      // Toutes les cellules en noir" +
            "      const cells = row.cells;" +
            "      for (let cell of cells) {" +
            "        cell.style.background = 'black';" +
            "        cell.style.color = 'white';" +
            "      }" +
            "    } else {" +
            "      // Ligne non sélectionnée - normal" +
            "      row.style.background = '';" +
            "      row.style.color = '';" +
            "    }" +
            "  });" +
            "}, 50);",
            grid.getElement()
        );
    });
}
