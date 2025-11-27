// ✅ CHANGER LA COULEUR DE SÉLECTION DES LIGNES
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

// ✅ COULEUR DE SÉLECTION AVEC TEXTE
public void setSelectionColor(String backgroundColor, String textColor) {
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  [part~='selected'] {" +
        "    background-color: " + backgroundColor + " !important;" +
        "    color: " + textColor + " !important;" +
        "  }" +
        "  [part~='selected'] [part~='cell'] {" +
        "    background-color: " + backgroundColor + " !important;" +
        "    color: " + textColor + " !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

// ✅ COULEURS PRÉDÉFINIES
public void setBlueSelection() {
    setSelectionColor("#2196F3", "white");
}

public void setGreenSelection() {
    setSelectionColor("#4CAF50", "white");
}

public void setRedSelection() {
    setSelectionColor("#F44336", "white");
}

public void setOrangeSelection() {
    setSelectionColor("#FF9800", "black");
}

public void setPurpleSelection() {
    setSelectionColor("#9C27B0", "white");
}

public void setGoldSelection() {
    setSelectionColor("#FFD700", "black");
}

public void setDarkSelection() {
    setSelectionColor("#424242", "white");
}

// ✅ COULEUR AVEC EFFET
public void setGradientSelection(String startColor, String endColor) {
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  [part~='selected'] {" +
        "    background: linear-gradient(90deg, " + startColor + ", " + endColor + ") !important;" +
        "    color: white !important;" +
        "  }" +
        "  [part~='selected'] [part~='cell'] {" +
        "    background: linear-gradient(90deg, " + startColor + ", " + endColor + ") !important;" +
        "    color: white !important;" +
        "  }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

//&&
// ✅ DANS PersonGrid - AJOUTER CES MÉTHODES
public void setSelectionBlue() {
    setBlueSelection();
}

public void setSelectionGreen() {
    setGreenSelection();
}

public void setSelectionRed() {
    setRedSelection();
}

public void setSelectionOrange() {
    setOrangeSelection();
}

public void setSelectionGold() {
    setGoldSelection();
}

//€€
// ✅ AJOUTER UN CONTRÔLE POUR LA COULEUR DE SÉLECTION
private void initializeComponents() {
    personGrid = new PersonGrid();
    
    Select<String> selectionColorSelector = new Select<>();
    selectionColorSelector.setLabel("Couleur de sélection");
    selectionColorSelector.setItems("Bleu", "Vert", "Rouge", "Orange", "Or", "Violet", "Sombre");
    selectionColorSelector.setValue("Bleu");
    
    selectionColorSelector.addValueChangeListener(event -> {
        switch (event.getValue()) {
            case "Bleu":
                personGrid.setSelectionBlue();
                break;
            case "Vert":
                personGrid.setSelectionGreen();
                break;
            case "Rouge":
                personGrid.setSelectionRed();
                break;
            case "Orange":
                personGrid.setSelectionOrange();
                break;
            case "Or":
                personGrid.setSelectionGold();
                break;
            case "Violet":
                personGrid.setSelectionColor("#9C27B0", "white");
                break;
            case "Sombre":
                personGrid.setSelectionColor("#37474F", "white");
                break;
        }
    });
    
    // Ajouter au layout
    HorizontalLayout controls = new HorizontalLayout(selectionColorSelector);
    add(controls);
}

// ✅ OU APPLIQUER DIRECTEMENT
public MainView() {
    initializeComponents();
    setupLayout();
    setupListeners();
    
    // ✅ COULEUR DE SÉLECTION PAR DÉFAUT
    personGrid.setSelectionBlue();
}
