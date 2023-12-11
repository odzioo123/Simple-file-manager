import java.util.Scanner;

public class Manager {
    private Catalog current_folder;
    private static Manager manager;

    private Manager() {
    }
    public static Manager getInstance() {
        if (manager == null) {
            manager = new Manager();
        }
        return manager;
    }

    public void init()
    {
        //Default catalog tree
        Catalog root = new Catalog(null, "root", null);
        current_folder = root;
        Catalog dev = new Catalog(root, "dev", null);
        root.addChild(dev);
        Catalog usr = new Catalog(root, "usr", null);
        root.addChild(usr);
        Catalog docs = new Catalog(root, "docs", null);
        root.addChild(docs);
        File admin = new File(usr, "admin", "administrator");
        usr.addChild(admin);
        File filetxt = new File(docs, "filetxt", "filetxt - TEXT");
        docs.addChild(filetxt);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Start");

        while(true)
        {
            current_folder.cd();
            System.out.print(":");

            String command = scanner.nextLine();
            String spaces = command.replaceAll("[^\\s]", "");
            int spaceCount = spaces.length();
            if(spaceCount > 2)
            {
                System.out.println("too many arguments");
                continue;
            }

            String addons = null;
            String addons2 = null;
            int spaceIndex = command.indexOf(' ');
            if (spaceIndex != -1)
            {
                addons = command.substring(spaceIndex + 1);
                command = command.substring(0, spaceIndex);

                spaceIndex = addons.indexOf(' ');
                if (spaceIndex != -1)
                {
                    addons2 = addons.substring(spaceIndex + 1);
                    addons = addons.substring(0, spaceIndex);
                }
            }

            boolean exit = false;

            switch (command)
            {
                case "cd":
                    this.cd(addons);
                    break;

                case "mkdir":
                    Commands.mkdir(addons, current_folder);
                    break;

                case "touch":
                    Commands.touch(addons, current_folder);
                    break;

                case "rm":
                    Commands.rm(addons, current_folder);
                    break;

                case "ls":
                    Commands.ls(addons, current_folder);
                    break;

                case "more":
                    Commands.more(addons, current_folder);
                    break;

                case "cp":
                    Commands.cp(addons, addons2, current_folder);
                    break;

                case "mv":
                    Commands.mv(addons, addons2, current_folder);
                    break;

                case "nano":
                    Commands.nano(addons, addons2, current_folder);
                    break;

                case "exit":
                    exit = true;
                    break;
                default:
                    System.out.println("unknown command");
            }
            if(exit)
                break;
        }

    }


    public void cd(String addons)
    {
        if(addons == null || addons.equals("\\"))
        {
            current_folder = Commands.cdHelper(null, current_folder).getCurrent_folder();
            return;
        }
        if(addons.contains("\\"))
        {
            if(addons.charAt(0) == '\\')
            {
                while (current_folder.getParent() != null)
                {
                    current_folder = current_folder.getParent();
                }
                addons = addons.substring(1);
            }
            String[] splitAddons = addons.split("\\\\");
            for(String addon : splitAddons)
            {
                CdHelper temp_folder = Commands.cdHelper(addon, current_folder);
                if(temp_folder == null)
                {
                    System.out.println("Wrong path");
                    return;
                }
                if(temp_folder.getCurrent_folder() != null)
                {
                    current_folder = temp_folder.getCurrent_folder();
                }
                else
                {
                    System.out.println("Wrong path");
                    return;
                }
            }
        }
        else
        {
            CdHelper temp_folder = Commands.cdHelper(addons, current_folder);
            if(temp_folder == null)
            {
                System.out.println("Wrong path");
                return;
            }
            if(temp_folder.getCurrent_folder() != null)
            {
                current_folder = temp_folder.getCurrent_folder();
            }
            else
            {
                System.out.println("Wrong path");
                return;
            }
        }
    }



}
