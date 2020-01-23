import { AuthConfig } from 'angular-oauth2-oidc';

export const authConfig: AuthConfig = {
  issuer: 'https://accounts.google.com',
  redirectUri: window.location.origin,
  strictDiscoveryDocumentValidation: false,
  scope: 'openid profile email',
  showDebugInformation: false,
  sessionChecksEnabled: false
};
