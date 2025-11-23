export const environment = {
  production: true,
  apiBaseUrl: 'https://your-production-api.com',
  uiBaseUrl: 'https://your-production-app.com',

  oauth2: {
    authorizeUri: '/oauth2/authorize',
    accessToken: 'accessToken',
    accessTokenHeaderKey: 'Authorization',

    google: {
      redirectUrl : 'http://localhost:4200/oauth2/google/redirect',
      authUrl: 'http://localhost:8080/oauth2/authorize/google'
    },
    github: {
      redirectUrl : 'http://localhost:4200/oauth2/github/redirect',
      authUrl: 'http://localhost:8080/oauth2/authorize/github'
    }
  }
};
