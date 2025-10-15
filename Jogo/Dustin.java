public class Dustin extends Personagem {
    // Dustin tem defesa um pouco maior e inventário inicial maior (representa itens úteis)
    public Dustin(String nome) {
        super(nome, 30, 7, 6, 1);
        // itens iniciais poderiam ser adicionados em Jogo
    }
    public Dustin() { this("Dustin"); }
    public Dustin(Dustin d) { super(d); }
}
