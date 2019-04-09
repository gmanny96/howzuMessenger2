package howzu.gm.chats;

import org.jivesoftware.smack.XMPPConnection;

import java.util.List;

public class Lists {

    private static List<contactsData> data = null;

    private static List<chatsData> data1 = null;

    private static List<chatPageData> data2 = null;

    private static List<searchData> data3 = null;

    private static Lists instance = null;

    private static XMPPConnection connection = null;

    public synchronized static Lists getInstance() {
        if(instance==null){
            instance = new Lists();
        }
        return instance;
    }

    public void setConnection(XMPPConnection connection){
        Lists.connection = connection;
    }

    public XMPPConnection getConnection() {
        return Lists.connection;
    }

    public void setContactList(List<contactsData> data){
        Lists.data = data;
    }

    public List<contactsData> getContactList() {
        return Lists.data;
    }

    public void setChatList(List<chatsData> data1){
        Lists.data1 = data1;
    }

    public List<chatsData> getChatList() {
        return Lists.data1;
    }

    public void setMsgList(List<chatPageData> data2){
        Lists.data2 = data2;
    }

    public List<chatPageData> getMsgList() {
        return Lists.data2;
    }

    public void setSearchList(List<searchData> data3){
        Lists.data3 = data3;
    }

    public List<searchData> getSearchList() {
        return Lists.data3;
    }
}
