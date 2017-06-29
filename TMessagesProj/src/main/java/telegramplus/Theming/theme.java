package telegramplus.Theming;

import android.os.Parcel;
import android.os.Parcelable;



public class theme implements Parcelable{

    private  String name , description,thumb1 ,thumb2 , thumb3 , xmllink , imagelink ,xmldata ;


    public theme(){

    }

    public theme(String name, String description, String thumb1, String thumb2, String thumb3, String xmllink, String imagelink , String xmldata) {

        this.name = name;
        this.description = description;
        this.thumb1 = thumb1;
        this.thumb2 = thumb2;
        this.thumb3 = thumb3;
        this.xmllink = xmllink;
        this.imagelink = imagelink;
        this.xmldata = xmldata ;

    }

    protected theme(Parcel in) {
        name = in.readString();
        description = in.readString();
        thumb1 = in.readString();
        thumb2 = in.readString();
        thumb3 = in.readString();
        xmllink = in.readString();
        imagelink = in.readString();
        xmldata = in.readString();
    }

    public static final Creator<theme> CREATOR = new Creator<theme>() {
        @Override
        public theme createFromParcel(Parcel in) {
            return new theme(in);
        }

        @Override
        public theme[] newArray(int size) {
            return new theme[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumb1() {
        return thumb1;
    }

    public void setThumb1(String thumb1) {
        this.thumb1 = thumb1;
    }

    public String getThumb2() {
        return thumb2;
    }

    public void setThumb2(String thumb2) {
        this.thumb2 = thumb2;
    }

    public String getThumb3() {
        return thumb3;
    }

    public void setThumb3(String thumb3) {
        this.thumb3 = thumb3;
    }

    public String getXmllink() {
        return xmllink;
    }

    public void setXmllink(String xmllink) {
        this.xmllink = xmllink;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    public String getXmldata() {
        return xmldata;
    }

    public void setXmldata(String xmldata) {
        this.xmldata = xmldata;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(thumb1);
        dest.writeString(thumb2);
        dest.writeString(thumb3);
        dest.writeString(xmllink);
        dest.writeString(imagelink);
        dest.writeString(xmldata);
    }
}
