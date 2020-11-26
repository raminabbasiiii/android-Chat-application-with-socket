package com.ramin.chat.ui.main.chatlist;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ramin.chat.R;
import com.ramin.chat.data.local.Preferences;
import com.ramin.chat.databinding.FragmentChatListBinding;
import com.ramin.chat.model.ChatModel;
import com.ramin.chat.ui.main.chat.ChatViewModel;
import com.ramin.chat.viewmodel.ViewModelProviderFactory;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ChatListFragment extends DaggerFragment {

    private FragmentChatListBinding binding;
    private ChatListViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    ChatListAdapter adapter;

    @Inject
    Preferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatListBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this,providerFactory).get(ChatListViewModel.class);
        configureLayout();
        subscribeChatListObserver();
    }

    private void configureLayout() {
        //Configure Toolbar
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.chatListToolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeButtonEnabled(false);

        //Configure RecyclerView
        binding.chatListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.chatListRecyclerView.setAdapter(adapter);
    }

    private void subscribeChatListObserver() {
        viewModel.observeChatList()
                .subscribe(new Observer<List<ChatModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ChatModel> chatModels) {
                        adapter.setChatList(chatModels);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}