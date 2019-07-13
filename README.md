# shopping-cart-redis-h2

Imagine you are working on a shopping Cart component, when user choose a product, it will be published to a message queue.

You are developing a micro service component which receives message from message queue, save it into database. 

The code should:

1. should have good test coverage.

2. the app should not lose message if the database connections goes down.

3. The app should be able to auto create table if the table doesn’t exist in database.

4. Self-healing from database issue without human intervention.

5. Good fault tolerate
