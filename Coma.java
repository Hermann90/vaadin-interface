public static void sortByCartoPlanString(
        List<Relationship> relationships,
        String searchedValue
) {
    relationships.sort(
        Comparator.comparing(
            r -> containsStringInPlans(r, searchedValue)
        ).reversed() // true en premier
    );
}

private static boolean containsStringInPlans(
        Relationship relationship,
        String searchedValue
) {
    if (relationship.getPlans() == null) {
        return false;
    }

    return relationship.getPlans().stream()
            .anyMatch(plan -> searchedValue.equals(plan.getCode()));
}
