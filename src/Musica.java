public class Musica implements Comparable<Musica> {
    private int id;
    private String nome; 
    private String artista;
    private double duracao;

    public Musica(int id, String nome, String artista, double duracao) {
        this.id = id;
        this.nome = nome; 
        this.artista = artista;
        this.duracao = duracao;
    }

    public int getId() { return id; }
    public String getNome() { return nome; } 
    public String getArtista() { return artista; }
    public double getDuracao() { return duracao; }

    @Override
    public String toString() {
        return String.format("ID: %d | Nome: %s | Artista: %s | Duração: %.2f", id, nome, artista, duracao);
    }

    @Override
    public int compareTo(Musica outra) {
        return Integer.compare(this.id, outra.id);
    }
}