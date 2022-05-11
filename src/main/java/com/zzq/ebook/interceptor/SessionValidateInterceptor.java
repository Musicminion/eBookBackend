//package com.zzq.ebook.interceptor;
//
//
////import com.zzq.ebook.service.UserService;
//import com.zzq.ebook.message.Msg;
//
//import com.zzq.ebook.message.MsgCode;
//import com.zzq.ebook.message.MsgUtil;
////import com.reins.bookstore.utils.sessionutils.SessionUtil;
//import net.sf.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
///**
// * @ClassName SessionValidateInterceptor
// * @Description Interceptor to check the user information in session
// * @Author thunderBoy
// * @Date 2019/11/7 20:05
// */
//public class SessionValidateInterceptor extends HandlerInterceptorAdapter {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception{
//
//        System.out.println("Failed");
//        Msg msg = MsgUtil.makeMsg(MsgCode.NOT_LOGGED_IN_ERROR);
//        sendJsonBack(response, msg);
//
//         boolean status = SessionUtil.checkAuth();
//        if(!status){
//            System.out.println("Failed");
//            Msg msg = MsgUtil.makeMsg(MsgCode.NOT_LOGGED_IN_ERROR);
//            sendJsonBack(response, msg);
//            return false;
//        }
//        System.out.println("Success");
//        return true;
//    }
//
//    private void sendJsonBack(HttpServletResponse response, Msg msg){
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json; charset=utf-8");
//        try (PrintWriter writer = response.getWriter()) {
//            writer.print(JSONObject.fromObject(msg));
//        } catch (IOException e) {
//            System.out.println("send json back error");
//        }
//    }
//
//}
