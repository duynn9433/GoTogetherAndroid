package duynn.gotogether.ui_layer.activity.chat;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import duynn.gotogether.data_layer.model.chat.Client;
import duynn.gotogether.data_layer.model.chat.Message;
import duynn.gotogether.databinding.ActivityChatBinding;
import duynn.gotogether.domain_layer.common.Constants;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private MessageAdapter messageAdapter;
    private ChatViewModel chatViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        chatViewModel= new ViewModelProvider(this).get(ChatViewModel.class);

        Bundle bundle = getIntent().getBundleExtra(Constants.Bundle);
        Client client = (Client) bundle.getSerializable(Constants.CLIENT);

        initRecyclerView();
        initButtonSend();
        observeData();

        chatViewModel.setReceiver(client);
        chatViewModel.saveClient();//for test
        chatViewModel.getAllMessage();
    }

    private void observeData() {
        chatViewModel.getMessages().observe(this, messages -> {
            messageAdapter.setMessages(messages);
            messageAdapter.notifyDataSetChanged();
        });
    }

    private void initButtonSend() {
        binding.chatInputSend.setOnClickListener(v -> {
            String content = binding.chatInputText.getText().toString();
            if(!content.isEmpty()){
                Message message = Message.builder()
                        .content(content)
                        .createdAt(Calendar.getInstance().getTimeInMillis())
                        .senderId(chatViewModel.getSender().getId())
                        .receiverId(chatViewModel.getReceiver().getId())
                        .build();
                chatViewModel.sendMessage(message);
                binding.chatInputText.setText("");
            }
        });
    }

    private void initRecyclerView() {
        messageAdapter = new MessageAdapter(new ArrayList<>(),this);
        binding.msgRecyclerView.setAdapter(messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.msgRecyclerView.setLayoutManager(linearLayoutManager);
    }
}