package com.ramin.chat.ui.main.contacts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramin.chat.databinding.ContactsListBinding;
import com.ramin.chat.model.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ContactModel> contacts = new ArrayList<>();
    private ContactsListItemOnClick itemListener;

    public ContactsAdapter(ContactsListItemOnClick itemListener) {
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ContactsListBinding binding = ContactsListBinding.inflate(layoutInflater,parent,false);
        return new CustomHolder(binding,this.itemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CustomHolder)holder).bind(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void setContacts(List<ContactModel> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    private ContactModel getItem(int adapterPosition) {
        return contacts.get(adapterPosition);
    }

    public class CustomHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ContactsListBinding binding;
        private ContactsListItemOnClick itemListener;

        public CustomHolder(ContactsListBinding binding, ContactsListItemOnClick itemListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.itemListener = itemListener;
            binding.getRoot().setOnClickListener(this);
        }

        public void bind(ContactModel user) {
            binding.tvContactName.setText(user.getName());
            binding.tvContactMobileNumber.setText(user.getMobileNumber());
            if (user.getImage() != null) {
                Bitmap image = getImageBitmap(user.getImage());
                binding.contactImage.setImageBitmap(image);
            }
        }

        @Override
        public void onClick(View v) {
            ContactModel contactModel = getItem(getAdapterPosition());
            this.itemListener.onItemClick(contactModel.getMobileNumber(), contactModel.getName(), contactModel.getImage());
        }
    }

    private static Bitmap getImageBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
