import {Attribute} from "./Attribute";
import {EmployementTime} from "./EmployementTime";
import {InternalRecord} from "./InternalRecord";
import {Category} from "./Category";
import {RegularWorkingTime} from "./RegularWorkingTime";

export class Employee {
  private _userId: string;
  private _nachname: string;
  private _vorname: string;
  private _email: string;
  private _personalNr: string;
  private _strasse: string;
  private _plz: string;
  private _ort: string;
  private _telefon: string;
  private _fax: string;
  private _mobil: string;
  private _telefonPrivat: string;
  private _bemerkung: string;
  private _sprache: string;
  private _anrede: string;
  private _geburtsdatum: string;
  private _kostentraeger: string;
  private _iban: string;
  private _bic: string;
  private _kontoNr: string;
  private _blz: string;
  private _bankname: string;
  private _abteilung: string;
  private _preisgruppe: string;
  private _beschaeftigungszeitListe: Array<EmployementTime>;
  private _regelarbeitszeitListe: Array<RegularWorkingTime>;
  private _freigabedatum: string;
  private _titel: string;
  private _internersatzListe: Array<InternalRecord>;
  private _waehrung: string;
  private _mwst: number;
  private _freelancer: number;
  private _created: string;
  private _modified: string;
  private _kurzzeichen: string;
  private _rechte: number;
  private _vorjahresueberstunden: number;
  private _urlaubsanspruchProJahr: number;
  private _urlaubsanspruchJahresanfang: number;
  private _kategorieListe: Array<Category>;
  private _attributes: Array<Attribute>;

  get userId(): string {
    return this._userId;
  }

  set userId(value: string) {
    this._userId = value;
  }

  get nachname(): string {
    return this._nachname;
  }

  set nachname(value: string) {
    this._nachname = value;
  }

  get vorname(): string {
    return this._vorname;
  }

  set vorname(value: string) {
    this._vorname = value;
  }

  get email(): string {
    return this._email;
  }

  set email(value: string) {
    this._email = value;
  }

  get personalNr(): string {
    return this._personalNr;
  }

  set personalNr(value: string) {
    this._personalNr = value;
  }

  get strasse(): string {
    return this._strasse;
  }

  set strasse(value: string) {
    this._strasse = value;
  }

  get plz(): string {
    return this._plz;
  }

  set plz(value: string) {
    this._plz = value;
  }

  get ort(): string {
    return this._ort;
  }

  set ort(value: string) {
    this._ort = value;
  }

  get telefon(): string {
    return this._telefon;
  }

  set telefon(value: string) {
    this._telefon = value;
  }

  get fax(): string {
    return this._fax;
  }

  set fax(value: string) {
    this._fax = value;
  }

  get mobil(): string {
    return this._mobil;
  }

  set mobil(value: string) {
    this._mobil = value;
  }

  get telefonPrivat(): string {
    return this._telefonPrivat;
  }

  set telefonPrivat(value: string) {
    this._telefonPrivat = value;
  }

  get bemerkung(): string {
    return this._bemerkung;
  }

  set bemerkung(value: string) {
    this._bemerkung = value;
  }

  get sprache(): string {
    return this._sprache;
  }

  set sprache(value: string) {
    this._sprache = value;
  }

  get anrede(): string {
    return this._anrede;
  }

  set anrede(value: string) {
    this._anrede = value;
  }

  get geburtsdatum(): string {
    return this._geburtsdatum;
  }

  set geburtsdatum(value: string) {
    this._geburtsdatum = value;
  }

  get kostentraeger(): string {
    return this._kostentraeger;
  }

  set kostentraeger(value: string) {
    this._kostentraeger = value;
  }

  get iban(): string {
    return this._iban;
  }

  set iban(value: string) {
    this._iban = value;
  }

  get bic(): string {
    return this._bic;
  }

  set bic(value: string) {
    this._bic = value;
  }

  get kontoNr(): string {
    return this._kontoNr;
  }

  set kontoNr(value: string) {
    this._kontoNr = value;
  }

  get blz(): string {
    return this._blz;
  }

  set blz(value: string) {
    this._blz = value;
  }

  get bankname(): string {
    return this._bankname;
  }

  set bankname(value: string) {
    this._bankname = value;
  }

  get abteilung(): string {
    return this._abteilung;
  }

  set abteilung(value: string) {
    this._abteilung = value;
  }

  get preisgruppe(): string {
    return this._preisgruppe;
  }

  set preisgruppe(value: string) {
    this._preisgruppe = value;
  }

  get beschaeftigungszeitListe(): Array<EmployementTime> {
    return this._beschaeftigungszeitListe;
  }

  set beschaeftigungszeitListe(value: Array<EmployementTime>) {
    this._beschaeftigungszeitListe = value;
  }

  get regelarbeitszeitListe(): Array<RegularWorkingTime> {
    return this._regelarbeitszeitListe;
  }

  set regelarbeitszeitListe(value: Array<RegularWorkingTime>) {
    this._regelarbeitszeitListe = value;
  }

  get freigabedatum(): string {
    return this._freigabedatum;
  }

  set freigabedatum(value: string) {
    this._freigabedatum = value;
  }

  get titel(): string {
    return this._titel;
  }

  set titel(value: string) {
    this._titel = value;
  }

  get internersatzListe(): Array<InternalRecord> {
    return this._internersatzListe;
  }

  set internersatzListe(value: Array<InternalRecord>) {
    this._internersatzListe = value;
  }

  get waehrung(): string {
    return this._waehrung;
  }

  set waehrung(value: string) {
    this._waehrung = value;
  }

  get mwst(): number {
    return this._mwst;
  }

  set mwst(value: number) {
    this._mwst = value;
  }

  get freelancer(): number {
    return this._freelancer;
  }

  set freelancer(value: number) {
    this._freelancer = value;
  }

  get created(): string {
    return this._created;
  }

  set created(value: string) {
    this._created = value;
  }

  get modified(): string {
    return this._modified;
  }

  set modified(value: string) {
    this._modified = value;
  }

  get kurzzeichen(): string {
    return this._kurzzeichen;
  }

  set kurzzeichen(value: string) {
    this._kurzzeichen = value;
  }

  get rechte(): number {
    return this._rechte;
  }

  set rechte(value: number) {
    this._rechte = value;
  }

  get vorjahresueberstunden(): number {
    return this._vorjahresueberstunden;
  }

  set vorjahresueberstunden(value: number) {
    this._vorjahresueberstunden = value;
  }

  get urlaubsanspruchProJahr(): number {
    return this._urlaubsanspruchProJahr;
  }

  set urlaubsanspruchProJahr(value: number) {
    this._urlaubsanspruchProJahr = value;
  }

  get urlaubsanspruchJahresanfang(): number {
    return this._urlaubsanspruchJahresanfang;
  }

  set urlaubsanspruchJahresanfang(value: number) {
    this._urlaubsanspruchJahresanfang = value;
  }

  get kategorieListe(): Array<Category> {
    return this._kategorieListe;
  }

  set kategorieListe(value: Array<Category>) {
    this._kategorieListe = value;
  }

  get attributes(): Array<Attribute> {
    return this._attributes;
  }

  set attributes(value: Array<Attribute>) {
    this._attributes = value;
  }
}
