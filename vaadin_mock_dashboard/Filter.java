// TOUTE CETTE MÉTHODE EST NOUVELLE :
private void selectFirstVisibleItem() {
    // Obtenir la liste filtrée des éléments
    List<DataModel> filteredItems = dataProvider.getItems().stream()
        .filter(dataProvider.getFilter() == null ? item -> true : dataProvider.getFilter())
        .collect(Collectors.toList());
    
    // Si la sélection actuelle n'est plus dans la liste filtrée
    // OU si la liste n'est pas vide et rien n'est sélectionné
    if (!filteredItems.isEmpty() && 
        (selectedItem == null || !filteredItems.contains(selectedItem))) {
        
        // Sélectionner le premier élément de la liste filtrée
        selectedItem = filteredItems.get(0);
        dataProvider.refreshAll();
    }
}

xxxx
  // J'ai AJOUTÉ dans resetFilters() :
private void resetFilters() {
    codeFilter.clear();
    typeFilter.clear();
    dataProvider.clearFilters();
    
    // Après réinitialisation, sélectionner à nouveau le premier élément
    if (!dataProvider.getItems().isEmpty()) {
        selectedItem = dataProvider.getItems().get(0); // ← NOUVEAU
        dataProvider.refreshAll(); // ← NOUVEAU
    }
}

//45

// Ces méthodes sont NOUVELLES :
public DataModel getSelectedItem() {
    return selectedItem;
}

public void setSelectedItem(DataModel item) {
    selectedItem = item;
    dataProvider.refreshAll();
}
