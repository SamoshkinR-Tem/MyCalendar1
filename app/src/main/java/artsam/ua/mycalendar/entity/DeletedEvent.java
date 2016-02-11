package artsam.ua.mycalendar.entity;

public class DeletedEvent {

    String mText;
    String mImgPath;

    public DeletedEvent() {
    }

    public DeletedEvent(String text, String mImgPath) {
        this.mText = text;
        this.mImgPath = mImgPath;
    }

    public String getText() {
        return mText;
    }
    public void setText(String text) {
        this.mText = text;
    }

    public String getImagePath() {
        return mImgPath;
    }
    public void setImagePath(String imgPath) {
        this.mImgPath = imgPath;
    }

}
