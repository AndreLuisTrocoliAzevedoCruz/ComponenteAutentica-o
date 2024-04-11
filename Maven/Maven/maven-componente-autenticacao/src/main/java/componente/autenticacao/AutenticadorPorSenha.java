package componente.autenticacao;

public class AutenticadorPorSenha implements Autenticador {
    @Override
    public boolean autenticar(Usuario usuario) {

        return usuario != null && usuario.getSenha() != null && usuario.getSenha().equals("senha123");
    }

   
}