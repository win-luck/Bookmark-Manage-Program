import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.swing.JFrame;

public class Test {
	
	public static void main(String[] args) throws Exception {
		BookmarkList BMList = new BookmarkList("./bookmark.txt");
		BookmarkManager frame = new BookmarkManager(BMList);
	}
}