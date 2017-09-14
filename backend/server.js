const express = require('express');
const { MongoClient } = require('mongodb');
const bodyParser = require('body-parser');
const db = require('./config/db');
const routes = require('./app/routes');

const app = express();

const port = 8000;

app.use(bodyParser.urlencoded({ extended: true })); // config to parse urlencoded
app.use(bodyParser.json({ type: 'application/json' })); // config to parse json

MongoClient.connect(db.url, (err, database) => {
  if (err) {
    console.log(err);
    return;
  }
  routes(app, database);

  app.listen(port, () => {
    console.log(`we are listening on ${port}`);
  });
});
