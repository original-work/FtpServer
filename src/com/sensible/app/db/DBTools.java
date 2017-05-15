/*  HISTORY:     NAME               DATE          REASON	 */
/*  ------     -------              ----          ------	 */
/*            WangXinXin          02/12/2014      Original	 */
package com.sensible.app.db;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sensible.app.data.CarrierFtpInfo;




@SuppressWarnings("unchecked")
public class DBTools{
    private static Log log = LogFactory.getLog(DBTools.class);
	private static final DBTools instance = new DBTools();
	private NamedParameterJdbcOperations template;
	public static List<CarrierFtpInfo> listcarrierftpinfo = new ArrayList<CarrierFtpInfo>();
	
	public NamedParameterJdbcOperations getTemplate() {
		return template;
	}

	public void setTemplate(NamedParameterJdbcOperations template) {
		this.template = template;
	}
	
	public static final DBTools getInstance(){
		return instance;
	}

	
	public void addRequestFile(String FileName, int carrierId, String ServerTime, long size){

		if (FileName == null || FileName.length() == 0) {
			return;
		}
		log.debug("addRequestFile: FileName="+FileName);
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource("");
		String[] filename = FileName.trim().split(",");
		
		if (filename.length > 0) {
			for (String ss : filename) {
				if (ss == null || ss == "" || ss.trim().length() <=1) {
					continue;
				}
				
				String Name = ss.substring(0,ss.lastIndexOf("."));
				
				String updatesql = "insert requestcsv(name, fileName, carrierId, carrierTime, chinatelecomTime, rnmTime, size) " + "VALUES('" + Name + 
					"','" + ss + "','" + carrierId + "','" + ServerTime + "','" + new Timestamp(System.currentTimeMillis()) + "','" + 
					"0000-00-00" + "','" + size + "')";
				log.debug("addRequestFile: updatesql="+updatesql);
				try {
						template.update(updatesql, namedParameters);
				} catch (DataAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	public List<String> getRequestFile(){
		List<String> listbean = new ArrayList<String>();
		String sql = "select * from requestcsv";
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource("");
		try {
			List<Map> listmap = template.queryForList(sql, namedParameters);
			if(listmap!=null && listmap.size()>0){
				Map map = null;
				int i=0;
				while(!listmap.isEmpty()){
				    map = listmap.remove(0);
		            String file = (String)map.get("fileName");
                    log.debug("getRequestFile: filename["+i+"] is "+file);
                    i++;
		            if (file != null && file.trim().length() > 0) {
		            	listbean.add((String)map.get("fileName"));
		            }
				}
			}
		} catch (DataAccessException e) {
			log.error(e,e);
			return null;
		}
		return listbean;
	}
	
	
	
	
	public void addResultFile(String FileName, int carrierId, String ServerTime, long size){

		if (FileName == null || FileName.length() == 0) {
			return;
		}
		log.debug("addFile: FileName="+FileName);
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource("");
		String[] filename = FileName.trim().split(",");
		
		if (filename.length > 0) {
			for (String ss : filename) {
				if (ss == null || ss.trim().length() <=1) {
					continue;
				}
				
				String Name = ss.substring(0,ss.indexOf("."));
				
				String updatesql = "insert resultcsv(name, fileName, carrierId, crnmsyncdataTime, chinatelecomTime, endTime, size) " + "VALUES('" + Name + 
					"','" + ss + "','" + carrierId + "','" + ServerTime + "','" + new Timestamp(System.currentTimeMillis()) + "','" + 
					"0000-00-00" + "','" + size + "')";
				log.debug("addResultFile: updatesql="+updatesql);
				try {
						template.update(updatesql, namedParameters);
				} catch (DataAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	public List<String> getResultFile(){
		
		List<String> listbean = new ArrayList<String>();
		String sql = "select * from resultcsv";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		try {
			List<Map> listmap = template.queryForList(sql, namedParameters);
			if(listmap!=null && listmap.size()>0){
				Map map = null;
				int i=0;
				while(!listmap.isEmpty()){		
				    map = listmap.remove(0);
		            String file = (String)map.get("fileName");
                    log.debug("getFile: filename["+i+"] is "+file);
                    i++;
		            if (file != null && file.trim().length() > 0) {
		            	listbean.add((String)map.get("fileName"));
		            }
				}
			}
		} catch (DataAccessException e) {
			log.error(e);
			return null;
		}
		return listbean;
	}
	
	
//	public int getCarrierNum(){
//		int num=0;
//		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
//		String sql = "select * from Carrierftpinfo";
//		
//		try{
//			List<Map> listmap=template.queryForList(sql, namedParameters);
//			num=listmap.size();
//		}catch(Exception e){
//			log.error(e);
//			log.info("ERROR: getCarrierNum failure");
//			System.exit(1);
//		}
//		return num;
//	}
	
	
	public void initListCarrierFtpInfo() {
		String sql = "select * from Carrierftpinfo";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		try {
			List<Map> listmap = template.queryForList(sql, namedParameters);
			Map map = null;
			
			if(listmap!=null && listmap.size()>0){
				while(!listmap.isEmpty()){		
				    map = listmap.remove(0);
				    CarrierFtpInfo bean = new CarrierFtpInfo();
				    
		            bean.setCarrierAvailable((Boolean)map.get("carrierAvailable"));
		            bean.setCarrierId((Integer)map.get("carrierId"));
		            bean.setCarrierIp((String)map.get("carrierIp"));
		            bean.setCarrierName((String)map.get("carrierName"));
		            bean.setCarrierPassword((String)map.get("carrierPassword"));
		            bean.setCarrierPort((Integer)map.get("carrierPort"));
		            bean.setCarrierUserName((String)map.get("carrierUserName"));
		            bean.setRequestPath((String)map.get("requestPath"));
		            bean.setRequestRegex((String)map.get("requestRegex"));
		            bean.setResultPath((String)map.get("resultPath"));
		            bean.setResultRegex((String)map.get("resultRegex"));
		            bean.setUploadIp((String)map.get("uploadIp"));
		            bean.setUploadPassword((String)map.get("uploadPassword"));
		            bean.setUploadPort((Integer)map.get("uploadPort"));
		            bean.setUploadUserName((String)map.get("uploadUserName"));

		            listcarrierftpinfo.add(bean);
				}
			}
		} catch (DataAccessException e) {
			log.error(e);
		}
	}
	
	
//	public void initListCarrierFtpInfo() {
//		for(int i=0;i<getCarrierNum();i++){
//			String sql = "select * from Carrierftpinfo limit "+ i +", 1";
//			List<Map> listmap = getBeans(sql);
//			if(listmap!=null && listmap.size()>0){
//				Map map = null;
//				while(!listmap.isEmpty()){
//					CarrierFtpInfo bean = new CarrierFtpInfo();
//		            map = listmap.remove(0);
//		            
//		            bean.setCarrierAvailable((Boolean)map.get("carrierAvailable"));
//		            bean.setCarrierId((Integer)map.get("carrierId"));
//		            bean.setCarrierIp((String)map.get("carrierIp"));
//		            bean.setCarrierName((String)map.get("carrierName"));
//		            bean.setCarrierPassword((String)map.get("carrierPassword"));
//		            bean.setCarrierPort((Integer)map.get("carrierPort"));
//		            bean.setCarrierUserName((String)map.get("carrierUserName"));
//		            bean.setRequestPath((String)map.get("requestPath"));
//		            bean.setRequestRegex((String)map.get("requestRegex"));
//		            bean.setResultPath((String)map.get("resultPath"));
//		            bean.setResultRegex((String)map.get("resultRegex"));
//		            bean.setUploadIp((String)map.get("uploadIp"));
//		            bean.setUploadPassword((String)map.get("uploadPassword"));
//		            bean.setUploadPort((Integer)map.get("uploadPort"));
//		            bean.setUploadUserName((String)map.get("uploadUserName"));
//
//		            listcarrierftpinfo.add(bean);
//				}
//			}
//		}
//	}
	
	
	public List<Map> getBeans(String sql){
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		List<Map> listmap = null;
		try {
			listmap = template.queryForList(sql, namedParameters);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
		return listmap;
	}
	
	
	public void updateRequestTime(String filename) {
		String updatesql = "update requestcsv set rnmTime = " + "'" + new Timestamp(System.currentTimeMillis()) + "'" +
				" where fileName = " + "\"" + filename + "\"";
		log.debug(updatesql);
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource("");
		try {
			template.update(updatesql, namedParameters);
		} catch (DataAccessException e) {
			log.error(e);
		}
	}
	
	
	public void updateResultTime(String filename) {
		String updatesql = "update resultcsv set endTime = " + "'" + new Timestamp(System.currentTimeMillis()) + "'" + 
				"where fileName = " + "\"" + filename + "\"";
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource("");
		try {
			template.update(updatesql, namedParameters);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}
	
}
