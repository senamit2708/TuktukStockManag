package smit.aen.tuktukstockmanag.Model;

public class TopicMain {
    private String topicName;
    private int image;

    public TopicMain(String topicName, int image) {
        this.topicName = topicName;
        this.image = image;
    }

    public TopicMain(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
