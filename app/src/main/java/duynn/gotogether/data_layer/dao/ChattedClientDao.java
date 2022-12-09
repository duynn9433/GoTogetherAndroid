package duynn.gotogether.data_layer.dao;

import androidx.room.*;
import duynn.gotogether.data_layer.model.chat.ChattedClient;
import duynn.gotogether.data_layer.model.chat.ChattedClientFull;
import duynn.gotogether.data_layer.model.chat.Message;

import java.util.List;

@Dao
public interface ChattedClientDao {
    @Query("SELECT * FROM chatted_client")
    List<ChattedClient> getAll();

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    List<Message> loadAllByIds(int[] userIds);

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    Message findByName(String first, String last);

    @Insert
    void insertAll(ChattedClient... users);

    @Delete
    void delete(ChattedClient user);

    @Transaction
    @Query("SELECT * FROM chatted_client WHERE owner_id = :ownerId")
    List<ChattedClientFull> getByOwnerId(Long ownerId);

    @Insert
    void insert(ChattedClient chattedClient);

    @Query("SELECT * FROM chatted_client WHERE owner_id = :ownerId AND client = :clientId")
    List<ChattedClient> getByOwnerIdAndClientId(Long ownerId, Long clientId);
}
