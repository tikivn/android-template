const noteRoutes = require('./note_routes');
const productRouters = require('./product_routes');

module.exports = (app, db) => {
  noteRoutes(app, db);
  productRouters(app, db);
};
