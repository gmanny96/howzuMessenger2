package howzu.gm.chats;

import java.nio.channels.SocketChannel;

public class meLists {

    private static SocketChannel socket = null;
    private static long time = 0;
    final long NANOSEC_PER_SEC = 1000l * 1000 * 1000;
    private static String Page = null;

    Chats chats;

    private static meLists instance = null;

    public synchronized static meLists getInstance() {
        if(instance==null){
            instance = new meLists();
        }
        return instance;
    }

    public void setConnection(SocketChannel socket){
        meLists.socket = socket;
        setNewTime();
    }

    public SocketChannel getConnection() {
        return meLists.socket;
    }

    public void setChats(Chats chats){
        this.chats = chats;
    }

    public Chats getChats() {
        return this.chats;
    }

    public void setNewTime(){
        time = System.nanoTime();
    }

    public boolean checkTime(){
        return (System.nanoTime() - time) < 20 * 60 * NANOSEC_PER_SEC;
    }

    public void setPage(String page){

    }

    public boolean getConnectionStatus()
    {
        return socket!=null;
    }

    public boolean getPage(){
        return true;
    }

}
