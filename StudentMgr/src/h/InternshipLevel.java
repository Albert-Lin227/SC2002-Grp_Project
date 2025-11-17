package h;

public enum InternshipLevel {
    BASIC("Basic", 1),
    INTERMEDIATE("Intermediate", 2),
    ADVANCED("Advanced", 3);

    private final String displayName;
    private final int level;

    InternshipLevel(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }

    public static InternshipLevel getByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return displayName;
    }
}