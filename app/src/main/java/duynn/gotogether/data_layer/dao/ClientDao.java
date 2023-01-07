package duynn.gotogether.data_layer.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import duynn.gotogether.data_layer.model.chat.Client;

import java.util.List;

@Dao
public interface ClientDao {
    @Insert
    void insertAll(Client... clients);

    @Insert
    void insert(Client client);

    @Update
    void update(Client client);

    @Query("SELECT * FROM client WHERE id = :id")
    List<Client> getById(Long id);

}
