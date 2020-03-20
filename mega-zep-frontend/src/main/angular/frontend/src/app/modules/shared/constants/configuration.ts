export const configuration = {
  PAGE_NAMES: {
    LOGIN: 'Login',
    EMPLOYEES: 'MITARBEITERÜBERSICHT',
    MONTHLY_REPORT: 'MEIN MEGA',
    ERROR: 'Error'
  },

  PAGE_URLS: {
    MONTHLY_REPORT: 'monthlyReport',
    EMPLOYEES: 'employees',
    LOGIN: 'login',
    ERROR: 'error'
  },

  SPREADSHEET_URL: 'https://docs.google.com/spreadsheets/d/1wUJHMtkY47RhLIGytg_MXVe_hgPAj_yzozLMcv5hrdU/edit#gid=604001411',
  ZEP_URL: 'https://www.zep-online.de/zepgepardecservices',

  EMPLOYEE_FUNCTIONS: {
    '01': 'Technische/r PL',
    '02': 'SoftwareentwicklerIn',
    '03': 'Verwaltung',
    '04': 'Senior',
    '05': 'Junior',
    '06': 'ExpertIn Inbetriebnahme',
    '06-1': 'Software-ArchitektIn',
    '07': 'FerialpraktikantIn',
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
