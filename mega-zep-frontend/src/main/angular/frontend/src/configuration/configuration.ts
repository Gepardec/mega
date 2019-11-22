import {HttpHeaders} from "@angular/common/http";

export const configuration = {
  // Base url
  BASEURL: 'http://localhost:8080',

  //FIXME GAJ: check if key can be assigned to array of objects for finding single URL easier

  PAGE_NAMES: {
    LOGIN: "Login",
    EMPLOYEES: "Employees",
    MONTHLY_REPORT : "Monhtly-Report"
  },

  PAGE_URLS: {
    LOGIN: "/login",
    EMPLOYEES: "/employees",
    MONTHLY_REPORT : "/monthlyReport"
  },

  EMPLOYEE_FUNCTIONS: {
    '01': 'Technischer PL',
    '02': 'Softwareentwickler',
    '03': 'Verwaltung',
    '04': 'Senior',
    '05': 'Junior',
    '06': 'Experte Inbetriebnahme',
    '06-1': 'Software-Architekt',
    '07': 'Ferialpraktikant',
    '08': 'Consultant Senior',
    '99': 'Reisezeiten'
  },

  // Http Headers
  httpOptions: {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
    withCredentials: true
  },

  dateFormat: 'yyyy-MM-dd',
  EMPLOYEE_ROLES: {
    USER: 0,
    ADMINISTRATOR: 1,
    CONTROLLER: 2,
    USER_MIT_ZUSATZRECHTEN: 3
  }
};
