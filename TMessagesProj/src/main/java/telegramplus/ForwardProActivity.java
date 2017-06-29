package telegramplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import ir.mmnotimm.telegramplus.R;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.SizeNotifierFrameLayout;



public class ForwardProActivity extends BaseFragment {
    private MessageObject selectedObject;
    protected ChatActivityEnterView chatActivityEnterView;
    private FrameLayout emptyViewContainer;
    private TextView media;
    private TextView mediaCaption;
    private final static int id_chat_compose_panel = 1000;
    private String caption = "";
    protected TLRPC.Chat currentChat;

    public ForwardProActivity(MessageObject selectedObject, TLRPC.Chat currentChat) {
        this.selectedObject = new MessageObject(newMessage(selectedObject.messageOwner), null, true);
        this.selectedObject.photoThumbs = selectedObject.photoThumbs;
        this.currentChat = currentChat;
    }

    @Override
    public View createView(final Context context) {
        actionBar.setBackgroundColor(Theme.ACTION_BAR_MEDIA_PICKER_COLOR);
        actionBar.setItemsBackgroundColor(Theme.ACTION_BAR_PICKER_SELECTOR_COLOR);
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString("ProForward", R.string.ProForward));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });


        fragmentView = new FrameLayout(context);
        fragmentView.setLayoutParams(new FrameLayout.LayoutParams(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        MessageObject myObject = selectedObject;
        if (myObject.caption != null)
            caption = myObject.caption.toString();
        else if (myObject.messageText != null)
            caption = myObject.messageText.toString();

//        fragmentView = new SizeNotifierFrameLayout(context) {
//
//            int inputFieldHeight = 0;
//
//            @Override
//            protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
//                boolean result = super.drawChild(canvas, child, drawingTime);
//                if (child == actionBar) {
//                    parentLayout.drawHeaderShadow(canvas, actionBar.getMeasuredHeight());
//                }
//                return result;
//            }
//
//            @Override
//            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//                int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//                int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//                setMeasuredDimension(widthSize, heightSize);
//                heightSize -= getPaddingTop();
//
//                measureChildWithMargins(actionBar, widthMeasureSpec, 0, heightMeasureSpec, 0);
//                int actionBarHeight = actionBar.getMeasuredHeight();
//                heightSize -= actionBarHeight;
//
//                int keyboardSize = getKeyboardHeight();
//
//                if (keyboardSize <= AndroidUtilities.dp(20) && !AndroidUtilities.isInMultiwindow) {
//                    heightSize -= chatActivityEnterView.getEmojiPadding();
//                }
//
//                int childCount = getChildCount();
//
//                measureChildWithMargins(chatActivityEnterView, widthMeasureSpec, 0, heightMeasureSpec, 0);
//                inputFieldHeight = chatActivityEnterView.getMeasuredHeight();
//
//                for (int i = 0; i < childCount; i++) {
//                    View child = getChildAt(i);
//                    if (child == null || child.getVisibility() == GONE || child == chatActivityEnterView || child == actionBar) {
//                        continue;
//                    }
//                  /*  if (child == chatListView || child == progressView) {
//                        int contentWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
//                        int contentHeightSpec = MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10), heightSize - inputFieldHeight + AndroidUtilities.dp(2 + (chatActivityEnterView.isTopViewVisible() ? 48 : 0))), MeasureSpec.EXACTLY);
//                        child.measure(contentWidthSpec, contentHeightSpec);
//                    } else */
//                    if (child == emptyViewContainer) {
//                        int contentWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
//                        int contentHeightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
//                        child.measure(contentWidthSpec, contentHeightSpec);
//                    } else if (chatActivityEnterView.isPopupView(child)) {
//                        if (AndroidUtilities.isInMultiwindow) {
//                            if (AndroidUtilities.isTablet()) {
//                                child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(320), heightSize - inputFieldHeight + actionBarHeight - AndroidUtilities.statusBarHeight + getPaddingTop()), MeasureSpec.EXACTLY));
//                            } else {
//                                child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize - inputFieldHeight + actionBarHeight - AndroidUtilities.statusBarHeight + getPaddingTop(), MeasureSpec.EXACTLY));
//                            }
//                        } else {
//                            child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(child.getLayoutParams().height, MeasureSpec.EXACTLY));
//                        }
//                    } /*else if (child == mentionContainer) {
//                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mentionContainer.getLayoutParams();
//                        int height;
//                        mentionListViewIgnoreLayout = true;
//
//
//                        if (mentionsAdapter.isBotContext() && mentionsAdapter.isMediaLayout()) {
//                            int size = mentionGridLayoutManager.getRowsCount(widthSize);
//                            int maxHeight = size * 102;
//                            if (mentionsAdapter.isBotContext()) {
//                                if (mentionsAdapter.getBotContextSwitch() != null) {
//                                    maxHeight += 34;
//                                }
//                            }
//                            height = heightSize - chatActivityEnterView.getMeasuredHeight() + (maxHeight != 0 ? AndroidUtilities.dp(2) : 0);
//                            mentionListView.setPadding(0, Math.max(0, height - AndroidUtilities.dp(Math.min(maxHeight, 68 * 1.8f))), 0, 0);
//                        } else {
//                            int size = mentionsAdapter.getItemCount();
//                            int maxHeight = 0;
//                            if (mentionsAdapter.isBotContext()) {
//                                if (mentionsAdapter.getBotContextSwitch() != null) {
//                                    maxHeight += 36;
//                                    size -= 1;
//                                }
//                                maxHeight += size * 68;
//                            } else {
//                                maxHeight += size * 36;
//                            }
//                            height = heightSize - chatActivityEnterView.getMeasuredHeight() + (maxHeight != 0 ? AndroidUtilities.dp(2) : 0);
//                            mentionListView.setPadding(0, Math.max(0, height - AndroidUtilities.dp(Math.min(maxHeight, 68 * 1.8f))), 0, 0);
//                        }
//
//                        layoutParams.height = height;
//                        layoutParams.topMargin = 0;
//
//                        mentionListViewIgnoreLayout = false;
//                        child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY));
//                    }*/ else {
//                        measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
//                    }
//                }
//            }
//        };

        fragmentView = new SizeNotifierFrameLayout(context);
        SizeNotifierFrameLayout contentView = (SizeNotifierFrameLayout) fragmentView;
        contentView.setBackgroundImage(ApplicationLoader.getCachedWallpaper());


        ScrollView scrollView = new ScrollView(context);


        LinearLayout l = new LinearLayout(context);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setGravity(Gravity.TOP);
        l.setPadding(0, 80, 0, 250);

        ChatMessageCell chatMessageCell = new ChatMessageCell(getParentActivity());
        MessageObject temp = selectedObject;


        if (temp.messageOwner != null) {
            temp.messageOwner.message = "";
            if (temp.messageOwner.media != null)
                temp.messageOwner.media.caption = "";
        }
        temp.caption = "";
        temp.messageText = "";
        chatMessageCell.setMessageObject(temp);
        chatMessageCell.setOnClickListener(null);
        chatMessageCell.setOnTouchListener(null);
        chatMessageCell.setOnLongClickListener(null);
        chatMessageCell.setDelegate(new ChatMessageCell.ChatMessageCellDelegate() {
            @Override
            public void didPressedUserAvatar(ChatMessageCell cell, TLRPC.User user) {

            }

            @Override
            public void didPressedViaBot(ChatMessageCell cell, String username) {

            }

            @Override
            public void didPressedChannelAvatar(ChatMessageCell cell, TLRPC.Chat chat, int postId) {

            }

            @Override
            public void didPressedCancelSendButton(ChatMessageCell cell) {

            }

            @Override
            public void didLongPressed(ChatMessageCell cell) {

            }

            @Override
            public void didPressedReplyMessage(ChatMessageCell cell, int id) {

            }

            @Override
            public void didPressedUrl(MessageObject messageObject, ClickableSpan url, boolean longPress) {

            }

            @Override
            public void needOpenWebView(String url, String title, String description, String originalUrl, int w, int h) {

            }

            @Override
            public void didPressedImage(ChatMessageCell cell) {

            }

            @Override
            public void didPressedShare(ChatMessageCell cell) {

            }

            @Override
            public void didPressedOther(ChatMessageCell cell) {

            }

            @Override
            public void didPressedBotButton(ChatMessageCell cell, TLRPC.KeyboardButton button) {

            }

            @Override
            public boolean needPlayAudio(MessageObject messageObject) {
                return false;
            }

            @Override
            public boolean canPerformActions() {
                return false;
            }
        });
        l.addView(chatMessageCell, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.TOP, 0, 0, 0, 15));


        emptyViewContainer = new FrameLayout(context);
        emptyViewContainer.setBackgroundColor(-1);
        mediaCaption = new TextView(context);
        mediaCaption.setTextSize(2, 15.f);
        mediaCaption.setPadding(12, 0, 12, 0);
        mediaCaption.setSingleLine(true);
        mediaCaption.setEllipsize(TextUtils.TruncateAt.END);
        mediaCaption.setMaxLines(1);
        mediaCaption.setGravity(Gravity.CENTER_VERTICAL);
        mediaCaption.setBackgroundColor(0xfffafafa);
        mediaCaption.setText(LocaleController.getString("MediaCaption", R.string.MediaCaption) + " : ");
        scrollView.addView(l);
        scrollView.setPadding(0, 80, 0, 250);
        contentView.addView(scrollView, contentView.getChildCount() - 1, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.LEFT | Gravity.BOTTOM));


        emptyViewContainer = new FrameLayout(context);
        emptyViewContainer.setVisibility(View.INVISIBLE);
        contentView.addView(emptyViewContainer, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));
        emptyViewContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        chatActivityEnterView = new ChatActivityEnterView(getParentActivity(), contentView, null, false);
        chatActivityEnterView.setDialogId(selectedObject.getDialogId());

        LinearLayout l2 = new LinearLayout(context);
        l2.setOrientation(LinearLayout.VERTICAL);
        l2.addView(mediaCaption, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 34, Gravity.TOP | (LocaleController.isRTL ? 5 : 3), 0, 0, 0, 0));
        l2.addView(chatActivityEnterView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.LEFT | Gravity.BOTTOM));

        contentView.addView(l2, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM));


        emptyViewContainer = new FrameLayout(context);
        emptyViewContainer.setBackgroundColor(-1);
        media = new TextView(context);
        media.setTextSize(2, 17.f);
        media.setPadding(12, 0, 12, 0);
        media.setSingleLine(true);
        media.setEllipsize(TextUtils.TruncateAt.END);
        media.setMaxLines(1);
        media.setGravity(Gravity.CENTER_VERTICAL);
        media.setBackgroundColor(0xfffafafa);
        media.setText(LocaleController.getString("Media", R.string.Media) + " : ");
        contentView.addView(media, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 34, Gravity.TOP | (LocaleController.isRTL ? 5 : 3), 0, 0, 0, 0));


        chatActivityEnterView.setDelegate(new ChatActivityEnterView.ChatActivityEnterViewDelegate() {
            @Override
            public void onMessageSend(CharSequence message) {

            }

            @Override
            public void needSendTyping() {

            }

            @Override
            public void onTextChanged(CharSequence text, boolean bigChange) {

            }

            @Override
            public void onAttachButtonHidden() {

            }

            @Override
            public void onAttachButtonShow() {

            }

            @Override
            public void onWindowSizeChanged(int size) {

            }

            @Override
            public void onStickersTab(boolean opened) {

            }

            @Override
            public void onMessageEditEnd(boolean loading) {

            }
        });
        chatActivityEnterView.setAllowStickersAndGifs(false, false);
        chatActivityEnterView.getMessageEditText().setText(caption);


        chatActivityEnterView.getSendButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDone();
            }
        });


        return fragmentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateTheme();
        if (constant.AnalyticInitialized)
        ApplicationLoader.getInstance().trackScreenView("ForwardProActivity");
    }

    private void updateTheme() {
        SharedPreferences themePrefs = ApplicationLoader.applicationContext.getSharedPreferences(AndroidUtilities.THEME_PREFS, AndroidUtilities.THEME_PREFS_MODE);
        int def = themePrefs.getInt("themeColor", AndroidUtilities.defColor);
        actionBar.setBackgroundColor(themePrefs.getInt("prefHeaderColor", def));
        actionBar.setTitleColor(themePrefs.getInt("prefHeaderTitleColor", 0xffffffff));

        Drawable back = getParentActivity().getResources().getDrawable(R.drawable.ic_ab_back);
        back.setColorFilter(themePrefs.getInt("prefHeaderIconsColor", 0xffffffff), PorterDuff.Mode.MULTIPLY);
        actionBar.setBackButtonDrawable(back);
    }


    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (chatActivityEnterView != null) {
            chatActivityEnterView.onDestroy();
        }
    }


    private void editDone() {


        final MessageObject m = selectedObject;

        if (m != null && chatActivityEnterView.getMessageEditText().getText() != null) {
            m.messageOwner.message = chatActivityEnterView.getMessageEditText().getText().toString();
            if (m.messageOwner.media != null)
                m.messageOwner.media.caption = chatActivityEnterView.getMessageEditText().getText().toString();

            m.caption = chatActivityEnterView.getMessageEditText().getText().toString();
            m.messageText = chatActivityEnterView.getMessageEditText().getText().toString();
            m.messageOwner.from_id = -1;
            m.applyNewText();
        }

        final ShareAlert d = new ShareAlert(getParentActivity(), m, ChatObject.isChannel(currentChat) && !currentChat.megagroup && currentChat.username != null && currentChat.username.length() > 0);
        d.getQuoteSwitch().setChecked(false);
        d.getQuoteSwitch().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (d.getQuoteSwitch().isChecked())
                    d.getQuoteSwitch().setChecked(false);

                Toast.makeText(getParentActivity(), getParentActivity().getResources().getString(R.string.ProForwardError), Toast.LENGTH_LONG).show();

            }
        });
        showDialog2(d);
        chatActivityEnterView.openKeyboard();
        d.getDoneButtonTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.DoneClicked();
                finishFragment();
            }
        });


    }



    private TLRPC.Message newMessage(TLRPC.Message message) {
        if (message == null) {
            return null;
        }
        TLRPC.Message message2 = new TLRPC.Message();
        if (message instanceof TLRPC.TL_message) {
            message2 = new TLRPC.TL_message();
        } else if (message instanceof TLRPC.TL_message_secret) {
            message2 = new TLRPC.TL_message_secret();
        }
        message2.id = message.id;
        message2.from_id = message.from_id;
        message2.to_id = message.to_id;
        message2.date = message.date;
        message2.action = message.action;
        message2.reply_to_msg_id = message.reply_to_msg_id;
        message2.fwd_from = message.fwd_from;
        message2.reply_to_random_id = message.reply_to_random_id;
        message2.via_bot_name = message.via_bot_name;
        message2.edit_date = message.edit_date;
        message2.silent = message.silent;
        message2.message = message.message;
        if (message.media != null) {
            message2.media = newMessageMedia(message.media);
        }
        message2.flags = message.flags;
        message2.mentioned = message.mentioned;
        message2.media_unread = message.media_unread;
        message2.out = message.out;
        message2.unread = message.unread;
        message2.entities = message.entities;
        message2.reply_markup = message.reply_markup;
        message2.views = message.views;
        message2.via_bot_id = message.via_bot_id;
        message2.send_state = message.send_state;
        message2.fwd_msg_id = message.fwd_msg_id;
        message2.attachPath = message.attachPath;
        message2.params = message.params;
        message2.random_id = message.random_id;
        message2.local_id = message.local_id;
        message2.dialog_id = message.dialog_id;
        message2.ttl = message.ttl;
        message2.destroyTime = message.destroyTime;
        message2.layer = message.layer;
        message2.seq_in = message.seq_in;
        message2.seq_out = message.seq_out;
        message2.replyMessage = message.replyMessage;
        return message2;
    }

    private TLRPC.MessageMedia newMessageMedia(TLRPC.MessageMedia messageMedia) {
        TLRPC.MessageMedia tL_messageMediaUnsupported_old = messageMedia instanceof TLRPC.TL_messageMediaUnsupported_old ? new TLRPC.TL_messageMediaUnsupported_old() : messageMedia instanceof TLRPC.TL_messageMediaAudio_layer45 ? new TLRPC.TL_messageMediaAudio_layer45() : messageMedia instanceof TLRPC.TL_messageMediaPhoto_old ? new TLRPC.TL_messageMediaPhoto_old() : messageMedia instanceof TLRPC.TL_messageMediaUnsupported ? new TLRPC.TL_messageMediaUnsupported() : messageMedia instanceof TLRPC.TL_messageMediaEmpty ? new TLRPC.TL_messageMediaEmpty() : messageMedia instanceof TLRPC.TL_messageMediaVenue ? new TLRPC.TL_messageMediaVenue() : messageMedia instanceof TLRPC.TL_messageMediaVideo_old ? new TLRPC.TL_messageMediaVideo_old() : messageMedia instanceof TLRPC.TL_messageMediaDocument_old ? new TLRPC.TL_messageMediaDocument_old() : messageMedia instanceof TLRPC.TL_messageMediaDocument ? new TLRPC.TL_messageMediaDocument() : messageMedia instanceof TLRPC.TL_messageMediaContact ? new TLRPC.TL_messageMediaContact() : messageMedia instanceof TLRPC.TL_messageMediaPhoto ? new TLRPC.TL_messageMediaPhoto() : messageMedia instanceof TLRPC.TL_messageMediaVideo_layer45 ? new TLRPC.TL_messageMediaVideo_layer45() : messageMedia instanceof TLRPC.TL_messageMediaWebPage ? new TLRPC.TL_messageMediaWebPage() : messageMedia instanceof TLRPC.TL_messageMediaGeo ? new TLRPC.TL_messageMediaGeo() : new TLRPC.MessageMedia();
        tL_messageMediaUnsupported_old.bytes = messageMedia.bytes;
        tL_messageMediaUnsupported_old.caption = messageMedia.caption;
        tL_messageMediaUnsupported_old.photo = messageMedia.photo;
        tL_messageMediaUnsupported_old.audio_unused = messageMedia.audio_unused;
        tL_messageMediaUnsupported_old.geo = messageMedia.geo;
        tL_messageMediaUnsupported_old.title = messageMedia.title;
        tL_messageMediaUnsupported_old.address = messageMedia.address;
        tL_messageMediaUnsupported_old.provider = messageMedia.provider;
        tL_messageMediaUnsupported_old.venue_id = messageMedia.venue_id;
        tL_messageMediaUnsupported_old.document = messageMedia.document;
        tL_messageMediaUnsupported_old.video_unused = messageMedia.video_unused;
        tL_messageMediaUnsupported_old.phone_number = messageMedia.phone_number;
        tL_messageMediaUnsupported_old.first_name = messageMedia.first_name;
        tL_messageMediaUnsupported_old.last_name = messageMedia.last_name;
        tL_messageMediaUnsupported_old.user_id = messageMedia.user_id;
        tL_messageMediaUnsupported_old.webpage = messageMedia.webpage;
        return tL_messageMediaUnsupported_old;
    }


}
