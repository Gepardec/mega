import {AuthConfig} from 'angular-oauth2-oidc';

export const authConfig: AuthConfig = {
  redirectUri: window.location.origin,
  strictDiscoveryDocumentValidation: false,
  showDebugInformation: false,
  sessionChecksEnabled: false
};

export const cypressAuthConfig: AuthConfig = {
  ...authConfig,
  oidc: false
};
