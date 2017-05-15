/*  HISTORY:     NAME               DATE          REASON	 */
/*  ------     -------              ----          ------	 */
/*            WangXinXin          02/12/2014      Original	 */
package com.sensible.app.data;




public class ConfigInfo {
	private static final ConfigInfo instance = new ConfigInfo();
	public static String rnmftp_ip;
	public static String rnmftp_port;
	public static String rnmftp_user;
	public static String rnmftp_password;
	public static String ftp_tmppath;
	public static String rnmftp_requestpath;
	public static String rnmftp_resultpath;
	public static String threadmgr_checkinterval;
	public static String threadmgr_restartinterval;

	
	public static ConfigInfo getInstance() {
		return instance;
	}
	
	public String getrnmftp_ip() {
		return rnmftp_ip;
	}
	
	public void setrnmftp_ip(String rnmftp_ip) {
		ConfigInfo.rnmftp_ip = rnmftp_ip;
	}

	public String getrnmftp_port() {
		return rnmftp_port;
	}
	
	public void setrnmftp_port(String rnmftp_port) {
		ConfigInfo.rnmftp_port = rnmftp_port;
	}
	
	public String getrnmftp_user() {
		return rnmftp_user;
	}
	
	public void setrnmftp_user(String rnmftp_user) {
		ConfigInfo.rnmftp_user = rnmftp_user;
	}

	public String getrnmftp_password() {
		return rnmftp_password;
	}
	
	public void setrnmftp_password(String rnmftp_password) {
		ConfigInfo.rnmftp_password = rnmftp_password;
	}
	
	public String getftp_tmppath() {
		return ftp_tmppath;
	}
	
	public void setftp_tmppath(String ftp_tmppath) {
		ConfigInfo.ftp_tmppath = ftp_tmppath;
	}

	public String getrnmftp_requestpath() {
		return rnmftp_requestpath;
	}
	
	public void setrnmftp_requestpath(String rnmftp_requestpath) {
		ConfigInfo.rnmftp_requestpath = rnmftp_requestpath;
	}
	
	public String getrnmftp_resultpath() {
		return rnmftp_resultpath;
	}
	
	public void setrnmftp_resultpath(String rnmftp_resultpath) {
		ConfigInfo.rnmftp_resultpath = rnmftp_resultpath;
	}
	
	public String getthreadmgr_checkinterval() {
		return threadmgr_checkinterval;
	}
	
	public void setthreadmgr_checkinterval(String threadmgr_checkinterval) {
		ConfigInfo.threadmgr_checkinterval = threadmgr_checkinterval;
	}

	public String getthreadmgr_restartinterval() {
		return threadmgr_restartinterval;
	}
	
	public void setthreadmgr_restartinterval(String threadmgr_restartinterval) {
		ConfigInfo.threadmgr_restartinterval = threadmgr_restartinterval;
	}
	
}
