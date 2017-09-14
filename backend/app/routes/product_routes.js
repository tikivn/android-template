const fs = require('fs');

module.exports = (app, database) => {
  const db = database.collection('products');
  let products = [];
  app.get('/products', (req, res) => {
    if (products.length > 0) {
      res.send(products);
    } else {
      const fileContent = fs.readFileSync('app/data/products.json', 'utf-8');
      products = JSON.parse(fileContent);
      res.send(products);
    }
  });
};
