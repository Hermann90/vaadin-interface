@Route("table-details")
@PageTitle("Détails de la Table")
public class TableDetailsView extends VerticalLayout {
    
    public TableDetailsView() {
        setPadding(true);
        setSpacing(true);
        setSizeFull();
        
        // Header avec titre et indicateur
        add(createHeader());
        
        // Section description
        add(createDescriptionSection());
        
        // Section attributs de la table
        add(createAttributesSection());
        
        // Séparateur horizontal
        add(createSeparator());
        
        // Section contrat avec navigation
        add(createContractSection());
    }
    
    private Component createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidth("100%");
        header.setPadding(false);
        header.setSpacing(true);
        header.setAlignItems(Alignment.CENTER);
        
        // Titre principal
        H1 title = new H1("# OPE_VIR_EUR");
        title.getStyle()
            .set("margin", "0")
            .set("font-size", "var(--lumo-font-size-xxxl)")
            .set("font-weight", "bold");
        
        // Indicateur facultatif
        Badge indicator = new Badge("Indicateur facultatif");
        indicator.getElement().getThemeList().add("contrast");
        indicator.getStyle().set("margin-left", "auto");
        
        header.add(title, indicator);
        return header;
    }
    
    private Component createDescriptionSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(false);
        section.setWidth("100%");
        
        // Description de la table
        Paragraph description = new Paragraph(
            "Table de suivi des opérations de virement. (commentaire de la table)"
        );
        description.getStyle()
            .set("margin", "0")
            .set("font-size", "var(--lumo-font-size-l)")
            .set("color", "var(--lumo-secondary-text-color)");
        
        // Titre section
        H2 sectionTitle = new H2("**Attributs de la table**");
        sectionTitle.getStyle()
            .set("margin", "20px 0 10px 0")
            .set("font-size", "var(--lumo-font-size-xl)")
            .set("font-weight", "bold");
        
        section.add(description, sectionTitle);
        return section;
    }
    
    private Component createAttributesSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(true);
        section.setWidth("100%");
        
        // Grid pour les attributs
        Grid<Attribute> attributesGrid = createAttributesGrid();
        section.add(attributesGrid);
        
        // Section Mode d'échantillonnage
        section.add(createSamplingModeSection());
        
        return section;
    }
    
    private Grid<Attribute> createAttributesGrid() {
        Grid<Attribute> grid = new Grid<>(Attribute.class);
        grid.setWidth("100%");
        grid.setHeight("100px");
        
        // Configuration des colonnes
        grid.removeAllColumns();
        
        // Colonne "Nom"
        grid.addComponentColumn(attr -> createAttributeCell(attr.getName()))
            .setHeader("Nom")
            .setAutoWidth(true)
            .setFlexGrow(0);
        
        // Colonne "Valeur"
        grid.addComponentColumn(attr -> createAttributeCell(attr.getValue()))
            .setHeader("Valeur")
            .setAutoWidth(true)
            .setFlexGrow(0);
        
        // Données
        List<Attribute> attributes = Arrays.asList(
            new Attribute("Nombre de lignes", "xxx"),
            new Attribute("Nom de domaine", "")
        );
        
        grid.setItems(attributes);
        
        // Style de la grid
        grid.getStyle()
            .set("border", "1px solid var(--lumo-contrast-10pct)")
            .set("border-radius", "4px");
        
        return grid;
    }
    
    private Component createAttributeCell(String content) {
        Div cell = new Div();
        cell.setText(content);
        cell.getStyle()
            .set("padding", "8px 12px")
            .set("display", "flex")
            .set("align-items", "center");
        return cell;
    }
    
    private Component createSamplingModeSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(true);
        section.setWidth("100%");
        
        // Label
        Div label = new Div();
        label.setText("Mode d'échantillonnage :");
        label.getStyle()
            .set("font-weight", "600")
            .set("font-size", "var(--lumo-font-size-m)");
        
        // Boutons radio
        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setItems("Sans Données", "Échantillonné", "FULL");
        radioGroup.setValue("Sans Données");
        radioGroup.getStyle().set("margin-top", "5px");
        
        section.add(label, radioGroup);
        return section;
    }
    
    private Component createSeparator() {
        HorizontalLayout separator = new HorizontalLayout();
        separator.setWidth("100%");
        separator.setHeight("1px");
        separator.getStyle()
            .set("background-color", "var(--lumo-contrast-10pct)")
            .set("margin", "20px 0");
        return separator;
    }
    
    private Component createContractSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(true);
        section.setWidth("100%");
        
        // Titre section Contrat
        H2 contractTitle = new H2("Contrat");
        contractTitle.getStyle()
            .set("margin", "0 0 15px 0")
            .set("font-size", "var(--lumo-font-size-xl)")
            .set("font-weight", "bold");
        
        // Navigation avec accordéon
        VerticalLayout navigation = createNavigationTree();
        
        section.add(contractTitle, navigation);
        return section;
    }
    
    private VerticalLayout createNavigationTree() {
        VerticalLayout tree = new VerticalLayout();
        tree.setPadding(false);
        tree.setSpacing(false);
        tree.setWidth("100%");
        
        // Niveau 1: Personne
        Div personneLevel = createTreeItem("Personne", true);
        
        // Niveau 2: MYT_CAC (avec indentation)
        Div mytCacLevel = createTreeItem("MYT_CAC", false);
        mytCacLevel.getStyle().set("margin-left", "20px");
        
        // Niveau 2: OPE_VIR_EUR (avec badge et indentation)
        HorizontalLayout opeVirEurLevel = createTreeItemWithBadge("OPE_VIR_EUR", "badge");
        opeVirEurLevel.getStyle().set("margin-left", "20px");
        
        // Niveau 2: Nouvelle Table (bouton, avec indentation)
        HorizontalLayout newTableLevel = createNewTableItem();
        newTableLevel.getStyle().set("margin-left", "20px");
        
        tree.add(personneLevel, mytCacLevel, opeVirEurLevel, newTableLevel);
        return tree;
    }
    
    private Div createTreeItem(String label, boolean isExpandable) {
        Div item = new Div();
        
        HorizontalLayout layout = new HorizontalLayout();
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setAlignItems(Alignment.CENTER);
        
        if (isExpandable) {
            Icon chevron = VaadinIcon.CHEVRON_RIGHT.create();
            chevron.setSize("var(--lumo-icon-size-s)");
            layout.add(chevron);
        }
        
        Span text = new Span(label);
        text.getStyle().set("font-size", "var(--lumo-font-size-m)");
        
        layout.add(text);
        layout.setFlexGrow(1, text);
        
        item.add(layout);
        return item;
    }
    
    private HorizontalLayout createTreeItemWithBadge(String label, String badgeType) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setAlignItems(Alignment.CENTER);
        layout.setWidth("100%");
        
        // Point (•) au lieu de chevron
        Span dot = new Span("•");
        dot.getStyle()
            .set("color", "var(--lumo-contrast-60pct)")
            .set("font-size", "var(--lumo-font-size-l)");
        
        Span text = new Span(label);
        text.getStyle().set("font-size", "var(--lumo-font-size-m)");
        
        // Badge
        Badge badge = new Badge();
        badge.setText("Table");
        badge.getElement().getThemeList().add("success");
        badge.getStyle().set("margin-left", "auto");
        
        layout.add(dot, text, badge);
        layout.setFlexGrow(1, text);
        
        return layout;
    }
    
    private HorizontalLayout createNewTableItem() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setAlignItems(Alignment.CENTER);
        layout.setWidth("100%");
        
        Icon plusIcon = VaadinIcon.PLUS.create();
        plusIcon.setSize("var(--lumo-icon-size-s)");
        plusIcon.getStyle().set("color", "var(--lumo-primary-color)");
        
        Span text = new Span("Nouvelle Table");
        text.getStyle()
            .set("font-size", "var(--lumo-font-size-m)")
            .set("color", "var(--lumo-primary-color)");
        
        layout.add(plusIcon, text);
        
        // Style de bouton
        layout.getStyle()
            .set("cursor", "pointer")
            .set("padding", "4px 8px")
            .set("border-radius", "4px");
        
        layout.addClickListener(e -> {
            Notification.show("Ajouter une nouvelle table");
        });
        
        return layout;
    }
    
    // Classe interne pour les attributs
    public static class Attribute {
        private String name;
        private String value;
        
        public Attribute(String name, String value) {
            this.name = name;
            this.value = value;
        }
        
        public String getName() { return name; }
        public String getValue() { return value; }
    }
}

