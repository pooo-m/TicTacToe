import java.util.ArrayList;

public class DeleteThread extends Thread {
    private ArrayList<ReadServer> allReadThread;

    public DeleteThread(ArrayList<ReadServer> all) {
        allReadThread = all;
    }

    public void run() {
        while (true) {
            ReadServer n;
            int i = 0;
            while (i < allReadThread.size()) {
                synchronized (allReadThread) {
                    n = allReadThread.get(i);
                    if (n == null) {
                        n.stop();
                        allReadThread.remove(n);
                    }
                    i++;
                }
            }
        }
    }
}
