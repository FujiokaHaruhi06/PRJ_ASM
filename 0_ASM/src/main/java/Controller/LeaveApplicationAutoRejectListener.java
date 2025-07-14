package Controller;

import dal.LeaveApplicationDBContext;
import Entity.Leave_Application;
import Entity.LA_status;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@WebListener
public class LeaveApplicationAutoRejectListener implements ServletContextListener {
    private Timer timer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        timer = new Timer(true);
        // Chạy ngay khi server khởi động
        timer.schedule(new AutoRejectTask(), 0);
        // Lên lịch chạy vào 9h sáng mỗi ngày
        timer.scheduleAtFixedRate(new AutoRejectTask(), getNext9AM(), 24 * 60 * 60 * 1000);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (timer != null) timer.cancel();
    }

    private Date getNext9AM() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 9);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if (cal.getTime().before(new Date())) {
            cal.add(Calendar.DATE, 1);
        }
        return cal.getTime();
    }

    class AutoRejectTask extends TimerTask {
        @Override
        public void run() {
            try {
                LeaveApplicationDBContext db = new LeaveApplicationDBContext();
                final int PENDING_STATUS_ID = 1; // Chờ duyệt
                final int REJECTED_STATUS_ID = 3; // Bị từ chối (cần xác nhận đúng sid trong DB)
                List<Leave_Application> pendingApps = db.getApplicationsByStatus(PENDING_STATUS_ID);
                Date now = new Date();
                Calendar nowCal = Calendar.getInstance();
                nowCal.setTime(now);
                for (Leave_Application app : pendingApps) {
                    Date startDate = app.getStartDate();
                    Calendar startCal = Calendar.getInstance();
                    startCal.setTime(startDate);
                    // Nếu ngày bắt đầu nghỉ < hôm nay, hoặc hôm nay và đã >= 9h sáng
                    boolean overdue = startCal.before(nowCal) ||
                        (sameDay(startCal, nowCal) && nowCal.get(Calendar.HOUR_OF_DAY) >= 9);
                    if (overdue) {
                        db.updateApplicationStatus(app.getLid(), REJECTED_STATUS_ID, 0); // 0 = hệ thống từ chối
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private boolean sameDay(Calendar c1, Calendar c2) {
            return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                   c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
        }
    }
} 