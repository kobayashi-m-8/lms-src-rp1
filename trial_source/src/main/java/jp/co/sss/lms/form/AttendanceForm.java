package jp.co.sss.lms.form;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;

import lombok.Data;

/**
 * 勤怠フォーム
 * 勤怠情報の編集・表示用フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class AttendanceForm {

    /** LMSユーザーID */
    private Integer lmsUserId;

    /** グループID */
    private Integer groupId;

    /** 年間計画No */
    private String nenkanKeikakuNo;

    /** ユーザー名 */
    private String userName;

    /** 退校フラグ（0:在籍中, 1:途中退校） */
    private Integer leaveFlg;

    /** 退校日 */
    private String leaveDate;

    /** 退校日（表示用） */
    private String dispLeaveDate;

    /** 中抜け時間プルダウン用リスト */
    private LinkedHashMap<Integer, String> blankTimes;

    /** 日次の勤怠フォームリスト */
    private List<DailyAttendanceForm> attendanceList;

    /** ポイント（小数点第1位まで表示） */
    private Double point;

    /** 中抜け時間マップ（内部処理用） */
    private LinkedHashMap<Integer, String> blankTimeMap;

    /** 時間マップ（内部処理用） */
    private LinkedHashMap<Integer, String> hourMap;

    /** 分マップ（内部処理用） */
    private LinkedHashMap<Integer, String> minuteMap;

    private LocalTime trainingStartTime;
    private LocalTime trainingEndTime;
    
    // Thymeleaf用の文字列フォーマットゲッター
    public String getTrainingStartTimeObj() {
        return trainingStartTime != null ? trainingStartTime.toString() : "";
    }

    public String getTrainingEndTimeObj() {
        return trainingEndTime != null ? trainingEndTime.toString() : "";
    }
}
