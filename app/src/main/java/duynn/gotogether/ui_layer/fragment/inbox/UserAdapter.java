package duynn.gotogether.ui_layer.fragment.inbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import duynn.gotogether.data_layer.model.chat.Client;
import duynn.gotogether.databinding.MsgUserItemBinding;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.chat.ChatActivity;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Setter
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<Client> clients;

    public UserAdapter(Context context, List<Client> clients) {
        this.context = context;
        this.clients = clients;
    }

    @NonNull
    @NotNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MsgUserItemBinding binding = MsgUserItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserViewHolder holder, int position) {
        holder.bind(clients.get(position));
    }

    @Override
    public int getItemCount() {
        if(clients == null) return 0;
        return clients.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private MsgUserItemBinding binding;

        public UserViewHolder(@NonNull MsgUserItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.CLIENT, clients.get(getAdapterPosition()));
                    intent.putExtra(Constants.Bundle, bundle);
                    context.startActivity(intent);
                }
            });
        }

        public void bind(Client client) {
            binding.name.setText(client.getName());
            binding.message.setText(client.getPhone());
        }
    }
}
