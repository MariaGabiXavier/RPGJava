public class Inimigo extends Personagem {

    private int xpDrop; // Quanto de XP o inimigo dá

    //  Construtor completo
    public Inimigo(String nome, int hp, int atk, int def, int nivel, int xpDrop) {
        super(nome, hp, atk, def, nivel);
        this.xpDrop = xpDrop;

        // Garante que todo inimigo tem um inventário inicializado
        if (this.inventario == null) {
            this.inventario = new Inventario();
        }
    }

    // Construtor padrão
    public Inimigo() {
        this("Demogorgon", 20, 6, 3, 1, 50);
    }

    // Construtor de cópia (para checkpoints)
    public Inimigo(Inimigo outro) {
        super(outro);
        this.xpDrop = outro.xpDrop;
        this.inventario = new Inventario(outro.inventario); // deep copy do inventário
    }

    // Retorna o XP que o jogador ganha ao derrotar este inimigo
    public int getValorExperiencia() {
        return this.xpDrop;
    }
}
