package componente.autenticacao;

import java.util.HashMap;
import java.util.Map;

// Classe responsável por autenticar usuários por senha e por token e gerenciar tentativas de autenticação
public class SistemaAutenticacaoPorToken {
    private static final int MAX_TENTATIVAS = 3; // Número máximo de tentativas de autenticação permitidas
    private static final long TEMPO_BLOQUEIO = 1000 * 60 * 60 * 8; // Tempo de bloqueio após atingir o máximo de tentativas (8 horas)

    // Mapas para rastrear o número de tentativas de autenticação por senha e por token para cada usuário
    private Map<String, Integer> tentativasSenha = new HashMap<>();
    private Map<String, Integer> tentativasToken = new HashMap<>();

    // Mapas para rastrear o tempo de bloqueio após atingir o máximo de tentativas de autenticação por senha e por token para cada usuário
    private Map<String, Long> bloqueioSenha = new HashMap<>();
    private Map<String, Long> bloqueioToken = new HashMap<>();

    // Autenticador para autenticação por senha
    private Autenticador autenticadorSenha;

    // Autenticador para autenticação por token
    private AutenticadorPorToken autenticadorToken;

    // Construtor da classe, recebe um autenticador por senha e um autenticador por token como parâmetros
    public SistemaAutenticacaoPorToken(Autenticador autenticadorSenha, AutenticadorPorToken autenticadorPorToken) {
        this.autenticadorSenha = autenticadorSenha;
        this.autenticadorToken = autenticadorPorToken;
    }

    // Método para autenticar um usuário por senha e por token
    public boolean autenticarUsuario(Usuario usuario) {
        long tempoAtual = System.currentTimeMillis();

        // Verifica se o usuário está bloqueado por tentativas de senha
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

        // Verifica se o usuário está bloqueado por tentativas de token
        if (bloqueioToken.containsKey(usuario.getNomeUsuario())) {
            long tempoBloqueioToken = bloqueioToken.get(usuario.getNomeUsuario());
            if (tempoAtual - tempoBloqueioToken < TEMPO_BLOQUEIO) {
                System.out.println("Usuário bloqueado devido a tentativas excessivas de token. Tempo restante: " + ((TEMPO_BLOQUEIO - (tempoAtual - tempoBloqueioToken)) / 1000) + " segundos.");
                return false;
            } else {
                bloqueioToken.remove(usuario.getNomeUsuario());
                tentativasToken.remove(usuario.getNomeUsuario());
            }
        }

        // Realiza a autenticação por senha
        boolean autenticacaoSenha = autenticadorSenha.autenticar(usuario);

        // Realiza a autenticação por token
        boolean autenticacaoToken = autenticadorToken.autenticar(usuario);

        // Se a autenticação por senha e por token for bem-sucedida, retorna true
        if (autenticacaoSenha && autenticacaoToken ) {
            tentativasSenha.remove(usuario.getNomeUsuario());
            tentativasToken.remove(usuario.getNomeUsuario());
            bloqueioSenha.remove(usuario.getNomeUsuario());
            bloqueioToken.remove(usuario.getNomeUsuario());
            return true;
        } else {
            // Se não for bem-sucedida, conta como uma tentativa e bloqueia se necessário
            int tentativaAtualSenha = tentativasSenha.getOrDefault(usuario.getNomeUsuario(), 0) + 1;
            tentativasSenha.put(usuario.getNomeUsuario(), tentativaAtualSenha);

            int tentativaAtualToken = tentativasToken.getOrDefault(usuario.getNomeUsuario(), 0) + 1;
            tentativasToken.put(usuario.getNomeUsuario(), tentativaAtualToken);

            if (tentativaAtualSenha >= MAX_TENTATIVAS || tentativaAtualToken >= MAX_TENTATIVAS) {
                long tempoBloqueio = Math.max(tempoAtual, Math.max(bloqueioSenha.getOrDefault(usuario.getNomeUsuario(), 0L), bloqueioToken.getOrDefault(usuario.getNomeUsuario(), 0L)));
                bloqueioSenha.put(usuario.getNomeUsuario(), tempoBloqueio);
                bloqueioToken.put(usuario.getNomeUsuario(), tempoBloqueio);
                System.out.println("Número máximo de tentativas alcançado. Usuário bloqueado temporariamente.");
            }
            return false;
        }
    }
}
