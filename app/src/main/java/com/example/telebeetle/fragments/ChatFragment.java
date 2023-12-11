package com.example.telebeetle.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.view.menu.ListMenuItemView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.chat.constants.CometChatConstants;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.core.ConversationsRequest;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.Conversation;
import com.cometchat.chat.models.Group;
import com.cometchat.chatuikit.conversations.CometChatConversations;
import com.cometchat.chatuikit.conversations.ConversationsConfiguration;
import com.cometchat.chatuikit.conversations.ConversationsStyle;
import com.cometchat.chatuikit.conversationswithmessages.CometChatConversationsWithMessages;
import com.cometchat.chatuikit.groups.GroupsConfiguration;
import com.cometchat.chatuikit.groupswithmessages.CometChatGroupsWithMessages;
import com.cometchat.chatuikit.messagelist.MessageListConfiguration;
import com.cometchat.chatuikit.messagelist.MessageListStyle;
import com.cometchat.chatuikit.messages.CometChatMessages;
import com.cometchat.chatuikit.messages.MessagesConfiguration;
import com.cometchat.chatuikit.messages.MessagesStyle;
import com.cometchat.chatuikit.shared.Interfaces.Function2;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.cometchatuikit.UIKitSettings;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatMessageTemplate;
import com.cometchat.chatuikit.shared.models.CometChatOption;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.views.CometChatDate.DateStyle;
import com.cometchat.chatuikit.shared.views.CometChatListBase.CometChatListBase;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.userswithmessages.CometChatUsersWithMessages;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityGeneralViewBinding;
import com.example.telebeetle.databinding.FragmentChatBinding;
import com.example.telebeetle.viewmodels.GroupChatViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {


    FragmentChatBinding binding;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_guid = "guid";
    private static final String ARG_name = "name";

    private static final String ARG_description = "description";

    private static final String ARG_type = "type";

    private static final String ARG_owner = "owner";

    private static final String ARG_memberscount = "memberscount";

    // TODO: Rename and change types of parameters
    private String mGuid;
    private String mName;

    private String mDescription;

    private String mType;

    private String mOwner;

    private int mMemberscount;

    boolean mFlag = false;

    public ChatFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String guid, String name, String description, String type,String owner,int memberscount) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_guid, guid);
        args.putString(ARG_name, name);
        args.putString(ARG_description, description);
        args.putString(ARG_type, type);
        args.putString(ARG_owner, owner);
        args.putInt(ARG_memberscount, memberscount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGuid = getArguments().getString("ARG_guid");
            mName = getArguments().getString("ARG_name");
            mDescription = getArguments().getString("ARG_description");
            mType = getArguments().getString("ARG_type");
            mOwner = getArguments().getString("ARG_owner");
            mMemberscount = getArguments().getInt("ARG_memberscount");
            mFlag = getArguments().getBoolean("flag");
        }
    }

    public Group groupViewModel = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Context context = requireContext();

        //CometChatGroupsWithMessages cometChatGroupsWithMessages = new CometChatGroupsWithMessages(getContext());
        CometChatConversationsWithMessages cometChatConversationsWithMessages = new CometChatConversationsWithMessages(getContext());
        Log.d("msg-test", String.valueOf(mGuid));
        if (mGuid != null && mFlag) {
            Group group = new Group();
            group.setGuid(mGuid);
            group.setName(mName);
            group.setDescription(mDescription);
            group.setGroupType(mType);
            group.setOwner(mOwner);
            group.setMembersCount(mMemberscount);
            group.setHasJoined(true);
            //cometChatGroupsWithMessages.setGroup(group);
            cometChatConversationsWithMessages.setGroup(group);
            mFlag=false;
            Log.d("msg-test", String.valueOf(group));
        }




        /*cometChatGroupsWithMessages.hideMenuIcon(true);
        cometChatGroupsWithMessages.hideSeparator(true);
        GroupsConfiguration groupsConfiguration= new GroupsConfiguration()
                .setHideSeparator(true)
                ;
        cometChatGroupsWithMessages.setGroupsConfiguration(groupsConfiguration);*/
        //cometChatConversationsWithMessages.hideToolbar(true);
        cometChatConversationsWithMessages.hideMenuIcon(true);
        //cometChatConversationsWithMessages.hideSeparator(true);

        int redColorValue = Color.parseColor("#a83232");
        ListItemStyle listItemStyle= new ListItemStyle();
        listItemStyle.setBorderWidth(0);

        //listItemStyle.setTitleAppearance(redColorValue);
        //listItemStyle.setTitleColor(redColorValue);



        //listItemStyle.setBackground(redColorValue);


        //int redColorValue = Color.parseColor("#a83232");
        //DateStyle dateStyle = new DateStyle();
        //dateStyle.setBorderWidth(0);
        //dateStyle.setBorderColor(redColorValue);

        //dateStyle.setTextColor(redColorValue);


        ///CometChatConversations cometChatConversations = new CometChatConversations(getContext());


        ConversationsStyle conversationsStyle = new ConversationsStyle();
        conversationsStyle.setBorderWidth(0);
        conversationsStyle.setCornerRadius(0);
        //int whiteColor = Color.parseColor("#FFFFFF");
       // conversationsStyle.setTitleColor(whiteColor);


        //Conversation conversation = new Conversation("conversationstr1", CometChatConstants.CONVERSATION_TYPE_GROUP);

        ConversationsConfiguration conversationsConfiguration = new ConversationsConfiguration();
        conversationsConfiguration.setListItemStyle(listItemStyle);
        conversationsConfiguration.setStyle(conversationsStyle);

        //Function2<android.content.Context,com.cometchat.chat.models.Conversation,android.view.View> tail =conversationsConfiguration.getTail();
        //tail.apply();

        //conversationsConfiguration.setTail();
        //conversationsConfiguration.hideSeparator(true);

        //conversationsConfiguration.setDateStyle(dateStyle);

        //Function2<Context, Conversation, View> = ()




        //int griscolor = Color.parseColor("#f0f0f0"); //#c7c7c7


        /*MessagesConfiguration messagesConfiguration = new MessagesConfiguration();
        MessagesStyle messagesStyle = new MessagesStyle();
        messagesStyle.setBorderWidth(0);
        //messagesStyle.setBackground(griscolor);
        messagesStyle.setCornerRadius(0);
        messagesConfiguration.setStyle(messagesStyle);

        MessageListConfiguration messageListConfiguration = new MessageListConfiguration();
        MessageListStyle messageListStyle = new MessageListStyle();

        //messageListStyle.setBackground(griscolor);
        messageListStyle.setCornerRadius(0);
        messageListConfiguration.setStyle(messageListStyle);
        messageListConfiguration.showAvatar(true);

        messagesConfiguration.setMessageListConfiguration(messageListConfiguration);*/
        cometChatConversationsWithMessages.setTitle("Chats Grupales de Eventos");
        cometChatConversationsWithMessages.setTitleAppearance(R.style.CustomTitleStyle);
        //cometChatConversationsWithMessages.setSearchBackground(redColorValue);
        //cometChatConversationsWithMessages.setSearchPlaceholderText("Search");
        //cometChatConversationsWithMessages.hideSearch(false);
        cometChatConversationsWithMessages.setConversationsConfiguration(conversationsConfiguration);
        //cometChatConversationsWithMessages.setBackground(redColorValue);
        //cometChatConversationsWithMessages.setMessagesConfiguration(messagesConfiguration);
        //return cometChatGroupsWithMessages;
        return cometChatConversationsWithMessages;

    }

}