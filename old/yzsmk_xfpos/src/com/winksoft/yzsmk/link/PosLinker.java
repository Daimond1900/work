package com.winksoft.yzsmk.link;

import java.util.HashMap;
import java.util.Map;

import com.winksoft.yzsmk.desfire.Consumption;
import com.winksoft.yzsmk.link.linker.ILinker;
import com.winksoft.yzsmk.link.linker.ILinkerCallback;
import com.winksoft.yzsmk.link.linker.LinkerHandler;
import com.winksoft.yzsmk.link.linker.LinkerMessageType;
import com.winksoft.yzsmk.link.linker.TcpClientLinker;
import com.winksoft.yzsmk.utils.ByteUtils;
//import com.winksoft.yzsmk.link.yfposdemo.pos.PosFuntion;
//import com.winksoft.yzsmk.link.yfposdemo.pos.PosLinker;

import android.content.Context;
import android.util.Log;

public class PosLinker {
	Context m_context;
	int tagNum = 1;
	private Thread thr;
	ILinker link;
	static byte[] rcvBuffer = new byte[1024];
//	static int rcvLen = 0;

	public PosLinker(Context context) {
		m_context = context;
	}

	public void linker(Map<String, String> settingMap) {
		int linktype = 1;
		final HashMap<String, String> param = (HashMap<String, String>) settingMap;
		link = TcpClientLinker.getLinker(m_context);
//		param.put("ip", "222.189.216.110");		 // 我的主机
//		param.put("port", "2000");


//		param.put("ip", "192.168.0.103");		 // 我的主机
//		param.put("port", "2000");
		 
//		param.put("ip", "218.4.71.218");		 // 我的主机
//		param.put("port", "8086");

//		param.put("ip", "192.168.0.103");		 // 我的主机
//		param.put("port", "8080");

//		param.put("ip", "10.10.10.2");		// 扬州市民卡 IP
//		param.put("port", "11000");			// 短连接端口
//		param.put("port", "12000");			// 长连接端口
		
//		param.put("ip", "218.4.71.218");	//  架设在苏州的 扬州市民卡测试平台
//		param.put("port", "8086");				

		thr = new Thread(new Runnable() {
			@Override
			public void run() {
				link.connect(param, myHandler);
//				while (true) {
//					for (int tagIndex = 0; tagIndex < tagNum; tagIndex++) {
						//byte[] data = new byte[] { (byte) 0x00, (byte) 0x01 };
						PosLinker.rcvBuffer =null;
//						link.sendData(new byte[]{(byte)0x01,(byte)0x02});
//					}
//					break;
//				}
			}
		});
		thr.start();
	}
	
	public void sendPackage(final byte type,final byte[] data) {
		// 组包发送
		int dataLength = 0;
		int taolLength = 0;
		
//		byte[] len = new byte[2];
//		(int)data[0];
//		len[1] = (byte)data[1];

		if (data == null){
			dataLength = 0;
		}else{
			dataLength = ByteUtils.byteToInt(data[0],data[1]) + 2;// data[0]*256 + data[1]+2;
		}
		
		taolLength = dataLength+4+1;
		byte[] sendBuf = new byte[taolLength+1+2+1];
		sendBuf[0] = (byte)type;
		sendBuf[1] = (byte)((taolLength>>8)&0xff);
		sendBuf[2] = (byte)(taolLength&0xff);
		System.arraycopy(TransInforDesfire.trans_random, 0, sendBuf, 1+2, 4);
		System.arraycopy(TransInforDesfire.package_no, 0, sendBuf, 1+2+4, 1);
		if (data != null){
			System.arraycopy(data, 0, sendBuf, 1+2+4+1, dataLength);
		}
		byte checksum = ByteUtils.checkSumCal(sendBuf,0,sendBuf.length - 1);
		sendBuf[sendBuf.length - 1] = checksum;
		this.send(sendBuf);
	}
	
	public void send(final byte[] data) {
		PosLinker.rcvBuffer =null;
		link.sendData(data);
		
//		thr = new Thread(new Runnable() {
//			@Override
//			public void run() {
////				while (true){
////					if (link != null)
////						link.connect(param, myHandler);
//					while (true) {
////					for (int tagIndex = 0; tagIndex < tagNum; tagIndex++) {
//						//byte[] data = new byte[] { (byte) 0x00, (byte) 0x01 };
//						PosLinker.rcvBuffer =null;
//				// test 2016-11-10			
//		//		byte data1[] = {0x55,0x55,0x55};
//		//		Log.i("send---time2",data1.toString());
////				Log.i("send---time",data.toString());
//						link.sendData(data);
//						break;
//					}
////					break;
////				}
////						}
//			}
//		});
//		thr.start();
	}
	
	public void close(){
		if (link != null){
			link.disconnect();
		}
	}
 
	LinkerHandler myHandler = new LinkerHandler(new ILinkerCallback() {
		@Override
		public void onConnected() {
			// ��ʾ���ӳɹ���Ϣ
			Log.i("XXXXXXXXX", "���ӳɹ���");
		}

		@Override
		public void onError(Exception e) {
			// ��ʾ������Ϣ
			Log.e("XXXXXXXXX", e.getMessage());
		}

		@Override
		public void onDisconnected() {
			Log.i("XXXXXXXXX", "���ӶϿ���");
		}

		@Override
		public void onDataReceived(byte[] buffer) {
			
//			Log.i("recv---time",buffer.toString());
			PosLinker.rcvBuffer =  buffer;
			//PosLinker.send(PosLinker.rcvBuffer);
			Consumption.RcvHandler.obtainMessage(1, PosLinker.rcvBuffer).sendToTarget();
//			int m=0;
//			
//			m = 2;
//			LDSWTag tag = LDSWProtocol.decode(buffer);
//			if (tag != null){
//				Intent i = new Intent();
//				i.setPackage("com.yifeng.mapdemo");
//				Bundle b = new Bundle();;
//				b.putSerializable("LDSWTag", tag);  
//				i.setAction("com.yifeng.ldsw.message");
//				i.putExtra("value", b);
//				sendBroadcast(i);
//			}
			 
		}

	});
}
