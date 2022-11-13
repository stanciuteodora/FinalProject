package ro.siit.finalProject.service;

public enum SortMethod {
    name("name"),
    favorite("favorite");

    private String value;

    SortMethod(String value) {
        this.value = value;
    }

    public static SortMethod getValue(String sortMethod) {
        for (SortMethod sort : SortMethod.values()) {
            if (sort.getValue().equals(sortMethod)) {
                return sort;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
