public class Mike extends Personagem {
    // Stats rebalanceados para Nível 0
    public Mike(String nome) { 
        super(nome, 22, 6, 4, 0); // Stats equilibrados
    }
    public Mike() { this("Mike"); }
    public Mike(Mike m) { super(m); }
}