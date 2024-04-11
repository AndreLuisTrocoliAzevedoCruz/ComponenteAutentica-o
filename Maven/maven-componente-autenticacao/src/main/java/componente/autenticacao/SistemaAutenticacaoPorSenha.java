package componente.autenticacao;
import java.util.HashMap;
import java.util.Map;
public class SistemaAutenticacaoPorSenha {
    private static final int MAX_TENTATIVAS = 3;
    private static final long TEMPO_BLOQUEIO = 1000 * 60 * 60 * 8;

    private Map<String, Integer> tentativasSenha = new HashMap<>();
    private Map<String, Long> bloqueioSenha = new HashMap<>();

    private Autenticador autenticadorSenha;

    public SistemaAutenticacaoPorSenha(Autenticador autenticadorSenha) {
        this.autenticadorSenha = autenticadorSenha;
    }

    public boolean autenticarUsuarioSenha(Usuario usuario) {
        long tempoAtual = System.currentTimeMillis();
    
        if (bloqueioSenha.containsKey(usuario.getNomeUsuario())) {
            long tempoBloqueioSenha = bloqueioSenha.get(usuario.getNomeUsuario());
            if (tempoAtual - tempoBloqueioSenha < TEMPO_BLOQUEIO) {
                System.out.println("Usuário bloqueado devido a tentativas excessivas de senha. Tempo restante: " + ((TEMPO_BLOQUEIO - (tempoAtual - tempoBloqueioSenha)) / 1000) + " segundos.");
                return false;
            } else {
                bloqueioSenha.remove(usuario.getNomeUsuario());
                tentativasSenha.remove(usuario.getNomeUsuario());
            }
        }
    
        boolean autenticacaoPorSenha = autenticadorSenha.autenticar(usuario);
    
        if (autenticacaoPorSenha) {
            tentativasSenha.remove(usuario.getNomeUsuario());
            bloqueioSenha.remove(usuario.getNomeUsuario());
            return true;
        } else {
            int tentativaAtualSenha = tentativasSenha.getOrDefault(usuario.getNomeUsuario(), 0) + 1;
            tentativasSenha.put(usuario.getNomeUsuario(), tentativaAtualSenha);
    
            if (tentativaAtualSenha >= MAX_TENTATIVAS) {
                long tempoBloqueio = (long) Math.max(tempoAtual, Math.max(bloqueioSenha.getOrDefault(usuario.getNomeUsuario(), 0L), (double) tempoAtual));
                bloqueioSenha.put(usuario.getNomeUsuario(), tempoBloqueio);
                System.out.println("Número máximo de tentativas alcançado. Usuário bloqueado temporariamente.");
            }
            return false;
        }
    }
}
