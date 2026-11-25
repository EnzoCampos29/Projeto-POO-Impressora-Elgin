import java.util.Scanner;
public class Impressora {

    String modelo;
    String conexao;
    int tipo;
    int parametro;
    boolean conexaoAberta = false;
    private static final String ASSINATURA_QR_CODE = "Q5DLkpdRijIRGY6YSSNsTWK1TztHL1vD0V1Jc4spo/CEUqICEb9SFy82ym8EhBRZjbh3btsZhF+sjHqEMR159i4agru9x6KsepK/q0E2e5xlU5cv3m1woYfgHyOkWDNcSdMsS6bBh2Bpq6s89yJ9Q6qh/J8YHi306ce9Tqb/drKvN2XdE5noRSS32TAWuaQEVd7u+TrvXlOQsE3fHR1D5f1saUwQLPSdIv01NF6Ny7jZwjCwv1uNDgGZONJdlTJ6p0ccqnZvuE70aHOI09elpjEO6Cd+orI7XHHrFCwhFhAcbalc+ZfO5b/+vkyAHS6CYVFCDtYR9Hi5qgdk31v23w==";

    void configurar(Scanner scanner) {
        System.out.println("=== CONFIGURAR CONEXÃO ===");

        do {
            System.out.print("Tipo de conexão (1-USB, 2-Serial, 3-Rede, 4-Bluetooth): ");

            try {
                tipo = scanner.nextInt();
                scanner.nextLine();

                if (tipo < 1 || tipo > 4) {
                    System.out.println("Tipo inválido! Digite entre 1 e 4.");
                }
            } catch(Exception e) {
                tipo = 0;
                System.out.println("Digite apenas números!");
                scanner.nextLine();
            }
        } while(tipo < 1 || tipo > 4);

        System.out.print("Modelo da impressora: ");
        modelo = scanner.nextLine();
        System.out.print("Conexão (USB, COM2 ou IP): ");
        conexao = scanner.nextLine();

        boolean parametroValido = false;

        while (parametroValido == false) {
            System.out.print("Parâmetro: ");

            try {
                parametro = scanner.nextInt();
                scanner.nextLine();
                parametroValido = true;
            } catch(Exception e) {
                System.out.println("Digite apenas números!");
                scanner.nextLine();
            }
        }

        System.out.println("Configuração realizada!");
    }

