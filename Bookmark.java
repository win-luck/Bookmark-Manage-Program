import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Bookmark{
	public Bookmark() {}
	
	String name= "";
	String datetime= ""; 
	String url = "";
	String groupname = "";
	String memo = "";
	
	Bookmark(String name, String datetime, String url, String groupname, String memo) {
		this.name = name;
		this.datetime = datetime;
		this.url = url;
		this.groupname = groupname;
		this.memo = memo;
	} // 인자들을 입력받고 북마크 객체를 생성해주는 메서드
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm"); // 현재 시간을 변환할 형식
	Bookmark(String url) {
		this.url = url;
		this.datetime = LocalDateTime.now().format(formatter);
	} // url을 인자로 받아 현재 시간으로 생성 시간을 넣어서 북마크 객체를 추가하는 메서드

	public void print() {
		System.out.println(this.name + "," + this.datetime + "," + this.url + "," + this.groupname + "," + this.memo);
	} // 북마크의 내용을 출력해주는 메서드
	
} // 채승운 맞습니다.