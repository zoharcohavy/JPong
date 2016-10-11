package pong;

public class Pong {
    private static MainMenu mm;
    private static MyGUI mg;
    private static int gui,status;
    public static void main(String[] args) {
        MainMenu mm = new MainMenu();
        //Make it so you dynamically create the objects
        MyGUI mg = new MyGUI(2); //Fix this
        while (true) {
            status = mm.getStatus();
            System.getenv(); //USELESS COMMAND, BUT WON'T WORK WITHOUT :)
            if (status != 0) break;
        }
        if (mm.getStatus() == 2) {
            mm.hideMe();
            mg.start();
        }
    }
}