///???
@Route("table-details-alt")
@PageTitle("Détails de la Table")
public class TableDetailsViewAlt extends VerticalLayout {
    
    public TableDetailsViewAlt() {
        setPadding(true);
        setSpacing(true);
        setSizeFull();
        
        // Contenu principal avec marge
        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(true);
        content.setWidth("100%");
        
        // Header
        content.add(createEnhancedHeader());
        
        // Description
        content.add(createEnhancedDescription());
        
        // Attributs dans une carte
        content.add(createAttributesCard());
        
        // Mode d'échantillonnage dans une carte
        content.add(createSamplingCard());
        
        // Contrat section
        content.add(createContractCard());
        
        add(content);
    }
    
    private Component createEnhancedHeader() {
        VerticalLayout header = new VerticalLayout();
        header.setPadding(false);
        header.setSpacing(false);
        header.setWidth("100%");
        
        // Ligne 1: Titre et badge
        HorizontalLayout titleRow = new HorizontalLayout();
        titleRow.setWidth("100%");
        titleRow.setPadding(false);
        titleRow.setSpacing(true);
        titleRow.setAlignItems(Alignment.CENTER);
        
        H1 title = new H1("# OPE_VIR_EUR");
        title.getStyle()
            .set("margin", "0")
            .set("font-size", "2rem")
            .set("font-weight", "900");
        
        Badge tableBadge = new Badge("Table");
        tableBadge.getElement().getThemeList().add("contrast");
        tableBadge.getStyle().set("margin-left", "auto");
        
        titleRow.add(title, tableBadge);
        
        header.add(titleRow);
        return header;
    }
    
