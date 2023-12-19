import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Commands {
    public static void mkdir(String addons, Catalog current_folder)
    {
        if(addons != null)
        {
            for(int i = 0; i < addons.length() - 1; i++)
            {
                if(!Character.isDigit(addons.charAt(i)) && !Character.isLetter(addons.charAt(i)))
                {
                    System.out.println("Wrong name");
                    return;
                }
            }

            Catalog catalog = new Catalog(current_folder, addons, null);
            current_folder.addChild(catalog);
        }
        else
        {
            System.out.println("catalog name needed");
        }
    }

    public static void touch(String addons, Catalog current_folder)
    {
        if(addons != null)
        {
            for(int i = 0; i < addons.length() - 1; i++)
            {
                if(!Character.isDigit(addons.charAt(i)) && !Character.isLetter(addons.charAt(i)))
                {
                    System.out.println("Wrong name");
                    return;
                }
            }

            File file = new File(current_folder, addons, "");
            current_folder.addChild(file);
        }
        else
        {
            System.out.println("file name needed");
        }
    }

    public static void rm(String addons, Catalog current_folder)
    {
        if(addons == null)
        {
            System.out.println("File or Catalog expected");
            return;
        }
        else
        {
            Catalog curr_folder = current_folder;
            if(addons.contains("\\"))
            {
                if(addons.charAt(0) == '\\')
                {
                    while(current_folder.getParent() != null)
                    {
                        current_folder = current_folder.getParent();
                    }
                    addons = addons.substring(1);
                    if(addons.equals(""))
                    {
                        System.out.println("Don't delete root wrr");
                        return;
                    }
                }
                String[] splitAddons = addons.split("\\\\");
                CdHelper temp_fd = null;
                for(String addon : splitAddons)
                {
                    temp_fd = Commands.cdHelper(addon, current_folder);
                    if (temp_fd != null)
                    {
                        current_folder = temp_fd.getCurrent_folder();
                    }
                    else
                    {
                        System.out.println("Wrong path");
                        return;
                    }
                }
                if (temp_fd.getCurrent_file() != null)
                {
                    temp_fd.getCurrent_file().setParent(null);
                    current_folder.removeChild(temp_fd.getCurrent_file().getName());
                }
                else
                {
                    current_folder.removeChildren();
                    current_folder.getParent().removeChild(current_folder.getName());
                }
            }
            else
            {
                CdHelper temp_fd = Commands.cdHelper(addons, current_folder);
                if (temp_fd != null)
                {
                    current_folder = temp_fd.getCurrent_folder();
                    if (temp_fd.getCurrent_file() != null)
                    {
                        temp_fd.getCurrent_file().setParent(null);
                        current_folder.removeChild(temp_fd.getCurrent_file().getName());
                    }
                    else
                    {
                        current_folder.removeChildren();
                        current_folder.getParent().removeChild(current_folder.getName());
                    }
                }
                else
                {
                    System.out.println("Wrong path");
                    return;
                }
            }
            current_folder = curr_folder;
        }
    }

    public static void ls(String addons, Catalog current_folder)
    {
        if(addons == null || addons == " ")
        {
            current_folder.ls();
        }
        else
        {
            Catalog curr_folder = current_folder;
            if(addons.contains("\\"))
            {
                if(addons.charAt(0) == '\\')
                {
                    while(current_folder.getParent() != null)
                    {
                        current_folder = current_folder.getParent();
                    }
                    addons = addons.substring(1);
                    if(addons.equals(""))
                    {
                        current_folder.ls();
                        current_folder = curr_folder;
                        return;
                    }
                }
                String[] splitAddons = addons.split("\\\\");
                for(String addon : splitAddons)
                {
                    CdHelper current_fd = Commands.cdHelper(addon, current_folder);
                    if(current_fd != null)
                    {
                        current_folder = current_fd.getCurrent_folder();
                    }
                }
                current_folder.ls();
            }
            else
            {
                CdHelper current_fd = Commands.cdHelper(addons, current_folder);
                if(current_fd != null)
                {
                    current_folder = current_fd.getCurrent_folder();
                    current_folder.ls();
                }
                else
                {
                    System.out.println("Wrong path");
                }
            }
            current_folder = curr_folder;
        }
    }

    public static void more(String addons, Catalog current_folder)
    {
        if(addons != null)
        {
            boolean done = false;
            for(Node node : current_folder.getChildren())
            {
                if(node.getName().equals(addons))
                {
                    ((File) node).more();
                    done = true;
                    break;
                }
            }
            if(!done)
            {
                touch(addons, current_folder);
            }
        }
        else
        {
            System.out.println("unknown command");
        }
    }

    public static CdHelper cdHelper(String addons, Catalog current_folder)
    {
        Catalog curr_folder = current_folder;
        int status = 0;

        if(addons != null)
        {
            if(addons.equals(".."))
            {
                if(current_folder.getParent() != null)
                {
                    current_folder = current_folder.getParent();
                }
                return new CdHelper(current_folder, null);
            }
            for(Node node : current_folder.getChildren())
            {
                if(node.getName().equals(addons))
                {
                    if(node instanceof Catalog)
                    {
                        current_folder = (Catalog) node;
                        return new CdHelper(current_folder, null);
                    }
                    else
                    {
                        return new CdHelper(current_folder, (File) node);
                    }
                }
            }
        }
        else
        {
            while(current_folder.getParent() != null)
            {
                current_folder = current_folder.getParent();
            }
            return new CdHelper(current_folder, null);
        }
        return null;
    }

    public static void cp(String addons, String addons2, Catalog current_folder)
    {
        if(addons == null || addons2 == null)
        {
            System.out.println("File or Catalog expected");
            return;
        }

        int file1 = 0;
        int file2 = 0;
        Node node1 = null;
        Node node2 = null;

        Catalog node1folder = current_folder;
        Catalog node2folder = current_folder;
        String lastSplitAddons1 = addons;
        String lastSplitAddons2 = addons2;

        Catalog curr_folder = current_folder;
        if(addons.contains("\\"))
        {
            if(addons.charAt(0) == '\\')
            {
                while(current_folder.getParent() != null)
                {
                    current_folder = current_folder.getParent();
                }
                addons = addons.substring(1);
                if(addons.equals(""))
                {
                    System.out.println("Don't copy root wrr");
                    current_folder = curr_folder;
                    return;
                }
            }
            String[] splitAddons = addons.split("\\\\");
            lastSplitAddons1 = splitAddons[splitAddons.length - 1];
            for(String addon : splitAddons)
            {
                CdHelper current_fd = Commands.cdHelper(addon, current_folder);
                if(current_fd != null)
                {
                    current_folder = current_fd.getCurrent_folder();
                }
            }
            node1folder = current_folder;
            current_folder = curr_folder;
        }
        else
        {
            CdHelper current_fd = Commands.cdHelper(addons, current_folder);
            if(current_fd != null)
            {
                node1folder = current_fd.getCurrent_folder();
            }
        }

        if(addons2.contains("\\"))
        {
            if(addons2.charAt(0) == '\\')
            {
                while(current_folder.getParent() != null)
                {
                    current_folder = current_folder.getParent();
                }
                addons2 = addons2.substring(1);
            }
            String[] splitAddons = addons2.split("\\\\");
            lastSplitAddons2 = splitAddons[splitAddons.length - 1];
            for(String addon : splitAddons)
            {
                CdHelper current_fd = Commands.cdHelper(addon, current_folder);
                if(current_fd != null)
                {
                    current_folder = current_fd.getCurrent_folder();
                }
            }

            node2folder = current_folder;
            current_folder = curr_folder;
        }
        else
        {
            CdHelper current_fd = Commands.cdHelper(addons2, current_folder);
            if(current_fd != null)
            {
                node2folder = current_fd.getCurrent_folder();
            }
        }


        if(node1folder.getName().equals(lastSplitAddons1))
        {
            file1 = 2;
        }
        else
        {
            for(Node node : node1folder.getChildren())
            {
                if(node.getName().equals(lastSplitAddons1))
                {
                    node1 = node;
                    if(node instanceof File)
                    {
                        file1 = 1;
                    }
                    else
                    {
                        file1 = 2;
                    }
                }
            }
        }

        if(node2folder.getName().equals(lastSplitAddons2))
        {
            file2 = 2;
        }
        else
        {
            for(Node node : node2folder.getChildren())
            {
                if(node.getName().equals(lastSplitAddons2))
                {
                    node2 = node;
                    if(node instanceof File)
                    {
                        file2 = 1;
                    }
                    else
                    {
                        file2 = 2;
                    }
                }
            }
        }

        if(node2folder == null || node1folder == null)
        {
            System.out.println("Wrong path.");
            return;
        }

        if(file1 == 0)
        {
            System.out.println("first argument doesn't exist");
            return;
        }

        if(file1 == 2 && file2 == 0)
        {
            cpCatalog(lastSplitAddons2, lastSplitAddons1, node1folder, current_folder);
            return;
        }

        if(file1 == 1 && file2 == 1)
        {
            rm(addons2, node2folder);
            File copy = new File(node2folder, lastSplitAddons2, ((File) node1).getContent());
            node2folder.addChild(copy);
        }

        if(file1 == 1 && file2 == 0)
        {
            File copy = new File(node2folder, lastSplitAddons2, ((File) node1).getContent());
            node2folder.addChild(copy);
        }

        if(file1 == 1 && file2 == 2)
        {
            File copy = new File(node2folder, lastSplitAddons1, ((File) node1).getContent());
            node2folder.addChild(copy);
        }

        if(file1 == 2 && file2 == 2)
        {
            cpCatalog(lastSplitAddons1, lastSplitAddons2, node1folder, node2folder);
        }
    }
    public static void cpCatalog(String addons, String addons2, Catalog node1folder, Catalog node2folder)
    {
        Catalog copy = new Catalog((Catalog) node2folder, addons, null);
        node2folder.addChild(copy);

        for(Node node : node1folder.getChildren())
        {
            if(node instanceof File)
            {
                copy.addChild((new File(copy, node.getName(), ((File) node).getContent())));
            }
            else
            {
                cpCatalog(node.getName(), copy.getName(), (Catalog) node, node2folder);
            }
        }
    }

    public static void mv(String addons, String addons2, Catalog current_folder)
    {
        if(addons == null || addons2 == null)
        {
            System.out.println("File or Catalog expected");
            return;
        }
        if(addons.equals(addons2))
            return;
        cp(addons, addons2, current_folder);
        rm(addons, current_folder);
    }

    public static void nano(String addons, String addons2, Catalog current_folder)
    {
        int success = 0;
        for(Node node : current_folder.getChildren())
        {
            if(node.getName().equals(addons))
            {
                if(node instanceof File)
                {
                    ((File) node).setContent(addons2);
                    success = 1;
                    break;
                }
            }
        }
        if(success == 0)
        {
            System.out.println("Wrong file name");
        }
    }
}
