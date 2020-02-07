export const configuration = {
  PAGE_NAMES: {
    LOGIN: 'Login',
    EMPLOYEES: 'MITARBEITERÜBERSICHT',
    MONTHLY_REPORT: 'MEIN MEGA'
  },

  PAGE_URLS: {
    MONTHLY_REPORT: 'monthlyReport',
    EMPLOYEES: 'employees',
    LOGIN: 'login'
  },

  SPREADSHEET_URL: 'https://docs.google.com/spreadsheets/d/1PmJ9wnYk-OJL91iwMzMBE_DTifHdhaa35HrOMtmEfgs/edit#gid=1076534321',
  ZEP_URL: 'https://www.zep-online.de/zepgepardecservices',

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

  dateFormat: 'yyyy-MM-dd',

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
