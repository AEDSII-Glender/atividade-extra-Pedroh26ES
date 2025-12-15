import java.util.Stack;

public class Historico {
    private Stack<Musica> musicasReproduzidas;

    public Historico() {
        this.musicasReproduzidas = new Stack<>();
    }

    public void adicionar(Musica musica) {
        musicasReproduzidas.push(musica);
        System.out.println(" (Salvo no histórico)");
    }

    public Musica voltar() {
        if (!musicasReproduzidas.isEmpty()) {
            return musicasReproduzidas.pop();
        }
        return null;
    }

    public boolean vazio() {
        return musicasReproduzidas.isEmpty();
    }

    public void exibir() {
        if (musicasReproduzidas.isEmpty()) {
            System.out.println("Histórico de reprodução vazio.");
            return;
        }
        
        System.out.println("=== Histórico de Reproduções (Do mais recente para o mais antigo) ===");
        for (int i = musicasReproduzidas.size() - 1; i >= 0; i--) {
            System.out.println((musicasReproduzidas.size() - i) + "ª anterior: " + musicasReproduzidas.get(i).getNome() + " - " + musicasReproduzidas.get(i).getArtista());
        }
    }
}