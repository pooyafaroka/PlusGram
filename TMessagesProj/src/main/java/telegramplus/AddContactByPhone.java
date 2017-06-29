package telegramplus;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import ir.mmnotimm.telegramplus.R;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.LayoutHelper;



public class AddContactByPhone extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {

    private View doneButton;
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText phoneNumberField;


    private final static int done_button = 1;

    public AddContactByPhone() {

    }

    @Override
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.updateInterfaces);
        return super.onFragmentCreate();
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.updateInterfaces);
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);

        actionBar.setTitle(LocaleController.getString("AddContactTitle", R.string.AddContactTitle));

        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                } else if (id == done_button) {
                    if (firstNameField.getText().length() != 0 && phoneNumberField.getText().length() != 0) {
                        TLRPC.User tL_user = new TLRPC.TL_user();
                        tL_user.phone = phoneNumberField.getText().toString();
                        tL_user.first_name = firstNameField.getText().toString();
                        if (lastNameField.getText().length() != 0)
                            tL_user.last_name = lastNameField.getText().toString();

                        ContactsController.getInstance().addContact(tL_user);
                        finishFragment();
                        NotificationCenter instance = NotificationCenter.getInstance();
                        int i = NotificationCenter.updateInterfaces;
                        Object[] objArr = new Object[done_button];
                        objArr[0] = Integer.valueOf(done_button);
                        instance.postNotificationName(i, objArr);
                    }
                }
            }
        });

        ActionBarMenu menu = actionBar.createMenu();
        doneButton = menu.addItemWithWidth(done_button, R.drawable.ic_done, AndroidUtilities.dp(56));

        fragmentView = new ScrollView(context);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ((ScrollView) fragmentView).addView(linearLayout);
        ScrollView.LayoutParams layoutParams2 = (ScrollView.LayoutParams) linearLayout.getLayoutParams();
        layoutParams2.width = ScrollView.LayoutParams.MATCH_PARENT;
        layoutParams2.height = ScrollView.LayoutParams.WRAP_CONTENT;
        linearLayout.setLayoutParams(layoutParams2);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        FrameLayout frameLayout = new FrameLayout(context);
        linearLayout.addView(frameLayout);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) frameLayout.getLayoutParams();
        layoutParams.topMargin = AndroidUtilities.dp(24);
        layoutParams.leftMargin = AndroidUtilities.dp(24);
        layoutParams.rightMargin = AndroidUtilities.dp(24);
        layoutParams.width = LayoutHelper.MATCH_PARENT;
        layoutParams.height = LayoutHelper.WRAP_CONTENT;
        frameLayout.setLayoutParams(layoutParams);


        phoneNumberField = new EditText(context);
        phoneNumberField.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        phoneNumberField.setHintTextColor(0xff979797);
        phoneNumberField.setTextColor(0xff212121);
        phoneNumberField.setMaxLines(1);
        phoneNumberField.setLines(1);
        phoneNumberField.setSingleLine(true);
        phoneNumberField.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
        phoneNumberField.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        phoneNumberField.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        phoneNumberField.setHint(LocaleController.getString("PhoneNumber", R.string.PhoneNumber));
        AndroidUtilities.clearCursorDrawable(phoneNumberField);
        linearLayout.addView(phoneNumberField);
        layoutParams = (LinearLayout.LayoutParams) phoneNumberField.getLayoutParams();
        layoutParams.topMargin = AndroidUtilities.dp(24);
        layoutParams.height = AndroidUtilities.dp(36);
        layoutParams.leftMargin = AndroidUtilities.dp(24);
        layoutParams.rightMargin = AndroidUtilities.dp(24);
        layoutParams.width = LayoutHelper.MATCH_PARENT;
        phoneNumberField.setLayoutParams(layoutParams);
        phoneNumberField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    firstNameField.requestFocus();
                    firstNameField.setSelection(firstNameField.length());
                    return true;
                }
                return false;
            }
        });


        firstNameField = new EditText(context);
        firstNameField.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        firstNameField.setHintTextColor(0xff979797);
        firstNameField.setTextColor(0xff212121);
        firstNameField.setMaxLines(1);
        firstNameField.setLines(1);
        firstNameField.setSingleLine(true);
        firstNameField.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
        firstNameField.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        firstNameField.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        firstNameField.setHint(LocaleController.getString("FirstName", R.string.FirstName));
        AndroidUtilities.clearCursorDrawable(firstNameField);
        linearLayout.addView(firstNameField);
        layoutParams = (LinearLayout.LayoutParams) firstNameField.getLayoutParams();
        layoutParams.topMargin = AndroidUtilities.dp(24);
        layoutParams.height = AndroidUtilities.dp(36);
        layoutParams.leftMargin = AndroidUtilities.dp(24);
        layoutParams.rightMargin = AndroidUtilities.dp(24);
        layoutParams.width = LayoutHelper.MATCH_PARENT;
        firstNameField.setLayoutParams(layoutParams);
        firstNameField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    lastNameField.requestFocus();
                    lastNameField.setSelection(lastNameField.length());
                    return true;
                }
                return false;
            }
        });

        lastNameField = new EditText(context);
        lastNameField.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        lastNameField.setHintTextColor(0xff979797);
        lastNameField.setTextColor(0xff212121);
        lastNameField.setMaxLines(1);
        lastNameField.setLines(1);
        lastNameField.setSingleLine(true);
        lastNameField.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
        lastNameField.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        lastNameField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        lastNameField.setHint(LocaleController.getString("LastName", R.string.LastName));
        AndroidUtilities.clearCursorDrawable(lastNameField);
        linearLayout.addView(lastNameField);
        layoutParams = (LinearLayout.LayoutParams) lastNameField.getLayoutParams();
        layoutParams.topMargin = AndroidUtilities.dp(16);
        layoutParams.height = AndroidUtilities.dp(36);
        layoutParams.leftMargin = AndroidUtilities.dp(24);
        layoutParams.rightMargin = AndroidUtilities.dp(24);
        layoutParams.width = LayoutHelper.MATCH_PARENT;
        lastNameField.setLayoutParams(layoutParams);
        lastNameField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    doneButton.performClick();
                    return true;
                }
                return false;
            }
        });

        TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(getParentActivity());
        textInfoPrivacyCell.setText(LocaleController.getString("AddContactHelp", R.string.AddContactHelp));
        textInfoPrivacyCell.setBackgroundResource(R.drawable.greydivider_bottom);
        linearLayout.addView(textInfoPrivacyCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER, 0, 25, 0, 0));


        return fragmentView;
    }


    public void didReceivedNotification(int id, Object... args) {
        if (id == NotificationCenter.updateInterfaces) {
            int mask = (Integer) args[0];
            if ((mask & MessagesController.UPDATE_MASK_AVATAR) != 0 || (mask & MessagesController.UPDATE_MASK_STATUS) != 0) {

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);
        boolean animations = preferences.getBoolean("view_animations", true);
        if (!animations) {
            firstNameField.requestFocus();
            AndroidUtilities.showKeyboard(firstNameField);
        }
    }

    @Override
    public void onTransitionAnimationEnd(boolean isOpen, boolean backward) {
        if (isOpen) {
            firstNameField.requestFocus();
            AndroidUtilities.showKeyboard(firstNameField);
        }
    }
}