    private Component createEnhancedDescription() {
        Div descriptionCard = new Div();
        descriptionCard.getStyle()
            .set("padding", "16px")
            .set("background-color", "var(--lumo-contrast-5pct)")
            .set("border-radius", "8px")
            .set("margin", "10px 0");
        
        Paragraph description = new Paragraph(
            "Table de suivi des opérations de virement. (commentaire de la table)"
        );
        description.getStyle()
            .set("margin", "0")
            .set("font-size", "1.1rem")
            .set("font-style", "italic");
        
        descriptionCard.add(description);
        return descriptionCard;
    }
    
    private Component createAttributesCard() {
        VerticalLayout card = new VerticalLayout();
        card.getStyle()
            .set("padding", "20px")
            .set("border", "2px solid var(--lumo-contrast-20pct)")
            .set("border-radius", "8px")
            .set("margin", "10px 0");
        
        // Titre de section
        H2 sectionTitle = new H2("Attributs de la table");
        sectionTitle.getStyle()
            .set("margin", "0 0 15px 0")
            .set("border-bottom", "2px solid var(--lumo-primary-color)")
            .set("padding-bottom", "5px");
        
        // Grid des attributs
        Grid<Attribute> grid = new Grid<>();
        grid.setWidth("100%");
        
        // Colonne 1: Nom
        grid.addComponentColumn(attr -> {
            Div cell = new Div();
            cell.setText(attr.getName());
            cell.getStyle()
                .set("padding", "10px")
                .set("font-weight", "600")
                .set("border-right", "1px solid var(--lumo-contrast-10pct)");
            return cell;
        }).setHeader("Nom").setWidth("50%");
        
        // Colonne 2: Valeur
        grid.addComponentColumn(attr -> {
            Div cell = new Div();
            cell.setText(attr.getValue());
            cell.getStyle().set("padding", "10px");
            return cell;
        }).setHeader("Valeur").setWidth("50%");
        
        // Données
        grid.setItems(
            new Attribute("Nombre de lignes", "xxx"),
            new Attribute("Nom de domaine", "")
        );
        
        // Supprimer les bordures de grid par défaut
        grid.getElement().getStyle().set("border", "none");
        
        card.add(sectionTitle, grid);
        return card;
    }
    
