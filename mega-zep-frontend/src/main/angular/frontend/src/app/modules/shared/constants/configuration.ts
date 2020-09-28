export const configuration = {
  PAGE_URLS: {
    MONTHLY_REPORT: 'monthlyReport',
    OFFICE_MANAGEMENT: 'officeManagement',
    LOGIN: 'login',
    ERROR: 'error'
  },

  SPREADSHEET_URL: 'https://docs.google.com/spreadsheets/d/1wUJHMtkY47RhLIGytg_MXVe_hgPAj_yzozLMcv5hrdU/',
  ZEP_URL: 'https://www.zep-online.de/zepgepardecservices',
  OFFICE_MANAGEMENT_SEGMENT: `view/index.php?menu=MitarbeiterVerwaltungMgr&
  modelContentMenu=true&mgr=MitarbeiterProjektzeitMgr&contentModelId=`,

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
