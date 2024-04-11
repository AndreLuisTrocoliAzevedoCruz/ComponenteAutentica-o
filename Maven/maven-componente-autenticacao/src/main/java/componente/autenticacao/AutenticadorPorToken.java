package componente.autenticacao;

public class AutenticadorPorToken implements Autenticador {
   
    public boolean autenticar(Usuario usuario) {
    return usuario.getToken() != null && usuario.getToken().equals("token123");

}

    
}
