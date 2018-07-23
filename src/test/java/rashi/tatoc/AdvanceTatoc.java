package rashi.tatoc;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AdvanceTatoc {

	
	WebElement element;
	WebDriver driver;
	
	public AdvanceTatoc() {
		System.setProperty("webdriver.chrome.driver", "C:/rashi/chromedriver/chromedriver.exe");
		driver = (WebDriver) new ChromeDriver();
		driver.get("http://10.0.1.86//tatoc");
	 	
	 	}

	@Test 
  public void test001_basicCource() {
		 Assert.assertEquals(driver.findElement(By.linkText("Advanced Course")).isDisplayed(), true);
				driver.findElement(By.linkText("Advanced Course")).click();
  }
	
	
	@Test
	  public void test002_menu_items() {
			 Assert.assertEquals(driver.findElement(By.cssSelector("body > div > div.page > div.menutop.m2 > span.menutitle")).isDisplayed(), true);
					driver.findElement(By.cssSelector("body > div > div.page > div.menutop.m2 > span.menutitle")).click();
					driver.findElement(By.cssSelector("body > div > div.page > div.menutop.m2 > span:nth-child(5)")).click();
	}
	@Test 
	public void test003_database_entry()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		
		String dbUrl="jdbc:mysql://10.0.1.86:3306/tatoc";
		String username="tatocuser";
				String password="tatoc01";
		String symbol= driver.findElement(By.id("symboldisplay")).getText();
		System.out.println("symbol="+symbol);
		String query1="select * from identity";
		String query2="select * from credentials";
		String id=null;
		String name=null;
		String passkey=null;
		Connection con = DriverManager.getConnection(dbUrl,username,password);
		Statement stmt = con.createStatement();	
		ResultSet rs= stmt.executeQuery(query1);
		while (rs.next()){
	/*	System.out.println(rs.getString(1));
		System.out.println(rs.getString(2));
		
		*/
			
			if(rs.getString(2).equalsIgnoreCase(symbol))
			{id=rs.getString(1);
			System.out.println("id="+id);}
			
		}
		ResultSet rs1= stmt.executeQuery(query2);
		while (rs1.next()){
	/*	System.out.println(rs.getString(1));
		System.out.println(rs.getString(2));
		System.out.println(rs.getString(3));
		*/
			
			if(rs1.getString(1).equals(id)){
			{
				name=rs1.getString(2);
				passkey=rs1.getString(3);
				System.out.println(name+" "+ passkey);
				driver.findElement(By.xpath("//*[@id='name']")).sendKeys(name);
				driver.findElement(By.id("passkey")).sendKeys(passkey);
				driver.findElement(By.id("submit")).click();
				
			}
		}
		
		} 
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test 
	public void test004_rest_Service() throws InterruptedException, IOException, JSONException
	{
		driver.get("http://10.0.1.86/tatoc/advanced/rest");
		String sessid = driver.findElement(By.id("session_id")).getText();
        sessid = sessid.substring(12,sessid.length());
        String Resturl = "http://10.0.1.86/tatoc/advanced/rest/service/token/"+sessid;

        URL url = new URL(Resturl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String responsejson=new String(response);
        
        JSONObject obj=new JSONObject(responsejson);
        responsejson=(String) obj.get("token");
        
        URL url1 = new URL("http://10.0.1.86/tatoc/advanced/rest/service/register");
        HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
        

        conn1.setRequestMethod("POST");
        
        conn1.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "id="+sessid+"&signature="+responsejson+"&allow_access=1";
        
        conn1.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(conn1.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = conn1.getResponseCode();
        
        conn1.disconnect();
        driver.findElement(By.xpath("/html/body/div/div[2]/a")).click();
       
	    
	   
	   
	
	}
	@Test
	public void test005_fileHandling() throws FileNotFoundException, InterruptedException
	{
		String element;
		driver.findElement(By.xpath("/html/body/div/div[2]/a")).click();
		Thread.sleep(2000);
		String path= "/home/qainfotech/Downloads/file_handle_test.dat";
		File file = new File(path);
		Scanner scnr = new Scanner(file);
		 List<String> storedlist= new ArrayList<String>();
		while(scnr.hasNextLine()){
		   String line = scnr.nextLine();
		   //System.out.println(line);
		   storedlist.add(line);
		  
		}
		element= storedlist.get(2);
		//System.out.println("element  "+signature);
		String [] arrayS= element.split(":");
		String signature=arrayS[1].trim();
		System.out.println("element  "+signature);
		
		driver.findElement(By.xpath("//*[@id='signature']")).sendKeys(signature);
				driver.findElement(By.xpath("/html/body/div/div[2]/form/input[2]")).click();
	}
	    
	
}