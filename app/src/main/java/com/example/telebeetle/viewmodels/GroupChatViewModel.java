package com.example.telebeetle.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chat.models.Group;
import com.example.telebeetle.Entity.Usuario;

public class GroupChatViewModel extends ViewModel {
    public MutableLiveData<String> getGuid() {
        return guid;
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<String> getGroup_description() {
        return group_description;
    }

    public MutableLiveData<String> getGroup_type() {
        return group_type;
    }

    public MutableLiveData<String> getGroup_owner() {
        return group_owner;
    }

    public MutableLiveData<String> getMember_count() {
        return member_count;
    }

    private final MutableLiveData<String> guid = new MutableLiveData<>();
    private final MutableLiveData<String> name = new MutableLiveData<>();
    private final MutableLiveData<String> group_description = new MutableLiveData<>();
    private final MutableLiveData<String> group_type = new MutableLiveData<>();
    private final MutableLiveData<String> group_owner = new MutableLiveData<>();

    private final MutableLiveData<String> member_count = new MutableLiveData<>();

    public MutableLiveData<Group> getGroup() {
        return group;
    }

    private final MutableLiveData<Group> group = new MutableLiveData<>();
}
