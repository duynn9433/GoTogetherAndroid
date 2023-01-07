package duynn.gotogether.data_layer.room_database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import duynn.gotogether.data_layer.dao.ChattedClientDao;
import duynn.gotogether.data_layer.dao.ClientDao;
import duynn.gotogether.data_layer.dao.MessageDao;
import duynn.gotogether.data_layer.model.chat.ChattedClient;
import duynn.gotogether.data_layer.model.chat.Client;
import duynn.gotogether.data_layer.model.chat.Message;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.internal.converter.MergingCollectionConverter;

@Database(entities = {Message.class, ChattedClient.class, Client.class}, version = 2)
public abstract class MessageDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "message_db";
    public static MessageDatabase instance;
    static Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE client ADD COLUMN fcm_token TEXT");
        }
    };
    public static synchronized MessageDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MessageDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return instance;
    }

    public abstract MessageDao messageDao();
    public abstract ChattedClientDao chattedClientDao();
    public abstract ClientDao clientDao();
}
