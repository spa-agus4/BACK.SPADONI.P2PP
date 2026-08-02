package BACKEND.SPADONI.P2PP.DTO;

// Lo que devuelve el login
public class TokenDTO {
    private String token;
    // getter y setter

    public TokenDTO() {}
    
    public TokenDTO(String token) {
        this.token = token;
    }
    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}