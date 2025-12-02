@Route("table-details")
@PageTitle("Détails de la Table")
public class TableDetailsView extends VerticalLayout {
    
    public TableDetailsView() {
        setPadding(true);
        setSpacing(true);
        setSizeFull();
        setAlignItems(Alignment.STRETCH);
        
        // Header avec titre et badge
        add(createHeader());
        
        // Description
        add(createDescription());
        
        // Attributs de la table
        add(createAttributesSection());
        
        // Mode d'échantillonnage
        add(createSamplingModeSection());
        
        // Séparateur
        add(createSeparator());
        
        // Section Contrat
        add(createContractSection());
    }
    
    private Component createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidth("100%");
        header.setPadding(false);
        header.setSpacing(true);
        header.setAlignItems(Alignment.CENTER);
        
        // Titre principal avec #
        H1 title = new H1("# OPE_VIR_EUR");
        title.getStyle()
            .set("margin", "0")
            .set("font-size", "2.5rem")
            .set("font-weight", "900")
            .set("color", "var(--lumo-primary-color)");
        
        // Badge "Indicateur facultatif"
        Span badge = createBadge("Indicateur facultatif", "contrast");
        badge.getStyle().set("margin-left", "auto");
        
        header.add(title, badge);
        header.setFlexGrow(1, title);
        
