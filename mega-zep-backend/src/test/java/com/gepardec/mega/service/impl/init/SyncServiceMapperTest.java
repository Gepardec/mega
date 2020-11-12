package com.gepardec.mega.service.impl.init;

import com.gepardec.mega.application.configuration.NotificationConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
class SyncServiceMapperTest {

    @Mock
    private Logger log;

    @Mock
    private NotificationConfig notificationConfig;

    @InjectMocks
    private SyncServiceMapper mapper;

    @Nested
    class MapEmployeeToUser {

        @Test
        void whenCalled_thenAllUsersHaveRoleEmployee() {

        }

        @Test
        void whenEmployee_thenUser() {

        }

        @Nested
        class WithProjects {

            @Test
            void whenNoProjects_thenNoUserHasRoleProjectLead() {

            }

            @Test
            void whenProjectsAndNoEmployeeIsLead_thenNoUserHasRoleProjectLead() {

            }

            @Test
            void whenProjectsAndEmployeeIsLead_thenNoUserHasRoleProjectLead() {

            }
        }

        @Nested
        class WithOmEmails {

            @Test
            void whenNoOmEmails_thenNoUserHasRoleOfficeManagement() {

            }

            @Test
            void whenOmEmailsAndNoEmployeeIsOm_thenNoUserHasRoleOfficeManagement() {

            }

            @Test
            void whenOmEmailsAndEmployeeIsOm_thenUserHasRoleOfficeManagement() {

            }
        }

        @Nested
        class WithLocale {

            @Test
            void whenLanguageIsNull_thenUserHasDefaultLocale() {

            }

            @Test
            void whenLanguageIsInvalid_thenUserHasDefaultLocale() {

            }

            @Test
            void whenLanguage_thenUserHasLocale() {

            }
        }
    }
}
