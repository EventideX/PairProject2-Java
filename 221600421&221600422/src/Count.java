import java.io.IOException;

public interface Count {
    public int CharCount() throws IOException;
    public int WordCount() throws IOException;
    public int LineCount() throws IOException;
    public void OutPutWords()throws IOException;
    public void CloseFile() throws IOException;
}
