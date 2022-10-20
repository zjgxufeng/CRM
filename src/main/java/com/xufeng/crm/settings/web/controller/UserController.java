package com.xufeng.crm.settings.web.controller;

import com.xufeng.crm.settings.common.domain.ReturnObject;
import com.xufeng.crm.settings.common.util.DateUtils;
import com.xufeng.crm.settings.contants.Contants;
import com.xufeng.crm.settings.domain.User;
import com.xufeng.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.print.attribute.standard.PresentationDirection;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin() {
        //跳转到页面
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    public @ResponseBody Object Login(String loginAct, String loginPwd, String isRemPwd, HttpSession session,
                                      HttpServletRequest request,HttpServletResponse response){

        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        // 查询用户
        User user = userService.queryUserByLoginActAndPwd(map);
        //创建返回对象
        ReturnObject returnObject = new ReturnObject();

        if(user==null){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage(Contants.LOGINACT_OR_LOGINPWD_FALSE);
        }else{
            //如果现在时间大于账户有效期,则账户已经过期
            if(DateUtils.formateDateTime(new Date()).compareTo(user.getExpireTime())>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage(Contants.LOGINACT_OVER_DATE);
            }else if("0".equals(user.getLockState())){ //用户状态码是0锁定
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage(Contants.LOGINACT_STATE_LOCKED);
            }else if(!user.getAllowIps().contains(request.getRemoteAddr())){ //用户ip地址不在范围内
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage(Contants.LOGINACT_IP_LIMITED);
            }else {
                //登录成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

                //把用户放进session
                session.setAttribute(Contants.SESSION_USER,user);

                //如果要记住密码
                if("true".equals(isRemPwd)){
                    Cookie c1= new Cookie("loginAct",loginAct);
                    c1.setMaxAge(10*24*60*60);
                    response.addCookie(c1);
                    Cookie c2= new Cookie("loginPwd",loginPwd);
                    c2.setMaxAge(10*24*60*60);
                    response.addCookie(c2);
                }else{
                    //把没有过期的cookie删掉
                    Cookie c1= new Cookie("loginAct",loginAct);
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2= new Cookie("loginPwd",loginPwd);
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }

        return returnObject;
    }
    @RequestMapping("settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        //销毁cookie
        Cookie c1= new Cookie("loginAct","1");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2= new Cookie("loginPwd","1");
        c2.setMaxAge(0);
        response.addCookie(c2);
        //销毁session
        session.invalidate();
        //跳转到首页
        return "redirect:/";
    }
}
