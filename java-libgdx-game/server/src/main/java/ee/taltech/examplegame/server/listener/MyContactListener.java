//package ee.taltech.examplegame.server.listener;
//
//import com.badlogic.gdx.physics.box2d.*;
//import ee.taltech.examplegame.server.game.object.Player;
//import ee.taltech.examplegame.server.game.CollisionCategories; // Import categories
//
//public class MyContactListener implements ContactListener {
//
//    @Override
//    public void beginContact(Contact contact) {
//        Fixture fixtureA = contact.getFixtureA();
//        Fixture fixtureB = contact.getFixtureB();
//
//        // Identify foot sensor and the other fixture
//        Fixture footSensor = getFixtureFromUserData(fixtureA, fixtureB, Player.USER_DATA_FOOT_SENSOR);
//        Fixture other = (footSensor == fixtureA) ? fixtureB : fixtureA;
//
//        // Proceed only if one fixture IS the foot sensor
//        if (footSensor != null) {
//            // Check if the OTHER fixture IS Ground
//            Filter otherFilter = other.getFilterData();
//            if ((otherFilter.categoryBits & CollisionCategories.GROUND_BIT) != 0) {
//                 // Ensure the other object is not a sensor itself (usually ground isn't)
//                 // Although the mask on the sensor should prevent contact with other sensors
//                 if (!other.isSensor()) {
//                     Object bodyUserData = footSensor.getBody().getUserData();
//                     if (bodyUserData instanceof Player) {
//                         Player player = (Player) bodyUserData;
//                         player.incrementFootContacts();
//                     }
//                 }
//            }
//        }
//
//        // --- Add other collision logic here ---
//        // e.g., Player body hitting something
//        // Fixture playerBody = getFixtureFromUserData(fixtureA, fixtureB, Player.USER_DATA_PLAYER);
//        // if (playerBody != null) { ... }
//    }
//
//    @Override
//    public void endContact(Contact contact) {
//        Fixture fixtureA = contact.getFixtureA();
//        Fixture fixtureB = contact.getFixtureB();
//
//        // Identify foot sensor and the other fixture
//        Fixture footSensor = getFixtureFromUserData(fixtureA, fixtureB, Player.USER_DATA_FOOT_SENSOR);
//        Fixture other = (footSensor == fixtureA) ? fixtureB : fixtureA;
//
//        // Proceed only if one fixture WAS the foot sensor
//        if (footSensor != null) {
//            // Check if the OTHER fixture WAS Ground
//            Filter otherFilter = other.getFilterData();
//            if ((otherFilter.categoryBits & CollisionCategories.GROUND_BIT) != 0) {
//                // Ensure the other object is not a sensor itself
//                if (!other.isSensor()) {
//                    Object bodyUserData = footSensor.getBody().getUserData();
//                    if (bodyUserData instanceof Player) {
//                         Player player = (Player) bodyUserData;
//                         player.decrementFootContacts();
//                    }
//                }
//            }
//        }
//
//         // --- Add other end-contact logic here ---
//    }
//
//    // Helper to simplify finding a fixture by user data
//    private Fixture getFixtureFromUserData(Fixture a, Fixture b, String userDataToFind) {
//        if (userDataToFind.equals(a.getUserData())) {
//            return a;
//        }
//        if (userDataToFind.equals(b.getUserData())) {
//            return b;
//        }
//        return null;
//    }
//
//
//    @Override
//    public void preSolve(Contact contact, Manifold oldManifold) { }
//
//    @Override
//    public void postSolve(Contact contact, ContactImpulse impulse) { }
//}
