package com.example.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("mock-dashboard")
@CssImport("./styles/shared-styles.css")
public class MockDashboardView extends VerticalLayout {

    public MockDashboardView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        add(createTopBar(), createMainContent());
    }

    private Component createTopBar() {
        HorizontalLayout top = new HorizontalLayout();
        top.addClassName("top-bar");
        top.setWidthFull();
        top.setPadding(true);
        top.setAlignItems(Alignment.CENTER);

        HorizontalLayout leftCluster = new HorizontalLayout();
        leftCluster.addClassName("cluster");
        leftCluster.setSpacing(true);
        leftCluster.add(createPill("DWH"), createPill("BMU"), createPill("CFO"), createPill("FNC"), createPill("..."));
        leftCluster.add(new Span("Point d'entrée UO"));
        leftCluster.add(new Span("Type échantillonnage XX"));

        Div filler = new Div();
        filler.getStyle().set("flex","1");

        VerticalLayout metaAndCommit = new VerticalLayout();
        metaAndCommit.setPadding(false);
        metaAndCommit.setSpacing(false);
        metaAndCommit.addClassName("meta-commit");

        Paragraph meta = new Paragraph("Dernière mise à jour : DD/MM/YYYY HH:mm:ss");
        meta.addClassName("meta-text");

        Button commit = new Button("COMMIT", new Icon(VaadinIcon.CHECK));
        commit.addClassName("commit-button");

        metaAndCommit.add(meta, commit);

        top.add(leftCluster, filler, metaAndCommit);
        return top;
    }

    private Component createMainContent() {
        HorizontalLayout main = new HorizontalLayout();
        main.setSizeFull();
        main.addClassName("main-area");
        main.setPadding(true);
        main.setSpacing(true);

        VerticalLayout leftPanel = new VerticalLayout();
        leftPanel.addClassName("left-panel");
        leftPanel.setWidth("33%");
        leftPanel.setHeightFull();

        HorizontalLayout controls = new HorizontalLayout();
        Button allBtn = new Button("ALL");
        Checkbox linkedTables = new Checkbox("Tables liées (oui/non)");
        Button samplingMode = new Button("mode d'échantillonnage");
        controls.add(allBtn, linkedTables, samplingMode);

        TextField search = new TextField();
        search.setPlaceholder("Rechercher une table");
        search.setWidthFull();

        VerticalLayout listArea = new VerticalLayout();
        listArea.addClassName("list-area");
        listArea.setWidthFull();
        listArea.setSpacing(true);

        listArea.add(createTableRow("Contrat", "green"),
                     createTableRow("Personne", "green"),
                     createTableRow("MVT_CAC", "green"),
                     createTableRow("OPE_VIR_EUR", "red"),
                     createTableRow("Nouvelle Table", "light"));

        leftPanel.add(controls, search, listArea);

        VerticalLayout rightPanel = new VerticalLayout();
        rightPanel.addClassName("right-panel");
        rightPanel.setWidth("66%");
        rightPanel.setHeightFull();

        H2 title = new H2("OPE_VIR_EUR");
        title.addClassName("detail-title");

        HorizontalLayout smallTabs = new HorizontalLayout();
        smallTabs.addClassName("small-tabs");
        smallTabs.add(new Button("Attributs"), new Button("Liens"), new Button("..."));

        VerticalLayout attrs = new VerticalLayout();
        attrs.addClassName("attrs-area");

        HorizontalLayout row1 = new HorizontalLayout();
        row1.add(new Span("Nombre de lignes : xxx"));
        row1.setWidthFull();

        HorizontalLayout row2 = new HorizontalLayout();
        row2.setSpacing(true);
        row2.add(new Span("Mode d'échantillonnage :"),
                 createPill("Full"),
                 createPill("sans Donnée"),
                 createPill("Echantillon"));

        HorizontalLayout row3 = new HorizontalLayout();
        row3.setSpacing(true);
        row3.add(new Span("Table mère par domaine :"),
                 createPill("Domaine Contrat"),
                 createPill("Domaine xx"),
                 createPill("Domaine yy"));

        attrs.add(row1, row2, row3);

        rightPanel.add(title, smallTabs, attrs);

        main.add(leftPanel, rightPanel);
        main.setFlexGrow(1, rightPanel);
        main.setFlexGrow(0, leftPanel);
        return main;
    }

    private Div createPill(String text) {
        Div pill = new Div();
        pill.addClassName("pill");
        pill.setText(text);
        return pill;
    }

    private Div createTableRow(String label, String colorType) {
        Div row = new Div();
        row.addClassName("table-row");
        switch (colorType) {
            case "green":
                row.addClassName("table-green");
                break;
            case "red":
                row.addClassName("table-red");
                break;
            default:
                row.addClassName("table-light");
                break;
        }
        row.setText(label);
        return row;
    }
}
