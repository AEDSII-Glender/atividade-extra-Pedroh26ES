public class NoLista {
    public Musica musica;
    public NoLista proximo;
    public NoLista anterior;

    public NoLista(Musica musica) {
        this.musica = musica;
        this.proximo = null;
        this.anterior = null;
    }
}