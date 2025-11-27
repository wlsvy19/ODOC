const express = require('express');
const path = require('path');
const httpProxy = require('http-proxy');
const fs = require('fs');
const morgan = require('morgan');
const rfs = require('rotating-file-stream');

const app = express();

const accessLogStream = rfs.createStream('access.log', {
  interval: '1d',
  path: path.join(__dirname, 'logs'),
});
app.use(morgan('combined', { stream: accessLogStream }));

const port = 9081;
const proxy = httpProxy.createProxyServer();
const cors = require('cors');
const corsOpt = {
  origin: 'http://10.100.10.148:9082',
};

app.use(cors(corsOpt));

app.use(express.static(path.join(__dirname, 'dist')));

app.all('/api/*', (req, res) => {
  proxy.web(
    req,
    res,
    {
      target: 'http://localhost:9080',
      xfwd: true,
    },
    (err) => {
      console.error('Proxy error:', err);
      res.writeHead(500, {
        'Content-Type': 'text/plain',
      });
      res.end('Something went wrong. err: ' + err);
    },
  );
});

app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'dist', 'index.html'));
});

app.listen(port, '0.0.0.0', () => {
  console.log(`Server is running on http://10.100.10.148:${port}`);
});
