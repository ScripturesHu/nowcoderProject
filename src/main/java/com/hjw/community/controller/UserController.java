package com.hjw.community.controller;

import com.hjw.community.annotation.LoginRequired;
import com.hjw.community.entity.User;
import com.hjw.community.service.UserService;
import com.hjw.community.util.CommunityUtil;
import com.hjw.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author hujw
 * @description
 * @create 2021-10-15 10:50
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    @LoginRequired//自定义注解
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }
    //上传头像
    @LoginRequired
    @RequestMapping(value = "/uploadHeader", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if (headerImage == null){
            model.addAttribute("headerErrorMsg", "您还没有选择图片！");
            return "/site/setting";
        }
        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("headerErrorMsg", "文件格式不正确！");
            return "/site/setting";
        }
        //生成随机文件名
        filename = CommunityUtil.generateUUID() + suffix;
        //确定文件存放的路径
        File dest = new File(uploadPath + "/" + filename);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            LOGGER.error("上传文件失败" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常!", e);
        }

        //更新当前用户的头像的路径（web访问路径）
        //http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header" + filename;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }
    //读取头像
    @RequestMapping(path = "/header/{filename}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response){
        //服务器存放头像照片路径
        filename = uploadPath + "/" + filename;
        //文件后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        //响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(filename);
                OutputStream os = response.getOutputStream();
                ){
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1){
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            LOGGER.error("读取头像失败：" + e.getMessage());
        }
    }
    //修改用户密码
    @LoginRequired
    @RequestMapping(value = "/uploadPassword", method = RequestMethod.POST)
    public String uploadPassword(String password, String newPassword, Model model){
        User user = hostHolder.getUser();

        //password = CommunityUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(CommunityUtil.md5(password + user.getSalt()))){
            model.addAttribute("passwordErrorMsg", "密码输入有误,请重新输入！");
            return "/site/setting";
        }
        if (StringUtils.isBlank(newPassword)){
            model.addAttribute("newPasswordErrorMsg", "密码不能为空！");
            return "/site/setting";
        }
        userService.updatePassword(user.getId(), CommunityUtil.md5(newPassword + user.getSalt()));

        return "redirect:/index";
    }

}

