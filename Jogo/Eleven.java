public class Eleven extends Personagem {
    // Eleven: alto ataque psíquico, HP moderado
    public Eleven(String nome) { super(nome, 35, 12, 3, 2); }
    public Eleven() { this("Eleven"); }
    public Eleven(Eleven e) { super(e); }

    // habilidade especial simples: "Impulse" que causa dano maior em um ataque (usa-se no Jogo)
}
