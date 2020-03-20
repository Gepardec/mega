package com.gepardec.mega.util;

import com.gepardec.mega.aplication.security.Role;
import com.gepardec.mega.aplication.utils.DateUtils;
import de.provantis.zep.*;

import javax.xml.ws.Response;
import java.time.LocalDate;
import java.util.Arrays;

public class ZepTestUtil {


    public static BeschaeftigungszeitType createBeschaeftigungszeitType(final LocalDate start, final LocalDate end) {
        final BeschaeftigungszeitType beschaeftigungszeitType = new BeschaeftigungszeitType();
        beschaeftigungszeitType.setStartdatum(start != null ? DateUtils.formatDate(start) : null);
        beschaeftigungszeitType.setEnddatum(end != null ? DateUtils.formatDate(end) : null);
        return beschaeftigungszeitType;
    }

    public static BeschaeftigungszeitListeType createBeschaeftigungszeitListeType(BeschaeftigungszeitType... beschaeftigungszeitType) {
        final BeschaeftigungszeitListeType beschaeftigungszeitListeType = new BeschaeftigungszeitListeType();
        beschaeftigungszeitListeType.getBeschaeftigungszeit().addAll(Arrays.asList(beschaeftigungszeitType));
        return beschaeftigungszeitListeType;
    }

    public static MitarbeiterType createMitarbeiterType(final int userId) {
        final MitarbeiterType mitarbeiter = new MitarbeiterType();
        final String name = "Thomas_" + userId;

        mitarbeiter.setEmail(name + "@gepardec.com");
        mitarbeiter.setVorname(name);
        mitarbeiter.setNachname(name + "_Nachname");
        mitarbeiter.setTitel("Ing.");
        mitarbeiter.setUserId(String.valueOf(userId));
        mitarbeiter.setAnrede("Herr");
        mitarbeiter.setPreisgruppe("ARCHITEKT");
        mitarbeiter.setFreigabedatum("2020-01-01");
        mitarbeiter.setRechte(Role.USER.roleId);

        return mitarbeiter;
    }

    public static ReadMitarbeiterResponseType createReadMitarbeiterResponseType(final MitarbeiterType... mitarbeiterType) {
        final ReadMitarbeiterResponseType readMitarbeiterResponseType = new ReadMitarbeiterResponseType();
        readMitarbeiterResponseType.setMitarbeiterListe(new MitarbeiterListeType());
        readMitarbeiterResponseType.getMitarbeiterListe().getMitarbeiter().addAll(Arrays.asList(mitarbeiterType));
        return readMitarbeiterResponseType;
    }

    public static ResponseHeaderType createResponseHeaderType(final String returnCode) {
        final ResponseHeaderType responseHeaderType = new ResponseHeaderType();
        responseHeaderType.setReturnCode(returnCode);
        return responseHeaderType;
    }

    public static UpdateMitarbeiterResponseType createUpaUpdateMitarbeiterResponseType(final ResponseHeaderType responseHeaderType) {
        final UpdateMitarbeiterResponseType updateMitarbeiterResponseType = new UpdateMitarbeiterResponseType();
        updateMitarbeiterResponseType.setResponseHeader(responseHeaderType);
        return updateMitarbeiterResponseType;
    }
}
