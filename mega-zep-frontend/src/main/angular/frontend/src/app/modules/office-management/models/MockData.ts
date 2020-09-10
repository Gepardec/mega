import { State } from '../../shared/models/State';
import { OfficeManagementEntry } from './OfficeManagementEntry';

export const omEntriesMock: Array<OfficeManagementEntry> = [
  {
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
      releaseDate: '2020-02-02'
    },
    comments: [],
    customerCheckState: State.DONE,
    employeeCheckState: State.DONE,
    internalCheckState: State.DONE,
    projectCheckState: State.DONE,
    id: 91919191919191
  },
  {
    employee: {
      firstName: 'A',
      sureName: 'Aslary',
      workDescription: '02',
      email: 'someemail@example.com',
      role: 1,
      salutation: 'Mister',
      title: 'Mag.',
      userId: '000-mother',
      active: true,
      releaseDate: '2020-02-02'
    },
    comments: [],
    customerCheckState: State.OPEN,
    employeeCheckState: State.DONE,
    internalCheckState: State.OPEN,
    projectCheckState: State.DONE,
    id: 123123123
  },
  {
    employee: {
      firstName: 'Mock',
      sureName: 'Berto',
      workDescription: '02',
      email: 'someemail@example.com',
      role: 1,
      salutation: 'Mister',
      title: 'Mag.',
      userId: '000-mother',
      active: true,
      releaseDate: '2020-02-02'
    },
    comments: [],
    customerCheckState: State.OPEN,
    employeeCheckState: State.DONE,
    internalCheckState: State.OPEN,
    projectCheckState: State.DONE,
    id: 1
  },
  {
    employee: {
      firstName: 'Lincoln',
      sureName: 'Burrows',
      workDescription: '02',
      email: 'someemail@example.com',
      role: 1,
      salutation: 'Mister',
      title: 'Mag.',
      userId: '000-mother',
      active: true,
      releaseDate: new Date().toString()
    },
    comments: [
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.OPEN,
        authorUpdateDate: '2020-02-02',
        id: 12,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.OPEN,
        authorUpdateDate: '2020-02-02',
        id: 15,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 14,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 13,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 131,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 1323,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 133344,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 1344444,
        message: 'asdasdasd'
      },
    ],
    customerCheckState: State.DONE,
    employeeCheckState: State.OPEN,
    internalCheckState: State.OPEN,
    projectCheckState: State.DONE,
    id: 2
  },
  {
    employee: {
      firstName: 'Italy',
      sureName: 'Spritzer',
      workDescription: '02',
      email: 'someemail@example.com',
      role: 1,
      salutation: 'Mister',
      title: 'Mag.',
      userId: '000-mother',
      active: true,
      releaseDate: '2020-02-02'
    },
    comments: [
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 12,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 15,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 14,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 16,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 17,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 18,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 19,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 20,
        message: 'There was a problem, have a look here please'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 21,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 22,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 23,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 24,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 25,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 26,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 27,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 28,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 29,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 30,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 31,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 32,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 33,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 34,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 35,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 36,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 37,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 38,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 39,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 40,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 41,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 42,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 43,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 44,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 45,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 46,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 47,
        message: 'asdasdasd'
      },
    ],
    customerCheckState: State.DONE,
    employeeCheckState: State.OPEN,
    internalCheckState: State.OPEN,
    projectCheckState: State.DONE,
    id: 3
  },
  {
    employee: {
      firstName: 'Carlos',
      sureName: 'Has no car',
      workDescription: '02',
      email: 'someemail@example.com',
      role: 1,
      salutation: 'Mister',
      title: 'Mag.',
      userId: '000-mother',
      active: true,
      releaseDate: '2020-02-02'
    },
    comments: [
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 12,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 15,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 14,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 13,
        message: 'asdasdasd'
      }
    ],
    customerCheckState: State.DONE,
    employeeCheckState: State.OPEN,
    internalCheckState: State.OPEN,
    projectCheckState: State.DONE,
    id: 4
  },
  {
    employee: {
      firstName: 'Pablo',
      sureName: 'Escobar',
      workDescription: '02',
      email: 'someemail@example.com',
      role: 1,
      salutation: 'Mister',
      title: 'Mag.',
      userId: '000-mother',
      active: true,
      releaseDate: '2020-02-02'
    },
    comments: [
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 13,
        message: 'https://youtube.com'
      }
    ],
    customerCheckState: State.OPEN,
    employeeCheckState: State.DONE,
    internalCheckState: State.OPEN,
    projectCheckState: State.DONE,
    id: 5
  },
  {
    employee: {
      firstName: 'Richard',
      sureName: 'Stallman',
      workDescription: '02',
      email: 'someemail@example.com',
      role: 1,
      salutation: 'Mister',
      title: 'Mag.',
      userId: '000-mother',
      active: true,
      releaseDate: '2020-02-02'
    },
    comments: [
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.OPEN,
        authorUpdateDate: '2020-02-02',
        id: 12,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.OPEN,
        authorUpdateDate: '2020-02-02',
        id: 15,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.OPEN,
        authorUpdateDate: '2020-02-02',
        id: 14,
        message: 'asdasdasd'
      },
      {
        stateUpdateDate: new Date().toString(),
        author: 'jeff',
        state: State.DONE,
        authorUpdateDate: '2020-02-02',
        id: 13,
        message: 'asdasdasd'
      }
    ],
    customerCheckState: State.DONE,
    employeeCheckState: State.OPEN,
    internalCheckState: State.OPEN,
    projectCheckState: State.DONE,
    id: 6
  }
];
