package com.zzq.ebook.utils.session;

import com.zzq.ebook.constant.constant;
import net.sf.json.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {

//    public static boolean checkAuth(){
//        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        // Session
//        if(requestAttributes != null) {
//            HttpServletRequest request = requestAttributes.getRequest();
//            HttpSession session = request.getSession(false);
//
//            if(session != null) {
//                Integer userType = (Integer) session.getAttribute(Constant.USER_TYPE);
//                return userType != null && userType >= 0;
//            }
//        }
//        return false;
//    }

    public static JSONObject getAuth(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // Session
        if(requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            HttpSession session = request.getSession(false);

            if(session != null) {
                JSONObject ret = new JSONObject();
                // ret.put(constant.USER_ID, (Integer)session.getAttribute(constant.USER_ID));
                ret.put(constant.USERNAME, (String)session.getAttribute(constant.USERNAME));
                ret.put(constant.PRIVILEGE, (Integer)session.getAttribute(constant.PRIVILEGE));
                return ret;
            }
        }
        return null;
    }

    public static void setSession(JSONObject data){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // Session
        if(requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            HttpSession session = request.getSession();

            for(Object str:data.keySet()){
                String key = (String)str;
                Object val = data.get(key);
                session.setAttribute(key, val);
            }
        }
    }

    public static Boolean removeSession(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        // Session
        if(requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            HttpSession session = request.getSession(false);

            if(session != null) {
                session.invalidate();
            }
        }
        return true;
    }

}
