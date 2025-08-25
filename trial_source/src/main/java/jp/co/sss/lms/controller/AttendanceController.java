package jp.co.sss.lms.controller;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.lms.dto.AttendanceManagementDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.form.AttendanceForm;
import jp.co.sss.lms.service.StudentAttendanceService;
import jp.co.sss.lms.util.AttendanceUtil;
import jp.co.sss.lms.util.Constants;

/**
 * 勤怠管理コントローラ
 * 
 * @author 東京ITスクール1
 */
@Controller
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private StudentAttendanceService studentAttendanceService;
	@Autowired
	private LoginUserDto loginUserDto;

	/**
	 * 勤怠管理画面 初期表示
	 */
	@RequestMapping(path = "/detail", method = RequestMethod.GET)
	public String index(Model model) {

		// 勤怠一覧の取得
		List<AttendanceManagementDto> attendanceManagementDtoList = studentAttendanceService
				.getAttendanceManagement(loginUserDto.getCourseId(), loginUserDto.getLmsUserId());
		model.addAttribute("attendanceManagementDtoList", attendanceManagementDtoList);

		// Task.25 - 過去日未入力チェック
		boolean hasPastUnentered = studentAttendanceService.hasPastUnentered(loginUserDto.getLmsUserId());
		model.addAttribute("showPastUnenteredDialog", hasPastUnentered);

		return "attendance/detail";
	}

	/**
	 * 勤怠管理画面 『出勤』ボタン押下
	 */
	@RequestMapping(path = "/detail", params = "punchIn", method = RequestMethod.POST)
	public String punchIn(Model model) {

		// 更新前のチェック
		String error = studentAttendanceService.punchCheck(Constants.CODE_VAL_ATWORK);
		model.addAttribute("error", error);
		// 勤怠登録
		if (error == null) {
			String message = studentAttendanceService.setPunchIn();
			model.addAttribute("message", message);
		}
		// 一覧の再取得
		List<AttendanceManagementDto> attendanceManagementDtoList = studentAttendanceService
				.getAttendanceManagement(loginUserDto.getCourseId(), loginUserDto.getLmsUserId());
		model.addAttribute("attendanceManagementDtoList", attendanceManagementDtoList);

		return "attendance/detail";
	}

	/**
	 * 勤怠管理画面 『退勤』ボタン押下
	 */
	@RequestMapping(path = "/detail", params = "punchOut", method = RequestMethod.POST)
	public String punchOut(Model model) {

		// 更新前のチェック
		String error = studentAttendanceService.punchCheck(Constants.CODE_VAL_LEAVING);
		model.addAttribute("error", error);
		// 勤怠登録
		if (error == null) {
			String message = studentAttendanceService.setPunchOut();
			model.addAttribute("message", message);
		}
		// 一覧の再取得
		List<AttendanceManagementDto> attendanceManagementDtoList = studentAttendanceService
				.getAttendanceManagement(loginUserDto.getCourseId(), loginUserDto.getLmsUserId());
		model.addAttribute("attendanceManagementDtoList", attendanceManagementDtoList);

		return "attendance/detail";
	}

	/**
	 * 勤怠管理画面 『勤怠情報を直接編集する』リンク押下
	 */
	@RequestMapping(path = "/update")
	public String update(Model model) {

	    // 勤怠管理リストの取得
	    List<AttendanceManagementDto> attendanceManagementDtoList = studentAttendanceService
	            .getAttendanceManagement(loginUserDto.getCourseId(), loginUserDto.getLmsUserId());

	    // 勤怠フォームの生成
	    AttendanceForm attendanceForm = studentAttendanceService.setAttendanceForm(attendanceManagementDtoList);

	    // LocalTime → HH:mm文字列に変換
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	    attendanceForm.getAttendanceList().forEach(daily -> {
	        if (daily.getTrainingStartTimeObj() != null) {
	            daily.setTrainingStartTime(daily.getTrainingStartTimeObj().format(formatter));
	        } else {
	            daily.setTrainingStartTime("");
	        }

	        if (daily.getTrainingEndTimeObj() != null) {
	            daily.setTrainingEndTime(daily.getTrainingEndTimeObj().format(formatter));
	        } else {
	            daily.setTrainingEndTime("");
	        }
	    });

	    // 選択肢の設定
	    attendanceForm.setBlankTimeMap(AttendanceUtil.createBlankTimeMap());
	    attendanceForm.setHourMap(AttendanceUtil.createHourMap());
	    attendanceForm.setMinuteMap(AttendanceUtil.createMinuteMap());

	    model.addAttribute("attendanceForm", attendanceForm);

	    return "attendance/update";
	}

	/**
	 * 勤怠情報直接変更画面 『更新』ボタン押下
	 */
	@RequestMapping(path = "/update", params = "complete", method = RequestMethod.POST)
	public String complete(AttendanceForm attendanceForm, Model model, BindingResult result)
	        throws ParseException {

	    // 更新
	    String message = studentAttendanceService.update(attendanceForm);
	    model.addAttribute("message", message);

	    // 一覧の再取得
	    List<AttendanceManagementDto> attendanceManagementDtoList = studentAttendanceService
	            .getAttendanceManagement(loginUserDto.getCourseId(), loginUserDto.getLmsUserId());

	    // DailyAttendanceForm を HH:mm 文字列に変換して再セット
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	    AttendanceForm updatedForm = studentAttendanceService.setAttendanceForm(attendanceManagementDtoList);
	    updatedForm.getAttendanceList().forEach(daily -> {
	        if (daily.getTrainingStartTimeObj() != null) {
	            daily.setTrainingStartTime(daily.getTrainingStartTimeObj().format(formatter));
	        } else {
	            daily.setTrainingStartTime("");
	        }

	        if (daily.getTrainingEndTimeObj() != null) {
	            daily.setTrainingEndTime(daily.getTrainingEndTimeObj().format(formatter));
	        } else {
	            daily.setTrainingEndTime("");
	        }
	    });

	    // 選択肢の再設定
	    updatedForm.setBlankTimeMap(AttendanceUtil.createBlankTimeMap());
	    updatedForm.setHourMap(AttendanceUtil.createHourMap());
	    updatedForm.setMinuteMap(AttendanceUtil.createMinuteMap());

	    model.addAttribute("attendanceForm", updatedForm);

	    return "attendance/detail";
	}
}
