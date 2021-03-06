import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;

/**
 * 得到未来6天的天气(含今天)
 * @author Anna
 *
 */

public Class Weather {
	String Cityid;
	URLConnection connectionData;
	StringBuilder sb;
	BufferedReader br; //读取data数据流
	JSONObject jsonData;
	JSONObject info;

	public Weather(String Cityid) throws IOException, NullPointerException{
		//解析本机ip地址
		this.Cityid = Cityid;
		//连接中央气象台的API
		URL url = new URL("http://m.weather.com.cn/data/"+Cityid+".html");
		connectionData = url.openConnection();
		connectionData.setConnectTimeout(1000);
		try{
			br=new BufferedReader(new InputStreamReader(
				connectionData.getInputStream(),"UTF-8"));
			sb=new StringBuilder();
			String line = null;
			while((line=br.readLine())!=null)
				sb.append(line);
		}catch(SocketTimeoutException e){
			System.out.println("连接超时");
		}catch(FileNotFoundException e){
			System.out.println("加载文件出错");
		}
		  String datas = sb.toString();
		  jsonData = JSONObject.fromObject(datas);
		  //System.out.println(jsonData.toString());
		  info=jsonData.getJSONObject("weatherinfo");
		  //得到1-6天的天气情况
		  List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		  for(int i=1; i<=6; i++){
		  	//得到未来6天的日期
		  	Calendar cal = Calendar.getInstance();
		  	cal.add(Calendar.DAY_OF_YEAR,i-1);
		  	Date date = cal.getTime();
		  	SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
		  	Map<String, Object> map = new HashMap<String,Object>();
		  	map.put("city",info.getString("city").toString()); //城市
		  	map.put("date_y",sf.format(date));//日期
		  	map.put("week",getWeek(cal.get(Calendar.DAY_OF_WEEK)));//星期
		  	map.put("fchh",info.getString("fchh").toString()); //发布时间
		  	map.put("weather", infor.getString("weather"+i).toString()); //天气
		  	map.put("temp",info.getString("temp"+i).toString());//温度
		  	map.put("wind",info.getString("wind"+i).toString());//风况
		  	list.add(map);
		  }

		  
     }//控制台打印出天气
       for(int j=0;j<list.size();j++){
        Map<String,Object> wMap = list.get(j);
        System.out.println(wMap.get("city")+"\t"+wMap.get("date_y")+"\t"+wMap.get("week")+"\t"
        +wMap.get("weather")+"\t"+wMap.get("temp")+"\t"+wMap.get("index_uv"));
        }
     }private String getWeek(int iw){
      String weekStr = "";
      switch (iw) {
 case 1:weekStr = "星期天";break;
 case 2:weekStr = "星期一";break;
 case 3:weekStr = "星期二";break;
 case 4:weekStr = "星期三";break;
 case 5:weekStr = "星期四";break;
 case 6:weekStr = "星期五";break;
 case 7:weekStr = "星期六";break;
 default:
 break;
 }
      return weekStr;
     }
     public static void main(String[] args) {
         try {
             new Weather("101010100"); // 101010100(北京)就是你的城市代码
        } catch (Exception e) {
             e.printStackTrace();
         }
     }
 }
	}
}