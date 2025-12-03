import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.icon.VaadinIcon;

@Route("")
public class OpeVirEurView extends VerticalLayout {

    public OpeVirEurView() {
        // Configuration de la mise en page principale
        setSpacing(false);
        setPadding(false);
        setWidthFull();
        
        // En-tête principal
        H1 title = new H1("# OPE_VIR_EUR");
        title.getStyle()
            .set("margin", "0")
            .set("padding", "1rem")
            .set("background-color", "#f0f0f0")
            .set("width", "100%");
        
        // Description de la table
        Div description = new Div();
        description.setText("Table de suivi des opérations de virement. (commentaire de la table)");
        description.getStyle()
            .set("padding", "1rem")
            .set("font-style", "italic")
            .set("border-bottom", "1px solid #ddd");
        
        // Section Attributs de la table
        H2 attributesTitle = new H2("Attributs de la table");
        attributesTitle.getStyle()
            .set("margin", "1rem 0 0.5rem 0")
            .set("padding-left", "1rem");
        
        // Ligne du nombre de lignes
        HorizontalLayout rowCountLayout = new HorizontalLayout();
        rowCountLayout.setPadding(true);
        rowCountLayout.setWidthFull();
        
        Span rowCountLabel = new Span("Nombre de lignes : ");
        TextField rowCountField = new TextField();
        rowCountField.setPlaceholder("xxx");
        rowCountField.getStyle().set("width", "150px");
        
        Span optionalIndicator = new Span("(indicateur facultatif)");
        optionalIndicator.getStyle()
            .set("color", "#666")
            .set("font-size", "0.9em")
            .set("margin-left", "0.5rem");
        
        rowCountLayout.add(rowCountLabel, rowCountField, optionalIndicator);
        rowCountLayout.setFlexGrow(1, rowCountLabel);
        
        // Ligne Nom de domaine
        HorizontalLayout domainLayout = new HorizontalLayout();
        domainLayout.setPadding(true);
        domainLayout.setWidthFull();
        
        Span domainLabel = new Span("Nom de domaine");
        TextField domainField = new TextField();
        domainField.setWidth("300px");
        
        domainLayout.add(domainLabel, domainField);
        domainLayout.setFlexGrow(1, domainLabel);
        
        // Ligne Mode d'échantillonnage
        HorizontalLayout samplingLayout = new HorizontalLayout();
        samplingLayout.setPadding(true);
        samplingLayout.setWidthFull();
        
        Span samplingLabel = new Span("Mode d'échantillonnage :");
        
        // Options d'échantillonnage avec Checkbox
        VerticalLayout samplingOptions = new VerticalLayout();
        samplingOptions.setSpacing(false);
        samplingOptions.setPadding(false);
        
        Checkbox sansDonnees = new Checkbox("Sans Données");
        Checkbox echantillonne = new Checkbox("Échantillonné");
        Checkbox full = new Checkbox("FULL");
        
        // Sélection unique (radio button behavior)
        sansDonnees.addValueChangeListener(e -> {
            if (e.getValue()) {
                echantillonne.setValue(false);
                full.setValue(false);
            }
        });
        
        echantillonne.addValueChangeListener(e -> {
            if (e.getValue()) {
                sansDonnees.setValue(false);
                full.setValue(false);
            }
        });
        
        full.addValueChangeListener(e -> {
            if (e.getValue()) {
                sansDonnees.setValue(false);
                echantillonne.setValue(false);
            }
        });
        
        samplingOptions.add(sansDonnees, echantillonne, full);
        
        samplingLayout.add(samplingLabel, samplingOptions);
        samplingLayout.setFlexGrow(1, samplingLabel);
        
        // Section Contrat
        H2 contractTitle = new H2("Contrat");
        contractTitle.getStyle()
            .set("margin", "2rem 0 0.5rem 0")
            .set("padding-left", "1rem")
            .set("border-top", "2px solid #ccc")
            .set("padding-top", "1rem");
        
        // Layout pour les options de contrat
        HorizontalLayout contractOptionsLayout = new HorizontalLayout();
        contractOptionsLayout.setPadding(true);
        contractOptionsLayout.setSpacing(true);
        
        // Boutons pour les options de contrat
        Button personneButton = createContractButton("Personne");
        Button mvtCacButton = createContractButton("MVT_CAC");
        Button opeVirEurButton = createContractButton("OPE_VIR_EUR");
        Button nouvelleTableButton = createContractButton("Nouvelle Table");
        
        // Mettre OPE_VIR_EUR en surbrillance (sélectionné par défaut)
        opeVirEurButton.getElement().setAttribute("theme", "primary");
        
        contractOptionsLayout.add(personneButton, mvtCacButton, opeVirEurButton, nouvelleTableButton);
        
        // Ligne de séparation
        Div separator = new Div();
        separator.getStyle()
            .set("height", "1px")
            .set("background-color", "#ddd")
            .set("margin", "1rem 0")
            .set("width", "100%");
        
        // Ajout de tous les composants à la vue
        add(title, description, attributesTitle, rowCountLayout, domainLayout, 
            samplingLayout, separator, contractTitle, contractOptionsLayout);
    }
    
    private Button createContractButton(String text) {
        Button button = new Button(text);
        button.setWidth("150px");
        button.setHeight("50px");
        
        // Style pour simuler une sélection
        button.addClickListener(e -> {
            // Réinitialiser tous les boutons
            button.getParent().ifPresent(parent -> {
                if (parent instanceof HorizontalLayout) {
                    ((HorizontalLayout) parent).getChildren()
                        .forEach(child -> {
                            if (child instanceof Button) {
                                ((Button) child).getElement().removeAttribute("theme");
                            }
                        });
                }
            });
            
            // Mettre le bouton cliqué en surbrillance
            button.getElement().setAttribute("theme", "primary");
        });
        
        return button;
    }
}
