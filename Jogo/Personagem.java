import java.util.Random;

public abstract class Personagem implements Cloneable {
    protected String nome;
    protected int pontosVida;
    protected int pontosVidaMax;
    protected int ataque;
    protected int defesa;
    protected int nivel;
    protected Inventario inventario;
    protected Random rng = new Random();

    protected int experiencia;
    protected int experienciaProximoNivel;


    // Nível Máximo Definido
    public static final int NIVEL_MAXIMO = 4; 

    public Personagem(String nome, int pontosVida, int ataque, int defesa, int nivelInicial) {
        this.nome = nome;
        this.pontosVidaMax = pontosVida;
        this.pontosVida = pontosVida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = nivelInicial;
        this.inventario = new Inventario();
        
        this.experiencia = 0;
        this.experienciaProximoNivel = calcularProximoNivelXP(nivelInicial);
    }

    // Nível inicial padrão agora é 0
    public Personagem() { this("SemNome", 20, 5, 3, 1); } 

    public Personagem(Personagem outro) {
        this.nome = outro.nome;
        this.pontosVidaMax = outro.pontosVidaMax;
        this.pontosVida = outro.pontosVida;
        this.ataque = outro.ataque;
        this.defesa = outro.defesa;
        this.nivel = outro.nivel;
        this.inventario = outro.inventario.clone();
        
        this.experiencia = outro.experiencia;
        this.experienciaProximoNivel = outro.experienciaProximoNivel;
    }

    // Getters
    public String getNome() { return nome; }
    public int getPontosVida() { return pontosVida; }
    public int getAtaque() { return ataque; }
    public int getDefesa() { return defesa; }
    public int getNivel() { return nivel; }
    public Inventario getInventario() { return inventario; }
    public int getPontosVidaMax() { return pontosVidaMax; }
    public int getExperiencia() { return experiencia; }
    public int getExperienciaProximoNivel() { return experienciaProximoNivel; }

    //  Métodos de Combate 
    public boolean estaVivo() { return pontosVida > 0; }
    public void receberDano(int d) { this.pontosVida -= Math.max(0, d); if (this.pontosVida < 0) this.pontosVida = 0; }
    public void curar(int q) { 
        this.pontosVida += q; 
        if (this.pontosVida > this.pontosVidaMax) { 
        this.pontosVida = this.pontosVidaMax;
        }
    }
    public int rolarDado() { return rng.nextInt(20) + 1; }

    // MÉTODOS DE NÍVEL

    private int calcularProximoNivelXP(int nivelAtual) {
        if (nivelAtual >= NIVEL_MAXIMO) return 0;
        return 100 * (int) Math.pow(2, nivelAtual);
    }

    public void ganharExperiencia(int xpGanha) {
        // Se já estiver no nível máximo, não ganha mais XP.
        if (this.nivel >= NIVEL_MAXIMO) {
            this.experiencia = 0;
            return; 
        }

        if (xpGanha <= 0) return;
        
        this.experiencia += xpGanha;
        System.out.printf("%s ganhou %d de XP. (Total: %d/%d)\n", 
            this.nome, xpGanha, this.experiencia, this.experienciaProximoNivel);
        
        verificarLevelUp();
    }

    private void verificarLevelUp() {
        // Verifica se pode subir de nível (NÃO pode se já estiver no max)
        while (this.nivel < NIVEL_MAXIMO && this.experiencia >= this.experienciaProximoNivel) {
            
            int excessoXP = this.experiencia - this.experienciaProximoNivel;
            
            subirNivel(); // Sobe de nível e aplica os bônus
            
            this.experiencia = excessoXP; // Atualiza o XP atual

            // Se atingiu o nível máximo
            if (this.nivel == NIVEL_MAXIMO) {
                System.out.println("****************************************");
                System.out.printf("  NÍVEL MÁXIMO ATINGIDO (Nível %d)! \n", NIVEL_MAXIMO);
                System.out.println("  Você sente uma presença sombria se aproximando...");
                System.out.println("****************************************");
                this.experiencia = 0;
                this.experienciaProximoNivel = 0; // Não há mais "próximo nível"
            } else {
                // Se não for max, calcula o próximo
                this.experienciaProximoNivel = calcularProximoNivelXP(this.nivel);
            }
        }
    }

    protected void subirNivel() {
        this.nivel++;
        System.out.println("----------------------------------------");
        System.out.printf("  LEVEL UP! %s alcançou o Nível %d! \n", this.nome, this.nivel);
        System.out.println("----------------------------------------");

        // Aumenta os status (exemplo de valores)
        int aumentoHP = 5;
        int aumentoAtaque = 2;
        int aumentoDefesa = 1;

        this.pontosVidaMax += aumentoHP;
        this.ataque += aumentoAtaque;
        this.defesa += aumentoDefesa;
        
        this.pontosVida = this.pontosVidaMax; // Cura total

        System.out.printf("HP Máximo: %d (+%d)\n", this.pontosVidaMax, aumentoHP);
        System.out.printf("Ataque: %d (+%d)\n", this.ataque, aumentoAtaque);
        System.out.printf("Defesa: %d (+%d)\n", this.defesa, aumentoDefesa);
        System.out.println("HP totalmente restaurado!");
    }
    
    // método de saque (usado ao vencer inimigo)
    protected void saquear(Personagem inimigo) {
        System.out.println("Saqueando o inimigo...");
        Inventario loot = inimigo.getInventario().clone();

        boolean obteveAlgo = false;

        for (Item it : loot.listarOrdenado()) {
            // só permite saquear certos itens ou chance aleatória de não pegar nada
            if (it.getNome().equals("Soro de Hawkins") || it.getNome().equals("Molotov Caseiro") || it.getNome().equals("Walkie-Talkie")) {
                int q = it.getQuantidade();
                int transferir = Math.max(1, q / 2); // metade da quantidade
                this.inventario.adicionar(new Item(it.getNome(), it.getDescricao(), it.getEfeito(), transferir));
                System.out.printf("Você obteve %d x %s\n", transferir, it.getNome());
                obteveAlgo = true;
            }
        }

        if (!obteveAlgo) {
            System.out.println("O inimigo não tinha nada útil para saquear.");
        }
    }

   
    @Override
    public Personagem clone() {
        try {
            Personagem copia = (Personagem) super.clone();
            copia.inventario = this.inventario.clone(); // clona o inventário também
            return copia;
        } catch (CloneNotSupportedException e) {
            return null; 
        }
    }


    @Override
    public String toString() {
        // Atualiza o toString para mostrar "MAX"
        if (nivel == NIVEL_MAXIMO) {
             return String.format("%s (Nível %d MAX) HP=%d/%d ATK=%d DEF=%d", 
                nome, nivel, pontosVida, pontosVidaMax, ataque, defesa);
        }
        return String.format("%s (Nível %d | XP: %d/%d) HP=%d/%d ATK=%d DEF=%d", 
            nome, nivel, experiencia, experienciaProximoNivel, pontosVida, pontosVidaMax, ataque, defesa);
    }
}