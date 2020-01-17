package com.gepardec.mega;

import de.provantis.zep.*;
import io.quarkus.test.Mock;

import javax.jws.WebMethod;
import javax.jws.WebResult;

@Mock
public class ZepSoapPortTypeMock implements ZepSoapPortType {

    private ZepSoapPortType delegate;

    @Override
    @WebResult(name = "ReadTeilaufgabeHistoryResponse", targetNamespace = "http://zep.provantis.de", partName = "readTeilaufgabeHistoryResponse")
    @WebMethod
    public ReadTeilaufgabeHistoryResponseType readTeilaufgabeHistory(ReadTeilaufgabeHistoryRequestType readTeilaufgabeHistoryRequest) {return delegate.readTeilaufgabeHistory(readTeilaufgabeHistoryRequest);}

    @Override
    @WebResult(name = "DeleteMitarbeiterResponse", targetNamespace = "http://zep.provantis.de", partName = "deleteMitarbeiterResponse")
    @WebMethod
    public DeleteMitarbeiterResponseType deleteMitarbeiter(DeleteMitarbeiterRequestType deleteMitarbeiterRequest) {return delegate.deleteMitarbeiter(deleteMitarbeiterRequest);}

    @Override
    @WebResult(name = "ReadKostenDatevResponse", targetNamespace = "http://zep.provantis.de", partName = "readKostenDatevResponse")
    @WebMethod
    public ReadKostenDatevResponseType readKostenDatev(ReadKostenDatevRequestType readKostenDatevRequest) {return delegate.readKostenDatev(readKostenDatevRequest);}

    @Override
    @WebResult(name = "ReadMitarbeiterResponse", targetNamespace = "http://zep.provantis.de", partName = "readMitarbeiterResponse")
    @WebMethod
    public ReadMitarbeiterResponseType readMitarbeiter(ReadMitarbeiterRequestType readMitarbeiterRequest) {return delegate.readMitarbeiter(readMitarbeiterRequest);}

    @Override
    @WebResult(name = "CreateArtikelResponse", targetNamespace = "http://zep.provantis.de", partName = "createArtikelResponse")
    @WebMethod
    public CreateArtikelResponseType createArtikel(CreateArtikelRequestType createArtikelRequest) {return delegate.createArtikel(createArtikelRequest);}

    @Override
    @WebResult(name = "DeleteKundeResponse", targetNamespace = "http://zep.provantis.de", partName = "deleteKundeResponse")
    @WebMethod
    public DeleteKundeResponseType deleteKunde(DeleteKundeRequestType deleteKundeRequest) {return delegate.deleteKunde(deleteKundeRequest);}

    @Override
    @WebResult(name = "CreateKundeResponse", targetNamespace = "http://zep.provantis.de", partName = "createKundeResponse")
    @WebMethod
    public CreateKundeResponseType createKunde(CreateKundeRequestType createKundeRequest) {return delegate.createKunde(createKundeRequest);}

    @Override
    @WebResult(name = "DeleteTicketResponse", targetNamespace = "http://zep.provantis.de", partName = "deleteTicketResponse")
    @WebMethod
    public DeleteTicketResponseType deleteTicket(DeleteTicketRequestType deleteTicketRequest) {return delegate.deleteTicket(deleteTicketRequest);}

    @Override
    @WebResult(name = "CreateProjektzeitResponse", targetNamespace = "http://zep.provantis.de", partName = "createProjektzeitResponse")
    @WebMethod
    public CreateProjektzeitResponseType createProjektzeit(CreateProjektzeitRequestType createProjektzeitRequest) {return delegate.createProjektzeit(createProjektzeitRequest);}

    @Override
    @WebResult(name = "UpdateProjektzeitResponse", targetNamespace = "http://zep.provantis.de", partName = "updateProjektzeitResponse")
    @WebMethod
    public UpdateProjektzeitResponseType updateProjektzeit(UpdateProjektzeitRequestType updateProjektzeitRequest) {return delegate.updateProjektzeit(updateProjektzeitRequest);}

