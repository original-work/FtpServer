/*  HISTORY:     NAME               DATE          REASON	 */
/*  ------     -------              ----          ------	 */
/*            WangXinXin          02/12/2014      Original	 */
package com.sensible.app.ftp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sensible.app.FtpServer;
import com.sensible.app.ftp.FtpThread;
import com.sensible.app.data.FtpInfo;
import com.sensible.app.data.ConfigInfo;
import com.sensible.app.data.CarrierFtpInfo;
import com.sensible.app.db.DBTools;



public class ThreadManager extends Thread {

	private static Log log = LogFactory.getLog(ThreadManager.class);
	private int carriernum;
	private List<FtpThread> loadthread = new ArrayList<FtpThread>();
	private List<FtpThread> uploadthread = new ArrayList<FtpThread>();
	private List< FtpInfo> listftpinfo = new ArrayList<FtpInfo>();
	private DBTools dbtools = (DBTools) FtpServer.getApplicationContext().getBean("dbtools");
	
	public void initCarrierNum() {
		carriernum = DBTools.listcarrierftpinfo.size();
	}
	
	public void initListFtpInfo() {
		for(int i=0;i<carriernum;i++){
			FtpInfo bean = new FtpInfo();
			CarrierFtpInfo Info = DBTools.listcarrierftpinfo.get(i);
			
			bean.FtpLoadInfo.setCarrierid(Info.getCarrierId());
			bean.FtpLoadInfo.setLoadIp(Info.getCarrierIp());
			bean.FtpLoadInfo.setLoadPassword(Info.getCarrierPassword());
			bean.FtpLoadInfo.setLoadPath(Info.getRequestPath());//source
			bean.FtpLoadInfo.setLoadPort(Info.getCarrierPort());
			bean.FtpLoadInfo.setRegex(Info.getRequestRegex());
			bean.FtpLoadInfo.setLoadUsername(Info.getCarrierUserName());
			bean.FtpLoadInfo.setTmpPath(ConfigInfo.ftp_tmppath);
			bean.FtpLoadInfo.setUploadIp(ConfigInfo.rnmftp_ip);
			bean.FtpLoadInfo.setUploadPassword(ConfigInfo.rnmftp_password);
			bean.FtpLoadInfo.setUploadPath(ConfigInfo.rnmftp_requestpath);//destination
			bean.FtpLoadInfo.setUploadPort(Integer.valueOf(ConfigInfo.rnmftp_port));
			bean.FtpLoadInfo.setUploadUsername(ConfigInfo.rnmftp_user);
			
			
			bean.FtpUpLoadInfo.setCarrierid(Info.getCarrierId());
			bean.FtpUpLoadInfo.setLoadIp(ConfigInfo.rnmftp_ip);
			bean.FtpUpLoadInfo.setLoadPassword(ConfigInfo.rnmftp_password);
			bean.FtpUpLoadInfo.setLoadPath(ConfigInfo.rnmftp_resultpath);//source
			bean.FtpUpLoadInfo.setLoadPort(Integer.valueOf(ConfigInfo.rnmftp_port));
			bean.FtpUpLoadInfo.setRegex(Info.getResultRegex());
			bean.FtpUpLoadInfo.setLoadUsername(ConfigInfo.rnmftp_user);
			bean.FtpUpLoadInfo.setTmpPath(ConfigInfo.ftp_tmppath);
			bean.FtpUpLoadInfo.setUploadIp(Info.getUploadIp());
			bean.FtpUpLoadInfo.setUploadPassword(Info.getUploadPassword());
			bean.FtpUpLoadInfo.setUploadPath(Info.getResultPath());//destination
			bean.FtpUpLoadInfo.setUploadPort(Info.getUploadPort());
			bean.FtpUpLoadInfo.setUploadUsername(Info.getUploadUserName());
			
			listftpinfo.add(bean);
		}
	}
	
	public void startFtpThread() {
		try{
			for(int i=0;i<carriernum;i++){
				String name = DBTools.listcarrierftpinfo.get(i).getCarrierName();
				
				log.info("startFtpThread(): i = "+i+" carriernum= "+carriernum+" name = "+name);
				
		    	FtpThread load_thread = new FtpThread(listftpinfo.get(i).FtpLoadInfo);
		    	loadthread.add(load_thread);
		    	name = name + "-load";
		    	load_thread.setThreadName(name);
		    	(new Thread(load_thread,name)).start();

		    	
		    	name = DBTools.listcarrierftpinfo.get(i).getCarrierName();
		    	FtpThread upload_thread = new FtpThread(listftpinfo.get(i).FtpUpLoadInfo);
		    	uploadthread.add(upload_thread);
		    	name = name + "-upload";
		    	upload_thread.setThreadName(name);
		    	(new Thread(upload_thread,name)).start();
		    }
		}catch (Exception e){
	        log.error("startFtpThread(): catch Exception, Exception is "+e);
	    }
	}
	
	
	private void checkFtp(FtpThread thread){
		if(!thread.getRunningFlag()){
			thread.threadRestartCountplus();
			log.info("Restart " + thread.getThreadName() +" "+ thread.getThreadRestartCount() + " times, please wait for a moment ...");
			try{
				thread.setRunningFlagTrue();
				log.info("Restart " + thread.getThreadName() + " successfully ...");
			}catch(Exception e){
			    log.info("Restart " + thread.getThreadName() + " catch Exception, Exception is "+e);
			}
		}else{
			thread.checkFailedCountplus();
		    log.info("check " + thread.getThreadName() + " failed "+ thread.getCheckFailedCount() + " times");
		    if(thread.getCheckFailedCount()>=Integer.parseInt(ConfigInfo.threadmgr_restartinterval)){
		    	System.exit(1);
		    }
		}
	}
	
	
	
	
	public void checkFtpThread() {

	    try{
	    
			Thread.sleep(Integer.parseInt(ConfigInfo.threadmgr_checkinterval));		
			
			for(int i=0;i<carriernum;i++){
				FtpThread load_thread = loadthread.get(i);
				FtpThread upload_thread = uploadthread.get(i);
				log.info("checkFtpThread(): load_thread is "+load_thread);
				log.info("checkFtpThread(): upload_thread is "+upload_thread);
				checkFtp(load_thread);
				checkFtp(upload_thread);				
			}
	    }catch (Exception e){
	        log.info("checkFtpThread(): catch Exception, Exception is "+e);
	    }
	}
	
	
	public ThreadManager() {
		
	    log.info("Starting ThreadManager ...");
	    
	    dbtools.initListCarrierFtpInfo();
	    initCarrierNum();
	    initListFtpInfo();
	    startFtpThread();
		
		log.info("Starting ThreadManager successfully ...");
	}


	@Override
	public void run() {
		
		while(true){
			checkFtpThread();
		}
		
	}

}
