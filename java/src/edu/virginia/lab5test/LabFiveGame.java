package edu.virginia.lab5test;

import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.SoundManager;
import edu.virginia.engine.display.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class LabFiveGame extends Game {
    Sprite background = new Sprite("bg", "blank.png");
    Sprite mario = new Sprite("Mario", "mario.png");
    Sprite bowser = new Sprite("Bowser", "bowser.png");
    Sprite luigi = new Sprite("Luigi", "luigi.png");
    Sprite winScreen = new Sprite("win", "winner.jpeg");
    Sprite ground1 = new Sprite("ground1", "ground.png");
    Sprite ground2 = new Sprite("ground2", "ground.png");
    Sprite mushroomScore0 = new Sprite("mushroomS0", "mushroom.png");
    Sprite mushroomScore1 = new Sprite("mushroomS1", "mushroom1.png");
    Sprite mushroomScore2 = new Sprite("mushroomS2", "mushroom.png");
    Sprite mushroomScore3 = new Sprite("mushroomS3", "mushroom.png");
    Sprite spike = new Sprite("spike", "spike.png");
    //Sprite spike = new Sprite("spike", "spike.png");
    SoundManager soundManager;
    private int visibilityBlocker = 10;
    private int score = 4;
    private boolean reverseMotion = false;
    private boolean didWin = false;
    private boolean isJumping = false;
    private int jumpingSpeed = 20;
    private int endingSpeed = -10;

    // bounce after jump
    private boolean isBouncingAfterJump = false;
    private int numBounced = 1;
    private int currentJumpingSpeed = 20;
    private String currentlyPlaying;
    private boolean doneBouncing = false;
    private final int GROUNDPOINT = 675;
    private final int INITIALJUMPINGSPEED = 20;
    private final int BOUNCOEFFICIENT = 3;
    private final int INITIALNUMBOUNCE = 1;
    private final int NUMTIMESTOBOUNCE = 3;

    public LabFiveGame() {
        super("Lab Five Test Game", 900, 900);
        //System.out.println("mush0 pos");
        background.addChild(mario);
        background.addChild(luigi);
        background.addChild(bowser);
        background.addChild(mushroomScore0);
        background.addChild(mushroomScore1);
        background.addChild(mushroomScore2);
        background.addChild(mushroomScore3);
        background.addChild(spike);
        background.addChild(ground1);
        background.addChild(ground2);
        background.addChild(winScreen);
        background.addChild(spike);

        winScreen.setPosition(new Point(500, 500));
        winScreen.setVisible(false);
        // The position setting below are all acting as offsets off the previous mushroom
        // very weird!
        mushroomScore0.setPosition(new Point(600, 10));
        mushroomScore1.setPosition(new Point(650, 10));
        mushroomScore2.setPosition(new Point(700, 10));
        mushroomScore3.setPosition(new Point(750, 10));
        spike.setPosition(new Point(300, 450));
        spike.setPivotPoint(new Point(350, 200));
        ground1.setPosition(new Point(0, 815));
        ground2.setPosition(new Point(550, 815));


        mario.setPosition(new Point(0, 675));
        luigi.setPosition(new Point(750, 600));
        bowser.setPosition(new Point(350, 300));
        //mushroomScore0.initializeRectangleHitbox();
        mario.initializeRectangleHitbox();
        luigi.initializeRectangleHitbox();
        bowser.initializeRectangleHitbox();
        ground1.initializeRectangleHitbox();
        ground2.initializeRectangleHitbox();
        spike.initializeRectangleHitbox();
        soundManager = new SoundManager();
        soundManager.LoadSoundEffect("bowserCollision", "dead.wav");
        soundManager.LoadSoundEffect("luigiCollision", "win.wav");
        soundManager.LoadMusic("bgMusic", "game_music.wav");
        this.currentlyPlaying = "bgMusic";
        soundManager.PlayMusic("bgMusic");

        mario.setPhysics(true);
        spike.setBounciness(true);
    }

    private void handleBounce() {
        if (isBouncingAfterJump) {
            //System.out.println("numtimes to bound: " + NUMTIMESTOBOUNCE + " --num bounce: " + this.numBounced);
            if (!this.isJumping && this.numBounced <= NUMTIMESTOBOUNCE) {
                this.currentJumpingSpeed = INITIALJUMPINGSPEED - (this.numBounced * BOUNCOEFFICIENT);
                this.numBounced += 1;
                this.isJumping = true;
            } else if (this.numBounced > NUMTIMESTOBOUNCE) {
                this.numBounced = this.INITIALNUMBOUNCE;
                this.isBouncingAfterJump = false;
                this.doneBouncing = true;
            }
        }
    }

    /**
     * Engine will automatically call this update method once per frame and pass to us
     * the set of keys (as strings) that are currently being pressed down
     */
    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);
