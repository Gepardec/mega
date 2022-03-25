export const configuration = {
  PAGE_URLS: {
    MONTHLY_REPORT: 'monthlyReport',
    OFFICE_MANAGEMENT: 'officeManagement',
    PROJECT_MANAGEMENT: 'projectManagement',
    LOGIN: 'login',
    ERROR: 'error'
  },

  OFFICE_MANAGEMENT_SEGMENT: `view/index.php?menu=MitarbeiterVerwaltungMgr&
  modelContentMenu=true&mgr=MitarbeiterProjektzeitMgr&contentModelId=`,

  dateFormat: 'YYYY-MM-DD',

  LogLevel: {
    All: 0,
    Debug: 1,
    Info: 2,
    Warn: 3,
    Error: 4,
    Fatal: 5,
    Off: 6
  },

  logWithDate: true,

  snackbar: {
    horizontalPosition: 'center',
    verticalPosition: 'top',
    duration: 5000
  }
};
