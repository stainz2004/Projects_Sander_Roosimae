package ee.taltech.examplegame.game;

import com.badlogic.gdx.physics.box2d.*;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Check if either fixture is the player
        if (fixtureA.getUserData() instanceof Player || fixtureB.getUserData() instanceof Player) {
            // Find which fixture is the player and which is the ground
            Fixture playerFixture = fixtureA.getUserData() instanceof Player ? fixtureA : fixtureB;
            Fixture groundFixture = playerFixture == fixtureA ? fixtureB : fixtureA;

            // Get player from user data
            Player player = (Player) playerFixture.getUserData();

            // Check if the other fixture is ground
            if (groundFixture.getUserData() != null && groundFixture.getUserData().equals("ground")) {
                // Check if the contact normal indicates the player is on top of the ground
                // This is a simplification - for more accurate ground detection you might
                // want to check the specific contact points
                if (contact.getWorldManifold().getNormal().y < 0) {
                    player.setGrounded(true);
                    System.out.println("Player grounded");
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Similar to beginContact, check if player is leaving ground
        if (fixtureA.getUserData() instanceof Player || fixtureB.getUserData() instanceof Player) {
            Fixture playerFixture = fixtureA.getUserData() instanceof Player ? fixtureA : fixtureB;
            Fixture groundFixture = playerFixture == fixtureA ? fixtureB : fixtureA;

            Player player = (Player) playerFixture.getUserData();

            if (groundFixture.getUserData() != null && groundFixture.getUserData().equals("ground")) {
                // We're no longer touching this ground object, but might still be on another
                // For a more accurate implementation, you might want to count the number of ground contacts
                player.setGrounded(false);
                System.out.println("Player not grounded");
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
