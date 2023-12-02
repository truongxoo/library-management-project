package example.java.designpattern;

final class Person {

    // All are set to final to make Person Immutable
    private final int id;
    private final String name;
    private final String surname;
    private final boolean isOccupied;

    // Constructor is private, so that only static
    // PersonBuilder can initiate the Person class instance
    private Person(PersonBuilder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.surname = builder.getSurname();
        this.isOccupied = builder.isOccupied();
    }

    public static class PersonBuilder {

        // Member variables of PersonBuilder are temporary storage
        // to create Immutable Person class instance
        private int id;
        private String name;
        private String surname;
        private boolean isOccupied;

        PersonBuilder() {
        }

        // The only method to initiate Person class
        Person build() {
            return new Person(this);
        }

        // Multiple Constructors for each member variable
        public PersonBuilder id(int id) {
            this.id = id;
            return this;
        }

        public PersonBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PersonBuilder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public PersonBuilder setOccupied(boolean isOccupied) {
            this.isOccupied = isOccupied;
            return this;
        }

        // getters, these will be used in private constructor
        // of Person class
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        public boolean isOccupied() {
            return isOccupied;
        }
    }

    // getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    @Override
    public String toString() {
        return String.format("Id:\t\t%d\nName:\t\t%s\nSurname:\t%s\nIsOccupied:\t%s\n", id, name, surname, isOccupied);
    }

}

public class BuilderPatternDemo2 {
    public static void main(String[] args) {
        Person.PersonBuilder builder = new Person.PersonBuilder();
        builder.name("AAA").surname("BBBB").id(666).setOccupied(true);
        Person person = builder.build();
        System.out.println(person);
    }

}