//

        if (this.didWin) {
            soundManager.PlaySoundEffect("luigiCollision");
            this.didWin = false;
            winScreen.setVisible(false);
        }
        visibilityBlocker--;
        if (visibilityBlocker == Integer.MAX_VALUE)
            visibilityBlocker = 10;
        int marioX = mario.getPosition().x;

        if (marioX > (ground1.getPosition().x + 300)  && marioX < ground2.getPosition().x && !isJumping) {
            mario.setPosition(new Point(marioX, mario.getPosition().y + 5));
            mario.updateHitbox(0, 5);
            mario.setFalling(true);
        }
        /* Collision handling */
        if (mario.collidesWith(ground1) || mario.collidesWith(ground2)) {
            mario.setPosition(new Point(mario.getPosition().x, 675));

        }


        if (mario.collidesWith(bowser) || bowser.collidesWith((mario)) || mario.getPosition().y > 900) {
            score -= 1;
            //System.out.println("removing mushroomS" + score);
            background.removeChild("mushroomS" + (score));
            soundManager.PlaySoundEffect("bowserCollision");
            soundManager.PlayMusic(this.currentlyPlaying);
            mario.setPosition(new Point(0, 675));
            mario.setPivotPoint(new Point(0, 0));
            mario.setRotation(0.0f);
            mario.setScaleX(1.0);
            mario.setScaleY(1.0);
            mario.initializeRectangleHitbox();
            bowser.initializeRectangleHitbox();
        }

        if (mario.collidesWith(spike) || spike.collidesWith(mario)) {
            if (spike.getBounciness())
                mario.setBounciness(true);
        }
        mario.bounce();

        if (mario.collidesWith(luigi)) {
            winScreen.setVisible(true);
            this.didWin = true;
            soundManager.PlaySoundEffect("luigiCollision");
            mario.initializeRectangleHitbox();

        }

        /* Jumping physics */
        if (isJumping) {
            mario.setPosition(new Point(mario.getPosition().x, mario.getPosition().y - currentJumpingSpeed));
            mario.updateHitbox(0, -currentJumpingSpeed);
            if (currentJumpingSpeed > endingSpeed) {
                currentJumpingSpeed -= 1;
            }
            if (mario.getPosition().y > GROUNDPOINT) {
                mario.setPosition(new Point(mario.getPosition().x, GROUNDPOINT));
                if (!this.doneBouncing)
                    this.isBouncingAfterJump = true;
                else
                    this.doneBouncing = false;


                currentJumpingSpeed = INITIALJUMPINGSPEED;
                isJumping = false;
            }
        }

        this.handleBounce();

        /* Make sure mario is not null. Sometimes Swing can auto cause an extra frame to go before everything is initialized */
        if (mario != null) mario.update(pressedKeys);


        if (pressedKeys.contains(KeyEvent.VK_V)) {
            /* visibilityBlocker only allows visibility to change once every 10 iterations of the loop
             * This prevents V key events that last more than one iteration from producing
             * no effect on the sprite
             */
            if (visibilityBlocker <= 0) {
                mario.setVisible(!mario.getVisible());
                visibilityBlocker = 10;
            }
            System.out.println(mario.getVisible());
        }

        if (pressedKeys.contains(KeyEvent.VK_Z)) {
            Float alpha = mario.getAlpha();
            if (alpha + 0.01f <= 1) {
                mario.setAlpha(mario.getAlpha() + 0.01f);
            }

            System.out.println(mario.getAlpha());
            System.out.println("-------------");

        }

        if (pressedKeys.contains(KeyEvent.VK_X)) {
            Float alpha = mario.getAlpha();
            if (alpha - 0.01f >= 0) {
                mario.setAlpha(mario.getAlpha() - 0.01f);
            }
            System.out.println(mario.getAlpha());
            System.out.println("-------------");
        }

        if (pressedKeys.contains(KeyEvent.VK_A)) {
            mario.setScaleX(mario.getScaleX() + 0.1);
            mario.setScaleY(mario.getScaleY() + 0.1);
            mario.updateHitbox(0.1);


        }

        if (pressedKeys.contains(KeyEvent.VK_S)) {
            if (mario.getScaleX() - 0.1 >= 0 && mario.getScaleY() - 0.1 >= 0) {
                mario.setScaleX(mario.getScaleX() - 0.1);
                mario.setScaleY(mario.getScaleY() - 0.1);
                mario.updateHitbox(-0.1);
            }

        }

        /* Up, down, left, right */
        if (pressedKeys.contains(KeyEvent.VK_UP) && !mario.getFalling()) {
            if (mario.getPhysics()) {
                isJumping = true;
            } else {
                mario.setPosition(new Point(mario.getPosition().x, mario.getPosition().y - 5));
                mario.updateHitbox(0, -5);
            }
        }

        if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
            mario.setPosition(new Point(mario.getPosition().x, mario.getPosition().y + 5));
            mario.updateHitbox(0, 5);
        }
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
            mario.setPosition(new Point(mario.getPosition().x - 5, mario.getPosition().y));
            mario.updateHitbox(-5, 0);
        }
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            if (!mario.getBounciness()) {
                mario.setPosition(new Point(mario.getPosition().x + 5, mario.getPosition().y));
                mario.updateHitbox(5, 0);
            }
        }

        /* I,J,K,L Pivot Point */
        if (pressedKeys.contains(KeyEvent.VK_I)) {
            mario.setPivotPoint(new Point(mario.getPivotPoint().x, mario.getPivotPoint().y - 5));
        }
        if (pressedKeys.contains(KeyEvent.VK_J)) {
            mario.setPivotPoint(new Point(mario.getPivotPoint().x - 5, mario.getPivotPoint().y));
        }
        if (pressedKeys.contains(KeyEvent.VK_K)) {
            mario.setPivotPoint(new Point(mario.getPivotPoint().x, mario.getPivotPoint().y + 5));
        }
        if (pressedKeys.contains(KeyEvent.VK_L)) {
            mario.setPivotPoint(new Point(mario.getPivotPoint().x + 5, mario.getPivotPoint().y));
        }

        /* W and Q Rotation */
        if (pressedKeys.contains(KeyEvent.VK_W)) {
            mario.setRotation(mario.getRotation() + 5.0f);
            mario.updateHitbox(5.0f);
        }
        if (pressedKeys.contains(KeyEvent.VK_Q)) {
            mario.setRotation(mario.getRotation() - 5.0f);
            mario.updateHitbox(-5.0f);
        }
        int y = bowser.getPosition().y;
        int spikeX = spike.getPosition().x;
        int spikeY = spike.getPosition().y;
        if (reverseMotion) {
            spike.setPosition(new Point(spikeX + 2, spikeY - 5));
            spike.updateHitbox(2, -5);
            bowser.setPosition(new Point(bowser.getPosition().x, y - 5));
            bowser.updateHitbox(0, -5);
            if (y <= 0)
                reverseMotion = !reverseMotion;
        } else {
            spike.setPosition(new Point(spikeX - 2, spikeY + 5));
            spike.updateHitbox(-2, 5);
            bowser.setPosition(new Point(bowser.getPosition().x, y + 5));
            bowser.updateHitbox(0, 5);
            if (y >= 800)
                reverseMotion = !reverseMotion;
        }



    }



    /**
     * Engine automatically invokes draw() every frame as well. If we want to make sure sun gets drawn to
     * the screen, we need to make sure to override this method and call sun's draw method.
     */
    @Override
    public void draw(Graphics g) {
        super.draw(g);
        /* Same, just check for null in case a frame gets thrown in before the sprites is initialized */
        //System.out.println("----------------- calling parent draw");
        if (background != null) background.draw(g);
        //System.out.println("----------------- parent draw");

    }

    /**
     * Quick main class that simply creates an instance of our game and starts the timer
     * that calls update() and draw() every frame
     */
    public static void main(String[] args) {
        LabFiveGame game = new LabFiveGame();
        game.start();
    }
}
