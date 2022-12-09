package duynn.gotogether.ui_layer.activity.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import duynn.gotogether.data_layer.model.chat.Message;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.databinding.MsgReceiveItemBinding;
import duynn.gotogether.databinding.MsgSendItemBinding;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messages;
    private Context context;
    private SessionManager sessionManager;
    private Long clientId;
    private final int MSG_SEND = 2;
    private final int MSG_RECEIVE = 1;

    public MessageAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
        sessionManager = SessionManager.getInstance(context);
        clientId = sessionManager.getClient().getId();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_SEND){
            MsgSendItemBinding binding = MsgSendItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false);
            return new SentMessageHolder(binding);
        }else{
            MsgReceiveItemBinding binding = MsgReceiveItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false);
            return new ReceivedMessageHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SentMessageHolder) {
            ((SentMessageHolder) holder).bind(messages.get(position));
        }else if (holder instanceof ReceivedMessageHolder) {
            ((ReceivedMessageHolder) holder).bind(messages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (messages == null) {
            return 0;
        }
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderId().equals(clientId)) {
            return MSG_SEND;
        } else {
            return MSG_RECEIVE;
        }
    }

    public class SentMessageHolder extends RecyclerView.ViewHolder {
        MsgSendItemBinding binding;

        public SentMessageHolder(@NonNull  MsgSendItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message message) {
            binding.msgSend.setText(message.getContent());
        }
    }
    public class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        MsgReceiveItemBinding binding;

        public ReceivedMessageHolder(@NonNull MsgReceiveItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message message) {
            binding.msgSend.setText(message.getContent());
        }
    }
}
