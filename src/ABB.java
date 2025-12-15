import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class ABB<K, V> implements IMapeamento<K, V>{

	private No<K, V> raiz; 
	private Comparator<K> comparador; 
	private int tamanho;
	private long comparacoes;
	private long inicio;
	private long termino;
	
	private void init(Comparator<K> comparador) {
		raiz = null;
		tamanho = 0;
		this.comparador = comparador;
	}

	@SuppressWarnings("unchecked")
	public ABB() {
	    init((Comparator<K>) Comparator.naturalOrder());
	}

	public ABB(Comparator<K> comparador) {
	    init(comparador);
	}

    public ABB(ABB<?, V> original, Function<V, K> funcaoChave) {
        ABB<K, V> nova = new ABB<>();
        nova = copiarArvore(original.raiz, funcaoChave, nova);
        this.raiz = nova.raiz;
    }
    
    private <T> ABB<T, V> copiarArvore(No<?, V> raizArvore, Function<V, T> funcaoChave, ABB<T, V> novaArvore) {
        if (raizArvore != null) {
    		novaArvore = copiarArvore(raizArvore.getEsquerda(), funcaoChave, novaArvore);
            V item = raizArvore.getItem();
            T chave = funcaoChave.apply(item);
    		novaArvore.inserir(chave, item);
    		novaArvore = copiarArvore(raizArvore.getDireita(), funcaoChave, novaArvore);
    	}
        return novaArvore;
    }
    
	public Boolean vazia() {
	    return (this.raiz == null);
	}
    
    @Override
	public V pesquisar(K chave) {
    	comparacoes = 0;
    	inicio = System.nanoTime();
    	V procurado = pesquisar(raiz, chave);
    	termino = System.nanoTime();
    	return procurado;
	}
    
    private V pesquisar(No<K, V> raizArvore, K procurado) {
    	int comparacao;
    	comparacoes++;
    	if (raizArvore == null)
    		throw new NoSuchElementException("O item não foi localizado na árvore!");
    	
    	comparacao = comparador.compare(procurado, raizArvore.getChave());
    	
    	if (comparacao == 0)
    		return raizArvore.getItem();
    	else if (comparacao < 0)
    		return pesquisar(raizArvore.getEsquerda(), procurado);
    	else
    		return pesquisar(raizArvore.getDireita(), procurado);
    }
    
    @Override
    public int inserir(K chave, V item) {
        raiz = inserirRecursivo(raiz, chave, item);
        return tamanho;
    }

    private No<K, V> inserirRecursivo(No<K, V> no, K chave, V item) {
        if (no == null) {
            tamanho++;
            return new No<>(chave, item);
        }
        
        int cmp = comparador.compare(chave, no.getChave());
        
        if (cmp < 0) {
            no.setEsquerda(inserirRecursivo(no.getEsquerda(), chave, item));
        } else {
            no.setDireita(inserirRecursivo(no.getDireita(), chave, item));
        }
        
        no.setAltura();
        return no;
    }

    @Override 
    public String toString(){
    	return percorrer();
    }

    @Override
    public String percorrer() {
    	return caminhamentoEmOrdem();
    }

    public String caminhamentoEmOrdem() {
        StringBuilder sb = new StringBuilder();
        caminhamentoRecursivo(raiz, sb);
        return sb.toString();
    }

    private void caminhamentoRecursivo(No<K, V> no, StringBuilder sb) {
        if (no != null) {
            caminhamentoRecursivo(no.getEsquerda(), sb);
            sb.append(no.getItem().toString()).append("\n");
            caminhamentoRecursivo(no.getDireita(), sb);
        }
    }

    @Override
    public V remover(K chave) {
        
    	return null;
    }

	@Override
	public int tamanho() { return tamanho; }
	
	@Override
	public long getComparacoes() { return comparacoes; }

	@Override
	public double getTempo() { return (termino - inicio) / 1_000_000; }
}