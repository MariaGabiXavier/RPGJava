public class Dustin extends Personagem {
    // Stats rebalanceados para Nível 0
    public Dustin(String nome) {
        super(nome, 20, 5, 5, 0); // HP baixo, Ataque baixo, Defesa alta
    }
    public Dustin() { this("Dustin"); }
    public Dustin(Dustin d) { super(d); }
}
