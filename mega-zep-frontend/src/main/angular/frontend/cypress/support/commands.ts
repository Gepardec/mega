// @ts-ignore
Cypress.Commands.add('loginByGoogleApi', () => {
  // see https://docs.cypress.io/guides/testing-strategies/google-authentication

  cy.log('Logging in to Google');

  cy.request({
    method: 'POST',
    url: 'https://www.googleapis.com/oauth2/v4/token',
    body: {
      grant_type: 'refresh_token',
      refresh_token: Cypress.env('googleRefreshToken'),
      client_id: Cypress.env('googleClientId'),
      client_secret: Cypress.env('googleClientSecret'),
    },
  }).then(({body}) => {
    const {access_token, id_token} = body;

    window.sessionStorage.setItem('access_token', access_token);
    window.sessionStorage.setItem('id_token', id_token);
  });
});
