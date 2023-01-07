package duynn.gotogether.ui_layer.activity.rating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import duynn.gotogether.data_layer.model.model.Comment;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.databinding.CommentItemBinding;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Setter
@Getter
public class CommentItemAdapter extends RecyclerView.Adapter<CommentItemAdapter.CommentItemViewHolder> {
    private List<Comment> commentList;
    private Context context;

    public CommentItemAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public CommentItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        CommentItemBinding binding = CommentItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new CommentItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentItemViewHolder holder, int position) {
        holder.bind(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        if(commentList == null){
            return 0;
        }
        return commentList.size();
    }

    public class CommentItemViewHolder extends RecyclerView.ViewHolder {
        CommentItemBinding binding;
        public CommentItemViewHolder(@NonNull CommentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Comment comment) {
            if(comment != null){
                if(comment.getSender() != null){
                    binding.senderName.setText(comment.getSender().getFullNameString());
                }
                if(comment.getContent() != null){
                    binding.content.setText(comment.getContent());
                }
                if(comment.getRating() != null){
                    binding.ratingBar.setRating(comment.getRating());
                }
            }
        }
    }
}
