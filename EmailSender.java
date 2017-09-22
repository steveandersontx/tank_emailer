package com.v2com.og.reporter.latest;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.v2com.og.data.TankLevelMeasurementRecord;

public class EmailSender {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Configuration configuration;
	
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * 
	 * @param record
	 * @return true if mail was sent, false otherwise
	 */
	public boolean sendRecord(TankLevelMeasurementRecord record) {
		if (configuration == null) {
			throw new IllegalStateException("configuration is null, set Configuration first!");
		}
		
		String mailHost = configuration.getString("mail.host");
		int mailPort = configuration.getInt("mail.port");
		String mailTo = configuration.getString("report.to");
		String mailFrom = configuration.getString("report.from");
		String dateFormat = configuration.getString("report.date.format");
		String numberFormat = configuration.getString("report.number.format");
		String subject = configuration.getString("report.subject");
		String tankName = configuration.getString("modbus.tankId");
				
		try {
			logger.debug("Creating email...");
			NumberFormat nf = new DecimalFormat(numberFormat);
			Email email = new SimpleEmail();
			email.setHostName(mailHost);
			email.setSmtpPort(mailPort);
			email.setFrom(mailFrom);
			email.setSubject(String.format(subject, tankName, record.getTimestamp().toString(dateFormat)));
			email.setMsg("This is your report:\r\n- Tank Name: " + tankName + "\r\n- Measurement at: "
					+ record.getTimestamp().toString(dateFormat) + "\r\n- Product level at: "
					+ nf.format(record.getProductLevelIn()) + " ft" + "\r\n- Interface level at: "
					+ nf.format(record.getInterfaceLevelIn()) + " ft" + "\r\n- Total Volume at: "
					+ nf.format(record.getTotalVolumeBbl()) + " bbl" + "\r\n- Battery level at: "
					+ nf.format(record.getSupplyVoltage()) + " V");
			email.addTo(mailTo);

			logger.debug("Sending email...");
			String receiptId = email.send();
			logger.debug("Sent email: {}", receiptId);
			return true;
		} catch (Exception e) {
			logger.error("Sending email for record {}", record, e);
			return false;
		}
	}
        
        	/**
	 * 
	 * @param statsMsg
	 * @return true if mail was sent, false otherwise
	 */
	public boolean sendStats(String statsMsg) {
		if (configuration == null) {
			throw new IllegalStateException("configuration is null, set Configuration first!");
		}
		
		String mailHost = configuration.getString("mail.host");
		int mailPort = configuration.getInt("mail.port");
		String mailTo = configuration.getString("report.to");
		String mailFrom = configuration.getString("report.from");
		String dateFormat = configuration.getString("report.date.format");
		String numberFormat = configuration.getString("report.number.format");
		String subject = configuration.getString("report.subject");
		String tankName = configuration.getString("modbus.tankId");
				
		try {
			logger.debug("Creating email...");
			NumberFormat nf = new DecimalFormat(numberFormat);
			Email email = new SimpleEmail();
			email.setHostName(mailHost);
			email.setSmtpPort(mailPort);
			email.setFrom(mailFrom);
			//email.setSubject("Today's statistics"));
			email.setMsg(statsMsg);
			email.addTo(mailTo);

			logger.debug("Sending email...");
			String receiptId = email.send();
			logger.debug("Sent email: {}", receiptId);
			return true;
		} catch (Exception e) {
			logger.error("Sending email for record {}", statsMsg, e);
			return false;
		}
	}
}
