package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Sudheer on 1/24/15.
 */
public class Boss extends Character {

    public static float xOffset = 0.3f;
    public static float yOffset = 0.3f;
    public static float widthOffset = 0.8f;
    public static float heightOffset = 0.7f;

    boolean usingDefensiveWeapon;
    boolean usingOffensiveWeapon;
    float timeDifference = 0;

    private Sword swordActor;

    Texture sh_left, sh_right, sh_up, sh_down, sword, aura;

    public Boss(String fileName) {
        super(fileName);
        this.health = 1000;
        sh_left = new Texture("weapons/shield/sh_left.png");
        sh_right = new Texture("weapons/shield/sh_right.png");
        sh_up = new Texture("weapons/shield/sh_up.png");
        sh_down = new Texture("weapons/shield/sh_down.png");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    private void feedMovement() {
        Hero hero = getMyStage().hero;
        Direction heroDirection = hero.currDirection;
        float x = getX();
        float y = getY();

        float heroX = hero.getX();
        float heroY = hero.getY();

        float xDiff = x - heroX;
        float yDiff = y - heroY;

        if (Math.abs(xDiff) > Math.abs(yDiff))
            takeXDir(x, y, heroX, heroY);
        else
            takeYDir(x, y, heroX, heroY);
    }

    private void takeXDir(float x, float y, float heroX, float heroY) {
        if (!(isDirFeasible(Direction.LEFT) || isDirFeasible(Direction.RIGHT)))
            return;
        if (x > heroX)
            currDirection = Direction.LEFT;
        else
            currDirection = Direction.RIGHT;
        if (!isDirFeasible(currDirection))
            takeYDir(x, y, heroX, heroY);
    }

    private void takeYDir(float x, float y, float heroX, float heroY){
        if (!(isDirFeasible(Direction.DOWN) || isDirFeasible(Direction.UP)))
            return;
        if (y > heroY)
            currDirection = Direction.DOWN;
        else
            currDirection = Direction.UP;
        if (!isDirFeasible(currDirection))
            takeXDir(x, y, heroX, heroY);
    }

    @Override
    public void act(float delta) {
        feedMovement();
        super.act(delta);
    }

    @Override
    public void collideWith(Actor actor) {
        super.collideWith(actor);
        if (actor instanceof Sword && actor != swordActor)
            this.health -= 5;
        else if (actor instanceof Arrow)
            this.health -= 1;
        if (this.health <= 0)
            Gdx.app.exit();
        Gdx.app.log("BOSS", "Health is : " + this.health);
    }

    private void reset() {
        usingDefensiveWeapon = false;
        usingOffensiveWeapon = false;
        timeDifference = 0;
    }

    private void drawAura(Batch batch, float parentAlpha){
        //Draw Aura around
    }

    public void useOffensiveWeapon() {
    }

    public void useDefensiveWeapon() {
        usingDefensiveWeapon = true;
    }

    public void setHasAura(boolean hasAura) {
    }

    public void setHasArrow(boolean hasArrow) {
    }

    public void setHasSword(boolean hasSword) {
    }

    public void useOffWeapon(){
    }

    @Override
    public void setX(float x) {
        super.setX(x);
    }
    @Override
    public void setY(float y) {
        super.setY(y);
    }
}
