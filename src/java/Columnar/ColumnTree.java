package java.Columnar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ColumnTree 
{
    private static final String ROOT = "ROOT";

    private Node rootNode;
    private int currentIndex = 0;

    private static Map<Object, Object> kvMaps = new IdentityHashMap<>();
    private static final Map<Object, KV> kvNodes = new ConcurrentHashMap<>();

    public ColumnTree() { 
        /**
         * Build Default Root in Column Trees
         */
        KV rootKv = new KV(ROOT, "left", "right");
        rootNode = new Node(ROOT, rootKv);

        kvMaps.put("left", "right");
        kvNodes.put(rootKv, rootKv);
    }

    /**
     * addtion new cloumn data(s)
     */
    public void add(Object parent, Object key, Object value) {
        kvMaps.put(key, value);
        KV newKV = new KV(parent, key, value);

        kvNodes.put(parent, newKV);

        currentIndex++;
    }

    /**
     * delete cloumn data, params(Column Name/Mapping Key)
     */
    public void delete(Object parent, Object key) {
        kvMaps.remove(key);
        kvNodes.remove(parent);

        --currentIndex;
    }

    /**
     * get cloumn parent mapping data
     */
    public List<KV> getParentListKV(Object parent) {
        List<KV> newList = new ArrayList<>();

        Set<Entry<Object, KV>> entry = kvNodes.entrySet();
        entry.parallelStream().forEach(action -> {
            if (action.getKey().equals(parent))
                newList.add(action.getValue());
        });

        return newList;
    }

    /**
     * get cloumn parent mapping value form mapping key
     */
    public Object getValueWithParentAndParent(Object parent, Object key) {
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
        return kvNodes.values().parallelStream().filter(
            predicate -> predicate.getLeft().equals(key))
                .findFirst().map(
                    mapper -> mapper.getParent()
                )
                .orElse(null);
    }

    public Object getParentWithValue(Object value) {
        return kvNodes.values().parallelStream().filter(
            predicate -> predicate.getRight().equals(value))
                .findFirst().map(
                    mapper -> mapper.getParent()
                )
                .orElse(null);
    }

    public List<Object> getParent() {
        return new ArrayList<>(kvNodes.keySet());
    }

    public boolean contains(Object parent) {
        return kvNodes.containsKey(parent);
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
    private static class Node
    {
        Object parent;

        public Node(Object parent, KV kv) {
            this.parent = parent;
            kvNodes.put(parent, kv);
        }

        public final Object getParent() {
            return parent;
        }

        public final KV getParentKV(final Object parent) {
            return kvNodes.get(parent);
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
    public class KV implements Serializable
    {
        /**
         *
         */
        private static final long serialVersionUID = -2956614816449059957L;

        Object left;
        Object right;
        Object parent;

        public KV(final Object parent, final Object left, final Object right) {
            this.left = left;
            this.right = right;
            this.parent = parent;

            kvMaps.put(left, right);
        }

        public final Object getLeft() {
            return left;
        }

        public final Object getRight() {
            return right;
        }

        public final Object getParent() {
            return parent;
        }
    }
}