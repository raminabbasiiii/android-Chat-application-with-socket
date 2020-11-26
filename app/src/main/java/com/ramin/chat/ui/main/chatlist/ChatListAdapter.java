package com.ramin.chat.ui.main.chatlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramin.chat.databinding.ChatListBinding;
import com.ramin.chat.model.ChatModel;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ChatModel> chatModelList = new ArrayList<>();
    private ChatListItemOnClick itemListener;

    public ChatListAdapter(ChatListItemOnClick itemListener) {
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ChatListBinding binding = ChatListBinding.inflate(layoutInflater,parent,false);
        return new CustomHolder(binding,this.itemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ChatListAdapter.CustomHolder)holder).bind(chatModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    public void setChatList(List<ChatModel> chatModels) {
        this.chatModelList = chatModels;
        notifyDataSetChanged();
    }

    private ChatModel getItem(int adapterPosition) {
        return chatModelList.get(adapterPosition);
    }

    public class CustomHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ChatListBinding binding;
        private ChatListItemOnClick itemListener;

        public CustomHolder(ChatListBinding binding, ChatListItemOnClick itemListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.itemListener = itemListener;
            binding.getRoot().setOnClickListener(this);

        }

        public void bind(ChatModel chatModel) {
            binding.tvName.setText(chatModel.getToName());
            binding.tvTime.setText(chatModel.getTime());
            binding.tvMessage.setText(chatModel.getMessage());
            if (chatModel.getToImage() != null) {
                Bitmap image = getImageBitmap(chatModel.getToImage());
                binding.imgProfile.setImageBitmap(image);
            }
        }

        @Override
        public void onClick(View view) {
            ChatModel chatModel = getItem(getAdapterPosition());
            this.itemListener.onChatListItemClick(chatModel.getToMobileNumber(), chatModel.getToName(), chatModel.getToImage());
        }
    }

    private static Bitmap getImageBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