    @Override
    @WebResult(name = "ReadKostenGenericResponse", targetNamespace = "http://zep.provantis.de", partName = "readKostenGenericResponse")
    @WebMethod
    public ReadKostenGenericResponseType readKostenGeneric(ReadKostenGenericRequestType readKostenGenericRequest) {return delegate.readKostenGeneric(readKostenGenericRequest);}

    @Override
    @WebResult(name = "ReadBelegAnhangResponse", targetNamespace = "http://zep.provantis.de", partName = "readBelegAnhangResponse")
    @WebMethod
    public ReadBelegAnhangResponseType readBelegAnhang(ReadBelegAnhangRequestType readBelegAnhangRequest) {return delegate.readBelegAnhang(readBelegAnhangRequest);}

    @Override
    @WebResult(name = "UpdateKundeResponse", targetNamespace = "http://zep.provantis.de", partName = "updateKundeResponse")
    @WebMethod
    public UpdateKundeResponseType updateKunde(UpdateKundeRequestType updateKundeRequest) {return delegate.updateKunde(updateKundeRequest);}

    @Override
    @WebResult(name = "ReadKundeResponse", targetNamespace = "http://zep.provantis.de", partName = "readKundeResponse")
    @WebMethod
    public ReadKundeResponseType readKunde(ReadKundeRequestType readKundeRequest) {return delegate.readKunde(readKundeRequest);}

    @Override
    @WebResult(name = "ReadErloeskontoResponse", targetNamespace = "http://zep.provantis.de", partName = "readErloeskontoResponse")
    @WebMethod
    public ReadErloeskontoResponseType readErloeskonto(ReadErloeskontoRequestType readErloeskontoRequest) {return delegate.readErloeskonto(readErloeskontoRequest);}

    @Override
    @WebResult(name = "ReadBelegResponse", targetNamespace = "http://zep.provantis.de", partName = "readBelegResponse")
    @WebMethod
    public ReadBelegResponseType readBeleg(ReadBelegRequestType readBelegRequest) {return delegate.readBeleg(readBelegRequest);}

    @Override
    @WebResult(name = "UpdateAbgeglicheneZeitenResponse", targetNamespace = "http://zep.provantis.de", partName = "updateAbgeglicheneZeitenResponse")
    @WebMethod
    public UpdateAbgeglicheneZeitenResponseType updateAbgeglicheneZeiten(UpdateAbgeglicheneZeitenRequestType updateAbgeglicheneZeitenRequest) {return delegate.updateAbgeglicheneZeiten(updateAbgeglicheneZeitenRequest);}

    @Override
    @WebResult(name = "DeleteProjektzeitResponse", targetNamespace = "http://zep.provantis.de", partName = "deleteProjektzeitResponse")
    @WebMethod
    public DeleteProjektzeitResponseType deleteProjektzeit(DeleteProjektzeitRequestType deleteProjektzeitRequest) {return delegate.deleteProjektzeit(deleteProjektzeitRequest);}

    @Override
    @WebResult(name = "CreateMahlzeitResponse", targetNamespace = "http://zep.provantis.de", partName = "createMahlzeitResponse")
    @WebMethod
    public CreateMahlzeitResponseType createMahlzeit(CreateMahlzeitRequestType createMahlzeitRequest) {return delegate.createMahlzeit(createMahlzeitRequest);}

    @Override
    @WebResult(name = "ReadArtikelResponse", targetNamespace = "http://zep.provantis.de", partName = "readArtikelResponse")
    @WebMethod
    public ReadArtikelResponseType readArtikel(ReadArtikelRequestType readArtikelRequest) {return delegate.readArtikel(readArtikelRequest);}

    @Override
    @WebResult(name = "CreateMitarbeiterResponse", targetNamespace = "http://zep.provantis.de", partName = "createMitarbeiterResponse")
    @WebMethod
    public CreateMitarbeiterResponseType createMitarbeiter(CreateMitarbeiterRequestType createMitarbeiterRequest) {return delegate.createMitarbeiter(createMitarbeiterRequest);}