    void abrirConexao() {
        System.out.println("=== ABRIR CONEXÃO ===");

        if (modelo == null || modelo.isEmpty() || conexao == null || conexao.isEmpty()) {
            System.out.println("Configure a impressora antes de abrir a conexão!");
            return;
        }

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
        } catch (Exception e) {
            conexaoAberta = false;
            System.out.println("Erro ao abrir conexão: " + e.getMessage());
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
        } catch (Exception e) {
            System.out.println("Erro ao fechar conexão: " + e.getMessage());
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
                ImpressoraDLL.INSTANCE.Corte(5);
            } else {
                System.out.println("Erro ao imprimir texto. Código: " + resultado);
            }
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Erro ao carregar a biblioteca da impressora.");
        } catch (Exception e) {
            System.out.println("Erro ao imprimir texto: " + e.getMessage());
        }
    }

    void imprimirQRCode(Scanner scanner) {
        String dados;
        int tamanho = 0;
        int nivelCorrecao = 0;

        if (conexaoAberta == false) {
            System.out.println("Conexão fechada. Abra a conexão antes.");
            return;
        }

        System.out.println("=== IMPRIMIR QR CODE ===");

        System.out.print("Digite o conteúdo do QR Code: ");
        dados = scanner.nextLine();

        do {
            System.out.print("Tamanho do QR Code (1 a 6): ");

            try {
                tamanho = scanner.nextInt();
                scanner.nextLine();

                if (tamanho < 1 || tamanho > 6) {
                    System.out.println("Tamanho inválido! Digite entre 1 e 6.");
                }
            } catch(Exception e) {
                tamanho = 0;
                System.out.println("Digite apenas números!");
                scanner.nextLine();
            }

        } while (tamanho < 1 || tamanho > 6);

        do {
            System.out.print("Nível de correção (1 a 4): ");

            try {
                nivelCorrecao = scanner.nextInt();
                scanner.nextLine();

                if (nivelCorrecao < 1 || nivelCorrecao > 4) {
                    System.out.println("Nível inválido! Digite entre 1 e 4.");
                }
            } catch(Exception e) {
                nivelCorrecao = 0;
                System.out.println("Digite apenas números!");
                scanner.nextLine();
            }
        } while (nivelCorrecao < 1 || nivelCorrecao > 4);

        try {
            int resultado = ImpressoraDLL.INSTANCE.ImpressaoQRCode(dados, tamanho, nivelCorrecao);

            if (resultado == 0) {
                System.out.println("QR Code enviado para a impressora!");
                ImpressoraDLL.INSTANCE.Corte(5);
            } else {
                System.out.println("Erro ao imprimir QR Code. Código: " + resultado);
            }
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Erro ao carregar a biblioteca da impressora.");
        } catch (Exception e) {
            System.out.println("Erro ao imprimir QR Code: " + e.getMessage());
        }
    }

    void imprimirCodigoBarras(Scanner scanner) {
        String dados = "{A12345678912}";
        int tipoCodigo = 0;
        int altura = 0;
        int largura = 0;
        int HRI = -1;

        if (conexaoAberta == false) {
            System.out.println("Conexão fechada. Abra a conexão antes.");
            return;
        }

        System.out.println("=== IMPRIMIR CÓDIGO DE BARRAS ===");
        System.out.print("Dados do código de barras: " + dados);

        while (tipoCodigo <= 0) {
            System.out.print("Digite o tipo do código de barras:");

            try {
                tipoCodigo = scanner.nextInt();
                scanner.nextLine();

                if (tipoCodigo <= 0) {
                    System.out.println("Digite um tipo válido!");
                }
            } catch(Exception e) {
                tipoCodigo = 0;
                System.out.println("Digite apenas números!");
                scanner.nextLine();
            }
        }

        while (altura <= 0) {
            System.out.print("Digite a altura: ");

            try {
                altura = scanner.nextInt();
                scanner.nextLine();

                if (altura <= 0) {
                    System.out.println("A altura deve ser maior que zero!");
                }
            } catch(Exception e) {
                altura = 0;
                System.out.println("Digite apenas números!");
                scanner.nextLine();
            }
        }

        while (largura <= 0) {
            System.out.print("Digite a largura: ");

            try {
                largura = scanner.nextInt();
                scanner.nextLine();

                if (largura <= 0) {
                    System.out.println("A largura deve ser maior que zero!");
                }
            } catch(Exception e) {
                largura = 0;
                System.out.println("Digite apenas números!");
                scanner.nextLine();
            }
        }

        do {
            System.out.print("Exibir os numeros abaixo do codigo? (1-Sim / 0-Nao): ");

            try {
                HRI = scanner.nextInt();
                scanner.nextLine();

                if (HRI != 0 && HRI != 1) {
                    System.out.println("Digite 1 ou 0!");
                }
            } catch (Exception e) {
                HRI = -1;
                System.out.println("Digite apenas números!");
                scanner.nextLine();
            }
        } while (HRI != 0 && HRI != 1);

        try {
            int resultado = ImpressoraDLL.INSTANCE.ImpressaoCodigoBarras(tipoCodigo, dados, altura, largura, HRI);

            if (resultado == 0) {
                System.out.println("Codigo de barras enviado para impressao!");
                ImpressoraDLL.INSTANCE.ImpressaoTexto("\n\n", 0, 0, 0);
                ImpressoraDLL.INSTANCE.Corte(5);
            } else {
                System.out.println("Erro ao imprimir codigo de barras. Codigo: " + resultado);
            }
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Erro ao carregar a biblioteca da impressora.");
        } catch (Exception e) {
            System.out.println("Erro ao imprimir código de barras: " + e.getMessage());
        }
    }

    void imprimirXMLSAT(Scanner scanner) {
        String caminho;

        if (conexaoAberta == false) {
            System.out.println("Conexão fechada. Abra a conexão antes.");
            return;
        }

        System.out.println("=== IMPRIMIR XML SAT ===");

        try {
            int resultado = ImpressoraDLL.INSTANCE.ImprimeXMLSAT("path=XMLSAT.xml", 0);

            if (resultado == 0) {
                System.out.println("XML SAT enviado para impressão!");
                ImpressoraDLL.INSTANCE.ImpressaoTexto("\n\n", 0, 0, 0);
                ImpressoraDLL.INSTANCE.Corte(5);
            } else {
                System.out.println("Erro ao imprimir XML SAT. Código: " + resultado);
            }
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Erro ao carregar a biblioteca da impressora.");
        } catch (Exception e) {
            System.out.println("Erro ao imprimir XML SAT: " + e.getMessage());
        }
    }

    void imprimirXMLCancelamentoSAT(Scanner scanner) {

        if (conexaoAberta == false) {
            System.out.println("Conexão fechada. Abra a conexão antes.");
            return;
        }

        String assQRCode;

        System.out.println("=== IMPRIMIR XML DE CANCELAMENTO SAT ===");

        int resultado = ImpressoraDLL.INSTANCE.ImprimeXMLCancelamentoSAT( "path=CANC_SAT.xml", ASSINATURA_QR_CODE, 0 );

        try {
            int resultado = ImpressoraDLL.INSTANCE.ImprimeXMLCancelamentoSAT("path=CANC_SAT.xml", assQRCode, 0);

            if (resultado == 0) {
                System.out.println("XML de cancelamento enviado para impressão!");
                ImpressoraDLL.INSTANCE.ImpressaoTexto("\n\n", 0, 0, 0);
                ImpressoraDLL.INSTANCE.Corte(5);
            } else {
                System.out.println("Erro ao imprimir XML de cancelamento. Código: " + resultado);
            }

        } catch (UnsatisfiedLinkError e) {
            System.out.println("Erro ao carregar a biblioteca da impressora.");
        } catch (Exception e) {
            System.out.println("Erro ao imprimir XML de cancelamento: " + e.getMessage());
        }
    }

    void abrirGaveta() {

        if (conexaoAberta == false) {
            System.out.println("Conexão fechada. Abra a conexão antes.");
            return;
        }

        System.out.println("=== ABRIR GAVETA ===");

        try {

            int resultado = ImpressoraDLL.INSTANCE.AbreGavetaElgin(0, 5, 5);

            if (resultado == 0) {
                System.out.println("Comando para abrir a gaveta enviado com sucesso!");
            } else {
                System.out.println("Erro ao abrir gaveta. Código: " + resultado);
            }

        } catch (UnsatisfiedLinkError e) {
            System.out.println("Erro ao carregar a biblioteca da impressora.");
        } catch (Exception e) {
            System.out.println("Erro ao abrir gaveta: " + e.getMessage());
        }
    }

    void sinalSonoro(int quantidade) {
        if (conexaoAberta == false) {
            System.out.println("Conexão fechada. Abra a conexão antes.");
            return;
        }

        if (quantidade <= 0) {
            System.out.println("Digite uma quantidade maior que zero!");
            return;
        }

        System.out.println("=== SINAL SONORO ===");

        try {
            int retorno = ImpressoraDLL.INSTANCE.SinalSonoro(quantidade, 50, 5);
            if (retorno == 0) {
                System.out.println("Bip!");
            } else {
                System.out.println("Erro no sinal sonoro. Código: " + retorno);
            }
        } catch(UnsatisfiedLinkError e) {
            System.out.println("Erro ao carregar a biblioteca da impressora.");
        } catch (Exception e) {
            System.out.println("Erro no sinal sonoro: " + e.getMessage());
        }
    }
}
