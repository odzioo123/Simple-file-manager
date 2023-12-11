public class CdHelper {
    Catalog current_folder;
    File current_file;

    public CdHelper(Catalog current_folder, File current_file) {
        this.current_folder = current_folder;
        this.current_file = current_file;
    }

    public Catalog getCurrent_folder() {
        return current_folder;
    }

    public File getCurrent_file() {
        return current_file;
    }

    public void setCurrent_folder(Catalog current_folder) {
        this.current_folder = current_folder;
    }

    public void setCurrent_file(File current_file) {
        this.current_file = current_file;
    }
}
