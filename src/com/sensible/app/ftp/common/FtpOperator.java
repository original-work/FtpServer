/*  HISTORY:     NAME               DATE          REASON	 */
/*  ------     -------              ----          ------	 */
/*            WangXinXin          02/12/2014      Original	 */
package com.sensible.app.ftp.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.sensible.app.FtpServer;
import com.sensible.app.db.DBTools;


public class FtpOperator {
	
	private static DBTools dbtools = (DBTools) FtpServer.getApplicationContext().getBean("dbtools");
	private static Log log = LogFactory.getLog(FtpOperator.class);
	private static byte[] bytes = new byte[409600];
	
	public static FTPClient connect(String hostname,int port,String username,String password) throws IOException{
        FTPClient ftpClient = new FTPClient();
		try{
    		ftpClient.setConnectTimeout(1000); // 连接超时为1秒钟，如果超过就判定超时了
    		log.info("FtpOperator connect "  + hostname + " ; " + port + " ; " + username + " ; " + password);
    		ftpClient.connect(hostname, port);
    		if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){
    			log.debug("ftp isPositiveCompletion");
    			if(ftpClient.login(username, password)){
    				log.debug("ftp login");
    				return ftpClient;
    			}
    		}
		}
		catch (Exception e){
		    log.info("ftp connect catch Exception, Exception is"+e);
		    try {
                if(ftpClient!=null){
                    disconnect(ftpClient);
                    ftpClient = null;
                    return null;  
                }
            } catch (IOException e1) {
                log.info("ftp connect catch Exception 1111, Exception is"+e1);
                e1.printStackTrace();
                return null; 
            }
		}
		log.debug("ftp connect fail");
        disconnect(ftpClient);     
        return null;  
	}  


	
	
    public static void disconnect(FTPClient ftpClient) throws IOException{
        try {  
            ftpClient.logout();
            if (ftpClient!=null && ftpClient.isConnected()) {  
                ftpClient.disconnect();  
                ftpClient = null;  
            }
        } catch (IOException e) {  
            log.debug("Could not disconnect from server.");  
        }  
    }
    
	
	
	public static boolean containsAny(String str, String searchChars) {
        if(str.contains(searchChars))
            return true;
        else
            return false;
    }

	
    
    public static DownLoadStatusStr downloadFileByID(FTPClient ftpClient, String regex, String remote, String tmppath, 
    		List<String> oldrequestfileList, List<String> oldresultfileList, int carrierId) throws IOException{  
        
    	boolean downloadflag=false;
    	DownLoadStatusStr result = new DownLoadStatusStr();
    	
        int reply=ftpClient.getReplyCode();
        if(!FTPReply.isPositiveCompletion(reply)){
            ftpClient.disconnect();
            log.debug("FTP server refused connectd");
            result.setStatus(DownloadStatus.Remote_File_Noexist);
            return result;
        }
  
        log.info("remote is "+remote);
        
        FTPFile[] files = ftpClient.listFiles(new String(remote.getBytes("GBK"),"iso-8859-1"));
        log.info("downloadFileByID files.length is "+files.length);
        if(files.length < 1){
            log.debug("remote file not exist");
            result.setStatus(DownloadStatus.Remote_File_Noexist);
            return result;
        }
        for(int i=0;i<files.length;i++){
            downloadflag=false;
            try {
                String filename = files[i].getName();
                
                if(!FtpOperator.containsAny(files[i].getName().toUpperCase(), "RES")&&(files[i].getName().toUpperCase().endsWith(".CSV"))) {
                	if (oldrequestfileList != null && oldrequestfileList.contains(filename)) {
                        continue;
                    }
				}
                if(FtpOperator.containsAny(files[i].getName().toUpperCase(), "RES")&&(files[i].getName().toUpperCase().endsWith(".CSV"))) {
                	if (oldresultfileList != null && oldresultfileList.contains(filename)) {
                        continue;
                    }
				}
                
//                if (oldfileList != null && oldfileList.contains(filename)) {
//                    continue;
//                }
                
                long lRemoteSize = files[i].getSize();
                if(lRemoteSize<30) {
	                log.info("file " + filename + " Size is " + lRemoteSize);
	                continue;
                }
                
                Pattern pattern = Pattern.compile(regex);
    			if(pattern.matcher(filename).matches()) {
    				downloadflag=true;
    				log.info("file " + filename + " matches");
    			}
                
                if(true==downloadflag){
                
	                log.info("..start to download [" +filename + "]");
	                
	                String[] str = ftpClient.getModificationTime(remote+"/"+files[i].getName()).split(" ");
	                String ModificationTime = str[1];

                    log.info("filename is " + tmppath+"/"+files[i].getName());
                    File f = new File(tmppath+"/"+files[i].getName());
                   
                   OutputStream out = new FileOutputStream(f);
                   InputStream in = ftpClient.retrieveFileStream(remote+"/"+files[i].getName());
                   if (in == null) {
                       log.warn("In is null ,fileName: " + files[i].getName()+"ftpClient.getReplyCode()="+ftpClient.getReplyCode());
                       continue;
                   }
                   byte[] bytes = new byte[1024];  
                   int c;  
                   while((c = in.read(bytes))!= -1){  
                       out.write(bytes, 0, c);  
                       out.flush();
                   }
                   in.close();  
                   out.close();
                   boolean upNewStatus = ftpClient.completePendingCommand();  
                   if(upNewStatus){    
                       result.setStatus(DownloadStatus.Download_New_Success);
                       result.setSuccessStr(files[i].getName());
                       log.info("Download_New_Success. filename is "+files[i].getName());
  					    
                       if(!FtpOperator.containsAny(files[i].getName().toUpperCase(), "RES")&&(files[i].getName().toUpperCase().endsWith(".CSV"))) {
                    	   dbtools.addRequestFile(files[i].getName(), carrierId, ModificationTime, lRemoteSize);
  					   }
	                   if(FtpOperator.containsAny(files[i].getName().toUpperCase(), "RES")&&(files[i].getName().toUpperCase().endsWith(".CSV"))) {
	                	   dbtools.addResultFile(files[i].getName(), carrierId, ModificationTime, lRemoteSize);
	  				   }
                   }else {    
                       result.setStatus(DownloadStatus.Download_New_Failed);
                       result.setSuccessStr(files[i].getName());
                   }  
                   f = null;
                }
            } catch (Exception e) {
                log.error(e);
                continue;
            }
        }       
        files = null ;
        return result;  
    }
	
	
	

	public static UploadStatus upload(FTPClient ftpClient,String local,String remote) throws IOException{  

	    UploadStatus result;  
	    UploadStatus status;
	    String remoteFileName = remote;  
	    
	    if(remote.contains("/")){
		    remoteFileName = remote.substring(remote.lastIndexOf("/")+1); 
		    log.info("remoteFileName is "+remoteFileName+"\nremote is "+remote);
		    status=CreateDirectory(remote, ftpClient);
		    log.info("CreateDirectory status is "+status);
		    if(status==UploadStatus.Create_Directory_Fail){
		        log.error("CreateDirectory fail");
		    	return UploadStatus.Create_Directory_Fail;
		    }  
	    }
	    FTPFile[] files = ftpClient.listFiles(new String(remoteFileName.getBytes("GBK"),"iso-8859-1"));
	    log.info("files.length is "+files.length );
	    

	    remoteFileName = "TEMP_" + remoteFileName;
	    log.info("remoteFileName is " + remoteFileName );

    	result = uploadFile(remoteFileName, new File(local), ftpClient);  

	    if(result == UploadStatus.Upload_From_Break_Success||result == UploadStatus.Upload_New_File_Success){
	    	ftpClient.rename(remoteFileName, remote.substring(remote.lastIndexOf("/")+1));
	    }
	    return result;  
	}  
	

	
	
	public static UploadStatus CreateDirectory(String remote,FTPClient ftpClient) throws IOException{  
		UploadStatus status = UploadStatus.Create_Directory_Success;  
		String directory = remote.substring(0,remote.lastIndexOf("/")+1);  
		
		log.info("directory is "+directory);

		if(!directory.equalsIgnoreCase("/")&&!ftpClient.changeWorkingDirectory(directory)){
		    int start=0;  
		    int end = 0;  
		    if(directory.startsWith("/")){  
		    	start = 1;  
		    }else{  
		    	start = 0;  
		    }  
		    end = directory.lastIndexOf("/");
		    while(true){
		        String subDirectory = new String(remote.substring(start,end));
		        log.info("subDirectory is "+subDirectory);
			    if(!ftpClient.changeWorkingDirectory(subDirectory)){  
			    	if(ftpClient.makeDirectory(subDirectory)){  
			    		ftpClient.changeWorkingDirectory(subDirectory);  
			    	}else {  
			    		log.error("makeDirectory fail , subDirectory is "+subDirectory);
			    		return UploadStatus.Create_Directory_Fail;  
			    	}  
			     }  
			     
			     start = end + 1;  
			     end = directory.indexOf("/",start);  
			     
			     if(end <= start){  
			    	 break;  
			     }  
		   }  
	   }  
	   return status;  
	}
	
	
	
	  
	public static UploadStatus uploadFile(String remoteFile,File localFile,FTPClient ftpClient) throws IOException{
	    
		UploadStatus status;
	    RandomAccessFile raf = new RandomAccessFile(localFile,"rw");
	    log.info("remoteFile is "+remoteFile+"\nlocalFile is "+localFile);
	    try{
	    	log.info("upload working directory is "+ftpClient.printWorkingDirectory());
//		    OutputStream out = ftpClient.appendFileStream(remoteFile);
	    	OutputStream out = ftpClient.storeFileStream(remoteFile);
	    	log.info("ftpClient.getReplyStrings: "+(java.util.Arrays.toString(ftpClient.getReplyStrings())));
		    log.info("out is "+out);
		    if(out==null){
		    	log.debug("out is null");
		    }
		      
		    int c;

		    while((c = raf.read(bytes))!= -1){
		        log.info("content="+c );
			    out.write(bytes,0,c);  
			    log.info("out.write end");
		    }
		    out.flush();  
		    raf.close();  
		    out.close();  
	    }catch(Exception e){
	    	log.error("uploadFile Exception: ", e);
	    }
	    boolean result =ftpClient.completePendingCommand();

	    status = result?UploadStatus.Upload_New_File_Success:UploadStatus.Upload_New_File_Failed;  

	    return status;  
	}

}