        return header;
    }
    
    private Span createBadge(String text, String type) {
        Span badge = new Span(text);
        
        String bgColor = "var(--lumo-primary-color-10pct)";
        String textColor = "var(--lumo-primary-color)";
        String borderColor = "var(--lumo-primary-color-30pct)";
        
        if ("success".equals(type)) {
            bgColor = "var(--lumo-success-color-10pct)";
            textColor = "var(--lumo-success-color)";
            borderColor = "var(--lumo-success-color-30pct)";
        } else if ("contrast".equals(type)) {
            bgColor = "var(--lumo-contrast-10pct)";
            textColor = "var(--lumo-contrast)";
            borderColor = "var(--lumo-contrast-30pct)";
        } else if ("warning".equals(type)) {
            bgColor = "var(--lumo-warning-color-10pct)";
            textColor = "var(--lumo-warning-color)";
            borderColor = "var(--lumo-warning-color-30pct)";
        }
        
        badge.getStyle()
            .set("background-color", bgColor)
            .set("color", textColor)
            .set("padding", "6px 16px")
            .set("border-radius", "20px")
            .set("font-size", "var(--lumo-font-size-xs)")
            .set("font-weight", "600")
            .set("border", "2px solid " + borderColor)
            .set("white-space", "nowrap");
        
        return badge;
    }
    
    private Component createDescription() {
        VerticalLayout descriptionSection = new VerticalLayout();
        descriptionSection.setPadding(false);
        descriptionSection.setSpacing(false);
        descriptionSection.setWidth("100%");
        
        Paragraph description = new Paragraph(
            "Table de suivi des opérations de virement. (commentaire de la table)"
        );
        description.getStyle()
            .set("margin", "15px 0 0 0")
            .set("font-size", "1.2rem")
            .set("color", "var(--lumo-secondary-text-color)")
            .set("font-style", "italic")
            .set("line-height", "1.5");
        
        descriptionSection.add(description);
        return descriptionSection;
    }
    
    private Component createAttributesSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(false);
        section.setWidth("100%");
        
        // Titre "Attributs de la table"
        H2 sectionTitle = new H2("Attributs de la table");
        sectionTitle.getStyle()
            .set("margin", "30px 0 20px 0")
            .set("font-size", "1.8rem")
            .set("font-weight", "700")
            .set("border-bottom", "3px solid var(--lumo-primary-color)")
            .set("padding-bottom", "10px");
        
        // Grid pour les attributs
        Grid<TableAttribute> attributesGrid = createAttributesGrid();
        
        section.add(sectionTitle, attributesGrid);
        return section;
    }
    
    private Grid<TableAttribute> createAttributesGrid() {
        Grid<TableAttribute> grid = new Grid<>();
        grid.setWidth("100%");
        grid.setHeight("120px");
        
        // Configuration pour éliminer les espaces
        grid.getElement().getStyle()
            .set("border-collapse", "collapse")
            .set("border-spacing", "0");
        
        // Colonne "Nom" - 50%
        Grid.Column<TableAttribute> nameColumn = grid.addComponentColumn(attr -> 
            createAttributeCell(attr.getName(), true)
        ).setHeader(createGridHeader("Nom"))
         .setWidth("50%")
         .setFlexGrow(0)
         .setResizable(false);
        
        nameColumn.getElement().getStyle()
            .set("padding-right", "0")
            .set("margin-right", "0");
        
        // Colonne "Valeur" - 50%
        Grid.Column<TableAttribute> valueColumn = grid.addComponentColumn(attr -> 
            createAttributeCell(attr.getValue(), false)
        ).setHeader(createGridHeader("Valeur"))
         .setWidth("50%")
         .setFlexGrow(0)
         .setResizable(false);
        
        valueColumn.getElement().getStyle()
            .set("padding-left", "0")
            .set("margin-left", "0");
        
        // Données des attributs
        List<TableAttribute> attributes = Arrays.asList(
            new TableAttribute("Nombre de lignes", "xxx"),
            new TableAttribute("Nom de domaine", "")
        );
        
        grid.setItems(attributes);
        
        // Style de la grid
        grid.getStyle()
            .set("border", "2px solid var(--lumo-contrast-30pct)")
            .set("border-radius", "8px")
            .set("margin-top", "10px")
            .set("overflow", "hidden");
        
        return grid;
    }
    
    private Component createGridHeader(String text) {
        Div header = new Div();
        header.setText(text);
        header.getStyle()
            .set("padding", "15px 20px")
            .set("font-weight", "bold")
            .set("font-size", "var(--lumo-font-size-m)")
            .set("background-color", "var(--lumo-primary-color-10pct)")
            .set("border-bottom", "2px solid var(--lumo-contrast-20pct)")
            .set("text-align", "left");
        return header;
    }
    
    private Component createAttributeCell(String content, boolean isBold) {
        Div cell = new Div();
        cell.setText(content != null ? content : "");
        cell.getStyle()
            .set("padding", "15px 20px")
            .set("display", "flex")
            .set("align-items", "center")
            .set("height", "100%")
            .set("border-bottom", "1px solid var(--lumo-contrast-10pct)")
            .set("box-sizing", "border-box");
        
        if (isBold) {
            cell.getStyle().set("font-weight", "600");
        }
        
        return cell;
    }
    
    private Component createSamplingModeSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(true);
        section.setWidth("100%");
        section.getStyle().set("margin-top", "30px");
        
        // Label
        Div label = new Div();
        label.setText("Mode d'échantillonnage :");
        label.getStyle()
            .set("font-weight", "bold")
            .set("font-size", "1.3rem")
            .set("margin-bottom", "15px");
        
        // Container pour les options
        VerticalLayout optionsContainer = new VerticalLayout();
        optionsContainer.setPadding(false);
        optionsContainer.setSpacing(true);
        optionsContainer.setWidth("100%");
        
        // Option 1: Sans Données (sélectionnée par défaut)
        optionsContainer.add(createSamplingOption("Sans Données", true));
        
        // Option 2: Échantillonné
        optionsContainer.add(createSamplingOption("Échantillonné", false));
        
        // Option 3: FULL
        optionsContainer.add(createSamplingOption("FULL", false));
        
        section.add(label, optionsContainer);
        return section;
    }
    
    private HorizontalLayout createSamplingOption(String labelText, boolean selected) {
        HorizontalLayout option = new HorizontalLayout();
        option.setPadding(false);
        option.setSpacing(true);
        option.setAlignItems(Alignment.CENTER);
        option.getStyle()
            .set("cursor", "pointer")
            .set("padding", "10px 15px")
            .set("border-radius", "6px")
            .set("border", "1px solid var(--lumo-contrast-20pct)")
            .set("background-color", selected ? 
                "var(--lumo-primary-color-5pct)" : "transparent");
        
        // Cercle radio custom
        Div radioCircle = new Div();
        radioCircle.getStyle()
            .set("width", "20px")
            .set("height", "20px")
            .set("border-radius", "50%")
            .set("border", "2px solid var(--lumo-primary-color)")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center")
            .set("flex-shrink", "0");
        
        if (selected) {
            Div innerCircle = new Div();
            innerCircle.getStyle()
                .set("width", "10px")
                .set("height", "10px")
                .set("border-radius", "50%")
                .set("background-color", "var(--lumo-primary-color)");
            radioCircle.add(innerCircle);
        }
        
        // Texte de l'option
        Span label = new Span(labelText);
        label.getStyle()
            .set("font-size", "var(--lumo-font-size-m)")
            .set("font-weight", selected ? "600" : "400");
        
        option.add(radioCircle, label);
        option.setFlexGrow(1, label);
        
        // Gestion du clic
        option.addClickListener(e -> {
            // Ici, vous devriez mettre à jour l'état de toutes les options
            // Pour cet exemple, nous gardons simple
        });
        
        return option;
    }
    
    private Component createSeparator() {
        HorizontalLayout separator = new HorizontalLayout();
        separator.setWidth("100%");
        separator.setHeight("3px");
        separator.getStyle()
            .set("background-color", "var(--lumo-contrast-30pct)")
            .set("margin", "40px 0 30px 0")
            .set("border-radius", "2px");
        return separator;
    }
    
    private Component createContractSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(true);
        section.setWidth("100%");
        
        // Titre "Contrat"
        H2 contractTitle = new H2("Contrat");
        contractTitle.getStyle()
            .set("margin", "0 0 25px 0")
            .set("font-size", "1.8rem")
            .set("font-weight", "700")
            .set("border-bottom", "3px solid var(--lumo-primary-color)")
            .set("padding-bottom", "10px");
        
        // Arbre de navigation
        VerticalLayout navigationTree = createNavigationTree();
        
        section.add(contractTitle, navigationTree);
        return section;
    }
    
    private VerticalLayout createNavigationTree() {
        VerticalLayout tree = new VerticalLayout();
        tree.setPadding(false);
        tree.setSpacing(false);
        tree.setWidth("100%");
        
        // Niveau 1: Personne (dossier)
        HorizontalLayout personneItem = createNavigationItem(
            "Personne", 
            VaadinIcon.FOLDER_OPEN, 
            true, 
            false
        );
        
        // Niveau 2: MYT_CAC (avec indentation)
        HorizontalLayout mytCacItem = createNavigationItem(
            "MYT_CAC", 
            VaadinIcon.FILE, 
            false, 
            true
        );
        mytCacItem.getStyle().set("margin-left", "35px");
        
        // Niveau 2: OPE_VIR_EUR (actif, avec badge)
        HorizontalLayout opeVirEurItem = createNavigationItem(
            "OPE_VIR_EUR", 
            VaadinIcon.FILE_TEXT, 
            false, 
            true
        );
        opeVirEurItem.getStyle().set("margin-left", "35px");
        
        // Ajouter badge "Table" à OPE_VIR_EUR
        Span tableBadge = createBadge("Table", "success");
        tableBadge.getStyle()
            .set("margin-left", "auto")
            .set("font-size", "var(--lumo-font-size-xs)");
        opeVirEurItem.add(tableBadge);
        
        // Niveau 2: Nouvelle Table (bouton d'action)
        HorizontalLayout newTableItem = createNewTableItem();
        newTableItem.getStyle().set("margin-left", "35px");
        
        tree.add(personneItem, mytCacItem, opeVirEurItem, newTableItem);
        return tree;
    }
    
    private HorizontalLayout createNavigationItem(String label, VaadinIcon icon, 
                                                 boolean isFolder, boolean isFile) {
        HorizontalLayout item = new HorizontalLayout();
        item.setPadding(false);
        item.setSpacing(true);
        item.setAlignItems(Alignment.CENTER);
        item.setWidth("100%");
        
        // Chevron pour les dossiers (si pliable)
        if (isFolder) {
            Icon chevron = VaadinIcon.CHEVRON_RIGHT.create();
            chevron.setSize("var(--lumo-icon-size-s)");
            chevron.getStyle()
                .set("color", "var(--lumo-contrast-60pct)")
                .set("margin-right", "5px");
            item.add(chevron);
        } else {
            // Espacement pour aligner avec les dossiers
            Div spacer = new Div();
            spacer.getStyle()
                .set("width", "var(--lumo-icon-size-s)")
                .set("margin-right", "5px");
            item.add(spacer);
        }
        
        // Icône selon le type
        Icon itemIcon = icon.create();
        itemIcon.setSize("var(--lumo-icon-size-s)");
        
        if (isFolder) {
            itemIcon.getStyle().set("color", "var(--lumo-primary-color)");
        } else if (isFile) {
            itemIcon.getStyle().set("color", "var(--lumo-contrast-70pct)");
        }
        
        item.add(itemIcon);
        
        // Texte
        Span text = new Span(label);
        text.getStyle()
            .set("font-size", "var(--lumo-font-size-m)")
            .set("font-weight", isFolder ? "600" : "400")
            .set("color", isFolder ? "var(--lumo-primary-text-color)" : 
                   label.equals("OPE_VIR_EUR") ? "var(--lumo-primary-color)" : 
                   "var(--lumo-body-text-color)")
            .set("margin-left", "8px");
        
        item.add(text);
        item.setFlexGrow(1, text);
        
        // Style interactif pour les dossiers
        if (isFolder) {
            item.getStyle()
                .set("cursor", "pointer")
                .set("padding", "8px 12px")
                .set("border-radius", "4px");
            
            item.addMouseEnterListener(e -> {
                item.getStyle().set("background-color", "var(--lumo-contrast-5pct)");
            });
            
            item.addMouseLeaveListener(e -> {
                item.getStyle().remove("background-color");
            });
        } else {
            item.getStyle().set("padding", "8px 12px");
        }
        
        return item;
    }
    
    private HorizontalLayout createNewTableItem() {
        HorizontalLayout item = new HorizontalLayout();
        item.setPadding(false);
        item.setSpacing(true);
        item.setAlignItems(Alignment.CENTER);
        
        // Icône plus
        Icon plusIcon = VaadinIcon.PLUS.create();
        plusIcon.setSize("var(--lumo-icon-size-s)");
        plusIcon.getStyle()
            .set("color", "var(--lumo-primary-color)")
            .set("margin-right", "8px");
        
        // Texte
        Span text = new Span("Nouvelle Table");
        text.getStyle()
            .set("font-size", "var(--lumo-font-size-m)")
            .set("color", "var(--lumo-primary-color)")
            .set("font-weight", "500");
        
        item.add(plusIcon, text);
        
        // Style de bouton
        item.getStyle()
            .set("cursor", "pointer")
            .set("padding", "10px 20px")
            .set("border-radius", "6px")
            .set("border", "2px dashed var(--lumo-primary-color)")
            .set("background-color", "var(--lumo-primary-color-5pct)")
            .set("margin-top", "10px")
            .set("width", "fit-content");
        
        // Effet hover
        item.addMouseEnterListener(e -> {
            item.getStyle()
                .set("background-color", "var(--lumo-primary-color-10pct)")
                .set("border-style", "solid");
        });
        
        item.addMouseLeaveListener(e -> {
            item.getStyle()
                .set("background-color", "var(--lumo-primary-color-5pct)")
                .set("border-style", "dashed");
        });
        
        // Action au clic
        item.addClickListener(e -> {
            Notification.show("Création d'une nouvelle table", 
                3000, Notification.Position.MIDDLE);
        });
        
        return item;
    }
    
    // Classe interne pour les attributs de table
    public static class TableAttribute {
        private String name;
        private String value;
        
        public TableAttribute(String name, String value) {
            this.name = name;
            this.value = value;
        }
        
        public String getName() { return name; }
        public String getValue() { return value; }
    }
}

///€€
