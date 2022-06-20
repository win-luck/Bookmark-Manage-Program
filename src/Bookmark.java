import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Bookmark{
	public Bookmark() {}
	
	private String name= "";
	private String datetime= ""; 
	private String url = "";
	private String groupname = "";
	private String memo = "";
	
	public String getname() {
		return this.name;
	}
	
	public String getdatetime() {
		return this.datetime;
	}
	
	public String geturl() {
		return this.url;
	}
	
	public String getgroupname() {
		return this.groupname;
	}
	
	public String getmemo() {
		return this.memo;
	}
	
	public void SetOtherelement(String newGroupname, String newname, String newmemo) {
		this.groupname = newGroupname;
		this.name = newname;
		this.memo = newmemo;
	}
	
	
	
	Bookmark(String name, String datetime, String url, String groupname, String memo) {
		this.name = name;
		this.datetime = datetime;
		this.url = url;
		this.groupname = groupname;
		this.memo = memo;
	} // 
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm"); 
	Bookmark(String url) {
		this.url = url;
		this.datetime = LocalDateTime.now().format(formatter);
	} 

	public void print() {
		System.out.println(this.name + "," + this.datetime + "," + this.url + "," + this.groupname + "," + this.memo);
	} 
	
} 