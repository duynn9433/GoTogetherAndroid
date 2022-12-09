package duynn.gotogether.data_layer.dao;

import androidx.room.*;
import duynn.gotogether.data_layer.model.chat.Message;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM message")
    List<Message> getAll();

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    List<Message> loadAllByIds(int[] userIds);

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    Message findByName(String first, String last);

    @Insert
    void insertAll(Message... messages);

    @Delete
    void delete(Message message);

    @Insert
    void insert(Message message);

    @Transaction
    @Query("SELECT * FROM message WHERE sender_id = :senderId AND receiver_id = :receiverId ORDER BY created_at ASC")
    List<Message> getBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
