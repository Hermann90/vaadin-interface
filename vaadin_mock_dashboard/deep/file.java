import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        // Header section
        H2 header = new H2("DWH");
        
        // First row of buttons
        HorizontalLayout buttonRow1 = new HorizontalLayout(
            new Button("BMIU"),
            new Button("CFO"),
            new Button("FNC")
        );
        buttonRow1.setSpacing(true);
        
        // Entry point section
        Div entryPointSection = new Div();
        entryPointSection.setText("Point d'entrée UO");
        
        // Sampling type section
        FormLayout samplingTypeLayout = new FormLayout();
        ComboBox<String> samplingType1 = new ComboBox<>("Type échantillonnage");
        samplingType1.setItems("xx", "yy");
        ComboBox<String> samplingType2 = new ComboBox<>("Type échantillonnage");
        samplingType2.setItems("xx", "yy");
        samplingTypeLayout.add(samplingType1, samplingType2);
        
        // ALL section
        Div allSection = new Div();
        allSection.setText("ALL");
        
        // Related tables section
        RadioButtonGroup<String> relatedTables = new RadioButtonGroup<>();
        relatedTables.setLabel("Tables liées (oui/non)");
        relatedTables.setItems("oui", "non");
        
        // Sampling mode section
        ComboBox<String> samplingMode = new ComboBox<>("mode d'échantillonnage");
        
        // Search section
        HorizontalLayout searchLayout = new HorizontalLayout(
            new TextField("Rechercher une table"),
            new Button("Rechercher")
        );
        searchLayout.setAlignItems(Alignment.BASELINE);
        
        // Table selection buttons
        HorizontalLayout tableButtons = new HorizontalLayout(
            new Button("Contrat"),
            new Button("Personne"),
            new Button("MYT_CAC"),
            new Button("OPE_VIR_EUR"),
            new Button("Nouvelle Table")
        );
        tableButtons.setSpacing(true);
        
        // Selected table section
        Div selectedTable = new Div();
        selectedTable.setText("OPE_VIR_EUR");
        selectedTable.getStyle().set("font-weight", "bold");
        
        // Attributes and links section
        HorizontalLayout attrLinks = new HorizontalLayout(
            new Button("Attributs"),
            new Button("Liens")
        );
        attrLinks.setSpacing(true);
        
        // Table info section
        FormLayout tableInfo = new FormLayout();
        tableInfo.addFormItem(new Div("Nombre de lignes : xxx"), "Number of rows");
        
        // Sampling mode options
        RadioButtonGroup<String> samplingOptions = new RadioButtonGroup<>();
        samplingOptions.setLabel("Mode d'échantillonnage :");
        samplingOptions.setItems("Full", "sans Donnée", "Échantillon");
        
        // Mother table by domain
        ComboBox<String> motherTableDomain = new ComboBox<>("Table mère par domaine");
        motherTableDomain.setItems("Domaine Contrat", "Domaine xx", "Domaine yy");
        
        // Add all components to main layout
        add(header, buttonRow1, entryPointSection, samplingTypeLayout, 
            allSection, relatedTables, samplingMode, searchLayout, 
            tableButtons, selectedTable, attrLinks, tableInfo, 
            samplingOptions, motherTableDomain);
        
        setSpacing(true);
        setPadding(true);
    }
}