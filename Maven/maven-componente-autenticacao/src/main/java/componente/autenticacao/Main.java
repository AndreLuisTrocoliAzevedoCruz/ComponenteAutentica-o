package componente.autenticacao;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Inicialização dos sistemas de encapsulamento de autenticação por senha e por token
        SistemaAutenticacaoPorToken sistemaToken = new SistemaAutenticacaoPorToken(new AutenticadorPorSenha(), new AutenticadorPorToken());
        SistemaAutenticacaoPorSenha sistemaSenha= new SistemaAutenticacaoPorSenha(new AutenticadorPorSenha());

        // Inicialização do hardware token para autenticação via pen drive
        HardwareToken acess = new HardwareToken();

        // Variável para controlar o loop do menu
        boolean sair = false;
    
        while (!sair) {
            exibirMenu();
            String input = scanner.nextLine();

            // Verifica se a entrada é um número
            if (input.matches("\\d+")) {
                int opcao = Integer.parseInt(input);
                switch (opcao) {
                    case 1:
                        autenticarUsuarioSenha(scanner, sistemaSenha); // Autenticação por senha
                        break;
                    case 2:
                        autenticarViaHardware(acess, scanner); // Autenticação via pen drive
                        break;
                    case 3:
                        autenticarUsuarioToken(scanner, sistemaToken); // Autenticação por token
                        break;
                    case 4:
                        sair = true; // Encerra o programa
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }

        // Fecha o scanner ao sair do programa
        scanner.close();
    }
    
    // Método para exibir o menu principal
    private static void exibirMenu() {
        System.out.println("\n=== Menu ===");
        System.out.println("1. Autenticar usuário por senha");
        System.out.println("2. Autenticar via Pen drive de acesso");
        System.out.println("3. Autenticar Usuario via token");
        System.out.println("4. Sair");
        System.out.print("Escolha uma opção: ");
    }
    
    // Método para autenticar usuário por senha
    private static void autenticarUsuarioSenha(Scanner scanner, SistemaAutenticacaoPorSenha sistemaSenha) {
        System.out.println("\n=== Autenticação ===");
        System.out.print("Digite o nome de usuário: ");
        String nomeUsuario = scanner.nextLine();
        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();
        Usuario usuario = new Usuario(nomeUsuario, senha, null);
        boolean autenticado = sistemaSenha.autenticarUsuarioSenha(usuario);
        
        // Verifica se a autenticação foi bem-sucedida
        if (autenticado) {
            System.out.println("Usuário autenticado com sucesso!");
            AcessoC();
        } else {
            System.out.println("Falha na autenticação. Verifique suas credenciais.");
            autenticarUsuarioSenha(scanner, sistemaSenha);
        }
    }
    
    // Método para autenticar usuário por token
    private static void autenticarUsuarioToken(Scanner scanner, SistemaAutenticacaoPorToken sistemaToken) {
        System.out.println("\n=== Autenticação ===");
        System.out.print("Digite o nome de usuário: ");
        String nomeUsuario = scanner.nextLine();
        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();
        System.out.print("Digite o token: ");
        String token = scanner.nextLine();
        Usuario usuario = new Usuario(nomeUsuario, senha, token);
        boolean autenticado = sistemaToken.autenticarUsuario(usuario);
        
        // Verifica se a autenticação foi bem-sucedida
        if (autenticado) {
            System.out.println("Usuário autenticado com sucesso!");
            AcessoA();
        } else {
            System.out.println("Falha na autenticação. Verifique suas credenciais.");
            autenticarUsuarioToken(scanner, sistemaToken);
        }
    }

    // Método para autenticar via hardware token (pen drive)
    private static void autenticarViaHardware(HardwareToken acess, Scanner scanner) {
        System.out.println("\n=== Autenticação ===");
        System.out.print("Insira o Pen Drive de acesso e aperte enter. ");
        scanner.nextLine();
        String penDrivePath = "Z:\\"; // Caminho do pen drive
        if (HardwareToken.AunteticarHardwareToken(penDrivePath)) {
            System.out.println("Token encontrado. Acesso concedido.");
            AcessoC();
            // Coloque aqui a lógica para permitir o acesso ao sistema ou aplicativo
        } else {
            System.out.println("Token não encontrado. Acesso negado.");
            // Coloque aqui a lógica para negar o acesso ao sistema ou aplicativo
        }
    }

    private static void AcessoA(){
        System.out.println("Acesso nivel A");
    }

    private static void AcessoC(){
        System.out.println("Acesso nivel C");
    }
}
