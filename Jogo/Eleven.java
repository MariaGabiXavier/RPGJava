public class Eleven extends Personagem {
    // Stats rebalanceados para Nível 0
    public Eleven(String nome) { 
        super(nome, 25, 8, 2, 0); // HP alto, Ataque alto, Defesa baixa
    }
    public Eleven() { this("Eleven"); }
    public Eleven(Eleven e) { super(e); }
}