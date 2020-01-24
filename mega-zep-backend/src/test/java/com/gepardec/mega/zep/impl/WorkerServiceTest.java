package com.gepardec.mega.zep.impl;

import com.gepardec.mega.zep.service.impl.WorkerServiceImpl;
import com.gepardec.mega.zep.soap.ZepSoapProvider;
import de.provantis.zep.MitarbeiterListeType;
import de.provantis.zep.MitarbeiterType;
import de.provantis.zep.ReadMitarbeiterResponseType;
import de.provantis.zep.ZepSoapPortType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;


@ExtendWith(MockitoExtension.class)
public class WorkerServiceTest {

    @Mock
    ZepSoapPortType zepSoapPortType;

    @Mock
    ZepSoapProvider zepSoapProvider;

    @InjectMocks
    WorkerServiceImpl workerService;

    @Test
    void getEmployee_nullMail_shouldReturnNull() {
//        when(zepSoapPortType.readMitarbeiter(any())).thenReturn(createReadMitarbeiterResponseType());
        assertNull(workerService.getEmployee(null));
    }

    private static ReadMitarbeiterResponseType createReadMitarbeiterResponseType() {
        ReadMitarbeiterResponseType mitarbeiterResponseType = new ReadMitarbeiterResponseType();
        MitarbeiterListeType mitarbeiterListeType = new MitarbeiterListeType();
        mitarbeiterListeType.getMitarbeiter().add(createMitarbeiter("max.muster@gepardec.com", "Max", "Muster", "001-maxmuster"));
        mitarbeiterListeType.getMitarbeiter().add(createMitarbeiter("guenter.gutfried@gepardec.com", "Guenter", "Gutfried", "002-guentergutfried"));
        mitarbeiterListeType.getMitarbeiter().add(createMitarbeiter("sarah.haltlos@gepardec.com", "Sarah", "Haltlos", "003-sarahhaltlos"));
        return mitarbeiterResponseType;
    }

    private static MitarbeiterType createMitarbeiter(String email, String vorname, String nachname, String personalNr) {
        MitarbeiterType mitarbeiterType = new MitarbeiterType();
        mitarbeiterType.setEmail(email);
        mitarbeiterType.setVorname(vorname);
        mitarbeiterType.setNachname(nachname);
        mitarbeiterType.setPreisgruppe(personalNr);
        return mitarbeiterType;
    }


}
