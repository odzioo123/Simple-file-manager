import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Catalog extends Node {
    private List<Node> children;

    public Catalog(Catalog parent, String name, List<Node> children) {
        super(parent, name);
        if (children == null)
        {
            this.children = new ArrayList<Node>();
        }
        else
        {
            this.children = children;
        }
    }

    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node node)
    {
        int error = 0;
        for(Node kid : children)
        {
            if(kid.getName().equals(node.getName()))
            {
                error = 1;
                System.out.println("This name already exists");
                break;
            }
        }
        if(error == 0)
        {
            children.add(node);
        }
    }

    public void removeChild(String name)
    {
        for(Node node : children)
        {
            if(node.getName().equals(name))
            {
                children.remove(node);
                break;
            }
        }
    }

    public void removeChildren()
    {
        Iterator<Node> iterator = children.iterator();
        while (iterator.hasNext())
        {
            Node node = iterator.next();
            node.setParent(null);
            iterator.remove();
        }
    }

    public void ls()
    {
        for(Node node : children)
        {
            if(node instanceof Catalog)
            {
                System.out.println("\u001B[33m" + node.getName() + "\u001B[0m");
            }
            else
            {
                System.out.println("\u001B[34m" + node.getName() + "\u001B[0m");
            }
        }
    }

    public void cd()
    {
        Node tempParent = parent;
        String path = "" + this.name;
        while(tempParent != null)
        {
            path = tempParent.getName() + "\\" + path;
            tempParent = tempParent.getParent();
        }
        System.out.print(path);
    }
}

