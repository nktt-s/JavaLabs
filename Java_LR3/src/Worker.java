public class Worker {
    private String surname;
    private String position;
    private String gender;
    private int birthYear;

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getBirthYear() {
        return birthYear;
    }
    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public Worker(String surname, String position, String gender, int birthYear) {
        this.surname = surname;
        this.position = position;
        this.gender = gender;
        this.birthYear = birthYear;
    }
}
