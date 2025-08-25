package jp.co.sss.lms.form;

import java.time.LocalTime;

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

    // LocalTime型で保持
    private LocalTime trainingStartTimeObj;
    private LocalTime trainingEndTimeObj;

    /** Thymeleaf用文字列 */
    private String trainingStartTime;
    private String trainingEndTime;

    private Integer blankTime;
    private String note;
    private Boolean isToday;

    // Thymeleaf用文字列ゲッター
    public String getTrainingStartTime() {
        return trainingStartTimeObj != null ? trainingStartTimeObj.toString() : "";
    }

    public String getTrainingEndTime() {
        return trainingEndTimeObj != null ? trainingEndTimeObj.toString() : "";
    }
}
