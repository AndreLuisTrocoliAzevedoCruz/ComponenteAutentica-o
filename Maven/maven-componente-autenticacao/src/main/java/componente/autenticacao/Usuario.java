package componente.autenticacao;

public class Usuario {
    private String nomeUsuario;
    private String senha;
    private String token;

    public Usuario(String nomeUsuario, String senha, String token) {
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.token = token;

    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public String getToken() {
        return token;
    }

   

}


