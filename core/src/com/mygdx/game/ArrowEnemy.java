package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Sudheer on 1/24/2015.
 */
public class ArrowEnemy extends Enemy{

    public ArrowEnemy(String fileName, boolean isXDir){
        super(fileName, isXDir);
        this.health = 20;
        this.maxHealth = 20;
        hasArrow = true;

        ARROW_TIMEOUT = 1.3f;
    }

    @Override
    public void collideWith(Actor actor) {
        if (actor instanceof Sword && actor != swordActor && actor != hitSword) {
            this.hitSword = (Sword) actor;
            this.health -= 15;
            Gdx.app.log("sword hit","");
        }
        else if (actor instanceof Arrow && actor != arrowActor) {
            this.health -= 10;
            ((Arrow) actor).removeSelf();
        }
        if (this.health <= 0) {
            this.removeSelf = true;
            getMyStage().gameEngine.enemyDying.play(0.4f);
        }

    }

    @Override
    public void act(float delta) {
        feedMovement();
        if(removeSelf){
            remove();
        }
        if(nextArrowUse >= 0){
            nextArrowUse -= delta;
        }
    }

    private void feedMovement() {
        Hero hero = getMyStage().hero;
        Direction heroDirection = hero.currDirection;
        float x = getX();
        float y = getY();

        float heroX = hero.getX();
        float heroY = hero.getY();

        if (isXDir) {
            if (Math.abs(y - heroY) < 0.01f) {
                if (x > heroX) {
                    if (currDirection == Direction.LEFT)
                        useOffensiveWeapon();
                    else {
                        currDirection = Direction.LEFT;
                        currShieldDirection = Direction.LEFT;
                        currentFrame = wLeftAni.getKeyFrame(0);
                    }
                }
                else {
                    if (currDirection == Direction.RIGHT)
                        useOffensiveWeapon();
                    else {
                        currDirection = Direction.RIGHT;
                        currShieldDirection = Direction.RIGHT;
                        currentFrame = wRightAni.getKeyFrame(0);
                    }
                }
            }
        }
        else {
            if (Math.abs(x - heroX) < 0.01f) {
                if (y > heroY) {
                    if (currDirection == Direction.DOWN)
                        useOffensiveWeapon();
                    else {
                        currDirection = Direction.DOWN;
                        currShieldDirection = Direction.DOWN;
                        currentFrame = wDownAni.getKeyFrame(0);
                    }
                }
                else {
                    if (currDirection == Direction.UP)
                        useOffensiveWeapon();
                    else {
                        currDirection = Direction.UP;
                        currShieldDirection = Direction.UP;
                        currentFrame = wUpAni.getKeyFrame(0);
                    }
                }
            }
        }
    }

//    public void useOffensiveWeapon() {
//        if(arrowActor != null && arrowActor.getParent() != null){
//            return;
//        }
//        arrowActor = new Arrow();
//        getMyStage().group.addActor(arrowActor);
//        arrowActor.setPosition(getX()+0.5f,getY()+0.5f);
//        arrowActor.shoot(currShieldDirection);
//    }

    @Override
    public void setX(float x) {
        super.setX(x);
        arrowActor.setX(x);
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        arrowActor.setY(y);
    }
}
