package com.ramin.chat.ui.main.chat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramin.chat.databinding.LtrMessageBinding;
import com.ramin.chat.databinding.RtlMessageBinding;
import com.ramin.chat.model.ChatModel;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ChatModel> chatModelList = new ArrayList<>();
    private static final int RIGHT_MSG = 0;
    private static final int LEFT_MSG = 2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == RIGHT_MSG) {
            RtlMessageBinding rtlBinding = RtlMessageBinding.inflate(layoutInflater, parent, false);
            return new RtlMessageHolder(rtlBinding);
        } else{
            LtrMessageBinding ltrBinding = LtrMessageBinding.inflate(layoutInflater, parent, false);
            return new MessageLtrHolder(ltrBinding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case RIGHT_MSG:
                ((RtlMessageHolder)holder).bind(position);
                break;

            case LEFT_MSG:
                ((MessageLtrHolder)holder).bind(position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    @Override
    public int getItemViewType(int position) {

            if (chatModelList.get(position).getDirection().equals("0")) {
                return RIGHT_MSG;
            } else if (chatModelList.get(position).getDirection().equals("2")) {
                return LEFT_MSG;
            }
        return position;
    }

    public void setChats(List<ChatModel> chatModels) {
        this.chatModelList = chatModels;
        notifyDataSetChanged();
    }

    public class RtlMessageHolder extends RecyclerView.ViewHolder {

        private RtlMessageBinding binding;

        public RtlMessageHolder(RtlMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(int position) {
            binding.tvMessage.setText(chatModelList.get(position).getMessage());
            binding.tvTime.setText(chatModelList.get(position).getTime());
        }
    }

    public class MessageLtrHolder extends RecyclerView.ViewHolder {

        private LtrMessageBinding binding;

        public MessageLtrHolder(LtrMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(int position) {
            binding.tvMessage.setText(chatModelList.get(position).getMessage());
            binding.tvTime.setText(chatModelList.get(position).getTime());
        }
    }
}
