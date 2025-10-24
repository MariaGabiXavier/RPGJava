import java.util.Objects;

public class Item implements Comparable<Item>, Cloneable {
    private String nome;
    private String descricao;
    private String efeito; 
    private int quantidade;


    public Item(String nome, String descricao, String efeito, int quantidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.efeito = efeito;
        this.quantidade = Math.max(0, quantidade);
    }

    public Item(Item outro) {
        this.nome = outro.nome;
        this.descricao = outro.descricao;
        this.efeito = outro.efeito;
        this.quantidade = outro.quantidade;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getEfeito() { return efeito; }
    public int getQuantidade() { return quantidade; }

    public void setQuantidade(int quantidade) { this.quantidade = Math.max(0, quantidade); }
    public void incrementar(int q) { setQuantidade(this.quantidade + q); }
    public void decrementar(int q) { setQuantidade(this.quantidade - q); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(nome.toLowerCase(), item.nome.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome.toLowerCase());
    }

    @Override
    public int compareTo(Item o) {
        return this.nome.compareToIgnoreCase(o.nome);
    }

    @Override
    public Item clone() {
        return new Item(this);
    }

    @Override
    public String toString() {
        return String.format("%s x%d - %s (%s)", nome, quantidade, descricao, efeito);
    }
}
