const fs = require('fs');

module.exports = (app, database) => {
  const db = database.collection('products');
  let products = [];
  app.get('/products', (req, res) => {
    if (products.length === 0) {
      const fileContent = fs.readFileSync('app/data/products.json', 'utf-8');
      products = JSON.parse(fileContent).map(product => ({
        title: product.title,
        price: product.price,
        image: product.image,
        description: product.description,
      }));
    }

    const page = parseInt(req.query.page, 10);
    const perPage = parseInt(req.query.per_page, 10);
    const startIndex = (page - 1) * perPage;
    const endIndex = startIndex + perPage;
    const responseProducts = products.filter((_, index) => index >= startIndex && index < endIndex);

    const total = products.length;
    const lastPage = Math.ceil(total / perPage);
    res.send({
      total,
      current_Page: page,
      last_page: lastPage,
      items: responseProducts,
    });
  });
};
