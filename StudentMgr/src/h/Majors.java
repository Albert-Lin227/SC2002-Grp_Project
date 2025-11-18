package h;

public enum Majors {
    CSC(1, "Computer Science"),
    EEE(2, "Electrical & Electronic Engineering"),
    MAE(3, "Mechanical & Aerospace Engineering"),
    CEE(4, "Civil & Environmental Engineering"),
    BUS(5, "Business"),
    ACC(6, "Accounting"),
    MSE(7, "Materials Science & Engineering");

    private final int id;
    private final String fullName;

    Majors(int id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return this.name();
    }
}