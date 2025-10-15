import java.util.*;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private Personagem jogador;
    private Random rng = new Random();

    public static void main(String[] args) {
        new JogoStrangerThings().iniciar();
    }

    private void iniciar() {
        System.out.println("=== Stranger Things: Aventuras em Hawkins ===");
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
        System.out.println("Você é: " + jogador);
    }

    private void prepararInventarioInicial() {
        jogador.getInventario().adicionar(new Item("Soro de Hawkins", "Restaura 25 HP", "cura25", 2));
        jogador.getInventario().adicionar(new Item("Walkie-Talkie", "Comunicação útil", "comunicacao", 1));
        jogador.getInventario().adicionar(new Item("Molotov Caseiro", "Ataque potente (efeito atk+3)", "atk+3", 2));
    }

    private void narrativaInicial() {
        System.out.println("\nHawkins está estranho. Relatos de criaturas do Mundo Invertido e desaparecimentos aumentam.");
        System.out.println("Sua missão: investigar, proteger os amigos e, se possível, fechar a fenda que liga os mundos.");
        System.out.println("Siga as pistas, tome decisões - e prepare-se para enfrentar o vilão final.\n");
    }

    private void loopPrincipal() {
        boolean rodando = true;
        boolean bossDerrotado = false;

        while (rodando && jogador.estaVivo()) {
            System.out.println("\n--- O que deseja fazer? ---");
            System.out.println("1) Explorar Hawkins  2) Usar item  3) Ver status/inventário  4) Salvar ponto (clone)  0) Sair");
            int opc = lerIntEntre(0, 4);
            switch (opc) {
                case 1:
                    int resultado = explorar();
                    if (resultado == 2) {
                        // encontrou pista importante; chance de acionar encontro com boss final no futuro
                        System.out.println("Pista coletada. Você se aproxima da fenda...");
                    } else if (resultado == 3) {
                        // evento que pode levar ao encontro com o boss
                        System.out.println("Algo muito errado — você sente uma presença...");
                        if (rng.nextInt(100) < 40) {
                            System.out.println("A presença se materializa: é o Mind Flayer!");
                            Inimigo boss = gerarBossFinal();
                            boolean v = combateInterativo(boss);
                            if (v) {
                                System.out.println("\nVocê derrotou o Mind Flayer! A fenda começa a se fechar.");
                                System.out.println("Parabéns — você salvou Hawkins!");
                                bossDerrotado = true;
                                rodando = false;
                            } else {
                                rodando = false;
                            }
                        }
                    }
                    break;
                case 2: usarItem(); break;
                case 3: mostrarStatus(); break;
                case 4: salvarPonto(); break;
                case 0: rodando = false; break;
            }
            if (!jogador.estaVivo()) {
                System.out.println("Você foi derrotado. A ameaça permanece em Hawkins...");
            }
        }

        if (jogador.estaVivo() && bossDerrotado) {
            System.out.println("\n=== FIM: Victory ===");
            System.out.println("Hawkins está salvo (por enquanto). Obrigado por jogar!");
        } else if (!jogador.estaVivo()) {
            System.out.println("\n=== FIM: Derrota ===");
            System.out.println("O Mundo Invertido venceu desta vez...");
        } else {
            System.out.println("\nSaindo do jogo. Até a próxima.");
        }
    }

    // retornar: 0 = nada especial, 2 = pista, 3 = presença forte
    private int explorar() {
        System.out.println("\nVocê explora uma área de Hawkins...");
        int evento = rng.nextInt(100);
        if (evento < 50) {
            Inimigo inimigo = gerarInimigoAleatorio();
            System.out.printf("Você encontrou um %s!\n", inimigo.getNome());
            System.out.println("Deseja: 1) Lutar  2) Tentar fugir  3) Observar com cuidado");
            int escolha = lerIntEntre(1, 3);
            if (escolha == 2) {
                if (tentarFugir(inimigo)) return 0;
                else {
                    boolean v = combateInterativo(inimigo);
                    return v ? 0 : 0;
                }
            } else if (escolha == 3) {
                // chance de evitar ou de conseguir pista
                if (rng.nextInt(100) < 50) {
                    System.out.println("Você observou de longe e evita o combate. Ganha uma pista sobre a fenda.");
                    return 2;
                } else {
                    System.out.println("Enquanto observa, o inimigo percebe você e ataca!");
                    boolean v = combateInterativo(inimigo);
                    return v ? 0 : 0;
                }
            } else {
                boolean v = combateInterativo(inimigo);
                return v ? 0 : 0;
            }
        } else if (evento < 75) {
            System.out.println("Você encontrou uma pista importante sobre a fenda dimensional.");
            return 2;
        } else if (evento < 90) {
            System.out.println("Uma sensação estranha surge; algo grande se aproxima...");
            return 3;
        } else {
            Item achado = new Item("Soro de Hawkins", "Restaura 25 HP", "cura25", 1);
            System.out.printf("Você encontrou %s x%d!\n", achado.getNome(), achado.getQuantidade());
            jogador.getInventario().adicionar(achado);
            return 0;
        }
    }

    private Inimigo gerarInimigoAleatorio() {
        int t = rng.nextInt(3);
        Inimigo ini;
        if (t == 0) {
            ini = new Inimigo("Demogorgon", 22, 7, 4, 1);
            ini.getInventario().adicionar(new Item("Pele do Demogorgon", "Material estranho", "material", 1));
        } else if (t == 1) {
            ini = new Inimigo("Voz do Invertido", 18, 6, 3, 1);
            ini.getInventario().adicionar(new Item("Fragmento Sombrio", "Residuo do Mundo Invertido", "material", 1));
        } else {
            ini = new Inimigo("Inseto Dimensional", 16, 5, 2, 1);
            ini.getInventario().adicionar(new Item("Fio Escuro", "Estranho", "material", rng.nextInt(2)+1));
        }
        return ini;
    }

    private Inimigo gerarBossFinal() {
        Inimigo boss = new Inimigo("Mind Flayer", 60, 12, 6, 5);
        boss.getInventario().adicionar(new Item("Nó do Mundo Invertido", "Fonte de poder", "boss", 1));
        return boss;
    }

    // combate interativo: jogador escolhe ações a cada turno (atacar, usar item, tentar fugir)
    private boolean combateInterativo(Inimigo inimigo) {
        System.out.println("\n--- Batalha: " + jogador.getNome() + " vs " + inimigo.getNome() + " ---");
        boolean combateAtivo = true;
        while (combateAtivo && jogador.estaVivo() && inimigo.estaVivo()) {
            System.out.printf("\nStatus: %s HP=%d | %s HP=%d\n", jogador.getNome(), jogador.getPontosVida(), inimigo.getNome(), inimigo.getPontosVida());
            System.out.println("Ações: 1) Atacar  2) Usar item  3) Tentar fugir");
            int acao = lerIntEntre(1, 3);
            if (acao == 1) {
                // jogador ataca
                int rollJog = jogador.rolarDado();
                int totalAtk = jogador.getAtaque() + rollJog;
                System.out.printf("%s rola %d (ATAQUE total %d) vs DEF %d\n", jogador.getNome(), rollJog, totalAtk, inimigo.getDefesa());
                if (totalAtk > inimigo.getDefesa()) {
                    int dano = Math.max(1, totalAtk - inimigo.getDefesa());
                    System.out.printf("Acertou e deu %d de dano!\n", dano);
                    inimigo.receberDano(dano);
                } else {
                    System.out.println("Ataque falhou.");
                }
            } else if (acao == 2) {
                usarItemEmCombate();
            } else {
                if (tentarFugir(inimigo)) return true;
            }

            // se inimigo morreu, saque e encerra
            if (!inimigo.estaVivo()) {
                System.out.println("Inimigo derrotado!");
                jogador.saquear(inimigo);
                return true;
            }

            // turno do inimigo
            int rollIni = inimigo.rolarDado();
            int totalAtkIni = inimigo.getAtaque() + rollIni;
            System.out.printf("%s rola %d (ATAQUE total %d) vs DEF %d\n", inimigo.getNome(), rollIni, totalAtkIni, jogador.getDefesa());
            if (totalAtkIni > jogador.getDefesa()) {
                int dano = Math.max(1, totalAtkIni - jogador.getDefesa());
                System.out.printf("%s acerta e causa %d de dano.\n", inimigo.getNome(), dano);
                jogador.receberDano(dano);
            } else {
                System.out.printf("%s erra o ataque.\n", inimigo.getNome());
            }
        }

        return jogador.estaVivo() && !inimigo.estaVivo();
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
                // efeito instantâneo: aplica um ataque forte agora
                int roll = jogador.rolarDado();
                int total = jogador.getAtaque() + 3 + roll;
                System.out.printf("Você usou um ataque especial! rola %d -> ataque total %d\n", roll, total);
                // para aplicar dano no inimigo precisamos de referência; simplificação: anuncio e trata no próximo ataque normal
                // como estamos em combate interativo, vamos simular um ataque adicional breve:
                System.out.println("Você consegue um ataque extra potente (efeito aplicado)! (o dano será aplicado no próximo turno de combate)");
                // para simplicidade, aqui apenas um efeito narrativo; se quiser, podemos ligar isso a um estado temporário
                break;
            default:
                jogador.getInventario().remover(nome, 1);
                System.out.println("Usou " + it.getNome() + ".");
                break;
        }
    }

    private boolean tentarFugir(Inimigo inimigo) {
        System.out.println("Tentando fugir...");
        int roll = rng.nextInt(20) + 1;
        int chance = roll + jogador.getNivel();
        System.out.printf("Rolagem: %d + nível %d = %d\n", roll, jogador.getNivel(), chance);
        if (chance > 12) {
            System.out.println("Fuga bem sucedida!");
            return true;
        } else {
            System.out.println("Falha! O inimigo ataca enquanto você tenta escapar.");
            int rollIni = inimigo.rolarDado();
            int valorAtaqueIni = inimigo.getAtaque() + rollIni;
            if (valorAtaqueIni > jogador.getDefesa()) {
                int dano = Math.max(1, valorAtaqueIni - jogador.getDefesa());
                jogador.receberDano(dano);
                System.out.printf("Você recebeu %d de dano. HP=%d\n", dano, jogador.getPontosVida());
            } else {
                System.out.println("O inimigo errou o ataque de oportunidade.");
            }
            return false;
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
        for (Item it : itens) {
            System.out.printf("%d) %s\n", idx++, it);
        }
        System.out.println("Escolha o item pelo número (0 para cancelar):");
        int escolha = lerIntEntre(0, itens.size());
        if (escolha == 0) return;
        Item selecionado = itens.get(escolha - 1);
        aplicarEfeitoItem(selecionado.getNome());
        System.out.printf("HP atual: %d\n", jogador.getPontosVida());
    }

    private void mostrarStatus() {
        System.out.println("\n--- Status do Jogador ---");
        System.out.println(jogador);
        System.out.println("Inventário:");
        System.out.println(jogador.getInventario());
    }

    private void salvarPonto() {
        Personagem backup = jogador.clone();
        System.out.println("Ponto de salvamento criado (clone do personagem).");
        System.out.println("Clone: " + backup);
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
