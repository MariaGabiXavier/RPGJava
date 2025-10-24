public class Inimigo extends Personagem {

    private int xpDrop; 

    public Inimigo(String nome, int hp, int atk, int def, int nivel, int xpDrop) {
        super(nome, hp, atk, def, nivel);
        this.xpDrop = xpDrop;

        if (this.inventario == null) {
            this.inventario = new Inventario();
        }
    }

    public Inimigo() {
        this("Demogorgon", 20, 6, 3, 1, 50);
    }

    public Inimigo(Inimigo outro) {
        super(outro);
        this.xpDrop = outro.xpDrop;
        this.inventario = new Inventario(outro.inventario); 
    }

    public int getValorExperiencia() {
        return this.xpDrop;
    }

    @Override
    public String toString() {
        return String.format(
            "%s [HP: %d, Atk: %d, Def: %d, Nível: %d, XP: %d, Itens: %s]",
            nome, pontosVida, ataque, defesa, nivel, xpDrop,
            inventario != null ? inventario.toString() : "<sem itens>"
        );
    }
}
