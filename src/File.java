public class File extends Node {
    private String content;

    public File(Catalog parent, String name, String content) {
        super(parent, name);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void more()
    {
        System.out.println(content);
    }
}
