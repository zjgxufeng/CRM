package com.xufeng.crm.workbench.web.controller;

import com.xufeng.crm.settings.common.domain.ReturnObject;
import com.xufeng.crm.settings.common.util.DateUtils;
import com.xufeng.crm.settings.common.util.HSSFUtils;
import com.xufeng.crm.settings.common.util.UUIDUtils;
import com.xufeng.crm.settings.contants.Contants;
import com.xufeng.crm.settings.domain.User;
import com.xufeng.crm.settings.service.UserService;
import com.xufeng.crm.workbench.domain.Activity;
import com.xufeng.crm.workbench.domain.ActivityRemark;
import com.xufeng.crm.workbench.service.ActivityRemarkService;
import com.xufeng.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import static javafx.scene.input.KeyCode.C;
import static javafx.scene.input.KeyCode.R;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        //1.查询所有用户
        List<User> userList = userService.queryAllUsers();
        //2.把所有用户保存到请求对象
        request.setAttribute("userList", userList);

        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/savecreateActivity.do")
    //自动封装参数
    public @ResponseBody
    Object savecreateActivity(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        //封装参数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formateDate(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        //对数据库进行增删改,可能出现问题都需要trycatch
        try {
            //保存市场活动
            int count = activityService.saveCreateActivity(activity);
            if (count <= 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage(Contants.SYSTEM_BUSY);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Contants.SYSTEM_BUSY);
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    public @ResponseBody
    Object queryActivityByConditionForPage(String owner, String name, String startDate,
                                           String endDate, int pageNo, int pageSize) {
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("owner", owner);
        map.put("name", name);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        //beginNo是当前页的第一条记录坐标
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        //模糊查询
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        //查询所有市场活动的数量
        int totalRows = activityService.queryCountOfActivityByCondition(map);
        //返回所有活动和总记录条数
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("activityList", activityList);
        returnMap.put("totalRows", totalRows);
        return returnMap;
    }

    @RequestMapping("workbench/activity/deleteActivityByIds.do")
    public @ResponseBody
    Object deleteActivityByIds(String[] id) {

        ReturnObject returnObject = new ReturnObject();
        try {
            int returnInt = activityService.deleteActivityByIds(id);
            if (returnInt > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage(Contants.SYSTEM_BUSY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Contants.SYSTEM_BUSY);
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    public @ResponseBody
    Object queryActivityById(String id) {
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }

    @RequestMapping("/workbench/activity/updateActivityById.do")
  public @ResponseBody Object updateActivityById(Activity activity,HttpSession session){
           User user = (User) session.getAttribute(Contants.SESSION_USER);
           activity.setEditBy(user.getId());
           activity.setEditTime(DateUtils.formateDateTime(new Date()));
           ReturnObject returnObject = new ReturnObject();
        System.out.println("activity="+activity);
        try {
            int returnInt = activityService.updateActivityById(activity);

            if (returnInt>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage(Contants.SYSTEM_BUSY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Contants.SYSTEM_BUSY);
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/exportAllActivitys.do")
    public @ResponseBody Object exportAllActivitys(HttpServletResponse response) throws Exception {
        //查询所有活动
        List<Activity> activityList = activityService.queryAllActivitys();
        //生成excel工作簿对象
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建一个工作簿
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        //创建第一行
        HSSFRow row = sheet.createRow(0);
        //创建第一列,表头
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("id");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");

        if(activityList!=null && activityList.size()>0){
            Activity activity = null;
            //取出一个活动就放一行
            for (int i = 0; i < activityList.size(); i++) {
                activity = activityList.get(i);
                row = sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }

        //根据wb对象生成excel文件
       /* OutputStream os=new FileOutputStream("D:\\course\\18-CRM\\阶段资料\\serverDir\\activityList.xls");
        wb.write(os);*/
        //关闭资源
        /*os.close();
        wb.close();*/

        //把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");
        OutputStream out=response.getOutputStream();
        /*InputStream is=new FileInputStream("D:\\course\\18-CRM\\阶段资料\\serverDir\\activityList.xls");
        byte[] buff=new byte[256];
        int len=0;
        while((len=is.read(buff))!=-1){
            out.write(buff,0,len);
        }
        is.close();*/

        wb.write(out);

        wb.close();
        out.flush();
        return null;
    }


    /**
     * 配置springmvc的文件上传解析器
     *
     */
    @RequestMapping("/workbench/activity/fileUpload.do")
    public @ResponseBody Object fileUpload(String userName, MultipartFile myFile) throws Exception{
        //把文本数据打印到控制台
        System.out.println("userName="+userName);
        //把文件在服务指定的目录中生成一个同样的文件
        String originalFilename=myFile.getOriginalFilename();
        File file=new File("D:\\course\\18-CRM\\阶段资料\\serverDir\\",originalFilename);//路径必须手动创建好，文件如果不存在，会自动创建
        myFile.transferTo(file);

        //返回响应信息
        ReturnObject returnObject=new ReturnObject();
        returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        returnObject.setMessage("上传成功");
        return returnObject;
    }

    @RequestMapping("/workbench/activity/importActivity.do")
    public @ResponseBody Object importActivity(MultipartFile activityFile, String userName, HttpSession session){
        System.out.println("userName="+userName);
        User user=(User) session.getAttribute(Contants.SESSION_USER);
        ReturnObject returnObject=new ReturnObject();
        try {
            //把excel文件写到磁盘目录中
            /*String originalFilename = activityFile.getOriginalFilename();
            File file = new File("D:\\course\\18-CRM\\阶段资料\\serverDir\\", originalFilename);//路径必须手动创建好，文件如果不存在，会自动创建
            activityFile.transferTo(file);*/

            //解析excel文件，获取文件中的数据，并且封装成activityList
            //根据excel文件生成HSSFWorkbook对象，封装了excel文件的所有信息
            //InputStream is=new FileInputStream("D:\\course\\18-CRM\\阶段资料\\serverDir\\"+originalFilename);

            InputStream is=activityFile.getInputStream();
            HSSFWorkbook wb=new HSSFWorkbook(is);
            //根据wb获取HSSFSheet对象，封装了一页的所有信息
            HSSFSheet sheet=wb.getSheetAt(0);//页的下标，下标从0开始，依次增加
            //根据sheet获取HSSFRow对象，封装了一行的所有信息
            HSSFRow row=null;
            HSSFCell cell=null;
            Activity activity=null;
            List<Activity> activityList=new ArrayList<>();
            for(int i=1;i<=sheet.getLastRowNum();i++) {//sheet.getLastRowNum()：最后一行的下标
                row=sheet.getRow(i);//行的下标，下标从0开始，依次增加
                activity=new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formateDateTime(new Date()));
                activity.setCreateBy(user.getId());

                for(int j=0;j<row.getLastCellNum();j++) {//row.getLastCellNum():最后一列的下标+1
                    //根据row获取HSSFCell对象，封装了一列的所有信息
                    cell=row.getCell(j);//列的下标，下标从0开始，依次增加

                    //获取列中的数据
                    String cellValue= HSSFUtils.getCellValueForStr(cell);
                    if(j==0){
                        activity.setName(cellValue);
                    }else if(j==1){
                        activity.setStartDate(cellValue);
                    }else if(j==2){
                        activity.setEndDate(cellValue);
                    }else if(j==3){
                        activity.setCost(cellValue);
                    }else if(j==4){
                        activity.setDescription(cellValue);
                    }
                }

                //每一行中所有列都封装完成之后，把activity保存到list中
                activityList.add(activity);
            }

            //调用service层方法，保存市场活动
            int ret=activityService.saveCreateActivityByList(activityList);

            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(ret);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Contants.SYSTEM_BUSY);
        }

        return returnObject;
    }
    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id,HttpServletRequest request){
          Activity activity = activityService.queryActivityForDetail(id);
         List<ActivityRemark> remarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
        request.setAttribute("activity",activity);
        request.setAttribute("remarkList",remarkList);
        System.out.println("市场活动="+activity);
        return "workbench/activity/detail";
    }

}


