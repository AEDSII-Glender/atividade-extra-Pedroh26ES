import java.util.Comparator;
import java.util.function.Predicate;

public class Playlist {
    private NoLista inicio;
    private NoLista fim;
    private int tamanho;

    public Playlist() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    public void adicionar(Musica musica) {
        NoLista novo = new NoLista(musica);
        if (inicio == null) {
            inicio = fim = novo;
        } else {
            fim.proximo = novo;
            novo.anterior = fim;
            fim = novo;
        }
        tamanho++;
        System.out.println("Música adicionada à playlist.");
    }

    public void remover(Predicate<Musica> criterio) {
        if (inicio == null) {
            System.out.println("Playlist vazia.");
            return;
        }
        
        NoLista atual = inicio;
        while (atual != null) {
            if (criterio.test(atual.musica)) {
                if (atual.anterior != null) {
                    atual.anterior.proximo = atual.proximo;
                } else {
                    inicio = atual.proximo;
                }
                
                if (atual.proximo != null) {
                    atual.proximo.anterior = atual.anterior;
                } else {
                    fim = atual.anterior;
                }
                
                tamanho--;
                System.out.println("Música removida: " + atual.musica.getNome());
                return; 
            }
            atual = atual.proximo;
        }
        System.out.println("Música não encontrada na playlist.");
    }

    public void exibir() {
        if (inicio == null) {
            System.out.println("Playlist vazia.");
            return;
        }
        NoLista atual = inicio;
        while (atual != null) {
            System.out.println(atual.musica);
            atual = atual.proximo;
        }
    }

    public int getTamanho() { return tamanho; }
    public NoLista getInicio() { return inicio; }
    public NoLista getFim() { return fim; }

    public void ordenarIterativo(Comparator<Musica> comparador) {
        if (inicio == null) return;
        for (NoLista i = inicio; i.proximo != null; i = i.proximo) {
            NoLista min = i;
            for (NoLista j = i.proximo; j != null; j = j.proximo) {
                if (comparador.compare(j.musica, min.musica) < 0) {
                    min = j;
                }
            }
            if (min != i) {
                Musica temp = i.musica;
                i.musica = min.musica;
                min.musica = temp;
            }
        }
    }

    public void ordenarQuickSort(Comparator<Musica> comparador) {
        quickSort(inicio, fim, comparador);
    }

    private void quickSort(NoLista inicio, NoLista fim, Comparator<Musica> comparador) {
        if (inicio != null && fim != null && inicio != fim && inicio != fim.proximo) {
            NoLista pivo = particionar(inicio, fim, comparador);
            quickSort(inicio, pivo.anterior, comparador);
            quickSort(pivo.proximo, fim, comparador);
        }
    }

    private NoLista particionar(NoLista inicio, NoLista fim, Comparator<Musica> comparador) {
        Musica pivo = fim.musica;
        NoLista i = inicio.anterior;
        for (NoLista j = inicio; j != fim; j = j.proximo) {
            if (comparador.compare(j.musica, pivo) <= 0) {
                i = (i == null) ? inicio : i.proximo;
                Musica temp = i.musica;
                i.musica = j.musica;
                j.musica = temp;
            }
        }
        i = (i == null) ? inicio : i.proximo;
        Musica temp = i.musica;
        i.musica = fim.musica;
        fim.musica = temp;
        return i;
    }
}