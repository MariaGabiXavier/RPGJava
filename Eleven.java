public class Eleven extends Personagem {
    public Eleven(String nome) { 
        super(nome, 25, 8, 2, 1); 
    }
    public Eleven() { this("Eleven"); }
    public Eleven(Eleven e) { super(e); }
}