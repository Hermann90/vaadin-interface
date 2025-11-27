import java.util.Objects;

public class Person {
    private int id;
    private String name;
    private String email;
    private String status; // ACTIVE, INACTIVE, PENDING, VIP, URGENT
    private String department; // Ventes, Marketing, IT, Direction, RH, Support
    private int age;
    private double salary;
    private String phone;
    private String address;
    private String hireDate;
    private boolean remote;
    private int performanceScore; // 1-10
    private String notes;

    // Constructeurs
    public Person() {}

    public Person(int id, String name, String email, String status, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
        this.department = department;
    }

    public Person(int id, String name, String email, String status, String department, 
                  int age, double salary, String phone, String address, String hireDate, 
                  boolean remote, int performanceScore, String notes) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
        this.department = department;
        this.age = age;
        this.salary = salary;
        this.phone = phone;
        this.address = address;
        this.hireDate = hireDate;
        this.remote = remote;
        this.performanceScore = performanceScore;
        this.notes = notes;
    }

    // Getters et setters
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    public String getDepartment() { 
        return department; 
    }
    
    public void setDepartment(String department) { 
        this.department = department; 
    }
    
    public int getAge() { 
        return age; 
    }
    
    public void setAge(int age) { 
        this.age = age; 
    }
    
    public double getSalary() { 
        return salary; 
    }
    
    public void setSalary(double salary) { 
        this.salary = salary; 
    }
    
    public String getPhone() { 
        return phone; 
    }
    
    public void setPhone(String phone) { 
        this.phone = phone; 
    }
    
    public String getAddress() { 
        return address; 
    }
    
    public void setAddress(String address) { 
        this.address = address; 
    }
    
    public String getHireDate() { 
        return hireDate; 
    }
    
    public void setHireDate(String hireDate) { 
        this.hireDate = hireDate; 
    }
    
    public boolean isRemote() { 
        return remote; 
    }
    
    public void setRemote(boolean remote) { 
        this.remote = remote; 
    }
    
    public int getPerformanceScore() { 
        return performanceScore; 
    }
    
    public void setPerformanceScore(int performanceScore) { 
        this.performanceScore = performanceScore; 
    }
    
    public String getNotes() { 
        return notes; 
    }
    
    public void setNotes(String notes) { 
        this.notes = notes; 
    }

    // Méthodes utilitaires
    public String getFormattedSalary() {
        return String.format("%,.2f €", salary);
    }

    public String getRemoteStatus() {
        return remote ? "Télétravail" : "Présentiel";
    }

    public String getPerformanceLevel() {
        if (performanceScore >= 9) return "Excellent";
        if (performanceScore >= 7) return "Bon";
        if (performanceScore >= 5) return "Moyen";
        return "À améliorer";
    }

    // Méthodes equals et hashCode basées sur l'ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Méthode toString
    @Override
    public String toString() {
        return String.format("Person{id=%d, name='%s', email='%s', status='%s', department='%s'}", 
                id, name, email, status, department);
    }

    // Méthode pour obtenir un affichage simplifié
    public String getDisplayName() {
        return name + " (" + department + ")";
    }

    // Méthode pour vérifier le statut
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    public boolean isVip() {
        return "VIP".equals(status);
    }

    public boolean isUrgent() {
        return "URGENT".equals(status);
    }
}

