package com.winksoft.android.yzjycy.ui.resume;

import java.util.Map;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.winksoft.android.yzjycy.BaseActivity;
import com.winksoft.android.yzjycy.R;
import com.winksoft.android.yzjycy.data.UserDAL;
import com.winksoft.android.yzjycy.util.Constants;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * comment:个人简历管理
 * @author:ZhangYan
 * Date:2012-8-28
 */
public class QztPersonalResumeActivity extends BaseActivity{
	private TextView userName,birth_place,id_cardno,graduation_school,major,major_desp,job_unit,job_post,work_year,oneself_desp,
	family_address,contact_phone,email_add,mobile_num,qq_number,edu_training,work_experience,request_jobpost,other_request,marriage_state,
	politics_status,educational_history,work_area,job_nature,graduation_date,sr,isApprove,updateTime;
	private Button add_btn,back_btn,editBtn;
	private TextView tip_info;
	private TableLayout infoTable;
	private ProgressDialog progressDialog ;
	private UserDAL userDal;
	private Map<String,String> info;
	private boolean isAdd=true;
	private ImageView head;
	private final String flage = "0";	//
//	private Bitmap headBamp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.qzt_personal_resume);
		
		userDal=new UserDAL(this);
		
		initPage();
		
	}
	
	private void setUserPage(){
		if(user.getIdCard().equals("")){
			   infoTable.setVisibility(View.GONE);
			   tip_info.setVisibility(View.VISIBLE);
			   editBtn.setVisibility(View.GONE);
			   
			   isAdd=true;
			   add_btn.setText("新增个人简历");
		 }else{
			   isAdd=false;
			   add_btn.setText("修改个人资料");
			   showProg("正在加载信息,请稍等...");
			   new Thread(runnable).start();
		  }
	}
	
	/** 
	 * 初始化界面
	 */
	private void initPage(){
		ButtonClickListener click = new ButtonClickListener();
		
		head=(ImageView)findViewById(R.id.head);
		
		tip_info=(TextView)findViewById(R.id.tip_info);
		infoTable=(TableLayout)findViewById(R.id.infoTable);
		
		add_btn = (Button)findViewById(R.id.add_btn);
		add_btn.setOnClickListener(click);
		
		back_btn = (Button)findViewById(R.id.back_btn);
		back_btn.setOnClickListener(click);
		
		editBtn=(Button)findViewById(R.id.editBtn);
		editBtn.setOnClickListener(click);
		
		userName = (TextView)findViewById(R.id.userName);//姓名
		birth_place = (TextView)findViewById(R.id.birth_place);//籍     贯
		work_year = (TextView)findViewById(R.id.work_year);//工龄
		contact_phone = (TextView)findViewById(R.id.contact_phone);//联系电话
		email_add = (TextView)findViewById(R.id.email_add);//emial
		qq_number = (TextView)findViewById(R.id.qq_number);//QQ号码
		family_address = (TextView)findViewById(R.id.family_address);//家庭住址
//		mobile_num = (TextView)findViewById(R.id.mobile_num);//手机号码
		id_cardno = (TextView)findViewById(R.id.id_cardno);//身份证号码
		oneself_desp = (TextView)findViewById(R.id.oneself_desp);//自我描述
		edu_training = (TextView)findViewById(R.id.edu_training);//教育培训
		graduation_date = (TextView)findViewById(R.id.graduation_date);//毕业时间
		graduation_school = (TextView)findViewById(R.id.graduation_school);//毕业学校
		major = (TextView)findViewById(R.id.major);//所学专业
		major_desp = (TextView)findViewById(R.id.major_desp);//专业描述
		job_unit = (TextView)findViewById(R.id.job_unit);//在职单位
		job_post = (TextView)findViewById(R.id.job_post);//在职岗位
	    work_experience = (TextView)findViewById(R.id.work_experience);//工作经验
		request_jobpost=(TextView)findViewById(R.id.request_jobpost);//求职岗位
		other_request=(TextView)findViewById(R.id.other_request);//其它
		marriage_state  = (TextView)findViewById(R.id.marriage_state);//婚姻状况
		politics_status = (TextView)findViewById(R.id.politics_status);//政治面貌
		educational_history = (TextView)findViewById(R.id.educational_history);//学历水平
		work_area = (TextView)findViewById(R.id.work_area);//工作地区
		job_nature = (TextView)findViewById(R.id.job_nature);//工作性质
		sr = (TextView)findViewById(R.id.sr);//出生年月
		isApprove = (TextView)findViewById(R.id.isApprove);//是否审核
//		updateTime=(TextView)findViewById(R.id.updateTime);//修改时间
	
	}
	
	 /**
	   * 初始化数据
	  */
	  private void initData(){
		     try{
		     if(info.get("state").equals(String.valueOf(1))){
		      userName.setText(info.get("xm"));//姓名
		   	  birth_place.setText(info.get("jg"));//籍贯
		   	  id_cardno.setText(info.get("sfzh"));//身份证号码
		   	  graduation_school.setText(info.get("byxx"));//毕业学校
		   	  major.setText(info.get("zy"));//所学专业
		   	  major_desp.setText(info.get("zyms"));//专业描述
		   	  job_unit.setText(info.get("zzdw"));//在职单位
		   	  job_post.setText(info.get("zzzw"));//在职岗位
		   	  work_year.setText(info.get("gl"));//工龄
		   	  oneself_desp.setText(info.get("zwms"));//自我描述
		   	  family_address.setText(info.get("jtzz"));//家庭地址
		   	  contact_phone.setText(info.get("lxdh"));//联系电话
		   	  email_add.setText(info.get("email"));//电子邮箱
//		   	  mobile_num.setText(info.get("sj"));//手机号码
		   	  qq_number.setText(info.get("qq"));//QQ号码
		   	  edu_training.setText(info.get("jypxjl"));//教育培训
		   	  work_experience.setText(info.get("gzjl"));//工作经历
		   	  request_jobpost.setText(info.get("qzgw"));//求职岗位
		   	  other_request.setText(info.get("qtyq"));//其它要求
		   	  graduation_date.setText(info.get("bysj"));//毕业时间
		   	  marriage_state.setText(info.get("hyzk"));//婚姻状况  
		   	  politics_status.setText(info.get("zzmm"));//政治面貌
		      educational_history.setText(info.get("xl"));//学历水平
		      work_area.setText(info.get("gzdq"));//工作区域
		      job_nature.setText(info.get("gzxz"));//工作性质
		      sr.setText(info.get("sr"));//生日
		      
		      String approve=info.get("is_open")==null?"N":info.get("is_open");
		      isApprove.setText(approve.equals("N")?"未审核":"已审核");
		      
//		      updateTime.setText(info.get("add_edit_date"));//修改时间
		   
		      /***********************************************************************************************************/
		      /**
		       * 头像获取  待修改
		       * 
		       */
		      String imgUrl = info.get("pic_addr");
		      if(imgUrl!=null && !imgUrl.equals("")){
					ImageLoader.getInstance().displayImage(Constants.IP +"images/"+ imgUrl.replace("\\", "/"),head);
				}
//		      String imgUrl = info.get("pic_addr");
//		      if(imgUrl !=null && !imgUrl.equals("")){//从远程加载图片
//		    	  headBamp=CommonUtil.getURLBitmap(ConstantUtil.ip+"images/"+imgUrl.replace("\\", "/"));
//					handler.sendEmptyMessage(2);
//		      }
		      
		     }else{
		    	 commonUtil.shortToast("信息加载失败!");
		    	 add_btn.setEnabled(false);
		    	 
		     }
		    }catch(Exception e){
		    	e.printStackTrace();
		    	commonUtil.shortToast("数据解析失败!");
		    	add_btn.setEnabled(false);
		    }
	  }
	  
	
	Runnable  runnable=new Runnable() {
		
		@Override
		public void run() {
			try{
				Thread.sleep(100);
				info=userDal.queryResumeDetail(user.getUserId(),"1");
				handler.sendEmptyMessage(1);
			}catch(Exception e){
				e.printStackTrace();
				handler.sendEmptyMessage(-1);
			}
		}
	};
	
	Handler handler=new Handler(){
		 public void handleMessage(Message msg) {
			 super.handleMessage(msg);
			 
			 if(msg.what==1){
				 initData();
			 }
//			 if(msg.what==2){//获取头象
//				 if(headBamp!=null){
//					 head.setImageBitmap(headBamp);
//				 }
//			 }
			  
			 if(progressDialog!=null){
				 progressDialog.dismiss();
			 }
		 };
	};
	
	private class ButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.add_btn:
				Intent addIntent = new Intent(QztPersonalResumeActivity.this,QztAddResumeActivity.class);
				//addIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				addIntent.putExtra("userId", user.getUserId());
				addIntent.putExtra("isAdd", isAdd);
				startActivity(addIntent);
				break;
			case R.id.editBtn:
				Intent editBtn = new Intent(QztPersonalResumeActivity.this,QztAddResumeActivity.class);
				//addIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				editBtn.putExtra("userId", user.getUserId());
				editBtn.putExtra("isAdd", isAdd);
				startActivity(editBtn);
				break;
			case R.id.back_btn:
				QztPersonalResumeActivity.this.finish();
				break;
			default:
				break;
			}
		}
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
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUserPage();
		
	}
	
	
}
