/*  HISTORY:     NAME               DATE          REASON	 */
/*  ------     -------              ----          ------	 */
/*            WangXinXin          02/12/2014      Original	 */
package com.sensible.app.ftp;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;


import com.sensible.app.FtpServer;
import com.sensible.app.data.FtpBean;
import com.sensible.app.db.DBTools;
import com.sensible.app.ftp.common.DownLoadStatusStr;
import com.sensible.app.ftp.common.DownloadStatus;
import com.sensible.app.ftp.common.FtpOperator;
import com.sensible.app.ftp.common.UploadStatus;



public class FtpThread implements Runnable {
	
	private static Log log = LogFactory.getLog(FtpThread.class);
	private boolean runningflag;
	private FtpBean Info = new FtpBean();
	private int threadRestartCount;
	private int checkFailedCount;
	private DBTools dbtools;
	private String threadName;
	

	public void setThreadName(String name){
		this.threadName=name;
	}
	
	public String getThreadName(){
		return this.threadName;
	}
	
	public void InitFtpBeanInfo(FtpBean Info) {
		this.Info=Info;
	}
	
	public boolean getRunningFlag() {
		return runningflag;
	}
	
	public void setRunningFlag(boolean runningflag) {
		this.runningflag = runningflag;
	}
	
	public void setRunningFlagTrue() {
		setRunningFlag(true);
	}
	
	public void setRunningFlagFalse() {
		setRunningFlag(false);
	}
	
	public void threadRestartCountplus() {
		threadRestartCount++;
	}
	
	public int getThreadRestartCount() {
		return threadRestartCount;
	}
	
	public void checkFailedCountplus() {
		checkFailedCount++;
	}
	
	public int getCheckFailedCount() {
		return checkFailedCount;
	}
	
	public void toTmpDir(){
		
		FTPClient ftpClient = new FTPClient();
	    
		String Tmppath = Info.getTmpPath();

		log.info("Tmppath is "+Tmppath);
		try {
			List<String> oldrequestfileList = dbtools.getRequestFile();
			List<String> oldresultfileList = dbtools.getResultFile();
			
			log.info("LoadIp is " + Info.getLoadIp() + "  LoadPort is " + Info.getLoadPort()
                    +"  LoadUsername is "+Info.getLoadUsername()+"  LoadPassword is "+Info.getLoadPassword());
	        ftpClient = FtpOperator.connect(Info.getLoadIp(), Info.getLoadPort(), Info.getLoadUsername(), Info.getLoadPassword());
	        if (ftpClient != null) {
	        	
	        	ftpClient.enterLocalPassiveMode();  
	    	    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	    	    ftpClient.setControlEncoding("GBK");
                
                log.info("load_path is " + Info.getLoadPath() + "  Tmppath is " + Tmppath );

                DownLoadStatusStr status = FtpOperator.downloadFileByID(ftpClient, Info.getRegex(), Info.getLoadPath(), Tmppath, oldrequestfileList, oldresultfileList, Info.getCarrierid());
                if (status != null) {
                    if(status.getStatus() == DownloadStatus.Download_New_Success||status.getStatus()  == DownloadStatus.Download_From_Break_Success){
                        log.info("DownloadStatus Success, status.getSuccessStr() is "+status.getSuccessStr());
                    }
                    else{
                        log.info("DownloadStatus is "+status.getStatus());
                    }
                }
                else{
                    log.info("DownloadStatus fail ");
                }
            }
            else{
                log.info("Ftp init client error");
            }
			
		}catch (Exception e) {
			
			log.info("catch Exception, Exception is "+e);

			try {
				if(ftpClient!=null){
					FtpOperator.disconnect(ftpClient);
					ftpClient = null;
				}
			} catch (IOException e1) {
	
				log.info("FtpOperator.disconnect catch Exception, Exception is "+e1);
				e1.printStackTrace();
			}finally{
				log.info("toTmpDir: finally in catch Exception");
			}
			
		}finally{
			log.info("toTmpDir: finally");
		}
	}
	
	
	
