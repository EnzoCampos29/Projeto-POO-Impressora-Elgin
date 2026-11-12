import java.util.Scanner;
public class Usuario {
        String nome;
        String nomeLogin;
        String senha;
        String senhaLogin;
        int tentativas = 3;

        void cadastro(Scanner scanner) {
            System.out.println("=== CADASTRO ===");
            System.out.print("Cadastre um nome de usuário: ");
            nome = scanner.nextLine();
            System.out.print("Cadastre um senha: ");
            senha = scanner.nextLine();
        }

        boolean login(Scanner scanner) {
            System.out.println("\n=== LOGIN ===");

            while(tentativas > 0){
                System.out.print("Digite o nome de usuário: ");
                nomeLogin = scanner.nextLine();
                System.out.print("Digite sua senha: ");
                senhaLogin = scanner.nextLine();

                if (nomeLogin.equals(nome) && senhaLogin.equals(senha)) {
                    System.out.println("Login realizado com sucesso!");
                    return true;
                } else {
                    tentativas--;
                    System.out.println("Usuario ou senha incorretos! Tentativas restantes: " + tentativas);
                }
            }
            System.out.println("Número de tentativas excedido!");
            return false;
        }
}
