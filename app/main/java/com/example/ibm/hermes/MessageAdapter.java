package com.example.ibm.hermes;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;

public class MessageAdapter extends Adapter<MessageAdapter.MessageViewHolder> {
    private FirebaseAuth mAuth;
    private List<Messages> mMessageList;

    public class MessageViewHolder extends ViewHolder {
        public TextView Sender;
        public TextView displayName;
        public TextView messageText;
        public CircleImageView profileImage;

        public MessageViewHolder(final View itemView) {
            super(itemView);
            this.messageText = (TextView) itemView.findViewById(R.id.message_text_layout);
            this.profileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_layout);
            this.displayName = (TextView) itemView.findViewById(R.id.name_text_layout);
            itemView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    final Context context = itemView.getContext();
                    CharSequence[] items = new CharSequence[]{"Copy", "Delete"};
                    Builder builder = new Builder(context);
                    builder.setTitle((CharSequence) "Select The Action");
                    builder.setItems(items, new OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            if (item == 0) {
                                Toast.makeText(context, ((Messages) MessageAdapter.this.mMessageList.get(2)).getMain_message(), 0).show();
                            }
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }
    }

    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;
    }

    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout, parent, false));
    }

    public void onBindViewHolder(final MessageViewHolder holder, final int position) {
        Messages c = (Messages) this.mMessageList.get(position);
        holder.messageText.setText(c.getMain_message());
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(c.getSender())) {
            holder.displayName.setText("You");
            holder.profileImage.setImageResource(R.drawable.dp);
        } else {
            holder.displayName.setText(c.getSender());
        }
        holder.itemView.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                final Context context = holder.itemView.getContext();
                CharSequence[] items = new CharSequence[]{"Copy", "Delete"};
                Builder builder = new Builder(context);
                builder.setTitle((CharSequence) "Select The Action");
                builder.setItems(items, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            ((ClipboardManager) context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text", ((Messages) MessageAdapter.this.mMessageList.get(position)).getMain_message()));
                            Toast.makeText(context, "Message Copied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    public int getItemCount() {
        return this.mMessageList.size();
    }
}
