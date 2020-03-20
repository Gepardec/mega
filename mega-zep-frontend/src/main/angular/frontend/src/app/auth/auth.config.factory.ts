import {ConfigService} from '../modules/shared/services/config/config.service';
import {OAuthModuleConfig} from 'angular-oauth2-oidc';

export function authConfigFactory(configService: ConfigService): OAuthModuleConfig {
  return {
    resourceServer: {
      allowedUrls: [configService.getBackendUrl()],
      sendAccessToken: true
    }
  };
}