	public void toDestDir(){
		FTPClient ftpClient = new FTPClient();
	    
		boolean downloadflag=false;
		try{
			String Tmppath = Info.getTmpPath();
			log.info("Tmppath is "+Tmppath);
			
			File file = new File(Tmppath);
			if(!file.exists()&&!file.isDirectory()){
			    log.info("file not found");
				file.mkdir();
			}else{
			    log.info(Tmppath+" exists");
			}
			
			File[] files = file.listFiles();
			log.info("files.length="+files.length);
			int len = 0;
			if(files!=null){
			    log.info("files!=null ");
				len = files.length;
			}
			log.info("len is "+len);
			if(len>0){
				String uploadpath = Info.getUploadPath();
				System.out.println("uploadpath is "+uploadpath);
				log.info("UploadIp is " + Info.getUploadIp() + "  UploadPort is " + Info.getUploadPort()
	                    +"  UploadUsername is " + Info.getUploadUsername() + "  UploadPassword is "+Info.getUploadPassword());
				ftpClient = FtpOperator.connect(Info.getUploadIp(), Info.getUploadPort(), Info.getUploadUsername(), Info.getUploadPassword());

				if(ftpClient!=null){
					
					ftpClient.enterLocalPassiveMode();  
				    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				    ftpClient.setControlEncoding("GBK");
					
					 for(File file1:files){
						 downloadflag=false;
						 
						 Pattern pattern = Pattern.compile(Info.getRegex());
		    			 if(pattern.matcher(file1.getName()).matches()) {
		    			 	 downloadflag=true;
		    				 log.info("file " + file1.getName() + " matches");
		    			 }
						 
						 if(true==downloadflag){
	
							 File tmpfile = new File(Tmppath+file1.getName());
							 log.info("tmpfile is" + tmpfile );
							 log.info(Tmppath + "/" + file1.getName() );
							 log.info(uploadpath + "/" + file1.getName() );
	
							UploadStatus status = FtpOperator.upload(ftpClient, Tmppath + "/" + file1.getName(), uploadpath + "/" + file1.getName());				
	        				
							log.info("FtpOperator.upload: status is " + status);
							
							if(status == UploadStatus.Upload_From_Break_Success || status  == UploadStatus.Upload_New_File_Success){
	        					 
	        					 File tmp = new File(Tmppath+"/"+file1.getName());
	        					 tmp.delete();
	        					 tmp = null;
	        					 log.info("FtpOperator.upload success. filename is "+file1.getName());
	        					 
	        					 if(!FtpOperator.containsAny(file1.getName().toUpperCase(), "RES")&&(file1.getName().toUpperCase().endsWith(".CSV"))) {
	        						 dbtools.updateRequestTime(file1.getName());
	        					 }
	        					 
	        					 if(FtpOperator.containsAny(file1.getName().toUpperCase(), "RES")&&(file1.getName().toUpperCase().endsWith(".CSV"))) {
	        						 dbtools.updateResultTime(file1.getName());
	        					 }
	        				 }else{
	
	        				     log.info("FtpOperator.upload status is "+status+". filename is "+file1.getName());
	        					 if(status  == UploadStatus.File_Exists){
	        						 File tmp = new File(Tmppath+"/"+file1.getName());
		        					 tmp.delete();
		        					 tmp = null;
	        					 } else {
	
	        					 }
	        				 }		 
							
						 }

					 }
					if(ftpClient!=null){
						FtpOperator.disconnect(ftpClient);
						ftpClient = null;
					}
				}else{
				    log.info("toDestDir: FTP init client error");
				}
				files = null;
			}
			else{
			    log.info("toDestDir: len="+len);
			}
		}catch (Exception e) {
			
			log.info("toDestDir: catch Exception, Exception is "+e);

			try {
				if(ftpClient!=null){
					FtpOperator.disconnect(ftpClient);
					ftpClient = null;
				}
			} catch (IOException e1) {
	
				log.info("FtpOperator.disconnect catch Exception, Exception is "+e1);
				e1.printStackTrace();
			}finally{
				log.info("toDestDir: finally in catch Exception");
			}
			
		}finally{
			log.info("toDestDir: finally");
		}
		
	}

	
	public FtpThread(FtpBean Info) {
		
	    log.info("Starting FtpThread ...");
	    
	    dbtools = (DBTools) FtpServer.getApplicationContext().getBean("dbtools");
	    InitFtpBeanInfo(Info);
	    
		log.info("Starting FtpThread successfully ...");
	}

	

	@Override
	public void run() {
		setRunningFlagTrue();
		while(true){
			try{
				if(runningflag){
					toTmpDir();
					toDestDir();
					setRunningFlagFalse();
				}else{
					try{
					    Thread.sleep(1000);
					}catch(Exception e){
						log.error("sleep error, ", e);
					} 
				}
			}catch(Exception  e){
				log.error("while error, ", e);
			}
		}
	}
}
