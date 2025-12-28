import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class Main {
    private static String windowsUsername;
    private static String windowsPassword;
    private static File currentDir = new File(System.getProperty("user.dir"));
    private static boolean isSystemStable = true;

    public static void main(String[] args) {
        // Setup username/password
        windowsUsername = JOptionPane.showInputDialog("Set username:");
        windowsPassword = JOptionPane.showInputDialog("Set password:");

        loginGUI();
    }

    private static void loginGUI() {
        JFrame loginFrame = new JFrame("Mini Windows OS Login");
        loginFrame.setSize(300, 200);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new GridLayout(3, 2, 5, 5));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        loginFrame.add(new JLabel("Username:"));
        loginFrame.add(usernameField);
        loginFrame.add(new JLabel("Password:"));
        loginFrame.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginFrame.add(loginButton);

        loginButton.addActionListener(e -> {
            if (usernameField.getText().equals(windowsUsername) &&
                    new String(passwordField.getPassword()).equals(windowsPassword)) {
                JOptionPane.showMessageDialog(loginFrame, "Logged in successfully!");
                loginFrame.dispose();
                mainDesktop();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Incorrect username or password!");
            }
        });

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    private static void mainDesktop() {
        JFrame desktop = new JFrame("Mini Windows OS");
        desktop.setSize(600, 400);
        desktop.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        desktop.setLayout(new FlowLayout());

        JLabel welcome = new JLabel("Welcome " + windowsUsername + "!");
        welcome.setFont(new Font("Arial", Font.BOLD, 16));
        desktop.add(welcome);

        JButton fileExplorerBtn = new JButton("File Explorer");
        JButton commandPromptBtn = new JButton("Command Prompt");
        JButton webBrowserBtn = new JButton("Web Browser");

        desktop.add(fileExplorerBtn);
        desktop.add(commandPromptBtn);
        desktop.add(webBrowserBtn);

        fileExplorerBtn.addActionListener(e -> new FileExplorerFrame());
        commandPromptBtn.addActionListener(e -> new CommandPromptFrame());
        webBrowserBtn.addActionListener(e -> new WebBrowserFrame());

        desktop.setLocationRelativeTo(null);
        desktop.setVisible(true);
    }

    // ---------- FILE EXPLORER ----------
    static class FileExplorerFrame extends JFrame {
        private JList<String> fileList;
        private DefaultListModel<String> listModel;
        private JLabel dirLabel;

        public FileExplorerFrame() {
            super("File Explorer");
            setSize(500, 400);
            setLayout(new BorderLayout());

            dirLabel = new JLabel(currentDir.getAbsolutePath());
            add(dirLabel, BorderLayout.NORTH);

            listModel = new DefaultListModel<>();
            fileList = new JList<>(listModel);
            fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(fileList);
            add(scrollPane, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel();
            JButton upBtn = new JButton("Up");
            JButton refreshBtn = new JButton("Refresh");
            JButton newFileBtn = new JButton("New File");
            JButton newDirBtn = new JButton("New Dir");
            JButton deleteBtn = new JButton("Delete");
            btnPanel.add(upBtn);
            btnPanel.add(refreshBtn);
            btnPanel.add(newFileBtn);
            btnPanel.add(newDirBtn);
            btnPanel.add(deleteBtn);
            add(btnPanel, BorderLayout.SOUTH);

            refresh();

            upBtn.addActionListener(e -> {
                currentDir = currentDir.getParentFile();
                refresh();
            });

            refreshBtn.addActionListener(e -> refresh());

            newFileBtn.addActionListener(e -> {
                String name = JOptionPane.showInputDialog("Enter file name:");
                if (name != null) {
                    try { new File(currentDir, name).createNewFile(); } catch (IOException ignored) {}
                    refresh();
                }
            });

            newDirBtn.addActionListener(e -> {
                String name = JOptionPane.showInputDialog("Enter directory name:");
                if (name != null) new File(currentDir, name).mkdir();
                refresh();
            });

            deleteBtn.addActionListener(e -> {
                String selected = fileList.getSelectedValue();
                if (selected != null) {
                    File f = new File(currentDir, selected);
                    if (f.isDirectory()) f.delete();
                    else f.delete();
                    refresh();
                }
            });

            fileList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        String selected = fileList.getSelectedValue();
                        if (selected != null) {
                            File f = new File(currentDir, selected);
                            if (f.isDirectory()) {
                                currentDir = f;
                                refresh();
                            } else {
                                try {
                                    Desktop.getDesktop().edit(f);
                                } catch (IOException ignored) {}
                            }
                        }
                    }
                }
            });

            setLocationRelativeTo(null);
            setVisible(true);
        }

        private void refresh() {
            dirLabel.setText(currentDir.getAbsolutePath());
            listModel.clear();
            String[] files = currentDir.list();
            if (files != null) for (String f : files) listModel.addElement(f);
        }
    }

    // ---------- COMMAND PROMPT ----------
    static class CommandPromptFrame extends JFrame {
        private JTextArea textArea;
        private JTextField entry;
        private ArrayList<String> installedApps = new ArrayList<>();

        public CommandPromptFrame() {
            super("Command Prompt");
            setSize(600, 400);
            setLayout(new BorderLayout());

            textArea = new JTextArea();
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            add(scrollPane, BorderLayout.CENTER);

            DefaultCaret caret = (DefaultCaret)textArea.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

            entry = new JTextField();
            add(entry, BorderLayout.SOUTH);

            textArea.append("Mini Command Prompt\nType 'help' for commands\n");

            entry.addActionListener(e -> runCommand(entry.getText()));

            setLocationRelativeTo(null);
            setVisible(true);
        }

        private void runCommand(String cmd) {
            entry.setText("");
            textArea.append("> " + cmd + "\n");
            String[] parts = cmd.split(" ");
            if (parts.length == 0) return;
            String command = parts[0].toLowerCase();

            try {
                switch(command) {
                    case "help":
                        textArea.append("Commands:\nls, cd, cat, touch, del, mkdir, rmdir, install, delapp, scan, status, reboot, exit\n");
                        break;
                    case "ls":
                        for(String f : currentDir.list()) textArea.append(f + "\n");
                        break;
                    case "cd":
                        if(parts.length > 1){
                            File newDir = new File(currentDir, parts[1]);
                            if(newDir.isDirectory()) currentDir = newDir;
                            else textArea.append("Directory not found\n");
                        } else textArea.append(currentDir.getAbsolutePath() + "\n");
                        break;
                    case "cat":
                        if(parts.length > 1){
                            File f = new File(currentDir, parts[1]);
                            if(f.isFile()){
                                BufferedReader br = new BufferedReader(new FileReader(f));
                                String line;
                                while((line = br.readLine())!=null) textArea.append(line+"\n");
                                br.close();
                            } else textArea.append("File not found\n");
                        }
                        break;
                    case "touch":
                        if(parts.length > 1){
                            new File(currentDir, parts[1]).createNewFile();
                            textArea.append(parts[1] + " created\n");
                        }
                        break;
                    case "del":
                        if(parts.length > 1){
                            File f = new File(currentDir, parts[1]);
                            if(f.exists()) f.delete();
                            textArea.append(parts[1] + " deleted\n");
                        }
                        break;
                    case "mkdir":
                        if(parts.length > 1){
                            new File(currentDir, parts[1]).mkdir();
                            textArea.append(parts[1] + " created\n");
                        }
                        break;
                    case "rmdir":
                        if(parts.length > 1){
                            File f = new File(currentDir, parts[1]);
                            if(f.exists() && f.isDirectory()) f.delete();
                            textArea.append(parts[1] + " removed\n");
                        }
                        break;
                    case "install":
                        if(parts.length > 1){
                            String app = parts[1];
                            if(app.equalsIgnoreCase("virus.exe")||app.equalsIgnoreCase("wannacry.exe"))
                                textArea.append("App blocked due to malware!\n");
                            else{
                                installedApps.add(app);
                                textArea.append(app + " installed successfully\n");
                            }
                        }
                        break;
                    case "delapp":
                        if(parts.length > 1){
                            String app = parts[1];
                            if(installedApps.contains(app)) {installedApps.remove(app); textArea.append(app + " deleted\n");}
                            else textArea.append("App not installed\n");
                        }
                        break;
                    case "scan":
                        textArea.append("Running virus scan...\nVirus detected and removed\n");
                        break;
                    case "status":
                        textArea.append(isSystemStable ? "System is stable\n":"ERR_CRITICAL_PROCESS_DIED\n");
                        break;
                    case "reboot":
                        textArea.append("Rebooting system...\nSystem rebooted\n");
                        break;
                    case "exit":
                        dispose();
                        break;
                    default:
                        textArea.append(cmd + " executed\n");
                        break;
                }
            } catch(Exception e){textArea.append("Error: "+e.getMessage()+"\n");}
        }
    }

    // ---------- WEB BROWSER ----------
    static class WebBrowserFrame extends JFrame {
        public WebBrowserFrame() {
            super("Web Browser");
            setSize(500, 150);
            setLayout(new FlowLayout());

            JTextField urlField = new JTextField("https://www.google.com", 30);
            JButton goBtn = new JButton("Go");

            add(new JLabel("Enter URL:"));
            add(urlField);
            add(goBtn);

            goBtn.addActionListener(e -> {
                String url = urlField.getText();
                if(!url.startsWith("http")) url = "https://" + url;
                try { Desktop.getDesktop().browse(new java.net.URI(url)); }
                catch(Exception ignored){}
            });

            setLocationRelativeTo(null);
            setVisible(true);
        }
    }
}
