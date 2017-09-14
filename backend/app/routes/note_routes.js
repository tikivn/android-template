const { ObjectID } = require('mongodb');

module.exports = (app, database) => {
  const db = database.collection('notes');

  app.get('/notes/:id', (req, res) => {
    const { id } = req.params;
    console.log(id);
    const query = { _id: new ObjectID(id) };
    console.log(query);
    db
      .findOne(query)
      .then(result => res.send(result))
      .catch((error) => {
        console.log(error);
        res.send({ error });
      });
  });

  app.post('/notes', (req, res) => {
    const { title, body } = req.body;
    const note = {
      title,
      body,
    };

    db.insert(note, (err, result) => {
      if (err) {
        res.send({ error: 'An error occurred' });
      } else {
        res.send(result.ops[0]);
      }
    });
  });
};
