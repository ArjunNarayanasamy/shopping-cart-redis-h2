# shopping-cart-redis-h2

A shopping Cart component, when user choose a product, it will be published to a message queue.

We have micro service component which receives message from message queue, save it into database. 

The code designed with following design constrains in mind:

1. the app should not lose message if the database connections goes down.

2. The app should be able to auto create table if the table doesn’t exist in database.

3. Self-healing from database issue without human intervention.

4. Good fault tolerate

5. should have good test coverage.
