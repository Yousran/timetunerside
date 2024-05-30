package timetuner.models;

public class User extends Model {
    private String username;
    private String email;
    private String password;
    private String profil_path;

    public User(int id, String username, String email, String password, String profil_path) {
        super(id);
        this.username = username;
        this.email = email;
        this.password = password;
        this.profil_path = profil_path;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfil_path() {
        return profil_path;
    }

    public void setProfil_path(String profil_path) {
        this.profil_path = profil_path;
    }
    
    
}
