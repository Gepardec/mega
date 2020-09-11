import { MonthlyReport } from './MonthlyReport';
import { State } from '../../shared/models/State';

export const monthlyReportMock: MonthlyReport = {
  employeeCheckState: State.DONE,
  otherChecksDone: true,
  employee: {
    firstName: 'Mock',
    sureName: 'Other',
    workDescription: '02',
    email: 'someemail@example.com',
    role: 1,
    salutation: 'Mister',
    title: 'Mag.',
    userId: '000-mother',
    active: true,
    releaseDate: '2020-0asd2-02'
  },
  comments: [
    // {
    //   stateUpdateDate: new Date().toString(),
    //   message: 'wrong time',
    //   id: 12,
    //   authorUpdateDate: '2020-02-02',
    //   state: State.OPEN,
    //   author: 'some author'
    // },
    // {
    //   stateUpdateDate: new Date().toString(),
    //   message: 'wrong time',
    //   id: 13,
    //   authorUpdateDate: '2020-02-02',
    //   state: State.DONE,
    //   author: 'some author'
    // },
  ],
  timeWarnings: [
    {
      warnings: ['1', '2'],
      missingRestTime: 12,
      missingBreakTime: 12,
      excessWorkTime: 12,
      date: new Date().toString()
    }
  ],
  journeyWarnings: [
    // {warnings: ['1', '2'], date: new Date().toString()}
  ]
};
