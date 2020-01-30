package java.Columnar;

class kvTable<K, V>
{
    private K key;
    private V value;
    private int index = 0;

    public kvTable(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public int getIndex() {
        return index;
    }
}