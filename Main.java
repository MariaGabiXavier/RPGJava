import java.util.*;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private Personagem jogador;
    private Personagem checkpoint; 
    private Random rng = new Random();

    public static void main(String[] args) {
        new Main().iniciar();
    }

    private void iniciar() {
        System.out.println("=== Stranger Things - Aventuras em Hawkins ===");
        criarPersonagem();
        prepararInventarioInicial();
        narrativaInicial();
        loopPrincipal();
    }

    private void criarPersonagem() {
        System.out.print("Escolha seu nome (ou deixe em branco para usar o nome do personagem): ");
        String nome = scanner.nextLine().trim();

        System.out.println("Escolha seu personagem:");
        System.out.println("1) Eleven (psíquica)  2) Mike (líder)  3) Dustin (esperto)");
        int escolha = lerIntEntre(1, 3);

        switch (escolha) {
            case 1:
                if (nome.isEmpty()) nome = "Eleven";
                jogador = new Eleven(nome);
                break;
            case 2:
                if (nome.isEmpty()) nome = "Mike";
                jogador = new Mike(nome);
                break;
            default:
                if (nome.isEmpty()) nome = "Dustin";
                jogador = new Dustin(nome);
                break;
        }

        System.out.println("Você é " + jogador);
    }

    private void prepararInventarioInicial() {
        jogador.getInventario().adicionar(new Item("Soro de Hawkins", "Restaura 25 HP", "cura25", 2));
        jogador.getInventario().adicionar(new Item("Walkie-Talkie", "Comunicação útil", "comunicacao", 1));
        jogador.getInventario().adicionar(new Item("Molotov Caseiro", "Ataque potente (efeito atk+3)", "atk+3", 2));
    }

    private void narrativaInicial() {
        System.out.println("\nHawkins está estranho. Relatos de criaturas do Mundo Invertido e desaparecimentos aumentam.");
        System.out.println("Sua missão é investigar, proteger os amigos e, se possível, fechar a fenda que liga os mundos.");
        System.out.println("Siga as pistas, tome decisões, e prepare-se para enfrentar o vilão final.\n");
    }

    private void loopPrincipal() {
        boolean rodando = true;
        boolean bossDerrotado = false;

        while (rodando && jogador.estaVivo()) {

            // Boss final
            if (jogador.getNivel() >= Personagem.NIVEL_MAXIMO && !bossDerrotado) {
                System.out.println("\n***************************************************");
                System.out.println("VOCÊ ATINGIU O NÍVEL MÁXIMO!");
                System.out.println("A fenda se abre. A presença que você sentia se materializa...");
                System.out.println("É O MIND FLAYER! A BATALHA FINAL COMEÇA!");
                System.out.println("***************************************************");
                
                Inimigo boss = gerarBossFinal();
                boolean vitoriaBoss = combateInterativo(boss);
                
                if (vitoriaBoss) {
                    bossDerrotado = true;
                    rodando = false;
                } else {
                    jogadorMorreu();
                    if (!jogador.estaVivo()) rodando = false;
                }
                continue;
            }

            System.out.println("\n--- O que deseja fazer? ---");
            System.out.println("1) Explorar Hawkins  2) Usar item  3) Ver status/inventário  4) Salvar ponto (clone)  0) Sair");
            int opc = lerIntEntre(0, 4);
            switch (opc) {
                case 1:
                    int resultado = explorar();
                    if (resultado == 2) System.out.println("Pista coletada. Você se aproxima da fenda...");
                    break;
                case 2: usarItem(); break;
                case 3: mostrarStatus(); break;
                case 4: salvarPonto(); break;
                case 0: rodando = false; break;
            }

            if (!jogador.estaVivo()) {
                jogadorMorreu();
            }
        }

        // Fim de jogo
        if (jogador.estaVivo() && bossDerrotado) {
            System.out.println("\n=== FIM - Victory ===");
            System.out.println("Hawkins está salvo (por enquanto). Obrigado por jogar!");
        } else if (!jogador.estaVivo()) {
            System.out.println("\n=== FIM - Derrota ===");
            System.out.println("O Mundo Invertido venceu desta vez...");
        } else {
            System.out.println("\nSaindo do jogo. Até a próxima.");
        }
    }

    // Quando o jogador morre
    private void jogadorMorreu() {
        System.out.println("Você foi derrotado...");
        if (checkpoint != null) {
            System.out.println("\nDeseja voltar ao último ponto salvo?");
            System.out.println("1) Sim  2) Não (sair do jogo)");
            int escolha = lerIntEntre(1, 2);
            if (escolha == 1) {
                jogador = checkpoint.clone();
                System.out.println("\nVocê voltou ao seu último ponto salvo!");
                System.out.println("Status restaurado: " + jogador);
            } else {
                jogador.receberDano(jogador.getPontosVida()); // força morte total
            }
        } else {
            System.out.println("Não há checkpoint disponível. Jogo encerrado.");
        }
    }

    //  SALVAR PONTO (CHECKPOINT)
    private void salvarPonto() {
        checkpoint = jogador.clone();
        System.out.println("\nPonto de salvamento criado! (Clone do personagem)");
        System.out.println("Checkpoint salvo: " + checkpoint.getNome() + " | Nível " + checkpoint.getNivel());
    }

    // EXPLORAR 
    private int explorar() {
        System.out.println("\nVocê explora uma área de Hawkins...");
        int evento = rng.nextInt(100);

        if (evento < 60) {
            Inimigo inimigo = gerarInimigoAleatorio();
            System.out.printf("Você encontrou um %s!\n", inimigo.getNome());
            System.out.println("Deseja: 1) Lutar  2) Tentar fugir  3) Observar com cuidado");
            int escolha = lerIntEntre(1, 3);
            if (escolha == 2) {
                if (tentarFugir(inimigo)) return 0;
                else combateInterativo(inimigo);
            } else if (escolha == 3) {
                if (rng.nextInt(100) < 50) {
                    System.out.println("Você observou e evitou o combate. Ganha uma pista sobre a fenda.");
                    return 2;
                } else {
                    System.out.println("Enquanto observa, o inimigo percebe você e ataca!");
                    combateInterativo(inimigo);
                }
            } else {
                combateInterativo(inimigo);
            }
        } else if (evento < 85) {
            System.out.println("Você encontrou uma pista importante sobre a fenda dimensional.");
            return 2;
        } else {
            Item achado = new Item("Soro de Hawkins", "Restaura 25 HP", "cura25", 1);
            System.out.printf("Você encontrou %s x%d!\n", achado.getNome(), achado.getQuantidade());
            jogador.getInventario().adicionar(achado);
        }
        return 0;
    }

   private Inimigo gerarInimigoAleatorio() {
        int t = rng.nextInt(3);
        Inimigo inimigo;

        if (t == 0) {
            inimigo = new Inimigo("Demogorgon", 22, 7, 4, 1, 75);
            inimigo.getInventario().adicionar(new Item("Soro de Hawkins", "Restaura 15 HP", "cura", 1));
        } 
        else if (t == 1) {
            inimigo = new Inimigo("Voz do Invertido", 18, 6, 3, 1, 60);
            inimigo.getInventario().adicionar(new Item("Molotov Caseiro", "Aumenta ataque temporariamente", "ataque", 1));
        } 
        else {
            inimigo = new Inimigo("Inseto Dimensional", 16, 5, 2, 1, 40);
            inimigo.getInventario().adicionar(new Item("Fragmento do Invertido", "Pequena fonte de energia", "defesa", 1));
        }

        return inimigo;
    }


    private Inimigo gerarBossFinal() {
        Inimigo boss = new Inimigo("Mind Flayer", 50, 10, 6, 5, 500);
        boss.getInventario().adicionar(new Item("Nó do Mundo Invertido", "Fonte de poder", "ataque", 2));
        boss.getInventario().adicionar(new Item("Elixir Psíquico", "Restaura toda a vida", "cura", 1));
        return boss;
    }


    private boolean combateInterativo(Inimigo inimigo) {
        System.out.println("\n--- Batalha: " + jogador.getNome() + " vs " + inimigo.getNome() + " ---");
        while (jogador.estaVivo() && inimigo.estaVivo()) {
            System.out.printf("\nStatus: %s HP=%d/%d | %s HP=%d/%d\n", 
                jogador.getNome(), jogador.getPontosVida(), jogador.getPontosVidaMax(), 
                inimigo.getNome(), inimigo.getPontosVida(), inimigo.getPontosVidaMax());

            System.out.println("Ações: 1) Atacar  2) Usar item  3) Tentar fugir");
            int acao = lerIntEntre(1, 3);
            if (acao == 1) atacar(inimigo);
            else if (acao == 2) usarItemEmCombate();
            else if (tentarFugir(inimigo)) return true;

            if (!inimigo.estaVivo()) {
                System.out.println("Inimigo derrotado!");
                jogador.ganharExperiencia(inimigo.getValorExperiencia());
                jogador.saquear(inimigo);
                return true;
            }

            if (inimigo.estaVivo()) inimigoAtaca(inimigo);
        }
        return jogador.estaVivo();
    }

    private void atacar(Inimigo inimigo) {
        int roll = jogador.rolarDado();
        int total = jogador.getAtaque() + roll;
        if (total > inimigo.getDefesa()) {
            int dano = total - inimigo.getDefesa();
            inimigo.receberDano(dano);
            System.out.printf("Você acertou e causou %d de dano!\n", dano);
        } else {
            System.out.println("O ataque falhou!");
        }
    }

    private void inimigoAtaca(Inimigo inimigo) {
        int roll = inimigo.rolarDado();
        int total = inimigo.getAtaque() + roll;
        if (total > jogador.getDefesa()) {
            int dano = total - jogador.getDefesa();
            jogador.receberDano(dano);
            System.out.printf("%s acertou e causou %d de dano!\n", inimigo.getNome(), dano);
        } else {
            System.out.println(inimigo.getNome() + " errou o ataque!");
        }
    }

    private void usarItemEmCombate() {
        List<Item> itens = jogador.getInventario().listarOrdenado();
        if (itens.isEmpty()) {
            System.out.println("Seu inventário está vazio.");
            return;
        }
        System.out.println("Escolha o item para usar (0 cancelar):");
        int idx = 1;
        for (Item it : itens) System.out.printf("%d) %s\n", idx++, it);
        int escolha = lerIntEntre(0, itens.size());
        if (escolha == 0) return;
        Item selecionado = itens.get(escolha - 1);
        aplicarEfeitoItem(selecionado.getNome());
    }

    private void aplicarEfeitoItem(String nome) {
        Item it = jogador.getInventario().pegarItem(nome);
        if (it == null) {
            System.out.println("Item não encontrado.");
            return;
        }
        switch (it.getEfeito()) {
            case "cura25":
                jogador.getInventario().remover(nome, 1);
                jogador.curar(25);
                System.out.println("Usou Soro de Hawkins. Recuperou 25 HP.");
                break;
            case "atk+3":
                jogador.getInventario().remover(nome, 1);
                System.out.println("Você usou um Molotov e se prepara para causar mais dano no próximo ataque!");
                break;
            default:
                jogador.getInventario().remover(nome, 1);
                System.out.println("Usou " + it.getNome() + ".");
                break;
        }
    }

    private void usarItem() {
        System.out.println("\nInventário:");
        List<Item> itens = jogador.getInventario().listarOrdenado();
        if (itens.isEmpty()) {
            System.out.println("Nada no inventário.");
            return;
        }
        int idx = 1;
        for (Item it : itens) System.out.printf("%d) %s\n", idx++, it);
        System.out.println("Escolha o item (0 para cancelar):");
        int escolha = lerIntEntre(0, itens.size());
        if (escolha == 0) return;
        Item selecionado = itens.get(escolha - 1);
        aplicarEfeitoItem(selecionado.getNome());
        System.out.printf("HP atual: %d/%d\n", jogador.getPontosVida(), jogador.getPontosVidaMax());
    }

    private void mostrarStatus() {
        System.out.println("\n--- Status do Jogador ---");
        System.out.println(jogador);
        System.out.println("Inventário:");
        System.out.println(jogador.getInventario());
    }

    private boolean tentarFugir(Inimigo inimigo) {
        if (inimigo.getNome().equals("Mind Flayer")) {
            System.out.println("Não há como fugir da batalha final!");
            return false;
        }

        System.out.println("Tentando fugir...");
        int roll = rng.nextInt(20) + 1;
        int chance = roll + jogador.getNivel();
        System.out.printf("Rolagem: %d + nível %d = %d\n", roll, jogador.getNivel(), chance);
        if (chance > 12) {
            System.out.println("Fuga bem sucedida!");
            return true;
        } else {
            System.out.println("Falha! O inimigo ataca enquanto você tenta escapar.");
            inimigoAtaca(inimigo);
            return false;
        }
    }

    private int lerIntEntre(int min, int max) {
        while (true) {
            try {
                System.out.print("> ");
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val < min || val > max) {
                    System.out.printf("Digite um número entre %d e %d\n", min, max);
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Tente novamente.");
            }
        }
    }
}
