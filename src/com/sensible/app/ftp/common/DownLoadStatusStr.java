/*  HISTORY:     NAME               DATE          REASON	 */
/*  ------     -------              ----          ------	 */
/*            WangXinXin          02/12/2014      Original	 */
package com.sensible.app.ftp.common;


public class DownLoadStatusStr {
	private String successStr = "";
	private String failureStr = "";
	private DownloadStatus status;
	
	
	public String getSuccessStr() {
		return successStr;
	}
	public void setSuccessStr(String successStr) {
		if(successStr.trim().length()==0)
			this.successStr = successStr;
		else
			this.successStr = this.successStr+","+successStr;
	}
	
	public String getFailureStr() {
		return failureStr;
	}
	public void setFailureStr(String failureStr) {
		if(failureStr.trim().length()==0)
			this.failureStr = failureStr;
		else
			this.failureStr = this.failureStr+","+failureStr;
	}
	
	public DownloadStatus getStatus() {
		return status;
	}
	public void setStatus(DownloadStatus status) {
		this.status = status;
	}
  
}