//???
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PersonDataGenerator {
    private static final Random random = new Random();
    
    private static final String[] NAMES = {
        "Jean Dupont", "Marie Martin", "Pierre Durand", "Sophie Lambert", 
        "Thomas Moreau", "Laura Petit", "Marc Dubois", "Nathalie Leroy",
        "David Bernard", "Christine Richard", "Michel Simon", "Isabelle Laurent",
        "Patrick Morel", "Monique Garcia", "Daniel Fournier", "Catherine Lefebvre"
    };
    
    private static final String[] DEPARTMENTS = {
        "Ventes", "Marketing", "IT", "Direction", "RH", "Support", "Finance", "Production"
    };
    
    private static final String[] STATUSES = {
        "ACTIVE", "INACTIVE", "PENDING", "VIP", "URGENT"
    };
    
    private static final String[] ADDRESSES = {
        "123 Rue de Paris, 75001 Paris",
        "456 Avenue Victor Hugo, 69002 Lyon",
        "789 Boulevard Gambetta, 13001 Marseille",
        "321 Rue de la République, 31000 Toulouse",
        "654 Cours de la Libération, 33000 Bordeaux"
    };

    public static List<Person> generateSampleData(int count) {
        Person[] persons = new Person[count];
        
        for (int i = 0; i < count; i++) {
            persons[i] = createPerson(i + 1);
        }
        
        return Arrays.asList(persons);
    }
    
    private static Person createPerson(int id) {
        String name = NAMES[random.nextInt(NAMES.length)];
        String department = DEPARTMENTS[random.nextInt(DEPARTMENTS.length)];
        String status = STATUSES[random.nextInt(STATUSES.length)];
        
        // Générer des données cohérentes
        String email = generateEmail(name);
        int age = 25 + random.nextInt(40); // 25-65 ans
        double salary = 30000 + random.nextInt(70000); // 30k-100k
        String phone = generatePhone();
        String address = ADDRESSES[random.nextInt(ADDRESSES.length)];
        String hireDate = generateHireDate();
        boolean remote = random.nextBoolean();
        int performanceScore = 1 + random.nextInt(10); // 1-10
        String notes = generateNotes(name, department);
        
        return new Person(
            id, name, email, status, department,
            age, salary, phone, address, hireDate,
            remote, performanceScore, notes
        );
    }
    
    private static String generateEmail(String name) {
        String cleanedName = name.toLowerCase()
            .replace(" ", ".")
            .replace("é", "e")
            .replace("è", "e")
            .replace("ê", "e")
            .replace("à", "a")
            .replace("â", "a");
        return cleanedName + "@company.com";
    }
    
    private static String generatePhone() {
        return String.format("+33 %02d %02d %02d %02d %02d",
            random.nextInt(10), random.nextInt(100),
            random.nextInt(100), random.nextInt(100), random.nextInt(100));
    }
    
    private static String generateHireDate() {
        int year = 2015 + random.nextInt(9); // 2015-2024
        int month = 1 + random.nextInt(12);
        int day = 1 + random.nextInt(28);
        return String.format("%02d/%02d/%04d", day, month, year);
    }
    
    private static String generateNotes(String name, String department) {
        String[] notesTemplates = {
            "Excellent collaborateur",
            "À former sur les nouveaux processus",
            "Très motivé et impliqué",
            "Expert technique reconnu",
            "En progression constante",
            "Leader naturel de l'équipe",
            "Compétences transversales appréciées"
        };
        return notesTemplates[random.nextInt(notesTemplates.length)];
    }
}
//@@@

public class PersonGrid extends SelectableGrid<Person> {

    public PersonGrid() {
        super(Person.class);
        configureGrid();
        setupRowColors();
        loadSampleData();
    }

    private void configureGrid() {
        Grid<Person> grid = getGrid();
        
        grid.removeAllColumns();
        grid.addColumn(Person::getId).setHeader("ID").setWidth("80px");
        grid.addColumn(Person::getName).setHeader("Nom").setAutoWidth(true);
        grid.addColumn(Person::getEmail).setHeader("Email").setAutoWidth(true);
        grid.addColumn(Person::getStatus).setHeader("Statut").setWidth("120px");
        grid.addColumn(Person::getDepartment).setHeader("Département").setWidth("150px");
        grid.addColumn(Person::getAge).setHeader("Âge").setWidth("100px");
        grid.addColumn(Person::getFormattedSalary).setHeader("Salaire").setWidth("120px");
        grid.addColumn(Person::getRemoteStatus).setHeader("Mode travail").setWidth("130px");
        grid.addColumn(Person::getPerformanceLevel).setHeader("Performance").setWidth("140px");
        
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    private void loadSampleData() {
        // Utiliser le générateur de données
        List<Person> sampleData = PersonDataGenerator.generateSampleData(15);
        setItems(sampleData);
    }

    // ... (le reste des méthodes pour les couleurs)
}
