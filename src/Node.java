public abstract class Node {
    protected Catalog parent;
    protected String name;

    public Node(Catalog parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    public Catalog getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }
    public void setParent(Catalog parent) {
        this.parent = parent;
    }
    public void setName(String name) {
        this.name = name;
    }
}
