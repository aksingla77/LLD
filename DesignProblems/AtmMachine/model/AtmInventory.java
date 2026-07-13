package DesignProblems.AtmMachine.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AtmInventory {
    public HashMap<Denomination, Integer> inventory;

    public AtmInventory(){this.inventory = new HashMap<>();}

    public void insertNotes(Denomination denomination, int count){
        inventory.put(denomination, inventory.getOrDefault(denomination, 0) + count);
    }

    public int getNoteCount(Denomination denomination){
        return inventory.getOrDefault(denomination, 0);
    }

    public boolean canDispense(int amount){
        return computePlan(amount) != null;
    }

    public Map<Denomination, Integer> dispenseNotes(int amount) throws Exception {
        Map<Denomination, Integer> plan = computePlan(amount);
        if(plan == null) throw new Exception("Cannot dispense amount: " + amount);
        for(Map.Entry<Denomination, Integer> e : plan.entrySet()){
            inventory.put(e.getKey(), inventory.get(e.getKey()) - e.getValue());
        }
        return plan;
    }

    private Map<Denomination, Integer> computePlan(int amount){
        if(amount <= 0) return null;

        Denomination[] denoms = Denomination.values();
        Arrays.sort(denoms, (a, b) -> b.getValue() - a.getValue());

        Map<Denomination, Integer> plan = new LinkedHashMap<>();
        int remaining = amount;
        for(Denomination d : denoms){
            if(remaining <= 0) break;
            int take = Math.min(remaining / d.getValue(), inventory.getOrDefault(d, 0));
            if(take > 0){
                plan.put(d, take);
                remaining -= take * d.getValue();
            }
        }
        return remaining == 0 ? plan : null;
    }
}
