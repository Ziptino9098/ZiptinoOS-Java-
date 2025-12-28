import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static String windowsUsername;
    private static String windowsPassword;
    private static boolean isSystemStable = true;

    public static void main(String[] args) throws InterruptedException {
        installWindows();
        login();
        shellLoop();
        sc.close();
    }

    private static void installWindows() throws InterruptedException {
        System.out.print("Windows 11 edition (home/pro): ");
        String edition = sc.nextLine().toLowerCase();
        System.out.println("Installing Windows 11 " + (edition.equals("home") ? "Home" : "Pro") + "...");
        Thread.sleep(2000);
        System.out.println("Installed");

        System.out.print("Enter username: ");
        windowsUsername = sc.nextLine();
        System.out.print("Enter password: ");
        windowsPassword = sc.nextLine();
        System.out.println("Password set");
    }

    private static void login() {
        System.out.print("Enter login password: ");
        String input = sc.nextLine();
        if (input.equals(windowsPassword)) {
            System.out.println("Logged in");
        } else {
            System.out.println("Wrong password");
        }
    }

    private static void shellLoop() throws InterruptedException {
        System.out.println("\nMini Windows OS Shell. Type 'help' for commands.");
        boolean running = true;

        while (running) {
            System.out.print("\n> ");
            String command = sc.nextLine().toLowerCase();

            switch (command) {
                case "help":
                    showHelp();
                    break;
                case "install":
                    installApp();
                    break;
                case "del":
                    deleteFile();
                    break;
                case "delapp":
                    deleteApp();
                    break;
                case "cd":
                    changeDirectory();
                    break;
                case "reboot":
                    reboot();
                    break;
                case "scan":
                    virusScan();
                    break;
                case "appstore":
                    appStore();
                    break;
                case "email":
                    sendEmail();
                    break;
                case "status":
                    checkStatus();
                    break;
                case "exit":
                    running = false;
                    System.out.println("Exiting shell...");
                    break;
                default:
                    System.out.println(command + " executed successfully");
            }
        }
    }

    private static void showHelp() {
        System.out.println("Available commands:");
        System.out.println("install   - Install an app");
        System.out.println("del       - Delete a file");
        System.out.println("delapp    - Delete an app");
        System.out.println("cd        - Change directory");
        System.out.println("reboot    - Reboot system");
        System.out.println("scan      - Run virus detection scan");
        System.out.println("appstore  - Open Mini Windows app store");
        System.out.println("email     - Send an email");
        System.out.println("status    - Check system stability");
        System.out.println("exit      - Quit shell");
    }

    // Original installApp prompts user
    private static void installApp() {
        System.out.print("App to install: ");
        String app = sc.nextLine();
        installApp(app); // call the new overload
    }

    // Overloaded installApp accepts app name as argument
    private static void installApp(String app) {
        if (isBlockedApp(app)) {
            System.out.println("App blocked due to malware!");
        } else {
            System.out.println(app + " installed successfully.");
        }
    }

    private static boolean isBlockedApp(String app) {
        String lower = app.toLowerCase();
        return lower.equals("noescape.exe") || lower.equals("wannacry.exe") ||
                lower.equals("iloveyou.exe") || lower.equals("quasarrat.exe") ||
                lower.equals("virus.exe");
    }

    private static void deleteFile() {
        System.out.print("File to delete: ");
        String file = sc.nextLine();
        System.out.println(file + " deleted");
    }

    private static void deleteApp() {
        System.out.print("App to delete: ");
        String app = sc.nextLine();
        System.out.println(app + " deleted");
    }

    private static void changeDirectory() {
        System.out.print("Directory to change to: ");
        String dir = sc.nextLine();
        System.out.println("Current directory is now " + dir);
    }

    private static void reboot() throws InterruptedException {
        System.out.println("Rebooting...");
        Thread.sleep(2000);
        System.out.println("System rebooted");
    }

    private static void virusScan() throws InterruptedException {
        System.out.println("Running virus scan...");
        Thread.sleep(2000);
        System.out.println("Virus detected and removed");
    }

    private static void appStore() {
        System.out.print("App store install: ");
        String app = sc.nextLine();
        installApp(app); // now works correctly
    }

    private static void sendEmail() {
        System.out.print("Email recipient: ");
        String email = sc.nextLine();
        if (email.toLowerCase().startsWith("noreply")) {
            System.out.println("Cannot email noreply addresses");
        } else {
            System.out.println("Email sent to " + email);
        }
    }

    private static void checkStatus() {
        if (isSystemStable) {
            System.out.println("System is stable");
        } else {
            System.out.println("ERR_CRITICAL_PROCESS_DIED");
        }
    }
}
