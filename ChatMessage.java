package pediatriccare;
//////////////////////////
public class ChatMessage {
    private final String senderName;
    private final String recipientName;
    private final String content;
    private final String timestamp;
    private final boolean isFromStaff;

    public ChatMessage(String senderName, String recipientName, String content, String timestamp, boolean isFromStaff) {
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.content = content;
        this.timestamp = timestamp;
        this.isFromStaff = isFromStaff;
    }

    public String senderName() { return senderName; }
    public String recipientName() { return recipientName; }
    public String content() { return content; }
    public String timestamp() { return timestamp; }
    public boolean isFromStaff() { return isFromStaff; }
}

