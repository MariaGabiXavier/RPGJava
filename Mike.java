public class Mike extends Personagem {
    public Mike(String nome) { 
        super(nome, 22, 6, 4, 1); 
    }
    public Mike() { this("Mike"); }
    public Mike(Mike m) { super(m); }
}