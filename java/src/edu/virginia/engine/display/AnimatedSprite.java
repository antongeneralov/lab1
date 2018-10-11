package edu.virginia.engine.display;

import edu.virginia.engine.util.GameClock;

import java.util.ArrayList;
import java.awt.*;
import java.lang.Thread;

public class AnimatedSprite extends Sprite {

    static final int DEFAULT_ANIMATION_SPEED = 1000;

    private ArrayList<Animation> animations;
    private Boolean playing;
    private String fileName;
    private ArrayList<DisplayObject> frames;
    private Integer currentFrame;
    private Integer startFrame;
    private Integer endFrame;
    private Integer animationSpeed;
    private GameClock gameClock;



    public AnimatedSprite(String id, String fileName, Point pos) {
        super(id);
        this.fileName = fileName;
        this.position = pos;
        this.gameClock = new GameClock();
    }


    public void initGameClock() {
        if (gameClock == null)
            this.gameClock = new GameClock();
    }

    public void setAnimations(ArrayList<Animation> animations) {
        this.animations = animations;
    }

    public void setAnimationSpeed(Integer animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    public void draw() {
        currentFrame = startFrame;
        while (currentFrame != endFrame) {

        }


    }

    /* Partner 2 - Part 3 and 4 */
    public void animate(Animation animateObject) {
        this.startFrame = animateObject.getStartFrame();
        this.endFrame = animateObject.getEndFrame();
    }
    public void animate(String id) {


    }
    public void animate(Integer startFrame, Integer endFrame) {
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }

    public void stopAnimation(Integer frameNumber) {

    }
    public void stopAnimation() {

    }


}