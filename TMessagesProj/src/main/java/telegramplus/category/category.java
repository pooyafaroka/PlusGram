package telegramplus.category;

import android.os.Parcel;
import android.os.Parcelable;



public class category implements Parcelable {

    int id;
    String name;
    int size = 0;

    public category(){

    }

    public category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public category(int id, String name, int size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    protected category(Parcel in) {
        id = in.readInt();
        name = in.readString();
        size = in.readInt();
    }

    public static final Creator<category> CREATOR = new Creator<category>() {
        @Override
        public category createFromParcel(Parcel in) {
            return new category(in);
        }

        @Override
        public category[] newArray(int size) {
            return new category[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(size);
    }
}
