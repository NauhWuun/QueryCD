package java.Columnar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ColumnTree 
{
    private static final String ROOT = "ROOT";

    private Node rootNode;
    private int currentIndex = 0;

    private static Map<Object, KV> kvMaps = new HashMap<>();
    private static final List<Node> kvNodes = new ArrayList<>();

    public ColumnTree() { 
        /**
         * Build Default Root in Column Trees
         */
        KV rootKv = new KV(ROOT, "left", "right");
        rootNode = new Node(rootKv);

        kvNodes.add(rootNode);
    }

    /**
     * addtion new cloumn data(s)
     */
    public void put(Object parent, Object key, Object value) {
        boolean hasKey = kvMaps.keySet().stream().allMatch(action -> action.equals(parent));

        KV newKV = new KV(parent, key, value);

        if (hasKey) {    
            kvMaps.put(parent, newKV);
        } else {
            kvNodes.add(new Node(newKV));
        }

        currentIndex++;
    }

    /**
     * delete cloumn data, params(Column Name/Mapping Key)
     */
    public void delete(Object parent) {
        kvMaps.remove(parent);

        --currentIndex;
    }

    /**
     * get cloumn parent mapping data
     */
    public List<KV> getParentListKV(Object parent) {
        List<KV> newList = new ArrayList<>();

        Set<Entry<Object, KV>> entry = kvMaps.entrySet();
        entry.parallelStream().forEach(action -> {
            if (action.getKey().equals(parent))
                newList.add(action.getValue());
        });

        return newList;
    }

    /**
     * get cloumn parent mapping value form mapping key
     */
    public Object getValueWithParentAndKey(Object parent, Object key) {
        return getParentListKV(parent).parallelStream().filter(
            action -> action.getLeft().equals(key))
                .findFirst().map(
                    action -> action.getRight()
                )
                .orElse(null);
    }

    public Object getValueWithKey(Object key) {
        return kvMaps.get(key);
    }

    public List<Object> getValues() {
        return new ArrayList<>(kvMaps.values());
    }

    public Object getParentWithKey(Object key) {
        return kvMaps.values().parallelStream().filter(
            predicate -> predicate.getLeft().equals(key))
                .findFirst().map(
                    mapper -> mapper.getParent()
                )
                .orElse(null);
    }

    public Object getParentWithValue(Object value) {
        return kvMaps.values().parallelStream().filter(
            predicate -> predicate.getRight().equals(value))
                .findFirst().map(
                    mapper -> mapper.getParent()
                )
                .orElse(null);
    }

    public List<Object> getParent() {
        return new ArrayList<>(kvMaps.keySet());
    }

    public boolean contains(Object parent) {
        return kvMaps.containsKey(parent);
    }

    public Object previous() {
        return getParent().listIterator().previous();
    }

    public Object first() {
        return rootNode;
    }

    public Object next() {
        return getParent().listIterator().next();
    }

    public Object last() {
        return getParent().get(currentIndex++);
    }

    /**
     * Override This
     */
    public void toDisk() {}
    public void loadIn() {}

    /**
     * size of from Cloumn in list(s)
     */
    public int size() {
        return kvNodes.size();
    }

    /**         
     *        Basic-Trees
     *             ↓
     * |==========================|
     * |         parent           |
     * |           |              |
     * | ----------------------   |   
     * |   |               |      |
     * |  Left           right    |
     * |   |               |      |
     * |  key            value    |
     * |==========================|           
     */
    class Node
    {
        KV kv;

        private Node(final KV kv) {
            this.kv = kv;
        }

        public final KV getKV() {
            return kv;
        }
    }

    /**
     *        Mapping
     *           ↓
     *         Parent
     *    ↓             ↓
     * =========================
     *   Name   <=>   Result
     *    |             |
     *   Key    <=>   value
     *    |             |
     *  [" "]   <=>   [" "]
     *    |             |
     *  [index] <=>  [index]
     * =========================
     */
    class KV
    {
        Object parent;
        Object left;
        Object right;

        public KV(final Object parent, final Object left, final Object right) {
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public final Object getLeft() {
            return left;
        }

        public final Object getRight() {
            return right;
        }

        public Object getParent() {
            return parent;
        }
    }
}
