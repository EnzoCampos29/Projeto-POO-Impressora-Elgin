import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
public class Impressora {

    String modelo;
    String conexao;
    int tipo;
    int parametro;
    boolean conexaoAberta = false;

    void configurar(Scanner scanner) {
        System.out.println("=== CONFIGURAR CONEXÃO ===");

        System.out.print("Tipo de conexão (1-USB, 2-Serial, 3-Rede, 4-Bluetooth): ");
        tipo = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Modelo da impressora: ");
        modelo = scanner.nextLine();
        System.out.print("Conexão (USB, COM2 ou IP): ");
        conexao = scanner.nextLine();
        System.out.print("Parâmetro: ");
        parametro = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Configuração realizada!");
    }

    void abrirConexao() {
        System.out.println("=== ABRIR CONEXÃO ===");

        try {
            int retorno = ImpressoraDLL.INSTANCE.AbreConexaoImpressora(tipo, modelo, conexao, parametro);
            if (retorno == 0) {
                System.out.println("Conexao aberta com sucesso!");
                conexaoAberta = true;
            } else {
                System.out.println("Erro ao abrir. Código: " + retorno);
            }
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Não foi possível carregar a DLL: " + e.getMessage());
        }
    }

    void fecharConexao() {
        if (conexaoAberta == false) {
            System.out.println("A conexão já está fechada!");
            return;
        }

        System.out.println("=== FECHAR CONEXÃO ===");

        try {
            int resultado = ImpressoraDLL.INSTANCE.FechaConexaoImpressora();

            if (resultado == 0) {
                conexaoAberta = false;
                System.out.println("Conexão fechada com sucesso!");
            } else {
                System.out.println("Erro ao fechar conexao. Código " + resultado);
            }
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Erro ao carregar a biblioteca da impressora.");
            System.out.println(e.getMessage());
        }
    }

    void imprimirTexto(Scanner scanner) {
        String dados;

        if (conexaoAberta == false) {
            System.out.println("Conexão fechada. Abra a conexão antes.");
            return;
        }

        System.out.println("=== IMPRIMIR TEXTO ===");

        System.out.print("Digite o texto que deseja imprimir: ");
        dados = scanner.nextLine();

        try {
            int resultado = ImpressoraDLL.INSTANCE.ImpressaoTexto(dados + "\n", 0, 0, 0);

            if (resultado >= 0) {
                System.out.println("Texto enviado para impressão!");
            } else {
                System.out.println("Erro ao imprimir texto. Código: " + resultado);
            }
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Erro ao carregar a biblioteca da impressora.");
        }
    }

    void imprimirQRCode(Scanner scanner) {
        String dados;
        int tamanho;
        int nivelCorrecao;

        if (conexaoAberta == false) {
            System.out.println("Conexão fechada. Abra a conexão antes.");
            return;
        }

        System.out.println("=== IMPRIMIR QR CODE ===");

        System.out.print("Digite o conteúdo do QR Code: ");
        dados = scanner.nextLine();

        do {
            System.out.print("Tamanho do QR Code (1 a 6): ");
            tamanho = scanner.nextInt();
            scanner.nextLine();

            if (tamanho < 1 || tamanho > 6) {
                System.out.println("Tamanho inválido! Digite entre 1 e 6.");
            }
        } while (tamanho < 1 || tamanho > 6);

        do {
            System.out.print("Nível de correção (1 a 4): ");
            nivelCorrecao = scanner.nextInt();
            scanner.nextLine();

            if (nivelCorrecao < 1 || nivelCorrecao > 4) {
                System.out.println("Nível inválido! Digite entre 1 e 4.");
            }
        } while (nivelCorrecao < 1 || nivelCorrecao > 4);

        try {
            int resultado = ImpressoraDLL.INSTANCE.ImpressaoQRCode(dados, tamanho, nivelCorrecao);

            if (resultado == 0) {
                System.out.println("QR Code enviado para a impressora!");
            } else {
                System.out.println("Erro ao imprimir QR Code. Código: " + resultado);
            }
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Erro ao carregar a biblioteca da impressora.");
        }
    }

