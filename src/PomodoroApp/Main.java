package PomodoroApp;

public class Main {

    public static void main(String[] args) {
        //System.out.println("Countdown Beginning");

        String OS = System.getProperty("os.name").toLowerCase();
        System.out.println("Operating system is: " + OS);

        boolean isUnix = false;
        if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
            isUnix = true;
        }

        new PomodoroTimer(isUnix);
    }
}