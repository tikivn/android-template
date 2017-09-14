const {
  ObjectID,
} = require('mongodb');

module.exports = (app, database) => {
  const db = database.collection('notes');

  app.delete('/notes/:id', (req, res) => {
    const {
      id,
    } = req.params;
    console.log(id);
    Promise.resolve(id)
      .then(queryId => new ObjectID(queryId))
      .then(_id => ({
        _id,
      }))
      .then(query => db.remove(query))
      .then(result => res.send(result))
      .catch((err) => {
        console.log(err);
        res.send({
          error: 'An error occurred',
        });
      });
  });

  app.get('/notes', (req, res) => {
    db.find().toArray()
      .then(result => res.send(result))
      .catch((err) => {
        console.log(err);
        const error = 'An error occurred';
        res.send({
          error,
        });
      });
  });

  app.get('/notes/:id', (req, res) => {
    const {
      id,
    } = req.params;
    console.log(id);
    Promise.resolve(id)
      .then(queryId => new ObjectID(queryId))
      .then(_id => ({
        _id,
      }))
      .then(query => db.findOne(query))
      .then(result => res.send(result))
      .catch((err) => {
        console.log(err);
        const error = `${id} not found`;
        res.send({
          error,
        });
      });
  });

  app.post('/notes', (req, res) => {
    console.log(req.body);
    if (Array.isArray(req.body)) {
      const notes = [...req.body];
      db.insertMany(notes)
        .then(result => res.send(result))
        .catch((err) => {
          console.log(err);
          res.send({
            error: 'An error occurred',
          });
        });
    } else {
      const {
        title,
        body,
      } = req.body;
      const note = {
        title,
        body,
      };
      console.log(note);
      db
        .insert(note)
        .then(result => res.send(result.ops[0]))
        .catch((err) => {
          console.log(err);
          res.send({
            error: 'An error occurred',
          });
        });
    }
  });
};
