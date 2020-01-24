export const configuration = {
  PAGE_NAMES: {
    LOGIN: 'Login',
    EMPLOYEES: 'Freigabe',
    MONTHLY_REPORT: 'Monats-Auswertung'
  },

  PAGE_URLS: {
    MONTHLY_REPORT: 'monthlyReport',
    EMPLOYEES: 'employees',
    LOGIN: 'login'
  },

  SPREADSHEET_URL: 'https://docs.google.com/spreadsheets/d/1PmJ9wnYk-OJL91iwMzMBE_DTifHdhaa35HrOMtmEfgs/edit#gid=1076534321',

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
    99: 'Reisezeiten'
  },

  dateFormat: 'yyyy-MM-dd',

  EMPLOYEE_ROLES: {
    USER: '0',
    ADMINISTRATOR: '1',
    CONTROLLER: '2',
    USER_MIT_ZUSATZRECHTEN: '3'
  },

  LogLevel: {
    All: 0,
    Debug: 1,
    Info: 2,
    Warn: 3,
    Error: 4,
    Fatal: 5,
    Off: 6
  },

  logWithDate: true
};
