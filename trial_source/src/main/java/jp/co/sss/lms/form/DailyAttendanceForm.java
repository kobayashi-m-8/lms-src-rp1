package jp.co.sss.lms.form;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class DailyAttendanceForm {

	private Integer studentAttendanceId;
	private String trainingDate;
	private String dispTrainingDate;
	private String sectionName;

	/** 勤怠ステータス（コード用） */
	private String status;

	/** 勤怠ステータス（表示用） */
	private String statusDispName;

	/** 中抜け時間（表示用） */
	private String blankTimeValue;

	/** DB保存用（HH:mm） */
	private String trainingStartTime;
	private String trainingEndTime;
	private LocalTime trainingStartTimeObj;
	private LocalTime trainingEndTimeObj;
	private Integer startHour;
	private Integer startMinute;
	private Integer endHour;
	private Integer endMinute;

	private Integer blankTime;
	private String note;
	private Boolean isToday;

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	/**
	 * Task26
	 *  出勤時間を文字列（HH:mm形式）で取得する。
	 * @return 出勤時間（HH:mm形式）、未設定の場合は空文字
	 * @author 小林
	 */
	public String getTrainingStartTime() {
		if (trainingStartTimeObj != null) {
			return trainingStartTimeObj.format(FORMATTER);
		}
		return trainingStartTime != null ? trainingStartTime : "";
	}

	public String getTrainingEndTime() {
		if (trainingEndTimeObj != null) {
			return trainingEndTimeObj.format(FORMATTER);
		}
		return trainingEndTime != null ? trainingEndTime : "";
	}

	/**
	 * Task26
	 * 退勤時間を文字列（HH:mm形式）で取得する。
	 * @return 退勤時間（HH:mm形式）、未設定の場合は空文字
	 * @author 小林
	 */
	public void setTrainingStartTime(String trainingStartTime) {
		this.trainingStartTime = trainingStartTime;
		if (trainingStartTime != null && !trainingStartTime.isEmpty()) {
			this.trainingStartTimeObj = LocalTime.parse(trainingStartTime, FORMATTER);
			this.startHour = trainingStartTimeObj.getHour();
			this.startMinute = trainingStartTimeObj.getMinute();
		}
	}

	public void setTrainingEndTime(String trainingEndTime) {
		this.trainingEndTime = trainingEndTime;
		if (trainingEndTime != null && !trainingEndTime.isEmpty()) {
			this.trainingEndTimeObj = LocalTime.parse(trainingEndTime, FORMATTER);
			this.endHour = trainingEndTimeObj.getHour();
			this.endMinute = trainingEndTimeObj.getMinute();
		}
	}

	/**
	 * Task26
	 * 時間を設定する。[時][分]
	 * @author 小林
	 */
	public void setStartHour(Integer startHour) {
		this.startHour = startHour;
		syncStartTime();
	}

	public void setStartMinute(Integer startMinute) {
		this.startMinute = startMinute;
		syncStartTime();
	}

	public void setEndHour(Integer endHour) {
		this.endHour = endHour;
		syncEndTime();
	}

	public void setEndMinute(Integer endMinute) {
		this.endMinute = endMinute;
		syncEndTime();
	}

	private void syncStartTime() {
		if (startHour != null && startMinute != null) {
			this.trainingStartTimeObj = LocalTime.of(startHour, startMinute);
			this.trainingStartTime = trainingStartTimeObj.format(FORMATTER);
		}
	}

	private void syncEndTime() {
		if (endHour != null && endMinute != null) {
			this.trainingEndTimeObj = LocalTime.of(endHour, endMinute);
			this.trainingEndTime = trainingEndTimeObj.format(FORMATTER);
		}
	}
}
