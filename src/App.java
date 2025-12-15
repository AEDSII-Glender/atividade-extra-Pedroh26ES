import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Scanner;

public class App {
    static ABB<Integer, Musica> buscador;
    static Playlist playlist;
    static Historico historico;
    static Scanner teclado = new Scanner(System.in, StandardCharsets.UTF_8);

    public static void main(String[] args) {
        buscador = new ABB<>();
        playlist = new Playlist();
        historico = new Historico();

        carregarMusicas("musicas");

        int opcao;
        do {
            limparTela();
            System.out.println("=== SPOTIFY ===");
            System.out.println("[1] Adicionar música na Playlist");
            System.out.println("[2] Remover Música da Playlist");
            System.out.println("[3] Ordenar Playlist");
            System.out.println("[4] Reproduzir na Ordem");
            System.out.println("[5] Reproduzir Aleatoriamente");
            System.out.println("[6] Exibir a minha playlist");
            System.out.println("[7] Histórico de Reprodução"); 
            System.out.println("0. Sair do Programa");
            System.out.print("Digite: ");
            try {
                opcao = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1 -> adicionarMusica();
                case 2 -> removerMusica();
                case 3 -> ordenarPlaylist();
                case 4 -> reproduzirOrdem();
                case 5 -> reproduzirAleatorio();
                case 6 -> { playlist.exibir(); pausa(); }
                case 7 -> gerenciarHistorico(); 
            }
        } while (opcao != 0);
    }

