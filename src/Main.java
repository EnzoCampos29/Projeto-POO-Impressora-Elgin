import java.util.Scanner;
public class Main {

    int resposta;

    void menu(Scanner scanner, Impressora impressora){
        while (true) {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1 - Configurar conexão");
            System.out.println("2 - Abrir conexão");
            System.out.println("3 - Fechar conexão");
            System.out.println("4 - Imprimir texto");
            System.out.println("5 - Imprimir QR Code");
            System.out.println("6 - Imprimir código de barras");
            System.out.println("7 - Imprimir XML SAT");
            System.out.println("8 - Imprimir XML de cancelamento SAT");
            System.out.println("9 - Abrir gaveta");
            System.out.println("10 - Sinal sonoro");
            System.out.println("0 - Sair");

            System.out.print("Escolha uma opcao: ");
            try {
                resposta = scanner.nextInt();
                scanner.nextLine();

            } catch (java.util.InputMismatchException e) {
                System.out.println("Digite apenas números!");
                scanner.nextLine();
                continue;
            }

            switch (resposta) {
                case 1:
                    impressora.configurar(scanner);
                    break;

                case 2:
                    impressora.abrirConexao();
                    break;

                case 3:
                    impressora.fecharConexao();
                    break;

                case 4:
                    impressora.imprimirTexto(scanner);
                    break;

                case 5:
                    impressora.imprimirQRCode(scanner);
                    break;

                case 6:
                    impressora.imprimirCodigoBarras(scanner);
                    break;

                case 7:
                    impressora.imprimirXMLSAT(scanner);
                    break;

                case 8:
                    impressora.imprimirXMLCancelamentoSAT(scanner);
                    break;

                case 9:
                    impressora.abrirGaveta();
                    break;

                case 10:
                    System.out.print("Quantidade de bipes: ");
                    try {
                        int quantidade = scanner.nextInt();
                        scanner.nextLine();

                        if (quantidade > 0) {
                            impressora.sinalSonoro(quantidade);
                        } else {
                            System.out.println("Digite uma quantidade maior que zero!");
                        }

                    } catch (java.util.InputMismatchException e) {
                        System.out.println("Digite apenas números!");
                        scanner.nextLine();
                    }

                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opção inválida!");
                    break;
            }
            if (resposta == 0) {
                System.out.println("Encerrando programa...");
                break;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Usuario user = new Usuario();
        user.cadastro(scanner);

        Impressora impressora = new Impressora();

        boolean acesso = user.login(scanner);

        if (acesso) {
            Main programa = new Main();
            programa.menu(scanner, impressora);
        } else {
            System.out.println("Acesso negado!");
        }
        scanner.close();
    }
}