import { ProjectManagementEntry } from './ProjectManagementEntry';
import { State } from '../../shared/models/State';

const projectManagementResponseMock = new Map<string, Array<ProjectManagementEntry>>();

const project1Entries: Array<ProjectManagementEntry> = [
  {
    employeeName: 'Timothy Peterson',
    customerCheckState: State.DONE,
    employeeCheckState: State.DONE,
    internalCheckState: State.OPEN,
    projectCheckState: State.OPEN,
    comments: [
      {state: State.DONE, updateDate: new Date().toString(), message: 'You made a mistake', author: 'CEO Timmy', id: 1}
    ]
  },
  {
    employeeName: 'Rick Grimes',
    customerCheckState: State.OPEN,
    employeeCheckState: State.DONE,
    internalCheckState: State.OPEN,
    projectCheckState: State.OPEN,
    comments: []
  },
  {
    employeeName: 'Barney Stinson',
    customerCheckState: State.OPEN,
    employeeCheckState: State.OPEN,
    internalCheckState: State.DONE,
    projectCheckState: State.OPEN,
    comments: [
      {
        state: State.OPEN,
        updateDate: new Date().toString(),
        message: 'I love NYC',
        author: 'Just stop it',
        id: 3
      }
    ]
  }
];

const project2Entries: Array<ProjectManagementEntry> = [
  {
    employeeName: 'Timothy Peterson',
    customerCheckState: State.OPEN,
    employeeCheckState: State.OPEN,
    internalCheckState: State.OPEN,
    projectCheckState: State.DONE,
    comments: [
      {
        state: State.DONE,
        updateDate: new Date().toString(),
        message: 'Timmy is in 2 projects :D',
        author: 'James Gosling',
        id: 4
      }
    ]
  },
  {
    employeeName: 'Ted Mosbey',
    customerCheckState: State.DONE,
    employeeCheckState: State.DONE,
    internalCheckState: State.OPEN,
    projectCheckState: State.OPEN,
    comments: [
      {
        state: State.OPEN,
        updateDate: new Date().toString(),
        message: 'As your best man, I want ...',
        author: 'Marshall',
        id: 5
      }
    ]
  }
];

projectManagementResponseMock.set('ÖGK-RGKK2WC-2020', project1Entries);
projectManagementResponseMock.set('ÖGK-RGKKCC-2020', project2Entries);

export default projectManagementResponseMock;
