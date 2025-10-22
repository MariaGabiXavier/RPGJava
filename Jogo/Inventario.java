import java.util.*;
import java.util.stream.Collectors;

public class Inventario implements Cloneable {
    private Map<String, Item> itens;

    public Inventario() {
        this.itens = new HashMap<>();
    }

    // Construtor de cópia (deep copy)
    public Inventario(Inventario outro) {
        this.itens = new HashMap<>();
        for (Item it : outro.itens.values()) {
            this.itens.put(it.getNome().toLowerCase(), it.clone());
        }
    }

    public void adicionar(Item item) {
        String chave = item.getNome().toLowerCase();
        if (itens.containsKey(chave)) {
            itens.get(chave).incrementar(item.getQuantidade());
        } else {
            itens.put(chave, item.clone());
        }
    }

    public boolean remover(String nome, int quantidade) {
        String chave = nome.toLowerCase();
        if (!itens.containsKey(chave)) return false;
        Item it = itens.get(chave);
        if (it.getQuantidade() < quantidade) return false;
        it.decrementar(quantidade);
        if (it.getQuantidade() <= 0) itens.remove(chave);
        return true;
    }

    public Item pegarItem(String nome) {
        Item it = itens.get(nome.toLowerCase());
        return it == null ? null : it;
    }

    public List<Item> listarOrdenado() {
        return itens.values().stream()
                .sorted()
                .map(Item::clone)
                .collect(Collectors.toList());
    }

    public boolean contem(String nome) {
        return itens.containsKey(nome.toLowerCase());
    }

    public Item retirarUmaUnidade(String nome) {
        Item it = itens.get(nome.toLowerCase());
        if (it == null || it.getQuantidade() <= 0) return null;
        Item copia = new Item(it.getNome(), it.getDescricao(), it.getEfeito(), 1);
        remover(nome, 1);
        return copia;
    }

    @Override
    public Inventario clone() {
        Inventario copia = new Inventario();
        for (Item item : this.itens.values()) {
            copia.adicionar(item.clone()); // usa o clone de Item
        }
        return copia;
    }


    @Override
    public String toString() {
        List<Item> list = listarOrdenado();
        if (list.isEmpty()) return "<vazio>";
        StringBuilder sb = new StringBuilder();
        for (Item i : list) sb.append(i).append("\n");
        return sb.toString();
    }
}
