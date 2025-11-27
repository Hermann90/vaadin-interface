// ✅ DANS SelectableGrid - AJOUTER CETTE MÉTHODE
public void setUniformRowColorProvider(Function<T, String> colorProvider) {
    this.rowColorProvider = colorProvider;
    
    // ✅ INSTRUCTION CLÉ : Utiliser setPartNameGenerator
    grid.setPartNameGenerator(item -> {
        String color = colorProvider.apply(item);
        if (color != null) {
            return "colored-row-" + color.replace("#", "");
        }
        return null;
    });
    
    applyUniformRowStyles();
}

// ✅ INSTRUCTION SUPPORT : Appliquer les styles CSS
private void applyUniformRowStyles() {
    getElement().executeJs(
        "const style = document.createElement('style');" +
        "style.textContent = `" +
        "  [part~='colored-row-lightgreen'] { background-color: lightgreen !important; }" +
        "  [part~='colored-row-lightgray'] { background-color: lightgray !important; }" +
        "  [part~='colored-row-lightyellow'] { background-color: lightyellow !important; }" +
        "  [part~='colored-row-lightblue'] { background-color: lightblue !important; }" +
        "  [part~='colored-row-lightcoral'] { background-color: lightcoral !important; }" +
        "  [part~='colored-row-plum'] { background-color: plum !important; }" +
        "  [part~='colored-row-gold'] { background-color: gold !important; }" +
        "  [part~='colored-row-pink'] { background-color: pink !important; }" +
        "  [part~='colored-row-orange'] { background-color: orange !important; }" +
        "  [part~='colored-row-limegreen'] { background-color: limegreen !important; }" +
        "  [part~='colored-row-yellow'] { background-color: yellow !important; }" +
        "  [part~='colored-row-tomato'] { background-color: tomato !important; }" +
        "  [part~='colored-row-whitesmoke'] { background-color: whitesmoke !important; }" +
        "  [part~='colored-row-white'] { background-color: white !important; }" +
        "`;" +
        "document.head.appendChild(style);"
    );
}

///!!

private void setupUniformRowColors() {
    // ✅ REMPLACER l'ancien setRowColorProvider par :
    setUniformRowColorProvider(person -> {
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
