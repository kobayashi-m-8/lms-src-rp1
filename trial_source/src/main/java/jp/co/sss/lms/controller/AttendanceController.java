package jp.co.sss.lms.controller;

import java.text.ParseException;
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

        List<AttendanceManagementDto> attendanceManagementDtoList = studentAttendanceService
                .getAttendanceManagement(loginUserDto.getCourseId(), loginUserDto.getLmsUserId());
        model.addAttribute("attendanceManagementDtoList", attendanceManagementDtoList);

        boolean hasPastUnentered = studentAttendanceService.hasPastUnentered(loginUserDto.getLmsUserId());
        model.addAttribute("showPastUnenteredDialog", hasPastUnentered);

        return "attendance/detail";
    }

    /**
     * 『出勤』ボタン
     */
    @RequestMapping(path = "/detail", params = "punchIn", method = RequestMethod.POST)
    public String punchIn(Model model) {

        String error = studentAttendanceService.punchCheck(Constants.CODE_VAL_ATWORK);
        model.addAttribute("error", error);

        if (error == null) {
            String message = studentAttendanceService.setPunchIn();
            model.addAttribute("message", message);
        }

        List<AttendanceManagementDto> attendanceManagementDtoList = studentAttendanceService
                .getAttendanceManagement(loginUserDto.getCourseId(), loginUserDto.getLmsUserId());
        model.addAttribute("attendanceManagementDtoList", attendanceManagementDtoList);

        return "attendance/detail";
    }

    /**
     * 『退勤』ボタン
     */
    @RequestMapping(path = "/detail", params = "punchOut", method = RequestMethod.POST)
    public String punchOut(Model model) {

        String error = studentAttendanceService.punchCheck(Constants.CODE_VAL_LEAVING);
        model.addAttribute("error", error);

        if (error == null) {
            String message = studentAttendanceService.setPunchOut();
            model.addAttribute("message", message);
        }

        List<AttendanceManagementDto> attendanceManagementDtoList = studentAttendanceService
                .getAttendanceManagement(loginUserDto.getCourseId(), loginUserDto.getLmsUserId());
        model.addAttribute("attendanceManagementDtoList", attendanceManagementDtoList);

        return "attendance/detail";
    }

    /**
     * 『勤怠情報を直接編集する』リンク
     * Task26
     * @author 小林
     * @param model Spring MVC Model
     * @return 勤怠編集画面ビュー名
     */
    @RequestMapping(path = "/update")
    public String update(Model model) {

        List<AttendanceManagementDto> attendanceManagementDtoList = studentAttendanceService
                .getAttendanceManagement(loginUserDto.getCourseId(), loginUserDto.getLmsUserId());

        AttendanceForm attendanceForm = studentAttendanceService.setAttendanceForm(attendanceManagementDtoList);

        // 選択肢マップをセット
        attendanceForm.setBlankTimeMap(AttendanceUtil.createBlankTimeMap());
        attendanceForm.setHourMap(AttendanceUtil.createHourMap());
        attendanceForm.setMinuteMap(AttendanceUtil.createMinuteMap());

        model.addAttribute("attendanceForm", attendanceForm);

        return "attendance/update";
    }

    /**
     * Task26
     * @author 小林
     * @param attendanceForm 勤怠編集フォーム
     * @param model Spring MVC Model
     * @param result バリデーション結果
     * @return 勤怠管理画面ビュー名
     * @throws ParseException 日付変換エラー
     */
    @RequestMapping(path = "/update", params = "complete", method = RequestMethod.POST)
    public String complete(AttendanceForm attendanceForm, Model model, BindingResult result)
            throws ParseException {

        // 更新処理
        String message = studentAttendanceService.update(attendanceForm);
        model.addAttribute("message", message);

        // 再取得して再セット
        List<AttendanceManagementDto> attendanceManagementDtoList = studentAttendanceService
                .getAttendanceManagement(loginUserDto.getCourseId(), loginUserDto.getLmsUserId());
        AttendanceForm updatedForm = studentAttendanceService.setAttendanceForm(attendanceManagementDtoList);

        updatedForm.setBlankTimeMap(AttendanceUtil.createBlankTimeMap());
        updatedForm.setHourMap(AttendanceUtil.createHourMap());
        updatedForm.setMinuteMap(AttendanceUtil.createMinuteMap());

        model.addAttribute("attendanceForm", updatedForm);

        return "attendance/detail";
    }
}
