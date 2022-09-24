package com.zzq.ebook.serviceImp;
import com.zzq.ebook.service.ClockService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class ClockServiceImp implements ClockService {

    long launchTime = 0;
    String launchTimeStr = "";

    @Override
    public String launchClock() {
        // 计时器未发起，避免重复发起
        if(launchTime == 0){
            this.launchTime = System.currentTimeMillis();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            this.launchTimeStr  = simpleDateFormat.format(new Date(launchTime));
        }
        return launchTimeStr;
    }

    @Override
    public String endClock() {
        if(launchTime > 0){
            long time_interval = (System.currentTimeMillis() - launchTime);
            launchTime = 0;
            long hh = time_interval / 1000 / 3600;
            long mm = (time_interval / 1000 % 3600) / 60;
            long ss = (time_interval / 1000 % 3600) % 60;
            long ms = (time_interval - (3600 * hh + 60 * mm + ss) * 1000 );

            return  "登录时间:" + launchTimeStr + "会话经过了：" + hh + "小时" + mm + "分钟" + ss + "秒" + ms + "毫秒";
        }
        else
            return "计时器未发起";
    }
}
