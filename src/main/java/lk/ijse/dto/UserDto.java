package lk.ijse.dto;

public class UserDto {
    private String displayName;
    private String email;
    private String password;

    public UserDto() {
    }

    public UserDto(String displayName, String email, String password) {
        this.displayName = displayName;
        this.email = email;
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    @Override
    public String toString() {
        return "UserDto{" +
                "displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
