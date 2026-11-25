package pediatriccare;

public record User(String username, String password, String role, String displayName, String staffId) {
    public User(String username, String password, String role) {
        this(username, password, role, username, role + "-001");
    }
}
