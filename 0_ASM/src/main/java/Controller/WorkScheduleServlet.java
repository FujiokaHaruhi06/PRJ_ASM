package Controller;

import Entity.User;
import Entity.Account;
import dal.LeaveApplicationDBContext;
import dal.UserDBContext;
import dal.AccountDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "WorkScheduleServlet", urlPatterns = {"/work_schedule"})
public class WorkScheduleServlet extends HttpServlet {

    private LeaveApplicationDBContext leaveDBContext;
    private UserDBContext userDBContext;
    private AccountDBContext accountDBContext;

    @Override
    public void init() throws ServletException {
        leaveDBContext = new LeaveApplicationDBContext();
        userDBContext = new UserDBContext();
        accountDBContext = new AccountDBContext();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Account account = (Account) session.getAttribute("account");
        
        // Ngày hôm nay là ngày đầu tiên, các ngày tiếp theo là hôm nay+1, +2, ...
        LocalDate today = LocalDate.now();
        List<LocalDate> weekDates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekDates.add(today.plusDays(i));
        }

        // Lấy danh sách account: bản thân + cấp dưới
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        Set<Account> subordinates = new java.util.HashSet<>();
        // Nếu là head của division nào đó, lấy tất cả account thuộc các group trong division đó
        dal.DivisionDBContext divisionDB = new dal.DivisionDBContext();
        List<Entity.Division> divisions = divisionDB.getAll();
        boolean isDivisionHead = false;
        for (Entity.Division div : divisions) {
            if (div.getHead() != null && div.getHead().getAid() == account.getAid()) {
                isDivisionHead = true;
                // Lấy tất cả group thuộc division này
                dal.GroupDBContext groupDB = new dal.GroupDBContext();
                List<Entity.Group> groups = groupDB.getByDivisionId(div.getDivid());
                for (Entity.Group g : groups) {
                    dal.AccountDBContext accDB = new dal.AccountDBContext();
                    Set<Account> groupMembers = new java.util.HashSet<>(accDB.getSubordinates(g.getManager()));
                    // Thêm cả trưởng nhóm
                    if (g.getManager() != null) groupMembers.add(g.getManager());
                    subordinates.addAll(groupMembers);
                }
            }
        }
        if (!isDivisionHead) {
            // Nếu không phải trưởng phòng, lấy cấp dưới như cũ
            subordinates = accountDBContext.getSubordinates(account);
        }
        // Loại bỏ bản thân nếu có trong subordinates
        subordinates.remove(account);
        accounts.addAll(subordinates);

        // Map<Account, Map<LocalDate, Boolean>>
        Map<Account, Map<LocalDate, Boolean>> workStatusWeekMap = new LinkedHashMap<>();
        for (Account acc : accounts) {
            Map<LocalDate, Boolean> statusPerDay = new LinkedHashMap<>();
            for (LocalDate date : weekDates) {
                Date utilDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
                boolean isOnLeave = leaveDBContext.isAccountOnLeaveOnDate(acc.getAid(), utilDate);
                statusPerDay.put(date, isOnLeave);
            }
            workStatusWeekMap.put(acc, statusPerDay);
        }

        DateTimeFormatter labelFmt = DateTimeFormatter.ofPattern("dd/MM");
        List<String> weekDateLabels = new ArrayList<>();
        for (LocalDate d : weekDates) {
            String label = d.format(labelFmt);
            if (d.equals(today)) label += " (hôm nay)";
            weekDateLabels.add(label);
        }
        
        request.setAttribute("weekDates", weekDates);
        request.setAttribute("workStatusWeekMap", workStatusWeekMap);
        request.setAttribute("weekDateLabels", weekDateLabels);
        request.getRequestDispatcher("view/work_schedule.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles the work schedule checking logic for user and their subordinates.";
    }
} 