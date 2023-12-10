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

        GroupChatViewModel groupChatViewModel = new ViewModelProvider(requireActivity())
                .get(GroupChatViewModel.class);

        //ArrayList<Group> listaGrupos = new ArrayList<>();

       /* groupChatViewModel.getGroup().observe(getViewLifecycleOwner(), group -> {
           if(group!=null) {
               Log.d("msg-test","se obtiene la clase Group");

               //Gson gson = new Gson();
               //String jsonString= "\"newKey\":\"New Value\"";
               // Utilizar Gson para convertir la cadena JSON a JsonElement
               //com.google.gson.JsonElement jsonElement = JsonParser.parseString(jsonString);

               // Convertir el JsonElement a un objeto JSONObject de org.json
               //JSONObject jsonObject = null;
               //try {
               //    jsonObject = new JSONObject(jsonString);
              // } catch (JSONException e) {
              //    e.printStackTrace();
              // }
               //group.setMetadata(jsonObject);
               //group.setMembersCount(2);
               //group.setCreatedAt(1700266115);
               //group.setUpdatedAt(1700268337);
               //cometChatGroupsWithMessages.setGroup(group);
               listaGrupos.add(group);
           }

        });*/


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


        /*groupViewModel = new Group();
        groupViewModel.setGuid("-njukuml4jrxkcfyj_-v");
        groupViewModel.setName("Semifinal Basket Damas 3vs3");
        groupViewModel.setMembersCount(2);
        groupViewModel.setDescription("El mejor evento de la historia");
        groupViewModel.setOwner("utynqvlw8gwsnhan4z5ekp9azum2");
        groupViewModel.setGroupType("private");
        groupViewModel.setHasJoined(true);

        if (groupViewModel != null) {
            cometChatGroupsWithMessages.setGroup(groupViewModel);

            Log.d("msg-test", String.valueOf(cometChatGroupsWithMessages));
            Log.d("msg-test", String.valueOf(groupViewModel));
        }*/

        /*if (!listaGrupos.isEmpty()){
            cometChatGroupsWithMessages.setGroup(listaGrupos.get(0));
            listaGrupos.remove(0);
        }*/

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


        cometChatConversationsWithMessages.setConversationsConfiguration(conversationsConfiguration);
        //cometChatConversationsWithMessages.setMessagesConfiguration(messagesConfiguration);
        //return cometChatGroupsWithMessages;
        return cometChatConversationsWithMessages;


        /*SharedPreferences sharedPreferences = requireContext().getSharedPreferences("share_preferences_conf", Context.MODE_PRIVATE);
        String groupUID = sharedPreferences.getString("guid", "null");
        if (!groupUID.equalsIgnoreCase("null")){
            Group group = new Group();
            group.setGuid(groupUID);
            String name = sharedPreferences.getString("name", "null");
            String group_description = sharedPreferences.getString("group_description", "null");
            String group_type = sharedPreferences.getString("group_type", "null");
            String group_owner = sharedPreferences.getString("group_owner", "null");
            String member_count = sharedPreferences.getString("member_count", "null");

            group.setName(name);
            group.setDescription(group_description);
            group.setGroupType(group_type);
            group.setOwner(group_owner);
            group.setMembersCount(Integer.parseInt(member_count));

            Log.d("msg-test","se obtiene la clase Group");
            cometChatGroupsWithMessages.setGroup(group);

        }

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove("guid");
        edit.remove("name");
        edit.remove("group_description");
        edit.remove("group_type");
        edit.remove("group_owner");
        edit.remove("member_count");
        // Aplicar los cambios
        edit.apply();*/



        /*ConversationsStyle style = new ConversationsStyle();


        style.setBorderWidth(0);
        int whitecolor = Color.RED;
        int res = Color.BLACK;
        int border = 50;
        style.setSeparatorColor(whitecolor);
        style.setCornerRadius(1);
        style.setBackground(whitecolor);
        style.setBorderColor(res);
        style.setBorderWidth(border);*/
        //View view = null;

        /*Bundle arguments = getArguments();
        if (arguments != null) {
            // Obtener el valor del extra "guid"
            String groupUID = arguments.getString("guid");
            Group group = new Group();
            group.setGuid(groupUID);
            Log.d("msg-test","se obtiene la clase Group");
            cometChatGroupsWithMessages.setGroup(group);
        }*/

    }


    /*@Override
    public void onResume() {
       super.onResume();
       Bundle args = getArguments();

        if (args != null) {
            mGuid = getArguments().getString("ARG_guid");
            mName = getArguments().getString("ARG_name");
            mDescription = getArguments().getString("ARG_description");
            mType = getArguments().getString("ARG_type");
            mOwner = getArguments().getString("ARG_owner");
            mMemberscount = getArguments().getInt("ARG_memberscount");
        }
        Log.d("msg-test", String.valueOf(mGuid));
        if (mGuid != null) {
            Group group = new Group();
            group.setGuid(mGuid);
            group.setName(mName);
            group.setDescription(mDescription);
            group.setGroupType(mType);
            group.setOwner(mOwner);
            group.setMembersCount(mMemberscount);
            group.setHasJoined(true);
            cometChatGroupsWithMessages.setGroup(group);
            Log.d("msg-test", String.valueOf(group));
        }

    }*/
}