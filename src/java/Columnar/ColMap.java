package java.Columnar;

import java.util.Map;

public interface ColMap<K, V>
{
    /**
     * override raw map basic functions
     */
    int size();
    boolean isEmpty();
    boolean containsKey(Object key);
    boolean containsValue(Object value);
    V get(Object key);
    V put(K key, V value);
    V remove(Object key);
    void putAll(Map<? extends K, ? extends V> m);
    void clear();
}