    @Override
    @WebResult(name = "UpdateMitarbeiterResponse", targetNamespace = "http://zep.provantis.de", partName = "updateMitarbeiterResponse")
    @WebMethod
    public UpdateMitarbeiterResponseType updateMitarbeiter(UpdateMitarbeiterRequestType updateMitarbeiterRequest) {return delegate.updateMitarbeiter(updateMitarbeiterRequest);}

    @Override
    @WebResult(name = "ReadKategorieResponse", targetNamespace = "http://zep.provantis.de", partName = "readKategorieResponse")
    @WebMethod
    public ReadKategorieResponseType readKategorie(ReadKategorieRequestType readKategorieRequest) {return delegate.readKategorie(readKategorieRequest);}

    @Override
    @WebResult(name = "ReadTicketStatusResponse", targetNamespace = "http://zep.provantis.de", partName = "readTicketStatusResponse")
    @WebMethod
    public ReadTicketStatusResponseType readTicketStatus(ReadTicketStatusRequestType readTicketStatusRequest) {return delegate.readTicketStatus(readTicketStatusRequest);}

    @Override
    @WebResult(name = "ReadRechnungDatevResponse", targetNamespace = "http://zep.provantis.de", partName = "readRechnungDatevResponse")
    @WebMethod
    public ReadRechnungDatevResponseType readRechnungDatev(ReadRechnungDatevRequestType readRechnungDatevRequest) {return delegate.readRechnungDatev(readRechnungDatevRequest);}

    @Override
    @WebResult(name = "DeleteTeilaufgabeResponse", targetNamespace = "http://zep.provantis.de", partName = "deleteTeilaufgabeResponse")
    @WebMethod
    public DeleteTeilaufgabeResponseType deleteTeilaufgabe(DeleteTeilaufgabeRequestType deleteTeilaufgabeRequest) {return delegate.deleteTeilaufgabe(deleteTeilaufgabeRequest);}

    @Override
    @WebResult(name = "ReadAbteilungResponse", targetNamespace = "http://zep.provantis.de", partName = "readAbteilungResponse")
    @WebMethod
    public ReadAbteilungResponseType readAbteilung(ReadAbteilungRequestType readAbteilungRequest) {return delegate.readAbteilung(readAbteilungRequest);}

    @Override
    @WebResult(name = "ReadProjektzeitenResponse", targetNamespace = "http://zep.provantis.de", partName = "readProjektzeitenResponse")
    @WebMethod
    public ReadProjektzeitenResponseType readProjektzeiten(ReadProjektzeitenRequestType readProjektzeitenRequest) {return delegate.readProjektzeiten(readProjektzeitenRequest);}

    @Override
    @WebResult(name = "UpdateProjektResponse", targetNamespace = "http://zep.provantis.de", partName = "updateProjektResponse")
    @WebMethod
    public UpdateProjektResponseType updateProjekt(UpdateProjektRequestType updateProjektRequest) {return delegate.updateProjekt(updateProjektRequest);}

    @Override
    @WebResult(name = "ReadTagessatzanteileResponse", targetNamespace = "http://zep.provantis.de", partName = "readTagessatzanteileResponse")
    @WebMethod
    public ReadTagessatzanteileResponseType readTagessatzanteile(ReadTagessatzanteileRequestType readTagessatzanteileRequest) {return delegate.readTagessatzanteile(readTagessatzanteileRequest);}

    @Override
    @WebResult(name = "UpdateBelegResponse", targetNamespace = "http://zep.provantis.de", partName = "updateBelegResponse")
    @WebMethod
    public UpdateBelegResponseType updateBeleg(UpdateBelegRequestType updateBelegRequest) {return delegate.updateBeleg(updateBelegRequest);}

    @Override
    @WebResult(name = "ReadProjekteResponse", targetNamespace = "http://zep.provantis.de", partName = "readProjekteResponse")
    @WebMethod
    public ReadProjekteResponseType readProjekte(ReadProjekteRequestType readProjekteRequest) {return delegate.readProjekte(readProjekteRequest);}

    @Override
    @WebResult(name = "ReadBelegartResponse", targetNamespace = "http://zep.provantis.de", partName = "readBelegartResponse")
    @WebMethod
    public ReadBelegartResponseType readBelegart(ReadBelegartRequestType readBelegartRequest) {return delegate.readBelegart(readBelegartRequest);}

