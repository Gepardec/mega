export const configuration = {
  PAGE_URLS: {
    MONTHLY_REPORT: 'monthlyReport',
    OFFICE_MANAGEMENT: 'officeManagement',
    PROJECT_MANAGEMENT: 'projectManagement',
    LOGIN: 'login',
    ERROR: 'error'
  },

  /* TODO: Remove these obsolete properties, because these properties are provided via the /config endpoint */
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
