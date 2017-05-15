/*  HISTORY:     NAME               DATE          REASON	 */
/*  ------     -------              ----          ------	 */
/*            WangXinXin          02/12/2014      Original	 */
package com.sensible.app.data;


public class CarrierFtpInfo {    
    
	private int carrierId;
	private String carrierName;
	private String carrierIp;
	private int carrierPort;
	private String carrierUserName;
	private String carrierPassword;
	private String requestPath;
	private String requestRegex;
	private boolean carrierAvailable;
	private String uploadIp;
	private int uploadPort;
	private String uploadUserName;
	private String uploadPassword;
	private String resultPath;
	private String resultRegex;
	
	
	public int getCarrierId() {
		return carrierId;
	}
	
	public void setCarrierId(int carrierId) {
		this.carrierId=carrierId;
	}

	public String getCarrierName() {
		return carrierName;
	}
	
	public void setCarrierName(String carrierName) {
		this.carrierName=carrierName;
	}

	public String getCarrierIp(){
		return carrierIp;
	}
	
	public void setCarrierIp(String carrierIp) {
		this.carrierIp=carrierIp;
	}
	
	public int getCarrierPort(){
		return carrierPort;
	}
	
	public void setCarrierPort(int carrierPort) {
		this.carrierPort=carrierPort;
	}
	
	public String getCarrierUserName(){
		return carrierUserName;
	}
	
	public void setCarrierUserName(String carrierUserName){
		this.carrierUserName=carrierUserName;
	}
	
	public String getCarrierPassword(){
		return carrierPassword;
	}
	
	public void setCarrierPassword(String carrierPassword){
		this.carrierPassword=carrierPassword;
	}
	
	public String getRequestPath(){
		return requestPath;
	}
	
	public void setRequestPath(String requestPath){
		this.requestPath=requestPath;
	}
	
	public String getRequestRegex(){
		return requestRegex;
	}
	
	public void setRequestRegex(String requestRegex){
		this.requestRegex=requestRegex;
	}
	
	public boolean getCarrierAvailable(){
		return carrierAvailable;
	}
	
	public void setCarrierAvailable(boolean carrierAvailable){
		this.carrierAvailable=carrierAvailable;
	}
	
	public String getUploadIp(){
		return uploadIp;
	}
	
	public void setUploadIp(String uploadIp){
		this.uploadIp=uploadIp;
	}
	
	public int getUploadPort(){
		return uploadPort;
	}
	
	public void setUploadPort(int uploadPort){
		this.uploadPort=uploadPort;
	}
	
	public String getUploadUserName(){
		return uploadUserName;
	}
	
	public void setUploadUserName(String uploadUserName){
		this.uploadUserName=uploadUserName;
	}
	
	public String getUploadPassword(){
		return uploadPassword;
	}
	
	public void setUploadPassword(String uploadPassword){
		this.uploadPassword=uploadPassword;
	}
	
	public String getResultPath(){
		return resultPath;
	}
	
	public void setResultPath(String resultPath){
		this.resultPath=resultPath;
	}
	
	public String getResultRegex(){
		return resultRegex;
	}
	
	public void setResultRegex(String resultRegex){
		this.resultRegex=resultRegex;
	}
	    
}
