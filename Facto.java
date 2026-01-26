public VerticalLayout getCartoPlansLayout2(
        List<CartoPlan> cartoPlans,
        List<CartoPlan> currentCartoPlans
) {
    VerticalLayout layout = new VerticalLayout();
    System.out.println("---- FILTRER LES DONNÉES ----");

    CheckboxGroup<CartoPlan> checkboxGroup = new CheckboxGroup<>();
    checkboxGroup.setLabel("Export data");
    checkboxGroup.setItems(cartoPlans);
    checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

    // 🔹 Pré-calcul des codes déjà présents
    Set<String> existingCodes = currentCartoPlans.stream()
            .map(CartoPlan::getCodeTableMere)
            .collect(Collectors.toSet());

    for (CartoPlan cartoPlan : cartoPlans) {
        boolean selected = existingCodes.contains(cartoPlan.getCodeTableMere());

        IconCheckBox iconCheckBox =
                new IconCheckBox(cartoPlan.getCodeTableMere(), selected);

        iconCheckBox.addClickListener(
                (ClickEvent<HorizontalLayout> event) ->
                        System.out.println("clicked on " + cartoPlan.getCodeTableMere())
        );

        layout.add(iconCheckBox);
    }

    layout.setPadding(false);
    layout.setMargin(true);

    return layout;
}