    private Component createSamplingCard() {
        VerticalLayout card = new VerticalLayout();
        card.getStyle()
            .set("padding", "20px")
            .set("border", "2px solid var(--lumo-contrast-20pct)")
            .set("border-radius", "8px")
            .set("margin", "10px 0");
        
        // Label
        Div label = new Div();
        label.setText("Mode d'échantillonnage");
        label.getStyle()
            .set("font-weight", "bold")
            .set("font-size", "1.1rem")
            .set("margin-bottom", "10px");
        
        // Options
        VerticalLayout options = new VerticalLayout();
        options.setPadding(false);
        options.setSpacing(true);
        
        // Option 1
        HorizontalLayout option1 = createOption("Sans Données", true);
        // Option 2  
        HorizontalLayout option2 = createOption("Échantillonné", false);
        // Option 3
        HorizontalLayout option3 = createOption("FULL", false);
        
        options.add(option1, option2, option3);
        card.add(label, options);
        return card;
    }
    
    private HorizontalLayout createOption(String label, boolean selected) {
        HorizontalLayout option = new HorizontalLayout();
        option.setPadding(false);
        option.setSpacing(true);
        option.setAlignItems(Alignment.CENTER);
        
        // Radio button custom
        Div radio = new Div();
        radio.getStyle()
            .set("width", "16px")
            .set("height", "16px")
            .set("border-radius", "50%")
            .set("border", "2px solid var(--lumo-primary-color)")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center");
        
        if (selected) {
            Div innerDot = new Div();
            innerDot.getStyle()
                .set("width", "8px")
                .set("height", "8px")
                .set("border-radius", "50%")
                .set("background-color", "var(--lumo-primary-color)");
            radio.add(innerDot);
        }
        
        Span text = new Span(label);
        text.getStyle().set("font-size", "var(--lumo-font-size-m)");
        
        option.add(radio, text);
        
        // Rendre cliquable
        option.getStyle().set("cursor", "pointer");
        option.addClickListener(e -> {
            // Logique de sélection ici
        });
        
        return option;
    }
    
    private Component createContractCard() {
        VerticalLayout card = new VerticalLayout();
        card.getStyle()
            .set("padding", "20px")
            .set("border", "2px solid var(--lumo-contrast-20pct)")
            .set("border-radius", "8px")
            .set("margin", "10px 0");
        
        // Titre avec icône
        HorizontalLayout titleRow = new HorizontalLayout();
        titleRow.setPadding(false);
        titleRow.setSpacing(true);
        titleRow.setAlignItems(Alignment.CENTER);
        
        Icon contractIcon = VaadinIcon.FILE_TEXT.create();
        contractIcon.setSize("var(--lumo-icon-size-m)");
        contractIcon.getStyle().set("color", "var(--lumo-primary-color)");
        
        H2 title = new H2("Contrat");
        title.getStyle().set("margin", "0");
        
        titleRow.add(contractIcon, title);
        
        // Arbre de navigation
        VerticalLayout tree = createEnhancedTree();
        
        card.add(titleRow, tree);
        return card;
    }
    
