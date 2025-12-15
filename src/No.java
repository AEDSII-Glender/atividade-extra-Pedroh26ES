public class No<K, V> {

	private K chave;       
	private V item;        
	private No<K, V> direita;   
	private No<K, V> esquerda;   
	private int altura;
	
	public No(K chave, V item) {
		setChave(chave);
		setItem(item);
	    setDireita(null);
	    setEsquerda(null);
	    altura = 0;
	}

	public V getItem() {
		return item;
	}

	public void setItem(V item) {
		this.item = item;
	}

	public K getChave() {
		return chave;
	}

	public void setChave(K chave) {
		this.chave = chave;
	}
	
	public No<K, V> getDireita() {
		return direita;
	}

	public void setDireita(No<K, V> direita) {
		this.direita = direita;
	}

	public No<K, V> getEsquerda() {
		return esquerda;
	}

	public void setEsquerda(No<K, V> esquerda) {
		this.esquerda = esquerda;
	}
	
	private int getAltura(No<K, V> no) {
		
		if (no != null)
			return no.getAltura();
		else
			return -1;
	}
	
	public int getAltura() {
		return this.altura;
	}

	public void setAltura() {
		
		int alturaEsquerda, alturaDireita;
		
		alturaEsquerda = getAltura(esquerda);
		alturaDireita = getAltura(direita);
		
		if (alturaEsquerda > alturaDireita)
			altura = alturaEsquerda + 1;
		else
			altura = alturaDireita + 1;
	}
	
	public int getFatorBalanceamento() {
		
		int alturaEsquerda, alturaDireita;
		
		alturaEsquerda = getAltura(esquerda);
		alturaDireita = getAltura(direita);
		
		return (alturaEsquerda - alturaDireita);
	}	
}