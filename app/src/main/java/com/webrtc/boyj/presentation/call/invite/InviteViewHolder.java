package com.webrtc.boyj.presentation.call.invite;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.databinding.ItemInviteUserBinding;

public class InviteViewHolder extends RecyclerView.ViewHolder {
    private ItemInviteUserBinding binding;

    InviteViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    void bindTo(@NonNull final User user) {
        binding.setUser(user);
    }

    ItemInviteUserBinding getBinding() {
        return binding;
    }
}