    void imprimirCodigoBarras(Scanner scanner) {
        String dados;
        int tipoCodigo;
        int altura;
        int largura;
        int HRI;

        if (conexaoAberta == false) {
            System.out.println("Conexão fechada. Abra a conexão antes.");
            return;
        }

        System.out.println("=== IMPRIMIR CÓDIGO DE BARRAS ===");

        System.out.print("Digite os dados do código de barras: ");
        dados = scanner.nextLine();
        System.out.print("Digite o tipo do código de barras:");
        tipoCodigo = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Digite a altura: ");
        altura = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Digite a largura: ");
        largura = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Exibir os numeros abaixo do codigo? (1-Sim / 0-Nao): ");
        HRI = scanner.nextInt();
        scanner.nextLine();

        try {
            int resultado = ImpressoraDLL.INSTANCE.ImpressaoCodigoBarras(tipoCodigo, dados, altura, largura, HRI);

            if (resultado == 0) {
                System.out.println("Codigo de barras enviado para impressao!");
            } else {
                System.out.println("Erro ao imprimir codigo de barras. Codigo: " + resultado);
            }
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Erro ao carregar a biblioteca da impressora.");
        }
    }

    void imprimirXMLSAT(Scanner scanner) {
        String caminho;

        if (conexaoAberta == false) {
            System.out.println("Conexão fechada. Abra a conexão antes.");
            return;
        }

        System.out.println("=== IMPRIMIR XML SAT ===");

        System.out.print("Digite o caminho do arquivo XML: ");
        caminho = scanner.nextLine();

        try {

            if (Files.isRegularFile(Paths.get(caminho)) == false) {
                System.out.println("Arquivo XML não encontrado!");
                return;
            }

            int parametroXML = 0;

            int resultado = ImpressoraDLL.INSTANCE.ImprimeXMLSAT("path=" + caminho, parametroXML);

            if (resultado == 0) {
                System.out.println("XML SAT enviado para impressão!");
            } else {
                System.out.println("Erro ao imprimir XML SAT. Código: " + resultado);
            }

        } catch (InvalidPathException e) {
            System.out.println("Caminho do arquivo inválido!");

        } catch (UnsatisfiedLinkError e) {
            System.out.println("Erro ao carregar a biblioteca da impressora.");
        }
    }

    void imprimirXMLCancelamentoSAT(Scanner scanner) {

        if (conexaoAberta == false) {
            System.out.println("Conexão fechada. Abra a conexão antes.");
            return;
        }

        String caminho;
        String assQRCode;

        System.out.println("=== IMPRIMIR XML DE CANCELAMENTO SAT ===");

        System.out.print("Digite o caminho do XML de cancelamento: ");
        caminho = scanner.nextLine();

        System.out.print("Digite a assinatura do QR Code da venda: ");
        assQRCode = scanner.nextLine();

        try {

            if (Files.isRegularFile(Paths.get(caminho)) == false ||
                    caminho.toLowerCase().endsWith(".xml") == false) {

                System.out.println("Arquivo XML inválido ou não encontrado!");
                return;
            }

            int parametroXML = 0;

            String caminhoCompleto = Paths.get(caminho)
                    .toAbsolutePath()
                    .toString()
                    .replace("\\", "/");

            int resultado = ImpressoraDLL.INSTANCE.ImprimeXMLCancelamentoSAT(
                    "path=" + caminhoCompleto,
                    assQRCode,
                    parametroXML
            );

            if (resultado == 0) {
                System.out.println("XML de cancelamento enviado para impressão!");
            } else {
                System.out.println("Erro ao imprimir XML de cancelamento. Código: " + resultado);
            }

        } catch (InvalidPathException e) {
            System.out.println("Caminho do arquivo inválido!");

        } catch (SecurityException e) {
            System.out.println("Sem permissão para acessar o arquivo.");

        } catch (UnsatisfiedLinkError e) {
            System.out.println("Erro ao carregar a biblioteca da impressora.");
        }
    }

    void abrirGaveta() {

        if (conexaoAberta == false) {
            System.out.println("Conexão fechada. Abra a conexão antes.");
            return;
        }

        System.out.println("=== ABRIR GAVETA ===");

        try {

            int resultado = ImpressoraDLL.INSTANCE.AbreGaveta(0, 5, 5);

            if (resultado == 0) {
                System.out.println("Comando para abrir a gaveta enviado com sucesso!");
            } else {
                System.out.println("Erro ao abrir gaveta. Código: " + resultado);
            }

        } catch (UnsatisfiedLinkError e) {
            System.out.println("Erro ao carregar a biblioteca da impressora.");
        }
    }

    void sinalSonoro(int quantidade) {
        System.out.println("=== SINAL SONORO ===");
        int retorno = ImpressoraDLL.INSTANCE.SinalSonoro(quantidade, 50, 5);
        if (retorno == 0) {
            System.out.println("Bip!");
        } else {
            System.out.println("Erro no sinal sonoro. Código: " + retorno);
        }
    }


}
