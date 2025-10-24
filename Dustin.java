public class Dustin extends Personagem {
    public Dustin(String nome) {
        super(nome, 20, 5, 5, 1); 
    }
    public Dustin() { this("Dustin"); }
    public Dustin(Dustin d) { super(d); }
}
