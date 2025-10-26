import java.util.Objects;

public class Item implements Comparable<Item>, Cloneable {
    private String nome;
    private String descricao;
    private String efeito; // ex: "cura25", "atk+3", "shield"
    private int quantidade;


    public Item(String nome, String descricao, String efeito, int quantidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.efeito = efeito;
        this.quantidade = Math.max(0, quantidade);
    }

    // Construtor de cópia
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
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;

        Item i = (Item) obj;

        if (!this.nome.equalsIgnoreCase(i.nome)) return false;
        if (!this.efeito.equalsIgnoreCase(i.efeito)) return false;
        if (this.quantidade != i.quantidade) return false;
        if (!this.descricao.equalsIgnoreCase(i.descricao)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int ret = 1;

        ret = ret * 2 + this.nome.toLowerCase().hashCode();
        ret = ret * 3 + this.descricao.toLowerCase().hashCode();
        ret = ret * 5 + this.efeito.toLowerCase().hashCode();
        ret = ret * 7 + ((Integer)this.quantidade).hashCode();

        if (ret < 0) ret = -ret;
        return ret;
    }

    @Override
    public int compareTo(Item i) {
        if (this == i) return 0;

        int cmpNome = this.nome.compareToIgnoreCase(i.nome);
        if (cmpNome != 0) return cmpNome;

        int cmpEfeito = this.efeito.compareToIgnoreCase(i.efeito);
        if (cmpEfeito != 0) return cmpEfeito;

        if (this.quantidade < i.quantidade) return -666;
        if (this.quantidade > i.quantidade) return 666;

        return this.descricao.compareToIgnoreCase(i.descricao);
    }

    @Override
    public Item clone() {
        try {
            return (Item) super.clone(); // faz o cast aqui
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // nunca deve acontecer
        }
    }
    

    @Override
    public String toString() {
        return String.format("%s x%d - %s (%s)", nome, quantidade, descricao, efeito);
    }
}
