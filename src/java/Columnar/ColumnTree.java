package java.Columnar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *                         Root
 *                          ↓
 *  ====================================================
 *          |                               |   
 *  [Node|Node|Node|...]          [Node|Node|Node|...]
 *    |     |   |    |              |    |    |    |
 *  ====================================================
 *          |                               |
 *       Parents                         Parents        
 * |===================|          |===================|
 * |      Parent       |          |      Parent       | 
 * | ------------------|          | ------------------| 
 * |   |        |      |          |   |        |      |
 * |  Left     right   |          |  Left     right   |
 * |   |        |      |          |   |        |      |
 * |  key      value   |          |  key      value   |
 * |===================|          |===================|
 *      
 */
public class ColumnTree
{
    private static final String ROOT = "ROOT"; 

    private List<Node> nodes;
    private Iterator<Node> iter;

    public ColumnTree(Object parent, Object key, Object value) {
        /**
         * Build Default Root in Column Trees
         */
        nodes = new ArrayList<>();
        KV rootKv = new KV("left", "right");
        Node rootNode = new Node(ROOT, rootKv);
        nodes.add(rootNode);
    }

    /**
     * addtion new cloumn data(s)
     */
    public void add(Object parent, Object key, Object value) {
        KV newKV = new KV(key, value);
        Node newNode = new Node(parent, newKV);
        nodes.add(newNode);
    }

    /**
     * delete cloumn data, params(Column Name/Mapping Key)
     */
    public void delete(Object parent, Object key) {
        List<KV> newList = getParentListKV(parent);
        if (newList == null)
            return;

        Iterator<KV> iter = newList.iterator();
        KV newKV;

        while (iter.hasNext()) {
            newKV = iter.next();
            if (newKV.getLeft() == key) {
                newList.remove(newKV);
            }
        }
    }

    /**
     * get cloumn parent mapping data
     */
    public List<KV> getParentListKV(Object parent) {
        iter = nodes.iterator();
        Node iterNode;

        while (iter.hasNext()) {
            iterNode = iter.next();
            if (iterNode.getParent() == parent)
                return iterNode.getKVList();
        }

        return null;
    }

    /**
     * get cloumn parent mapping value form mapping key
     */
    public Object getValue(Object parent, Object key) {
        List<KV> newList = getParentListKV(parent);
        if (newList == null)
            return null;

        Iterator<KV> iter = newList.iterator();
        KV newKV;

        while (iter.hasNext()) {
            newKV = iter.next();
            if (newKV.getLeft() == key)
                return newKV.getRight();
        }

        return null;
    }

    /**
     * compare cloumn parent
     */
    public boolean contains(Object parent) {
        iter = nodes.iterator();
        while (iter.hasNext()) {
            if (iter.next().getParent() == parent)
                return true;
        }

        return false;
    }

    /**
     * get cloumn iterator
     */
    public Iterator<Node> getNodeIterator() {
        return iter;
    }

    /**
     * size of from Cloumn in list(s)
     */
    public int size() {
        return nodes.size();
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
    private class Node
    {
        Object parent;
        List<KV> kv;

        public Node(Object parent, KV kv) {
            this.parent = parent;
            this.kv = new ArrayList<>();
            this.kv.add(kv);
        }

        public Object getParent() {
            return parent;
        }

        public List<KV> getKVList() {
            return kv;
        }
    }

    /**
     *        Mapping
     *           ↓
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
    public class KV
    {
        Object left;
        Object right;

        public KV(Object left, Object right) {
            this.left = left;
            this.right = right;
        }

        public Object getLeft() {
            return left;
        }

        public Object getRight() {
            return right;
        }
    }
}