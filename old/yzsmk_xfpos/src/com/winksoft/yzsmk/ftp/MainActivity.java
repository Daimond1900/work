package com.winksoft.yzsmk.ftp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.winksoft.yzsmk.ftp.BusinessMode;
import com.winksoft.yzsmk.xfpos.R;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	
	public static final String FTP_CONNECT_SUCCESSS = "ftp连接成功";
	public static final String FTP_CONNECT_FAIL = "ftp连接失败";
	public static final String FTP_DISCONNECT_SUCCESS = "ftp断开连接";
	public static final String FTP_FILE_NOTEXISTS = "ftp上文件不存在";
	
	public static final String FTP_UPLOAD_SUCCESS = "ftp文件上传成功";
	public static final String FTP_UPLOAD_FAIL = "ftp文件上传失败";
	public static final String FTP_UPLOAD_LOADING = "ftp文件正在上传";

	public static final String FTP_DOWN_LOADING = "ftp文件正在下载";
	public static final String FTP_DOWN_SUCCESS = "ftp文件下载成功";
	public static final String FTP_DOWN_FAIL = "ftp文件下载失败";
	
	public static final String FTP_DELETEFILE_SUCCESS = "ftp文件删除成功";
	public static final String FTP_DELETEFILE_FAIL = "ftp文件删除失败";
	private ListView listView1;
	private MyBaseAdapter1 adapter1;
	private List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
	//public FTP ftp;
	  String hostName ;
	  int serverPort ;
	  String userName ;
	  String password ;
	  boolean mode;
	  String runtime;
	  String remotePath = "";
	  String LocalPath_ORI = Environment.getExternalStorageDirectory() + "/FTP/filePath_ORI/";
	  EditText editText1 ;
	  EditText editText2 ;
	  Button button_modeA;
	  Button button_setRunTime;
	  Button button_start;
	  Button buttonCreateFile;
	  Button button_getestfile;
	  Button buttonUploadFile; 
	  Button button_getlog;
	  Button button_genxf;
	  Button button_getxf;
	  
	  Button button_clrlog;
	  Button button_gencz;
	  Button button_getcz;
	  
	  boolean brun = false;
	  boolean bAuto = true;
	  public BusinessMode bm;
	  private static Context cxt;
	  Handler msghandler = new Handler() {  
		    @Override  
		    public void handleMessage(Message msg) {  
		    	int what = msg.what;
		    	
		    	Bundle bundle = msg.getData();
		    	String strMsg = "";
				if(bundle != null)
				{
		       	    strMsg = bundle.getString("MSG");
				} 
		       switch(msg.what)
		       {  
		    	 case 2 :
		    		 button_modeA.setText(strMsg);
		    		 if(strMsg.equals("自动模式"))
		    		 {
		    			 editText1.setText(""); 
			        	 editText1.invalidate();
		    		 }
		             break;
		    	 case 3 :
		    		 button_start.setText(strMsg);
		    		 button_modeA.setEnabled(true);
   					 button_setRunTime.setEnabled(true);
   					 button_genxf.setEnabled(true);
   					 button_getxf.setEnabled(true);
   					 button_clrlog.setEnabled(true);
   				     button_gencz.setEnabled(true);
   				     button_getcz.setEnabled(true);
   					 //buttonCreateFile.setEnabled(true);
   				     //button_getestfile.setEnabled(true);
   				     //buttonUploadFile.setEnabled(true);
   				     button_getlog.setEnabled(true);
   					 break;
		         default:
		        	 editText1.setText(strMsg); 
		        	 editText1.invalidate();
		        	 break;
		       }
		       super.handleMessage(msg);  
		    }  
		};  
	  
	private void sendMsg(int what,String msg)
	{
		if(msghandler!=null)
		{
			   Message message = new Message();  
			   message.what = what;  
			   Bundle bundle = new Bundle();
			   bundle.putString("MSG",msg);   
			   message.setData(bundle); 
			   msghandler.sendMessage(message);  
		}
	}
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        hostName = "192.168.66.123";
		serverPort = 2121;
		userName = "test";
		password = "123456";
		mode = true;
		runtime = "09:00";
		remotePath = "/XF/"; //消费业务为:/XF/,充值业务为:/CZ/
        //ftp = FTP.get(hostName, serverPort, userName, password, mode,runtime,remotePath);
        initView();
        cxt = this.getApplicationContext();
    }    
    
    public void printList(List<Map<String, Object>> list)
    {
    	if(list!=null)
    	{
    	   System.out.println("list.size()=" + list.size());
    	   for(int i=0;i<list.size();i++)
    	   {
    		 Map map = list.get(i);
    		 {
    			String strline = "";
    			for (Object key : map.keySet()) 
    			{  
    				if(strline.equals(""))
    				{
    				    strline = key.toString() + "=" + map.get(key);  
    				} 
    				else
    				{
    					strline = strline + "," + key.toString() + "=" + map.get(key);
    				}
    			} 
    			strline = map.get("insertdate") + ":" + strline;
    			System.out.println(strline);
    		 }
    	   } 
    	}
    	else
    	{
    		System.out.println("list is null");
    	}
    }
    
    private void initView() {
    	
    	//上传功能
    	//new FTP().uploadMultiFile为多文件上传
    	//new FTP().uploadSingleFile为单文件上传
    	//button_createtestfile
    	//button_uploadtestfile
    	buttonCreateFile = (Button) findViewById(R.id.button_createtestfile);   
    	buttonCreateFile.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
            	//editText1.setText("start createTestFile");
            	new Thread(new Runnable() {			
					@Override
					public void run() {	
						if(bm!=null)
          		        {
						  bm.createTestFiles();
          		        }
					}
				}).start();
            	//editText1.setText("start finished");
            }
    	});
    	button_getestfile = (Button) findViewById(R.id.button_getestfile);   
    	button_getestfile.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
              File dir = new File(LocalPath_ORI);  
           	  File[] files = dir.listFiles();  
           	  if (files != null)  
           	  {
           		 for (int i = 0; i < files.length; i++) 
           		 {  
           		    if (!files[i].isDirectory()) 
           		    {  
           		    	editText1.setText("file" + i + ":" +  files[i].getName());
           		    }
           		 }
           	  }
            }
    	});
    	
    	buttonUploadFile = (Button) findViewById(R.id.button_uploadtestfile);     
    	buttonUploadFile.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
            	  new Thread(new Runnable() {			
          			@Override
          			public void run() {	
          				if(bm!=null)
          		        {
          					bm.uploadFiles();
          		        }
          			}
          		}).start();
            }
    	});
    	editText1 = (EditText) findViewById(R.id.editText1);

    	/***********************************************************/
    	editText2 = (EditText) findViewById(R.id.editText2);
    	SimpleDateFormat tempDate = new SimpleDateFormat("HH:mm");  
        String strDt = tempDate.format(new java.util.Date()).toString();  
    	editText2.setText(strDt);
    	button_setRunTime = (Button) findViewById(R.id.button_setRunTime);     
    	button_setRunTime.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
            	String time = editText2.getText().toString();
            	bm.setRunTime(time);
            }
    	});
    	
    	
    	button_modeA = (Button) findViewById(R.id.button_modeA);     
    	button_modeA.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
            	if(brun)
            	{
            	  if(!bAuto)
            	  {
            	     bm.setMode(true);
            	     bAuto = true;
            	     sendMsg(2,"手动模式");
            	  }
            	  else
            	  {
            	     bm.setMode(false);
              	     bAuto = false;
              	     sendMsg(2,"自动模式");
            	  }
            	}
            }
    	});
    	
    	button_start = (Button) findViewById(R.id.button_start);     
    	button_start.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
            	new Thread(new Runnable() {			
          			@Override
          			public void run() {	
          				if(bm==null)
          		        {
          					bm = BusinessMode.getInstance();
  					        bm.setParams(hostName, serverPort, userName, password,editText2.getText().toString(),remotePath,msghandler,cxt);
          		        }
          				 
      					if(!brun)
      	            	{
      	            	   bm.start();
      	            	   brun = true;
      	            	   sendMsg(3,"停止");
      	            	   if(!bAuto)
      	            	   {
      	            	     bm.setMode(true);
      	            	     bAuto = true;
      	            	     sendMsg(2,"手动模式");
      	            	   }
      	            	}
      	            	else
      	            	{
      	            		bm.stop();
      	            		brun = false;
      	            		sendMsg(3,"启动");
      	            	}
      					
          			}
          		}).start();
            }
    	});
    	
		     
    	listView1 = (ListView) findViewById(R.id.listView1);
    	//adapter1 = new MyBaseAdapter(this, list1);
    	adapter1 = new MyBaseAdapter1(this.getApplicationContext(), list1, R.layout.main_listview1_item);
		listView1.setAdapter(adapter1);  	
	    	
		/*listView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter1.setSelectItem(position);
				adapter1.notifyDataSetChanged();
				//showMenu2(list2.get(position));
				
			}
		});*/
		
    	button_getlog = (Button) findViewById(R.id.button_getlog);     
    	button_getlog.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
            	if(bm!=null)
  		        {
            		list1.clear();
            		adapter1.notifyDataSetChanged();
            		
            		List<Map<String, Object>> tmplist = new ArrayList<Map<String, Object>>();
            		tmplist = bm.getLogs(0,0);
            		for (Map<String, Object> map : tmplist) {
            			list1.add(map);
            		}
            		adapter1.notifyDataSetChanged();
  		        }
            }
    	}); 
    	
    	button_clrlog = (Button) findViewById(R.id.button_clrlog);     
    	button_clrlog.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
            	if(bm!=null)
  		        {
            		list1.clear();
            		adapter1.notifyDataSetChanged();
  		        }
            }
    	}); 
	     
    	button_genxf = (Button) findViewById(R.id.button_genxf);     
    	button_genxf.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
            	if(bm!=null)
  		        {     
            		button_getxf.setEnabled(false);
            		SimpleDateFormat tempDate = new SimpleDateFormat("yyMMddHHmmss");  
        	        String body_0 = tempDate.format(new java.util.Date()).toString();  //0	12	n	本地流水号	本地自定义流水号	M
     		        String body_12 = "123456123456";//12	20	an	卡片序列号	卡主账号，不足位数以空格填充	M
     		        String body_32 = "A123456789123456789";//32	19	n	主账号	向左对齐，不足19位时不足部分补空格（右补空格）。-乘客卡号	M
     		        String body_51 = "3456789123456789";//51	16	H	卡内号	卡主账号后16位	M
     		        String body_67 = "01";//67	3	n	卡种	01 普通卡；02 学生卡；03老年卡；05  拥军卡；06 离休卡；07 优抚卡；08 残疾人卡；09  敬老卡；10  异形卡；11 纪念卡	M
     		        String body_70 = "0";//70	1	n	记录类型	0：正常交易 1：黑卡交易	M
     		        int jyqje = (int)Math.round(Math.random()*100)+50;
     		        int jyje = (int)Math.round(Math.random()*20);
     		        int jyye = jyqje - jyje;
     		        String body_71 = jyqje + "";//71	8	H	交易前余额	消费前卡余额	M
     		        String body_79 = jyje + "";//79	8	H	*交易金额	用8个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	M
     		        String body_87 = jyye + "";//87	8	H	交易余额	用8个可见的16进制字符（0～9，A～F）表示；电子钱包的消费，本域后补两个F。  若无法填写用缺省值空格填充。  此字段表示卡片消费后余额	M
     		        String body_95 = "06";//95	2	n	*交易类型标识	交易性质  06-表示电子钱包脱机消费；09-复合消费的类型。	M
     		        //String body_97 = "010";//97	8	n	采集点编号	运营系统的公司采集点序号。	0
     		        String body_105 = "0000100";//105	12	n	终端机编号	POS机编号	M
     		        String body_117 = "1245";//117	12	n	*PSAM卡号	PSAM卡终端机编号	M
     		        String body_129 = "78123456";//129	9	n	*PSAM卡流水号	PSAM卡交易唯一流水号	M
     		        String body_138 = "0";//138	1	n	锁卡交易标志	0为正常交易；1为锁卡交易。统一为0，但发现黑名单卡要进行锁卡。	M
     		        String body_139 = "1A2B3C4D";//139	8	H	终端交易序号	用8个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。指：POS交易流水号	M
     		        String body_147 = "20161006";//147	8	n	*终端交易日期	格式为YYYYMMDD	M
     		        String body_155 = "100108";//155	6	n	*终端交易时间	格式为hhmmss	M
     		        String body_161 = "TAC001";//161	8	H	*交易验证代码（TAC）	用8个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	M
     		        String body_169 = "0K";//169	2	H	消费密钥版本号	用2个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	M
     		        String body_171 = "S1";//171	2	H	消费密钥索引	用2个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	M
     		        String body_173 = "S2";//173	4	H	卡片脱机交易序列号	用4个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。指卡消费计数器。	M
     		        String body_177 = "1357924680";//177	16	n	发卡机构标识	卡主账号前八位	M
     		        String body_193 = "3120";//193	4	n	*城市代码（交易地）	扬州城市代码：3120	M
     		        String body_197 = "3120";//197	4	n	*城市代码（卡属地）	卡片读取	M
     		        String body_201 = "87654321";//201	8	n	运营单位代码	商户编号	M
     		        String body_209 = "RRRR1";//209	20	ans	所属线路	线路编号(公交必备)	0
     		        String body_229 = "B";//229	8	n	交易柜台编号	公交为：公交车辆编号	M
     		        String body_259 = "07";//259	2	  n	行业代码	01-公交，02-出租，03-地铁，04-有轨电车，05-公共自行车，06-轮渡，07-小额消费，08-停车场	M
     		        //String body_276 = "0";//276	1	n	测试标志	0为正式数据；1为测试数据	M
     		        //String body_277 = "W277";//277	8	H	伪随机数	用8个可见的16进制字符（0～9，A～F）表示；若无法填写用缺省值空格填充。	O
     		        //String body_285 = "285";//285	30	ans	保留使用		O
     		      
     		    Map map = new HashMap<String, Object>();   
     		    map.put("body_0",body_0);
           		map.put("body_12",body_12);
           		map.put("body_32",body_32);
           		map.put("body_51",body_51);
           		map.put("body_67",body_67);
           		map.put("body_70",body_70);
           		map.put("body_71",body_71);
           		map.put("body_79",body_79);
           		map.put("body_87",body_87);
           		map.put("body_95",body_95);
           		//map.put("body_97",body_97);
           		map.put("body_105",body_105);
           		map.put("body_117",body_117);
           		map.put("body_129",body_129);
           		map.put("body_138",body_138);
           		map.put("body_139",body_139);
           		map.put("body_147",body_147);
           		map.put("body_155",body_155);
           		
           		map.put("body_161",body_161);
           		map.put("body_169",body_169);
           		map.put("body_171",body_171);
           		map.put("body_173",body_173);
           		map.put("body_177",body_177);
           		map.put("body_193",body_193);
           		map.put("body_197",body_197);
           		map.put("body_201",body_201);
           		
           		//map.put("body_209",body_209);
           		map.put("body_229",body_229);
           		//map.put("body_237",body_237);
           		//map.put("body_245",body_245);
           		map.put("body_259",body_259);
           		//map.put("body_261",body_261);
           		//map.put("body_262",body_262);
           		//map.put("body_276",body_276);
           		
           		//map.put("body_277",body_277);
           		//map.put("body_285",body_285);     			
           		
            		bm.saveToxfDb(map,null);
            		button_getxf.setEnabled(true);
  		        }
            }
    	}); 	
    	
    	button_getxf = (Button) findViewById(R.id.button_getxf);     
    	button_getxf.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
            	if(bm!=null)
  		        {
            		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            		String selection = "state=? and substr(insertdate,1,10)>=?";
            		String [] selectArray = new String[2];
            		selectArray[0] = "0";
            		selectArray[1] = "2016-10-07";
            		list = bm.getXFinfos(selection,selectArray);
            		printList(list);
  		        }
            }
    	}); 
    	
    	button_gencz = (Button) findViewById(R.id.button_gencz);     
    	button_gencz.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
            	if(bm!=null)
  		        {
            		button_gencz.setEnabled(false);
            		String CorpId = "12345678901";//	N11	充值受理方代码	
     				String CitizenCardNo = "32102719890001";//	N20	市民卡号	
     				
     				int jyqje = (int)Math.round(Math.random()*1000)+500;
       		        int jyje = (int)Math.round(Math.random()*200);
       		        int jyye = jyqje + jyje;
       		        
     				String CrdBalBef = jyqje + "";//	N14	充值前金额	
     				String CrdBalAft = jyye + "";//	N14	充值后金额	
     				String TxnAmt = jyje + "";//	N14	充值金额	
     				
     				SimpleDateFormat tempDate = new SimpleDateFormat("yyMMddHHmmss");  
        	        String TxnDt = tempDate.format(new java.util.Date()).toString(); //	yyyyMMddHHmmss	充值时间	
     				String SamNo = "1357924680";//	N12	SAM卡号	
     				String AccpetCusNo =  "1122334455";//	N20	网点编号	
     				String OprNo = "2002"; //N24	操作员编号	
     				String TAC = "01TAC23"; //	H8	交易认证码	ASCII码的十六进制数
     				String BusinessNo = "0003";//	N10	业务流水号	
     				String CurrCount = "4321";//	N8	卡计数器		
     				
     				Map map = new HashMap<String, Object>();
     				map.put("CorpId",CorpId);
     				map.put("CitizenCardNo",CitizenCardNo);
     				map.put("CrdBalBef",CrdBalBef);
     				map.put("CrdBalAft",CrdBalAft);
     				map.put("TxnAmt",TxnAmt);
     				map.put("TxnDt",TxnDt);
     				map.put("SamNo",SamNo);
     				map.put("AccpetCusNo",AccpetCusNo);
     				map.put("OprNo",OprNo);
     				map.put("TAC",TAC);
     				map.put("BusinessNo",BusinessNo);
     				map.put("CurrCount",CurrCount);
     				bm.saveToczDb(map,null);
     				button_gencz.setEnabled(true);
  		        }
            }
    	}); 
    	
    	button_getcz = (Button) findViewById(R.id.button_getcz);     
    	button_getcz.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
            	if(bm!=null)
  		        {
            		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            		String selection = "state=? and substr(insertdate,1,10)>=?";
            		String [] selectArray = new String[2];
            		selectArray[0] = "0";
            		selectArray[1] = "2016-10-07";
            		list = bm.getCZinfos(selection,selectArray);
            		printList(list);
  		        }
            }
    	}); 
		
		/*Button buttonUpload = (Button) findViewById(R.id.button_upload);        
    	buttonUpload.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {

					new Thread(new Runnable() {			
						@Override
						public void run() {
							
		                    // 上传
		                	File file = new File("/mnt/sdcard/ftpTest.docx");
		                    try {
		                    	
		                    	//单文件上传
		                    	ftp.uploadSingleFile(file, "/fff",new UploadProgressListener(){

									@Override
									public void onUploadProgress(String currentStep,long uploadSize,File file) {
										// TODO Auto-generated method stub
										Log.d(TAG, currentStep);										
										if(currentStep.equals(MainActivity.FTP_UPLOAD_SUCCESS)){
											Log.d(TAG, "-----shanchuan--successful");
										} else if(currentStep.equals(MainActivity.FTP_UPLOAD_LOADING)){
											long fize = file.length();
											float num = (float)uploadSize / (float)fize;
											int result = (int)(num * 100);
											Log.d(TAG, "-----shangchuan---"+result + "%");
										}
									}							
								});
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}).start();
                     
            }
        });
    
    	//下载功能
    	Button buttonDown = (Button)findViewById(R.id.button_down);
    	buttonDown.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				new Thread(new Runnable() {			
					@Override
					public void run() {
						
	                    // 下载
	                    try {
	                    	
	                    	//单文件下载
	                    	ftp.downloadSingleFile("/fff/ftpTest.docx","/mnt/sdcard/download/","ftpTest.docx",new DownLoadProgressListener(){

								@Override
								public void onDownLoadProgress(String currentStep, long downProcess, File file) {
									Log.d(TAG, currentStep);										
									if(currentStep.equals(MainActivity.FTP_DOWN_SUCCESS)){
										Log.d(TAG, "-----xiazai--successful");
									} else if(currentStep.equals(MainActivity.FTP_DOWN_LOADING)){
										Log.d(TAG, "-----xiazai---"+downProcess + "%");
									}
								}
	                    		
	                    	});	                    	
                    	
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}).start();
				
			}
		});
    
    	//删除功能
    	Button buttonDelete = (Button)findViewById(R.id.button_delete);
    	buttonDelete.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				new Thread(new Runnable() {			
					@Override
					public void run() {
						
	                    // 删除
	                    try {

	                    	ftp.deleteSingleFile("/fff/ftpTest.docx",new DeleteFileProgressListener(){

								@Override
								public void onDeleteProgress(String currentStep) {
									Log.d(TAG, currentStep);										
									if(currentStep.equals(MainActivity.FTP_DELETEFILE_SUCCESS)){
										Log.d(TAG, "-----shanchu--success");
									} else if(currentStep.equals(MainActivity.FTP_DELETEFILE_FAIL)){
										Log.d(TAG, "-----shanchu--fail");
									}
								}
	                    		
	                    	});	                    	
                    	
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}).start();
				
			}
		});*/
    
    }
}
