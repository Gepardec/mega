require('dotenv').config()

/**
 * @type {Cypress.PluginConfig}
 */
module.exports = (on, config) => {

  config.env.googleRefreshToken = process.env.GOOGLE_REFRESH_TOKEN
  config.env.googleClientId = process.env.MEGA_APP_GOOGLE_CLIENTID
  config.env.googleClientSecret = process.env.MEGA_APP_GOOGLE_CLIENT_SECRET

  return config;
}
