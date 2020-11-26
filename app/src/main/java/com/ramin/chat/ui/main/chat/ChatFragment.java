package com.ramin.chat.ui.main.chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.ramin.chat.R;
import com.ramin.chat.data.local.Preferences;
import com.ramin.chat.databinding.FragmentChatBinding;
import com.ramin.chat.model.ChatModel;
import com.ramin.chat.ui.main.OnBackPressedListener;
import com.ramin.chat.viewmodel.ViewModelProviderFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ChatFragment extends DaggerFragment implements View.OnClickListener, OnBackPressedListener {

    private FragmentChatBinding binding;
    private String toName,toMobileNumber,time,date;
    private byte[] image;
    private ChatViewModel viewModel;
    private Thread thread;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    Socket socket;

    @Inject
    MessageAdapter adapter;

    @Inject
    Handler handler;

    @Inject
    JSONObject sendData,isTyping,stopTyping;

    @Inject
    Preferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this,providerFactory).get(ChatViewModel.class);
        socket.on("privateMessage",handlePrivateMessage);
        socket.on("is typing",handleIsTyping);
        socket.on("stop typing",handleStopTyping);
        configureLayout();
        setProfile();
        subscribeChatsObserver();
        etTypeMessageTextChangeListener();
    }

    private void configureLayout() {
        //Configure Toolbar
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.startChatToolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setHomeButtonEnabled(false);

        //Configure RecyclerView
        binding.messageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.messageRecyclerView.setAdapter(adapter);

        //Configure OnClickListener
        binding.btnBack.setOnClickListener(this);
        binding.btnSendMessage.setOnClickListener(this);
    }

    private void setProfile() {
        if (getArguments() != null) {
            toMobileNumber = getArguments().getString("mobileNumber");
            toName = getArguments().getString("name");
            image = getArguments().getByteArray("image");
        }
        binding.tvName.setText(toName);
        if (image != null) {
            Bitmap bitmap = getImageBitmap(image);
            binding.userImage.setImageBitmap(bitmap);
        }
    }
    private static Bitmap getImageBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    private void sendMessage() {
        String message = binding.etTypeMessage.getText().toString().trim();
        if (!TextUtils.isEmpty(message)) {
            binding.etTypeMessage.setText("");
            try {
                sendData.put("from",preferences.getUserMobileNumber());
                sendData.put("to",toMobileNumber);
                sendData.put("message",message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("privateMessage", sendData);
        }
    }

    private void subscribeChatsObserver() {
        viewModel.observeChats(preferences.getUserMobileNumber(),toMobileNumber)
                .subscribe(new Observer<List<ChatModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ChatModel> chatModels) {
                        adapter.setChats(chatModels);
                        binding.messageRecyclerView.scrollToPosition(adapter.getItemCount()-1);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void etTypeMessageTextChangeListener() {
        binding.etTypeMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    isTyping.put("from",preferences.getUserMobileNumber());
                    isTyping.put("to",toMobileNumber);
                    isTyping.put("message","is typing...");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("is typing", isTyping);
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    stopTyping.put("from",preferences.getUserMobileNumber());
                    stopTyping.put("to",toMobileNumber);
                    stopTyping.put("message","online");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                thread = new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        handler.post(() -> socket.emit("stop typing", stopTyping));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        });
    }

    public Emitter.Listener handlePrivateMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            handler.post(() -> {
                JSONObject data = (JSONObject)args[0];
                try {
                    String fromName = preferences.getName() + " " + preferences.getFamily();
                    time = data.getString("mHours") + ":" + data.getString("mMin");
                    date = data.getString("mYear") + "/" + data.getString("mDay") + "/" + data.getString("mMonth");

                    if (data.getString("from").equals(preferences.getUserMobileNumber())) {
                        viewModel.insertChat(fromName,toName,preferences.getUserMobileNumber(),toMobileNumber,data.getString("message"),time,date,"0");
                    } else {
                        viewModel.insertChat(fromName,toName,preferences.getUserMobileNumber(),toMobileNumber,data.getString("message"),time,date,"2");
                    }

                    subscribeChatsObserver();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    };

    public Emitter.Listener handleIsTyping = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            handler.post(() -> {
                JSONObject data = (JSONObject)args[0];
                try {
                    binding.tvLastSeen.setText(data.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    };

    public Emitter.Listener handleStopTyping = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            handler.post(() -> {
                JSONObject data = (JSONObject)args[0];

                try {
                    binding.tvLastSeen.setText(data.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSendMessage) {
            sendMessage();
        } else if (view.getId() == R.id.btnBack) {
            back();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        socket.on("privateMessage",handlePrivateMessage).off();
        socket.on("is typing",handleIsTyping).off();
        socket.on("stop typing",handleStopTyping).off();
        requireActivity().findViewById(R.id.bottomNavigation).setVisibility(View.VISIBLE);
        NavHostFragment.findNavController(this).navigate(R.id.action_chatFragment_to_chatListFragment);
    }
}