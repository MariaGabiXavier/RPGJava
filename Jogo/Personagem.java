import java.util.Random;

public abstract class Personagem implements Cloneable {
    protected String nome;
    protected int pontosVida;
    protected int ataque;
    protected int defesa;
    protected int nivel;
    protected Inventario inventario;
    protected Random rng = new Random();

    public Personagem(String nome, int pontosVida, int ataque, int defesa, int nivel) {
        this.nome = nome;
        this.pontosVida = pontosVida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = nivel;
        this.inventario = new Inventario();
    }

    public Personagem() { this("SemNome", 20, 5, 3, 1); }

    public Personagem(Personagem outro) {
        this.nome = outro.nome;
        this.pontosVida = outro.pontosVida;
        this.ataque = outro.ataque;
        this.defesa = outro.defesa;
        this.nivel = outro.nivel;
        this.inventario = outro.inventario.clone();
    }

    public String getNome() { return nome; }
    public int getPontosVida() { return pontosVida; }
    public int getAtaque() { return ataque; }
    public int getDefesa() { return defesa; }
    public int getNivel() { return nivel; }
    public Inventario getInventario() { return inventario; }

    public boolean estaVivo() { return pontosVida > 0; }
    public void receberDano(int d) { this.pontosVida -= Math.max(0, d); if (this.pontosVida < 0) this.pontosVida = 0; }
    public void curar(int q) { this.pontosVida += q; }

    public int rolarDado() { return rng.nextInt(20) + 1; }

    // método de saque genérico (usado ao vencer inimigo)
    protected void saquear(Personagem inimigo) {
        System.out.println("Saqueando o inimigo...");
        Inventario loot = inimigo.getInventario().clone();
        for (Item it : loot.listarOrdenado()) {
            int q = it.getQuantidade();
            int transferir = Math.max(1, q / 2);
            this.inventario.adicionar(new Item(it.getNome(), it.getDescricao(), it.getEfeito(), transferir));
            System.out.printf("Você obteve %d x %s\n", transferir, it.getNome());
        }
    }

    @Override
    public Personagem clone() {
        try {
            Personagem copia = (Personagem) super.clone();
            copia.inventario = this.inventario.clone();
            return copia;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("%s (Nível %d) HP=%d ATK=%d DEF=%d", nome, nivel, pontosVida, ataque, defesa);
    }
}
