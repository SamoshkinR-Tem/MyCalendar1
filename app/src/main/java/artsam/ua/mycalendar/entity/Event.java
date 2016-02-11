package artsam.ua.mycalendar.entity;

public class Event {

    String mText;
    String mImgPath;

    public Event() {
    }

    public Event(String text, String imgPath) {
        this.mText = text;
        this.mImgPath = imgPath;
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

    @Override
    public String toString() {
        return "Event{" +
                "mText='" + mText + '\'' +
                ", mImgPath='" + mImgPath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        if (!mText.equals(event.mText)) return false;
        return mImgPath.equals(event.mImgPath);

    }

    @Override
    public int hashCode() {
        int result = mText.hashCode();
        result = 31 * result + mImgPath.hashCode();
        return result;
    }
}
