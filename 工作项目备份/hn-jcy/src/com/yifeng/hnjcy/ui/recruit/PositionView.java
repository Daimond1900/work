package com.yifeng.hnjcy.ui.recruit;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.yifeng.hnjcy.data.RecuitDal;
import com.yifeng.hnjcy.ui.BaseActivity;
import com.yifeng.hnjcy.ui.R;
import com.yifeng.hnjcy.util.ConstantUtil;
import com.yifeng.qzt.ui.enterprise.EnterpriseView;
import com.yifeng.qzt.ui.mapabc.MapInfoActivity;

import java.util.*;

/***
 * 职位详细
 * @author wujiahong
 * 2012-10-18
 */
@SuppressLint("HandlerLeak")
public class PositionView extends BaseActivity {
	private TableRow company_item,companyAddressItem;
	private Button back_btn,delivery_btn;
	private TextView positionTitle,companyName,pushDate,peopleCount,monthlyPay,
	record,area,telNumber,positionDesc,companyAddress,gzsm;
    private String telNo="",positionId="",companyId="";
    private ProgressDialog  progressDialog;
    private RecuitDal recuitDal;
    private Map<String,String> positionInfo,sendDelivery;
    private int postid=1;//1：加载界面数据,2投简历
    
    private String longitude="1192679470";//经度
    private String latitude="32248012";//纬度
    private String  companyNameStr="", companyAddressStr="";

    		
	@Override
    protected void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.position_info);
    	
    	recuitDal=new RecuitDal(this,new Handler());
    	
    	//初始化界面
    	initPage();
    	
    	//加载远程数据
    	postid=1;
    	showProg("正在加载数据....");
    	new Thread(loadRunnanle).start();
    	
    }
    private void initPage(){
    	
    	positionTitle=(TextView)findViewById(R.id.positionTitle);//职位名称
    	companyName=(TextView)findViewById(R.id.companyName);//公司名称
    	pushDate=(TextView)findViewById(R.id.pushDate);//有效期
    	gzsm = (TextView)findViewById(R.id.gzsm);	//工作说明 
    	peopleCount=(TextView)findViewById(R.id.peopleCount);//招聘人数
    	monthlyPay=(TextView)findViewById(R.id.monthlyPay);//工资
    	record=(TextView)findViewById(R.id.record);//学历要求
    	area=(TextView)findViewById(R.id.area);//用工区域
    	telNumber=(TextView)findViewById(R.id.telNumber);//联系电话
    	positionDesc=(TextView)findViewById(R.id.positionDesc);//职位描述
    	companyAddress=(TextView)findViewById(R.id.companyAddress);//公司地址
           	
    	MyOnclick onClick=new MyOnclick();
    	
    	positionId=this.getIntent().getStringExtra("positionId")==null?"":this.getIntent().getStringExtra("positionId");
    	companyId=this.getIntent().getStringExtra("companyId")==null?"":this.getIntent().getStringExtra("companyId");
    	
    	//点击公司查看公司详细
    	company_item=(TableRow)findViewById(R.id.company_item);
    	if(!this.getIntent().getBooleanExtra("isClick", false)){//如果是所有职位进来查看公司详细不可点
    	  company_item.setOnClickListener(onClick);
    	}else{
    		companyName.setCompoundDrawables(null, null, null, null);
    	}
    	
    	//点击地址查看地图
    	companyAddressItem=(TableRow)findViewById(R.id.companyAddressItem);
    	companyAddressItem.setOnClickListener(onClick);
    	
    	//返回按钮
    	back_btn=(Button)findViewById(R.id.back_btn);
    	back_btn.setOnClickListener(onClick);
    	
    	delivery_btn=(Button)findViewById(R.id.delivery_btn);
    	delivery_btn.setOnClickListener(onClick);
    	
    }
    
    private void showProg(String Msg) {
 	   progressDialog =new ProgressDialog(this);
 	   progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
 	   //progressDialog.setTitle("请稍等...");
 	   progressDialog.setMessage(Msg);
 	   progressDialog.setIndeterminate(true);//设置进度条是否为不明确
 	   progressDialog.setCancelable(true);//设置进度条是否可以按退回键取消  
 	   progressDialog.show();
    }
    
    private class MyOnclick implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.company_item:
				Intent bl=new Intent(PositionView.this,EnterpriseView.class);
				bl.putExtra("companyId", companyId);
				startActivity(bl);
				break;
			case R.id.companyAddressItem:
				if(!longitude.equals("")&&!latitude.equals("")){
					Intent map=new Intent(PositionView.this,MapInfoActivity.class);
					map.putExtra("companyName", companyNameStr);
					map.putExtra("companyAddress", companyAddressStr);
					map.putExtra("telNo", telNo);
					map.putExtra("longitude", longitude);//经度
					map.putExtra("latitude", latitude);//纬度
					
					startActivity(map);
				}else{
					commonUtil.shortToast(companyNameStr+",还没标注地理位置.");
				}
				break;
			case R.id.back_btn:
				PositionView.this.finish();
				break;
			case R.id.delivery_btn:
				Intent intent=new Intent(PositionView.this,SendResumeActivity.class);
				intent.putExtra("companyId", companyId);
				intent.putExtra("positionId", positionId);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
    }
    
    Runnable  loadRunnanle=new Runnable() {
		@Override
		public void run() {
			try{
				
				Thread.sleep(200);
				
				if(postid==1){
					positionInfo=recuitDal.getPosition(positionId,companyId);
					loadHandler.sendEmptyMessage(1);
				}
				
				if(postid==2){
					sendDelivery=recuitDal.doPostDelivery(positionId, user.getUserId(), user.getUserName(), companyId, "暂无");
					loadHandler.sendEmptyMessage(2);
				}
				
			}catch(Exception e){
				e.printStackTrace();
				loadHandler.sendEmptyMessage(-1);
			}
			
		}
	};
	
	Handler loadHandler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				setPageData();
			}
			if(msg.what==2){
				doPostDelivery();
			}
			if(progressDialog!=null)
				progressDialog.dismiss();
		};
	};
	
	/***
	 * 加载数据
	 * 
AAB004	单位名称
ACA112	招聘工种
ACB216	工种说明
ACB21R	招聘总人数
ACC034	月工资
ACC214	福利待遇
AAC039	外语水平       枚举表
AAC043	计算机水平     枚举表
AAC011	文化程度       枚举表
AAE013	其他说明及要求
ACB211	男性人数
ACB212	女性人数
ACB213	兼招人数
AAE030	有效起始日期
AAE031	有效终止日期
AAE036	经办日期
ACB21O	空位编号
AAE004	单位联系人
AAE005	单位联系人电话
AAE020	经办机构
AAE006	单位地址
ACB202	用工区域
ACB203	工作地点
ACB204	地点详细
	 */
    private void setPageData(){
    	     if(positionInfo.get("state").equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))){
    	    	        companyNameStr=positionInfo.get("aab004")==null?"":positionInfo.get("aab004");
    	    	    	positionTitle.setText(positionInfo.get("aca112")==null?"":positionInfo.get("aca112"));//职位名称
    	    	    	companyName.setText(companyNameStr);//公司名称
    	    	    	gzsm.setText(positionInfo.get("acb216"));	//工作说明 
    	    	    	pushDate.setText(positionInfo.get("aae030")+"至"+positionInfo.get("aae031"));//有效期
    	    	    	peopleCount.setText("男性:"+positionInfo.get("acb211")+";女性:"+positionInfo.get("acb212")+";兼招:"+positionInfo.get("acb213"));//招聘人数
    	    	    	
    	    	    	String price=positionInfo.get("acc034")==null?"":positionInfo.get("acc034");
    	    	    	if(price.equals(""))
    	    	    		price="面议";
    	    	    	monthlyPay.setText(price);//工资
    	    	    	
    	    	    	record.setText(positionInfo.get("aac011"));//学历要求
    	    	    	area.setText(positionInfo.get("acb202"));//用工区域
    	    	    	
    	    	    	telNo=positionInfo.get("aae005")==null?"":positionInfo.get("aae005");
    	    	    	telNumber.setText(telNo);//联系电话
    	    	    	
    	    	    	//公司编号
    	    	    	companyId=positionInfo.get("aab001")==null?"":positionInfo.get("aab001");
    	    	    	
    	    	    	//职位描述
    	    	    	positionDesc.setText(Html.fromHtml("福利待遇:"+positionInfo.get("acc214")+"" +
    	    	    			"<br/><br/>具体要求:"+positionInfo.get("aae013")));
    	    	    	
    	    	    	//如果没有公司详细地址就显示公司名称
    	    	    	companyAddressStr=positionInfo.get("aae006")==null?"":positionInfo.get("aae006");
    	    	    	companyAddress.setText(companyAddressStr.equals("")?companyNameStr:companyAddressStr+"	(地图定位)");
    	    	    	
    	    	    	longitude=positionInfo.get("lng");;//经度
    	    	    	latitude=positionInfo.get("lat");//纬度
    	    	        
    	    	    	
    	    	   
    	     }else{
    	    	commonUtil.shortToast("信息加载失败!");
    	     }
    }
    
    /***
	 * 投递简历
	 */
    private void doPostDelivery(){
    	     if(sendDelivery.get("state").equals(String.valueOf(ConstantUtil.LOGIN_SUCCESS))){
    	    	    if(sendDelivery.get("success").equals("true")){
    	    	    	//投递成功
    	    	    	
    	    	    }else{
    	    	    	//投递失败
    	    	    }
    	    	    commonUtil.shortToast(sendDelivery.get("msg"));
    	    	    this.finish();
    	     }else{
     	    	try{
     	    		commonUtil.shortToast(sendDelivery.get("msg"));
     	    		
     	    	}catch(Exception e){
     	    		commonUtil.shortToast("信息加载失败!");
     	    		e.printStackTrace();
     	    	}
     	    	
     	     }
    }
}
