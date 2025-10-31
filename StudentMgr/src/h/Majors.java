import java.util.HashSet;
import java.util.Set;

package h;

public enum Majors {
    CSC(1),
    EEE(2),
    MAE(3),
    CEE(4),
    BUS(5),
    ACC(6),
    MSE(7);

    private final int id;

    Majors(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Majors getMajorById(int id) {
        for (Majors major : Majors.values()) {
            if (major.getId() == id) {
                return major;
            }
        }
        return null;
    }

    public static boolean isValid(String major) {
        try {
            valueOf(major.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}