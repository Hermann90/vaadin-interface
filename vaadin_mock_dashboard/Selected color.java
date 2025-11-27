// ✅ CHANGER LA COULEUR DE SÉLECTION
public void setSelectionColor(String color) {
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  [part~='selected'] {" +
        "    background-color: " + color + " !important;" +
        "  }" +
        "  [part~='selected'] [part~='cell'] {" +
        "    background-color: " + color + " !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

// ✅ COULEUR DE SÉLECTION AVEC EFFET DE SURBRillance
public void setHighlightSelection(String backgroundColor, String textColor) {
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  [part~='selected'] {" +
        "    background-color: " + backgroundColor + " !important;" +
        "    color: " + textColor + " !important;" +
        "    font-weight: bold !important;" +
        "  }" +
        "  [part~='selected'] [part~='cell'] {" +
        "    background-color: " + backgroundColor + " !important;" +
        "    color: " + textColor + " !important;" +
        "  }" +
        "  [part~='selected'] [part~='cell']::before {" +
        "    background-color: " + backgroundColor + " !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

// ✅ COULEUR DE SÉLECTION AVEC BORDURE
public void setBorderedSelection(String backgroundColor, String borderColor) {
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  [part~='selected'] {" +
        "    background-color: " + backgroundColor + " !important;" +
        "    border: 2px solid " + borderColor + " !important;" +
        "    border-radius: 4px !important;" +
        "  }" +
        "  [part~='selected'] [part~='cell'] {" +
        "    background-color: " + backgroundColor + " !important;" +
        "  }" +
        "  [part~='selected'] [part~='cell']:first-child {" +
        "    border-left: 2px solid " + borderColor + " !important;" +
        "  }" +
        "  [part~='selected'] [part~='cell']:last-child {" +
        "    border-right: 2px solid " + borderColor + " !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

// ✅ COULEUR DE SÉLECTION AVEC DÉGRADÉ
public void setGradientSelection(String startColor, String endColor) {
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  [part~='selected'] {" +
        "    background: linear-gradient(90deg, " + startColor + ", " + endColor + ") !important;" +
        "    color: white !important;" +
        "    font-weight: bold !important;" +
        "  }" +
        "  [part~='selected'] [part~='cell'] {" +
        "    background: linear-gradient(90deg, " + startColor + ", " + endColor + ") !important;" +
        "    color: white !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

// ✅ PRÉDÉFINITIONS DE COULEURS
public void setBlueSelection() {
    setSelectionColor("#1976D2");
}

public void setGreenSelection() {
    setSelectionColor("#2E7D32");
}

public void setRedSelection() {
    setSelectionColor("#D32F2F");
}

public void setOrangeSelection() {
    setSelectionColor("#FF9800");
}

public void setPurpleSelection() {
    setSelectionColor("#7B1FA2");
}

public void setGoldSelection() {
    setHighlightSelection("#FFD700", "#000000");
}

public void setDarkSelection() {
    setHighlightSelection("#424242", "#FFFFFF");
}

///€€€

// ✅ DANS PersonGrid - AJOUTER CES MÉTHODES
public void setCustomSelectionColor(String color) {
    setSelectionColor(color);
}

public void setBlueSelection() {
    setBlueSelection();
}

public void setGreenSelection() {
    setGreenSelection();
}

public void setRedSelection() {
    setRedSelection();
}

public void setGoldSelection() {
    setGoldSelection();
}

///€€

// ✅ DANS MainView - AJOUTER UN SÉLECTEUR DE COULEUR
private void initializeComponents() {
    personGrid = new PersonGrid();
    
    // ✅ AJOUTER UN SÉLECTEUR POUR LA COULEUR DE SÉLECTION
    Select<String> selectionColorSelector = new Select<>();
    selectionColorSelector.setLabel("Couleur de sélection");
    selectionColorSelector.setItems(
        "Bleu", "Vert", "Rouge", "Orange", "Violet", "Or", "Sombre", "Dégradé Bleu"
    );
    selectionColorSelector.setValue("Bleu");
    
    selectionColorSelector.addValueChangeListener(event -> {
        switch (event.getValue()) {
            case "Bleu":
                personGrid.setBlueSelection();
                break;
            case "Vert":
                personGrid.setGreenSelection();
                break;
            case "Rouge":
                personGrid.setRedSelection();
                break;
            case "Orange":
                personGrid.setSelectionColor("orange");
                break;
            case "Violet":
                personGrid.setSelectionColor("purple");
                break;
            case "Or":
                personGrid.setGoldSelection();
                break;
            case "Sombre":
                personGrid.setDarkSelection();
                break;
            case "Dégradé Bleu":
                personGrid.setGradientSelection("#2196F3", "#1976D2");
                break;
        }
    });
}

// ✅ OU APPLIQUER DIRECTEMENT AU DÉMARRAGE
public MainView() {
    initializeComponents();
    setupLayout();
    setupListeners();
    
    // ✅ COULEUR DE SÉLECTION PAR DÉFAUT
    personGrid.setBlueSelection();
}