    private VerticalLayout createEnhancedTree() {
        VerticalLayout tree = new VerticalLayout();
        tree.setPadding(false);
        tree.setSpacing(false);
        
        // Personne (expandable)
        Div personne = createTreeItem("Personne", VaadinIcon.FOLDER, true, false);
        
        // MYT_CAC
        Div mytCac = createTreeItem("MYT_CAC", VaadinIcon.FILE, false, true);
        mytCac.getStyle().set("margin-left", "30px");
        
        // OPE_VIR_EUR (current)
        HorizontalLayout opeVirEur = createTreeItemWithStatus(
            "OPE_VIR_EUR", 
            VaadinIcon.FILE, 
            "current"
        );
        opeVirEur.getStyle().set("margin-left", "30px");
        
        // Nouvelle Table (action)
        HorizontalLayout newTable = createTreeItemAction(
            "Nouvelle Table", 
            VaadinIcon.PLUS
        );
        newTable.getStyle().set("margin-left", "30px");
        
        tree.add(personne, mytCac, opeVirEur, newTable);
        return tree;
    }
    
    private Div createTreeItem(String label, VaadinIcon icon, boolean expandable, boolean isFile) {
        Div item = new Div();
        
        HorizontalLayout layout = new HorizontalLayout();
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setAlignItems(Alignment.CENTER);
        
        // Icône
        Icon itemIcon = icon.create();
        itemIcon.setSize("var(--lumo-icon-size-s)");
        itemIcon.getStyle().set("color", isFile ? 
            "var(--lumo-contrast-60pct)" : "var(--lumo-primary-color)");
        
        // Chevron pour les dossiers expandable
        if (expandable) {
            Icon chevron = VaadinIcon.CHEVRON_RIGHT.create();
            chevron.setSize("var(--lumo-icon-size-s)");
            layout.add(chevron);
        }
        
        layout.add(itemIcon);
        
        // Texte
        Span text = new Span(label);
        text.getStyle().set("font-size", "var(--lumo-font-size-m)");
        layout.add(text);
        
        layout.setFlexGrow(1, text);
        item.add(layout);
        
        // Style interactif
        if (expandable) {
            item.getStyle().set("cursor", "pointer");
        }
        
        return item;
    }
    
    private HorizontalLayout createTreeItemWithStatus(String label, VaadinIcon icon, String status) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setAlignItems(Alignment.CENTER);
        layout.setWidth("100%");
        
        // Icône
        Icon itemIcon = icon.create();
        itemIcon.setSize("var(--lumo-icon-size-s)");
        itemIcon.getStyle().set("color", "var(--lumo-primary-color)");
        
        // Texte
        Span text = new Span(label);
        text.getStyle()
            .set("font-size", "var(--lumo-font-size-m)")
            .set("font-weight", "600");
        
        // Badge d'état
        Badge statusBadge = new Badge("Actif");
        statusBadge.getElement().getThemeList().add("success");
        statusBadge.getStyle().set("margin-left", "auto");
        
        layout.add(itemIcon, text, statusBadge);
        layout.setFlexGrow(1, text);
        
        return layout;
    }
    
    private HorizontalLayout createTreeItemAction(String label, VaadinIcon icon) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setAlignItems(Alignment.CENTER);
        
        // Icône
        Icon itemIcon = icon.create();
        itemIcon.setSize("var(--lumo-icon-size-s)");
        itemIcon.getStyle().set("color", "var(--lumo-primary-color)");
        
        // Texte
        Span text = new Span(label);
        text.getStyle()
            .set("font-size", "var(--lumo-font-size-m)")
            .set("color", "var(--lumo-primary-color)");
        
        layout.add(itemIcon, text);
        
        // Style de bouton
        layout.getStyle()
            .set("cursor", "pointer")
            .set("padding", "8px 12px")
            .set("border", "1px dashed var(--lumo-primary-color)")
            .set("border-radius", "4px")
            .set("background-color", "var(--lumo-primary-color-10pct)");
        
        layout.addClickListener(e -> {
            Notification.show("Ajouter une nouvelle table");
        });
        
        return layout;
    }
    
    public static class Attribute {
        private String name;
        private String value;
        
        public Attribute(String name, String value) {
            this.name = name;
            this.value = value;
        }
        
        public String getName() { return name; }
        public String getValue() { return value; }
    }
}
///???
