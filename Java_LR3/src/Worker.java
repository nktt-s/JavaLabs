public class Worker {
    private final String surname;
    private final String position;
    private final String gender;
    private final int birthYear;

    public String getAllData() {
        return surname + " " + position + " " + gender + " " + birthYear + "\n";
    }

    public Worker(String surname, String position, String gender, int birthYear) {
        this.surname = surname;
        this.position = position;
        this.gender = gender;
        this.birthYear = birthYear;
    }
}
