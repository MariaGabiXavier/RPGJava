public class Inimigo extends Personagem {
    public Inimigo(String nome, int hp, int atk, int def, int nivel) {
        super(nome, hp, atk, def, nivel);
    }
    public Inimigo() { super("Demogorgon", 20, 6, 3, 1); }
    public Inimigo(Inimigo outro) { super(outro); }
}
