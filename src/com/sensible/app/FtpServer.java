/*  HISTORY:     NAME               DATE          REASON	 */
/*  ------     -------              ----          ------	 */
/*            WangXinXin          02/12/2014      Original	 */
package com.sensible.app;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import com.sensible.app.ftp.ThreadManager;


public class FtpServer {
	private static Log log = LogFactory.getLog(FtpServer.class);
	private static AbstractApplicationContext ctx;
	
	public static ApplicationContext getApplicationContext() {
		while (ctx == null)
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return ctx;
	}

	public static void configLogProperty() {
		String confPath = System.getProperty("user.dir") + "/conf/";
		PropertyConfigurator.configure(confPath + "log4j.properties");
	}
	
	public static void getConfigInfo() {
		String confPath = System.getProperty("user.dir") + "/conf/";
		String[] confFiles = new String[] { 
				"file:" + confPath + "spring-beans.xml",
				"file:" + confPath + "spring-data.xml" };

		ctx = new FileSystemXmlApplicationContext(confFiles);
		ctx.registerShutdownHook();
	}
	
	public static void startThreadManager() {
		ThreadManager manager = new ThreadManager();
		manager.start();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			configLogProperty();
			getConfigInfo();
			startThreadManager();
		}catch( Exception e ) {
			e.printStackTrace();
			log.info(e);
		}
	}

}