    @Override
    @WebResult(name = "UpdateTeilaufgabeResponse", targetNamespace = "http://zep.provantis.de", partName = "updateTeilaufgabeResponse")
    @WebMethod
    public UpdateTeilaufgabeResponseType updateTeilaufgabe(UpdateTeilaufgabeRequestType updateTeilaufgabeRequest) {return delegate.updateTeilaufgabe(updateTeilaufgabeRequest);}

    @Override
    @WebResult(name = "CreateFehlzeitResponse", targetNamespace = "http://zep.provantis.de", partName = "createFehlzeitResponse")
    @WebMethod
    public CreateFehlzeitResponseType createFehlzeit(CreateFehlzeitRequestType createFehlzeitRequest) {return delegate.createFehlzeit(createFehlzeitRequest);}

    @Override
    @WebResult(name = "DeleteMahlzeitResponse", targetNamespace = "http://zep.provantis.de", partName = "deleteMahlzeitRespponse")
    @WebMethod
    public DeleteMahlzeitResponseType deleteMahlzeit(DeleteMahlzeitRequestType deleteMahlzeitRequest) {return delegate.deleteMahlzeit(deleteMahlzeitRequest);}

    @Override
    @WebResult(name = "ReadMahlzeitResponse", targetNamespace = "http://zep.provantis.de", partName = "readMahlzeitResponse")
    @WebMethod
    public ReadMahlzeitResponseType readMahlzeit(ReadMahlzeitRequestType readMahlzeitRequest) {return delegate.readMahlzeit(readMahlzeitRequest);}

    @Override
    @WebResult(name = "ReadSteuersatzResponse", targetNamespace = "http://zep.provantis.de", partName = "readSteuersatzResponse")
    @WebMethod
    public ReadSteuersatzResponseType readSteuersatz(ReadSteuersatzRequestType readSteuersatzRequest) {return delegate.readSteuersatz(readSteuersatzRequest);}

    @Override
    @WebResult(name = "ReadRechnungGenericResponse", targetNamespace = "http://zep.provantis.de", partName = "readRechnungGenericResponse")
    @WebMethod
    public ReadRechnungGenericResponseType readRechnungGeneric(ReadRechnungGenericRequestType readRechnungGenericRequest) {return delegate.readRechnungGeneric(readRechnungGenericRequest);}

    @Override
    @WebResult(name = "ReadRechnungspositionResponse", targetNamespace = "http://zep.provantis.de", partName = "readRechnungspositionResponse")
    @WebMethod
    public ReadRechnungspositionResponseType readRechnungsposition(ReadRechnungspositionRequestType readRechnungspositionRequest) {return delegate.readRechnungsposition(readRechnungspositionRequest);}

    @Override
    @WebResult(name = "ReadTicketHistoryResponse", targetNamespace = "http://zep.provantis.de", partName = "readTicketHistoryResponse")
    @WebMethod
    public ReadTicketHistoryResponseType readTicketHistory(ReadTicketHistoryRequestType readTicketHistoryRequest) {return delegate.readTicketHistory(readTicketHistoryRequest);}

    @Override
    @WebResult(name = "ReadTicketResponse", targetNamespace = "http://zep.provantis.de", partName = "readTicketResponse")
    @WebMethod
    public ReadTicketResponseType readTicket(ReadTicketRequestType readTicketRequest) {return delegate.readTicket(readTicketRequest);}

    @Override
    @WebResult(name = "ReadTeilaufgabeResponse", targetNamespace = "http://zep.provantis.de", partName = "readTeilaufgabeResponse")
    @WebMethod
    public ReadTeilaufgabeResponseType readTeilaufgabe(ReadTeilaufgabeRequestType readTeilaufgabeRequest) {return delegate.readTeilaufgabe(readTeilaufgabeRequest);}

    @Override
    @WebResult(name = "CreateProjektResponse", targetNamespace = "http://zep.provantis.de", partName = "createProjektResponse")
    @WebMethod
    public CreateProjektResponseType createProjekt(CreateProjektRequestType createProjektRequest) {return delegate.createProjekt(createProjektRequest);}

