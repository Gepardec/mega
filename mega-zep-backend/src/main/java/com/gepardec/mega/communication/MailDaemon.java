package com.gepardec.mega.communication;

import com.gepardec.mega.communication.dates.BusinessDayCalculator;
import com.gepardec.mega.security.Role;
import com.gepardec.mega.utils.DateUtils;
import com.gepardec.mega.zep.service.api.WorkerService;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gepardec.mega.communication.Reminder.*;

@ApplicationScoped
public class MailDaemon {

    @Inject
    BusinessDayCalculator businessDayCalculator;

    @Inject
    MailSender mailSender;

    @Inject
    WorkerService workerService;

    @Scheduled(cron = "0 0 9 ? * MON-FRI")
    void sendReminder() {
        Optional<Reminder> reminder = businessDayCalculator.getEventForDate(DateUtils.now());
        if (reminder.isPresent()) {
            switch (reminder.get()) {
                case USER_CHECK_PROJECTTIMES: {
                    sendReminderToUser();
                    break;
                }
                case PL_CHECK_USER_CONTENT: {
                    sendReminderToPL();
                    break;
                }
                case OM_CHECK_USER_CONTENT: {
                    sendReminderToOM(OM_CHECK_USER_CONTENT);
                    break;
                }
                case OM_RELEASE: {
                    sendReminderToOM(OM_RELEASE);
                    break;
                }
                case OM_SALARY_CHARGING: {
                    sendReminderToOM(OM_SALARY_CHARGING);
                    break;
                }
                case OM_SALARY_TRANSFER: {
                    sendReminderToOM(OM_SALARY_TRANSFER);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    private void sendReminderToPL() {
        workerService.getEmployeesByRoles(Role.ROLE_CONTROLLER)
                .forEach(employee -> mailSender.sendMail(employee.getEmail(), PL_CHECK_USER_CONTENT, ""));
    }

    private void sendReminderToOM(Reminder reminder) {
        //TODO: read from resource
        List<String> omMailAdresses = new ArrayList<>(0);
        omMailAdresses.forEach(mailAddress -> mailSender.sendMail(mailAddress, reminder, ""));
    }

    private void sendReminderToUser() {
        workerService.getAllEmployees()
                .forEach(employee -> mailSender.sendMonthlyFriendlyReminder(employee.getVorname(), employee.getEmail()));
    }
}
