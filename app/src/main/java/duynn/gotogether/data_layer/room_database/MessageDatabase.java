package duynn.gotogether.data_layer.room_database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import duynn.gotogether.data_layer.dao.ChattedClientDao;
import duynn.gotogether.data_layer.dao.ClientDao;
import duynn.gotogether.data_layer.dao.MessageDao;
import duynn.gotogether.data_layer.model.chat.ChattedClient;
import duynn.gotogether.data_layer.model.chat.Client;
import duynn.gotogether.data_layer.model.chat.Message;

@Database(entities = {Message.class, ChattedClient.class, Client.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "message_db";
    public static MessageDatabase instance;
    public static synchronized MessageDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MessageDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract MessageDao messageDao();
    public abstract ChattedClientDao chattedClientDao();
    public abstract ClientDao clientDao();
}