    static void carregarMusicas(String nomeArquivo) {
        File arquivo = new File(nomeArquivo);
        if (!arquivo.exists()) {
            arquivo = new File("../" + nomeArquivo);
        }

        try (Scanner scanner = new Scanner(arquivo, StandardCharsets.UTF_8)) {
            if (scanner.hasNextLine()) scanner.nextLine(); 
            
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                if (linha.trim().isEmpty()) continue;
                try {
                    String[] partes = linha.split(";");
                    int id = Integer.parseInt(partes[0]);
                    String nome = partes[1];
                    String artista = partes[2];
                    double duracao = Double.parseDouble(partes[3]);
                    
                    Musica m = new Musica(id, nome, artista, duracao);
                    buscador.inserir(id, m);
                } catch (Exception e) {
                    System.out.println("Erro ao ler linha: " + linha);
                }
            }
            System.out.println("Músicas carregadas com sucesso!");
            pausa(); 
        } catch (Exception e) {
            System.out.println("ERRO: Arquivo 'musicas' não encontrado.");
            pausa();
        }
    }

    static void adicionarMusica() {
        System.out.println("Músicas Disponíveis:");
        System.out.println(buscador.percorrer());
        System.out.print("Digite o ID da música: ");
        try {
            int id = Integer.parseInt(teclado.nextLine());
            Musica m = buscador.pesquisar(id);
            playlist.adicionar(m);
        } catch (Exception e) {
            System.out.println("Música não encontrada.");
        }
        pausa();
    }

    static void removerMusica() {
        System.out.println("Remover por: 1-ID, 2-Título/Nome");
        try {
            int op = Integer.parseInt(teclado.nextLine());
            if (op == 1) {
                System.out.print("Digite o ID: ");
                int id = Integer.parseInt(teclado.nextLine());
                playlist.remover(m -> m.getId() == id);
            } else if (op == 2) {
                System.out.print("Digite o Título (Nome): ");
                String nome = teclado.nextLine();
                playlist.remover(m -> m.getNome().equalsIgnoreCase(nome));
            } else {
                System.out.println("Opção inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
        pausa();
    }

    static void ordenarPlaylist() {
        System.out.println("Escolha Entre: 1-ID, 2-Nome, 3-Duração");
        try {
            int crit = Integer.parseInt(teclado.nextLine());
            Comparator<Musica> comparador = null;

            switch (crit) {
                case 1 -> comparador = Comparator.comparingInt(Musica::getId);
                case 2 -> comparador = Comparator.comparing(Musica::getNome, String::compareToIgnoreCase);
                case 3 -> comparador = Comparator.comparingDouble(Musica::getDuracao);
                default -> {
                    System.out.println("Critério inválido.");
                    pausa();
                    return;
                }
            }

            System.out.println("Método: 1-Iterativo, 2-QuickSort");
            int met = Integer.parseInt(teclado.nextLine());
            
            if (met == 1) playlist.ordenarIterativo(comparador);
            else playlist.ordenarQuickSort(comparador);
            
            System.out.println("Playlist ordenada.");
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
        }
        pausa();
    }

    static void reproduzirOrdem() {
        if (playlist.getInicio() == null) {
            System.out.println("Playlist vazia.");
            pausa();
            return;
        }

        System.out.println("Iniciar reprodução pelo: 1-Início, 2-Fim");
        int startOp = 1;
        try {
            startOp = Integer.parseInt(teclado.nextLine());
        } catch (Exception e) {}

        NoLista atual = (startOp == 2) ? playlist.getFim() : playlist.getInicio();

        while (atual != null) {
            System.out.println("-------------------------------------------------");
            System.out.println("Reproduzindo: " + atual.musica.getNome() + " - " + atual.musica.getArtista());
            System.out.println("-------------------------------------------------");
            historico.adicionar(atual.musica);
            
            System.out.println("Opções: [P]róximo, [A]nterior, [S]air");
            String op = teclado.nextLine().toUpperCase();

            if (op.equals("S")) break;
            
            if (op.equals("P")) {
                if (atual.proximo != null) {
                    atual = atual.proximo;
                } else {
                    System.out.println("Playlist finalizada. Deseja reiniciar? (S/N)");
                    if (teclado.nextLine().equalsIgnoreCase("S")) {
                        atual = playlist.getInicio();
                    } else {
                        break;
                    }
                }
            } else if (op.equals("A")) {
                if (atual.anterior != null) {
                    atual = atual.anterior;
                } else {
                    System.out.println("Início da playlist.");
                }
            }
        }
    }

    static void reproduzirAleatorio() {
        while (true) {
            System.out.println("===  Aleatorio ===");
            System.out.println("Digite o ID da música para tocar");
            System.out.println("Ou digite -1 para tocar a última música ouvida)");
            System.out.println("Ou digite 0 para Sair");
            System.out.print("Opção: ");
            
            try {
                int id = Integer.parseInt(teclado.nextLine());
                
                if (id == 0) break;
                
                if (id == -1) {
                    recuperarUltimaHistorico();
                    continue;
                }
                
                Musica m = buscador.pesquisar(id);
                System.out.println("================================================");
                System.out.println("Reproduzindo: " + m.getNome() + " - " + m.getArtista());
                System.out.println("================================================");
                historico.adicionar(m);
                
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
            } catch (Exception e) {
                System.out.println("Música não encontrada.");
            }
        }
    }

    static void gerenciarHistorico() {
        historico.exibir(); 
        
        if (!historico.vazio()) {
            System.out.println("\nDeseja recuperar (voltar) a última reprodução? (S/N)");
            String resp = teclado.nextLine();
            if (resp.equalsIgnoreCase("S")) {
                recuperarUltimaHistorico();
            }
        } else {
            pausa();
        }
    }

    static void recuperarUltimaHistorico() {
        if (historico.vazio()) {
            System.out.println("Histórico vazio.");
        } else {
            Musica m = historico.voltar();
            System.out.println("-------------------------------------------------");
            System.out.println("Voltando reprodução para: " + m.getNome() + " - " + m.getArtista());
            System.out.println("-------------------------------------------------");
        }
        pausa();
    }

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pausa() {
        System.out.println("Pressione Enter para continuar...");
        teclado.nextLine();
    }
}