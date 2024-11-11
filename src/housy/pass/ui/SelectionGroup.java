package housy.pass.ui;

import java.util.*;

public class SelectionGroup {

    public List<Selectable> items = new ArrayList<>();

    public boolean add(Selectable e) {
        return items.add(e);
    }

    public boolean remove(Selectable o) {
        return items.remove(o);
    }

    public void clearSelections() {
        items.forEach(x -> x.setSelected(false));
    }

    public void removeAll() {
        items.clear();
    }
}
