package com.example.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        // ===== TOP MENU BAR =====
        HorizontalLayout topMenu = new HorizontalLayout();
        topMenu.setWidthFull();
        topMenu.setAlignItems(FlexComponent.Alignment.CENTER);
        topMenu.setSpacing(true);

        Button dwh = new Button("DWH");
        Button bmu = new Button("BMU");
        Button cfo = new Button("CFO");
        Button fnc = new Button("FNC");
        Button more = new Button("...");

        Button pointEntree = new Button("Point d'entrée UO");
        Button typeXX = new Button("Type echantillonnage xx");
        Button typeYY = new Button("Type echantillonnage YY");

        Div updateInfo = new Div();
        updateInfo.getElement().setProperty("innerHTML",
                "<div>Dernière mise à jour de la carto : DD/MM/YYYY HH24:MI:SS</div>" +
                "<div>Dernière mise à jour MEGA : DD/MM/YYYY HH24:MI:SS</div>" +
                "<div style='color:orange;'>Dernier commit GIT : DD/MM/YYYY HH24:MI:SS</div>");

        Button commitBtn = new Button("COMMIT");
        commitBtn.getStyle().set("background-color", "red").set("color", "white");

        topMenu.add(dwh, bmu, cfo, fnc, more, pointEntree, typeXX, typeYY, updateInfo, commitBtn);
        topMenu.expand(updateInfo);

        // ===== MAIN CONTENT LAYOUT =====
        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.setSizeFull();
        mainContent.setSpacing(true);

        // LEFT PANEL
        VerticalLayout leftPanel = new VerticalLayout();
        leftPanel.setWidth("300px");

        HorizontalLayout filters = new HorizontalLayout(
                new Button("ALL"),
                new Button("Tables liées (oui/non)"),
                new Button("mode d'échantillonnage")
        );

        TextField searchField = new TextField();
        searchField.setPlaceholder("Rechercher une table");
        searchField.setWidthFull();

        Button tableContrat = createTableButton("Contrat", "#d4f7d4"); // green
        Button tablePersonne = createTableButton("Personne", "#d4f7d4");
        Button tableMvt = createTableButton("MVT_CAC", "#d4f7d4");
        Button tableOpe = createTableButton("OPE_VIR_EUR", "#f7d4d4"); // red
        Button tableNouvelle = createTableButton("Nouvelle Table", "#f7d4d4");

        leftPanel.add(filters, searchField,
                tableContrat, tablePersonne, tableMvt, tableOpe, tableNouvelle);

        // RIGHT PANEL
        VerticalLayout rightPanel = new VerticalLayout();
        rightPanel.setSizeFull();

        H3 tableTitle = new H3("OPE_VIR_EUR");

        HorizontalLayout attrButtons = new HorizontalLayout(
                new Button("Attributs"),
                new Button("Liens"),
                new Button("...")
        );

        Label attrInfo = new Label("Attributs de la table\n" +
                "Nombre de lignes : xxx\n" +
                "Mode d'échantillonnage : Full\n" +
                "Table mère par domaine :");

        HorizontalLayout attrModes = new HorizontalLayout(
                new Button("Full"),
                new Button("sans Donnée"),
                new Button("Echantillon")
        );

        HorizontalLayout domainButtons = new HorizontalLayout(
                new Button("Domaine Contrat"),
                new Button("Domaine xx"),
                new Button("Domaine yy")
        );

        rightPanel.add(tableTitle, attrButtons, attrInfo, attrModes, domainButtons);

        // Assemble main content
        mainContent.add(leftPanel, rightPanel);
        mainContent.expand(rightPanel);

        // Add to root layout
        add(topMenu, mainContent);
    }

    private Button createTableButton(String text, String bgColor) {
        Button btn = new Button(text);
        btn.getStyle()
                .set("background-color", bgColor)
                .set("border", "1px dashed #999")
                .set("width", "100%");
        return btn;
    }
}
