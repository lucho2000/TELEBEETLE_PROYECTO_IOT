package com.example.telebeetle.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.core.ConversationsRequest;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chatuikit.conversations.ConversationsConfiguration;
import com.cometchat.chatuikit.conversations.ConversationsStyle;
import com.cometchat.chatuikit.conversationswithmessages.CometChatConversationsWithMessages;
import com.cometchat.chatuikit.messages.MessagesConfiguration;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.cometchatuikit.UIKitSettings;
import com.cometchat.chatuikit.userswithmessages.CometChatUsersWithMessages;
import com.example.telebeetle.R;
import com.example.telebeetle.databinding.ActivityGeneralViewBinding;
import com.example.telebeetle.databinding.FragmentChatBinding;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    FragmentChatBinding binding;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        CometChatConversationsWithMessages cometChatConversationWithMessages = new CometChatConversationsWithMessages(getContext());
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
        ConversationsConfiguration conversationsConfiguration = new ConversationsConfiguration()
                .hideSeparator(true)
                ;
        cometChatConversationWithMessages.setConversationsConfiguration(conversationsConfiguration);

        return cometChatConversationWithMessages;





    }
}