package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;

/**
 * Created by Durga on 21-01-2015.
 */
public class MyScreen implements Screen {

    private final String mapName;
    private Protagonist protagonist;
    private MyStage stage;
    protected float ppy;
    protected float ppx;
    private TiledActor tiledActor;
    private FPSLogger fpsLogger;

    public MyScreen(String fName, Protagonist protagonist) {
        this.protagonist = protagonist;
        if(protagonist == null){
            this.protagonist = new Protagonist("test");
        }
        this.mapName = fName;
    }

    @Override
    public void show() {
        stage = new MyStage(protagonist);

//        group.addActor(new MyActor());



        tiledActor = new TiledActor("tiledMaps/"+mapName);

        tiledActor.setCamera((com.badlogic.gdx.graphics.OrthographicCamera) stage.getCamera());
        stage.gameEngine.setCollides(tiledActor.getCollidesLayer());
        stage.loadActors(tiledActor.getActorsLayer());

        Gdx.input.setInputProcessor(stage);

        fpsLogger = new FPSLogger();

    }

    @Override
    public void render(float delta) {

        stage.getBatch().begin();
        tiledActor.draw(stage.getBatch(),1f);
        stage.getBatch().end();
        stage.act(delta);
        stage.draw();

        fpsLogger.log();
    }

    @Override
    public void resize(int width, int height) {
        stage.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
