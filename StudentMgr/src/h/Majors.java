package h;

/**
 * Represents the various academic majors within the system.
 */
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

    /**
     * Constructs a Majors enum constant.
     * @param id The unique integer ID for the major.
     * @param fullName The full display name of the major.
     */
    Majors(int id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    /**
     * Gets the unique ID of the major.
     * @return The major's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the full display name of the major.
     * @return The major's full name.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns the short code and full name of the major.
     * @return A string in the format "CODE - Full Name".
     */
    @Override
    public String toString() {
        return this.name();
    }
}