    @Override
    @WebResult(name = "DeleteBelegResponse", targetNamespace = "http://zep.provantis.de", partName = "deleteBelegResponse")
    @WebMethod
    public DeleteBelegResponseType deleteBeleg(DeleteBelegRequestType deleteBelegRequest) {return delegate.deleteBeleg(deleteBelegRequest);}

    @Override
    @WebResult(name = "ReadSchlagworteResponse", targetNamespace = "http://zep.provantis.de", partName = "readSchlagworteResponse")
    @WebMethod
    public ReadSchlagworteResponseType readSchlagworte(ReadSchlagworteRequestType readSchlagworteRequest) {return delegate.readSchlagworte(readSchlagworteRequest);}

    @Override
    @WebResult(name = "UpdateArtikelResponse", targetNamespace = "http://zep.provantis.de", partName = "updateArtikelResponse")
    @WebMethod
    public UpdateArtikelResponseType updateArtikel(UpdateArtikelRequestType updateArtikelRequest) {return delegate.updateArtikel(updateArtikelRequest);}

    @Override
    @WebResult(name = "ReadFehlgrundResponse", targetNamespace = "http://zep.provantis.de", partName = "readFehlgrundResponse")
    @WebMethod
    public ReadFehlgrundResponseType readFehlgrund(ReadFehlgrundRequestType readFehlgrundRequest) {return delegate.readFehlgrund(readFehlgrundRequest);}

    @Override
    @WebResult(name = "UpdateFehlzeitResponse", targetNamespace = "http://zep.provantis.de", partName = "updateFehlzeitResponse")
    @WebMethod
    public UpdateFehlzeitResponseType updateFehlzeit(UpdateFehlzeitRequestType updateFehlzeitRequest) {return delegate.updateFehlzeit(updateFehlzeitRequest);}

    @Override
    @WebResult(name = "CreateRechnungspositionFestpreisResponse", targetNamespace = "http://zep.provantis.de", partName = "createRechnungspositionFestpreisResponse")
    @WebMethod
    public CreateRechnungspositionFestpreisResponseType createRechnungspositionFestpreis(CreateRechnungspositionFestpreisRequestType createRechnungspositionFestpreisRequest) {
        return delegate.createRechnungspositionFestpreis(createRechnungspositionFestpreisRequest);
    }

    @Override
    @WebResult(name = "UpdateMahlzeitResponse", targetNamespace = "http://zep.provantis.de", partName = "updateMahlzeitResponse")
    @WebMethod
    public UpdateMahlzeitResponseType updateMahlzeit(UpdateMahlzeitRequestType updateMahlzeitRequest) {return delegate.updateMahlzeit(updateMahlzeitRequest);}

    @Override
    @WebResult(name = "CreateTicketResponse", targetNamespace = "http://zep.provantis.de", partName = "createTicketResponse")
    @WebMethod
    public CreateTicketResponseType createTicket(CreateTicketRequestType createTicketRequest) {return delegate.createTicket(createTicketRequest);}

    @Override
    @WebResult(name = "DeleteFehlzeitResponse", targetNamespace = "http://zep.provantis.de", partName = "deleteFehlzeitResponse")
    @WebMethod
    public DeleteFehlzeitResponseType deleteFehlzeit(DeleteFehlzeitRequestType deleteFehlzeitRequest) {return delegate.deleteFehlzeit(deleteFehlzeitRequest);}

    @Override
    @WebResult(name = "ReadRechnungspositionFestpreisResponse", targetNamespace = "http://zep.provantis.de", partName = "readRechnungspositionFestpreisResponse")
    @WebMethod
    public ReadRechnungspositionFestpreisResponseType readRechnungspositionFestpreis(ReadRechnungspositionFestpreisRequestType readRechnungspositionFestpreisRequest) {
        return delegate.readRechnungspositionFestpreis(readRechnungspositionFestpreisRequest);
    }

