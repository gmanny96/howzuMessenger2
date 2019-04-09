package howzu.gm.chats;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class contacts_tb {

    @PrimaryKey
    private String id;

    private String display_name, name, email, number, imagePath;

    private int contact_type;
}
