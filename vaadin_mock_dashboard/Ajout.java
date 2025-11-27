// ✅ SUPPRIMER LES ESPACES BLANCS ENTRE LES CELLULES
public void removeCellSpacing() {
    // Supprimer les padding et margins des cellules
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  vaadin-grid-cell-content {" +
        "    padding: 0 !important;" +
        "    margin: 0 !important;" +
        "  }" +
        "  [part~='cell'] {" +
        "    padding: 0 !important;" +
        "    border: none !important;" +
        "  }" +
        "  [part~='body-cell'] {" +
        "    padding: 0 !important;" +
        "    border: none !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

// ✅ METHODE AVEC STYLES COMPLETS POUR SUPPRIMER TOUS LES ESPACES
public void compactGridStyle() {
    grid.addThemeVariants(GridVariant.LUMO_COMPACT);
    
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  vaadin-grid {" +
        "    border-spacing: 0 !important;" +
        "    border-collapse: collapse !important;" +
        "  }" +
        "  [part~='cell'] {" +
        "    padding: 2px 4px !important;" +
        "    border: none !important;" +
        "    margin: 0 !important;" +
        "  }" +
        "  [part~='body-cell'] {" +
        "    padding: 2px 4px !important;" +
        "    border: none !important;" +
        "  }" +
        "  vaadin-grid-cell-content {" +
        "    padding: 0 !important;" +
        "    margin: 0 !important;" +
        "    border: none !important;" +
        "  }" +
        "  .compact-cell div {" +
        "    padding: 0 !important;" +
        "    margin: 0 !important;" +
        "    border: none !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

// ✅ METHODE POUR DES CELLULES TRES COMPACTES
public void ultraCompactGrid() {
    grid.addThemeVariants(GridVariant.LUMO_COMPACT);
    
    // Appliquer un style ultra compact
    grid.getStyle()
        .set("border", "none")
        .set("border-spacing", "0");
    
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  vaadin-grid {" +
        "    border: none !important;" +
        "    border-spacing: 0 !important;" +
        "    border-collapse: collapse !important;" +
        "  }" +
        "  [part~='row'] {" +
        "    border: none !important;" +
        "    margin: 0 !important;" +
        "  }" +
        "  [part~='cell'] {" +
        "    padding: 1px 2px !important;" +
        "    border: none !important;" +
        "    margin: 0 !important;" +
        "    border-right: 1px solid #e0e0e0 !important;" +
        "  }" +
        "  [part~='cell']:last-child {" +
        "    border-right: none !important;" +
        "  }" +
        "  [part~='body-cell'] {" +
        "    padding: 1px 2px !important;" +
        "    border: none !important;" +
        "  }" +
        "  vaadin-grid-cell-content {" +
        "    padding: 0 !important;" +
        "    margin: 0 !important;" +
        "    border: none !important;" +
        "    line-height: 1.2 !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

// ✅ METHODE POUR COLORIER SANS ESPACES
public void setCompactRowColors(Function<T, String> colorProvider) {
    this.rowColorProvider = colorProvider;
    
    // D'abord appliquer le style compact
    compactGridStyle();
    
    // Puis colorier les lignes
    grid.setPartNameGenerator(item -> {
        String color = colorProvider.apply(item);
        if (color != null) {
            return "colored-row";
        }
        return null;
    });

    // Appliquer les couleurs sur les cellules compactes
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
        "        // Appliquer à la ligne et toutes ses cellules" +
        "        row.style.backgroundColor = color;" +
        "        const cells = row.querySelectorAll('[part~=\"cell\"]');" +
        "        cells.forEach(cell => {" +
        "          cell.style.backgroundColor = color;" +
        "          cell.style.border = 'none';" +
        "          cell.style.padding = '1px 2px';" +
        "        });" +
        "      }" +
        "    }" +
        "  });" +
        "}, 100);",
        grid.getElement(),
        (SerializableFunction<T, String>) rowColorProvider::apply
    );
}

///ttt
// ✅ DANS PersonGrid - AJOUTER CES MÉTHODES
public void applyCompactStyle() {
    removeCellSpacing(); // ou compactGridStyle() ou ultraCompactGrid()
}

public void setCompactRowColors() {
    setCompactRowColors(person -> {
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

//??

// ✅ APPELER AU DÉMARRAGE
public MainView() {
    initializeComponents();
    setupLayout();
    setupListeners();
    
    // ✅ AJOUTER CET APPEL
    personGrid.applyCompactStyle();
}
