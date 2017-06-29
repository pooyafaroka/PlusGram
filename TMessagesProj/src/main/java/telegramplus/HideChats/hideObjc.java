package telegramplus.HideChats;

import android.os.Parcel;
import android.os.Parcelable;



public class hideObjc implements Parcelable {

    int id, dialog_id, hideCode;
    int isChannel = 0, isGroup = 0, isHidden = 0;


    public hideObjc() {
    }


    protected hideObjc(Parcel in) {
        id = in.readInt();
        dialog_id = in.readInt();
        hideCode = in.readInt();
        isChannel = in.readInt();
        isGroup = in.readInt();
        isHidden = in.readInt();
    }

    public static final Creator<hideObjc> CREATOR = new Creator<hideObjc>() {
        @Override
        public hideObjc createFromParcel(Parcel in) {
            return new hideObjc(in);
        }

        @Override
        public hideObjc[] newArray(int size) {
            return new hideObjc[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDialog_id() {
        return dialog_id;
    }

    public void setDialog_id(int dialog_id) {
        this.dialog_id = dialog_id;
    }

    public int getHideCode() {
        return hideCode;
    }

    public void setHideCode(int hideCode) {
        this.hideCode = hideCode;
    }

    public int getIsChannel() {
        return isChannel;
    }

    public void setIsChannel(int isChannel) {
        this.isChannel = isChannel;
    }

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

    public int getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(int isHidden) {
        this.isHidden = isHidden;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(dialog_id);
        dest.writeInt(hideCode);
        dest.writeInt(isChannel);
        dest.writeInt(isGroup);
        dest.writeInt(isHidden);
    }
}
