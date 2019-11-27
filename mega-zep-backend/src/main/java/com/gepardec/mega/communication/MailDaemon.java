package com.gepardec.mega.communication;

import com.gepardec.mega.communication.dates.BusinessDayCalculator;
import com.gepardec.mega.utils.DateUtils;
import com.gepardec.mega.zep.service.api.WorkerService;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
    void sendNotifications() {
        Optional<Reminder> reminder = businessDayCalculator.getEventForDate(DateUtils.now());
        if (reminder.isPresent()) {
            switch (reminder.get()) {
                case USER_CHECK_PROJECTTIMES: {
//                    workerService.getAllEmployees()
//                            .forEach(employee -> mailSender.sendMonthlyFriendlyReminder(employee.getVorname(), employee.getEmail()));
                    break;
                }
                case OM_CHECK_USER_CONTENT: {
                    mailSender.sendMail("", OM_CHECK_USER_CONTENT, "");
                    break;
                }
                case OM_RELEASE: {
                    mailSender.sendMail("", OM_RELEASE, "");
                    break;
                }
                case OM_SALARY_CHARGING: {
                    mailSender.sendMail("", OM_SALARY_CHARGING, "");
                    break;
                }
                case OM_SALARY_TRANSFER: {
                    mailSender.sendMail("", OM_SALARY_TRANSFER, "");
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
}
