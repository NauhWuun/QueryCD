package java.Columnar;

class Row
{
    private Object left, right;
    private String name;

    public Row(String name, Object left, Object right) {
        this.left = left;
        this.right = right;
        this.name = name;
    }

    public Object getLeft() {
        return left;
    }

    public Object getRight() {
        return right;
    }

    public final String getName() {
        return this.name;
    }
}