    @Override
    @WebResult(name = "ReadWechselkursResponse", targetNamespace = "http://zep.provantis.de", partName = "readWechselkursResponse")
    @WebMethod
    public ReadWechselkursResponseType readWechselkurs(ReadWechselkursRequestType readWechselkursRequest) {return delegate.readWechselkurs(readWechselkursRequest);}

    @Override
    @WebResult(name = "ReadKostenLexwareResponse", targetNamespace = "http://zep.provantis.de", partName = "readKostenLexwareResponse")
    @WebMethod
    public ReadKostenLexwareResponseType readKostenLexware(ReadKostenLexwareRequestType readKostenLexwareRequest) {return delegate.readKostenLexware(readKostenLexwareRequest);}

    @Override
    @WebResult(name = "UpdateTicketResponse", targetNamespace = "http://zep.provantis.de", partName = "updateTicketResponse")
    @WebMethod
    public UpdateTicketResponseType updateTicket(UpdateTicketRequestType updateTicketRequest) {return delegate.updateTicket(updateTicketRequest);}

    @Override
    @WebResult(name = "DeleteProjektResponse", targetNamespace = "http://zep.provantis.de", partName = "deleteProjektResponse")
    @WebMethod
    public DeleteProjektResponseType deleteProjekt(DeleteProjektRequestType deleteProjektRequest) {return delegate.deleteProjekt(deleteProjektRequest);}

    @Override
    @WebResult(name = "CreateBelegResponse", targetNamespace = "http://zep.provantis.de", partName = "createBelegResponse")
    @WebMethod
    public CreateBelegResponseType createBeleg(CreateBelegRequestType createBelegRequest) {return delegate.createBeleg(createBelegRequest);}

    @Override
    @WebResult(name = "DeleteArtikelResponse", targetNamespace = "http://zep.provantis.de", partName = "deleteArtikelResponse")
    @WebMethod
    public DeleteArtikelResponseType deleteArtikel(DeleteArtikelRequestType deleteArtikelRequest) {return delegate.deleteArtikel(deleteArtikelRequest);}

    @Override
    @WebResult(name = "CreateTeilaufgabeResponse", targetNamespace = "http://zep.provantis.de", partName = "createTeilaufgabeResponse")
    @WebMethod
    public CreateTeilaufgabeResponseType createTeilaufgabe(CreateTeilaufgabeRequestType createTeilaufgabeRequest) {return delegate.createTeilaufgabe(createTeilaufgabeRequest);}

    @Override
    @WebResult(name = "ReadFehlzeitResponse", targetNamespace = "http://zep.provantis.de", partName = "readFehlzeitResponse")
    @WebMethod
    public ReadFehlzeitResponseType readFehlzeit(ReadFehlzeitRequestType readFehlzeitRequest) {return delegate.readFehlzeit(readFehlzeitRequest);}

    @Override
    @WebResult(name = "ReadAbgeglicheneZeitenResponse", targetNamespace = "http://zep.provantis.de", partName = "readAbgeglicheneZeitenResponse")
    @WebMethod
    public ReadAbgeglicheneZeitenResponseType readAbgeglicheneZeiten(ReadAbgeglicheneZeitenRequestType readAbgeglicheneZeitenRequest) {return delegate.readAbgeglicheneZeiten(readAbgeglicheneZeitenRequest);}

    @Override
    @WebResult(name = "ReadRechnungLexwareResponse", targetNamespace = "http://zep.provantis.de", partName = "readRechnungLexwareResponse")
    @WebMethod
    public ReadRechnungLexwareResponseType readRechnungLexware(ReadRechnungLexwareRequestType readRechnungLexwareRequest) {return delegate.readRechnungLexware(readRechnungLexwareRequest);}

    @Override
    @WebResult(name = "ReadZahlungsartResponse", targetNamespace = "http://zep.provantis.de", partName = "readZahlungsartResponse")
    @WebMethod
    public ReadZahlungsartResponseType readZahlungsart(ReadZahlungsartRequestType readZahlungsartRequest) {return delegate.readZahlungsart(readZahlungsartRequest);}

    public void setDelegate(ZepSoapPortType delegate) {
        this.delegate = delegate;
    }
}
