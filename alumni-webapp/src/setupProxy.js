
const { createProxyMiddleware } = require('http-proxy-middleware');

const API_TARGET = process.env.REACT_APP_API_TARGET;

module.exports = function(app) {
  app.use(
    '/api',
    createProxyMiddleware({
      target: API_TARGET,
      changeOrigin: true
    })
  );
};
