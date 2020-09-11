import { configuration } from '../app/modules/shared/constants/configuration';

export const environment = {
  production: true,
  // Base url
  frontendOriginSegment: 'mega-zep-frontend',
  backendOriginSegment: 'mega-zep-backend',
  ZEP_URL_OFFICE_MANAGEMENT: `${configuration.ZEP_URL}/${configuration.OFFICE_MANAGEMENT_SEGMENT}`
};
