package com.ivn.bs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.ivn.bs.Aplication;
import com.ivn.bs.models.Ball;
import com.ivn.bs.models.ShootingBall;
import com.ivn.bs.models.StaticBall;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class GameScreen implements Screen {

    private Aplication game;

    private SpriteBatch batch;

    private Array<StaticBall> staticBalls = new Array<>();
    private Array<ShootingBall> shootingBalls = new Array<>();


    private int TAMBALL = 0;
    private int TAMFILA = 0;

    public static Texture textureRed = new Texture("redBall.png");
    public static Texture textureBlue = new Texture("blueBall.png");
    public static Texture textureGreen = new Texture("greenBall.png");

    public GameScreen(Aplication game) {
        this.game = game;
    }

    @Override
    public void show() {
        textureBlue = new Texture("blueBall.png");
        textureRed = new Texture("redBall.png");

        batch = new SpriteBatch();

        TAMBALL = Gdx.graphics.getWidth()/24;
        TAMFILA = Gdx.graphics.getWidth()/TAMBALL;

        generarShootingBall();
        generarFilas();
    }

    @Override
    public void render(float delta) {

        draw();
        update();
    }

    private void draw() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for(StaticBall ball : staticBalls)
            ball.draw(batch);

        for(ShootingBall ball : shootingBalls)
            ball.draw(batch);

        batch.end();

    }

    private void update() {
        if(Gdx.input.justTouched()){
            shootingBalls.get(0).shot(new Vector2(Gdx.input.getX(),Gdx.input.getY()));
        }

        // Mover
        shootingBalls.get(0).move();
        // rotar trazado
        shootingBalls.get(0).setRotationTrazado(getRotation());


        // comprobar colisiones
        for(StaticBall ball : staticBalls) {
            if(ball.getRect().overlaps(shootingBalls.get(0).getRect())) {
                // todo

                shootingToStaticBall(ball);
                shootingBalls.clear();
                generarShootingBall();
            }
        }

        if(shootingBalls.get(0).getX() <= 0 || shootingBalls.get(0).getX()+shootingBalls.get(0).getTam() >= Gdx.graphics.getWidth() )
            shootingBalls.get(0).rebotar();
    }

    // cuando la shooting ball impacta la transformamos a staticball y comprobamos colores
    public void shootingToStaticBall(StaticBall staticBall){
        ShootingBall shootingBall = shootingBalls.get(0);

        // miramos donce impacta para colocarla a iza o der
        float x = 0 ;
        if (shootingBall.getX() > staticBall.getX())
            x = staticBall.getX() + staticBall.getTam()/2;
        else
            x = staticBall.getX() - staticBall.getTam()/2;

        shootingBall.setPosition(x,staticBall.getY()-shootingBall.getTam());

        staticBalls.add(new StaticBall(shootingBall.getBallColor(),shootingBall.getPos(),shootingBall.getTam()));

        // si ha dado a su color comprobamos y borramos
        comprobarColores(shootingBall.getBallColor(),shootingBall.getPos());

        int tamIndicesAEliminar = indicesAEliminar.size();

        // si se juntan 3 o mas borramos
        if(tamIndicesAEliminar >= 3){
            int[] arrayAux = new int[200];

            // copiamos los indices a un array normal
            System.out.println("VAMOS A BORRAR INDICES: "+indicesAEliminar);
            Iterator value = indicesAEliminar.iterator();
            int i = 0;
            while (value.hasNext()) {
                arrayAux[i++] = (Integer) value.next();
            }

            // procedemos al borrado
            Arrays.sort(arrayAux);
            if(tamIndicesAEliminar >= 3){
                for(int j = arrayAux.length -1  ; j > arrayAux.length -1 - tamIndicesAEliminar ; j--){
                    staticBalls.removeIndex(arrayAux[j]);
                    System.out.println("ultimo indice del array de bolas estaticas :"+ staticBalls.indexOf(staticBalls.peek(),false));
                }
            }
        }
        indicesAEliminar.clear();


        // comprobamos si se han quedado bolas huerfanas
        comprobarSiHuerfanas();
    }

    public  HashSet<Integer> indicesAEliminar = new HashSet<Integer>();

    public void comprobarColores(Ball.Color color, Vector2 posOrigen){

        posOrigen = new Vector2(posOrigen.x + TAMBALL/2,posOrigen.y + TAMBALL/2);

        for (int i = 0; i<staticBalls.size;i++) {
            // derecha
            if (staticBalls.get(i).getRect().contains(new Vector2(posOrigen.x + TAMBALL, posOrigen.y))) {
                System.out.println("Hay bola derecha");
                if (staticBalls.get(i).getBallColor() == color) {
                    System.out.println("mismo color");

                    if (!indicesAEliminar.contains(staticBalls.indexOf(staticBalls.get(i), false))) {
                        System.out.println(indicesAEliminar);
                        System.out.println("indice no registrado: " + staticBalls.indexOf(staticBalls.get(i), false));

                        indicesAEliminar.add(staticBalls.indexOf(staticBalls.get(i), false));
                        System.out.println("guardamos indice: " + staticBalls.indexOf(staticBalls.get(i), false));
                        comprobarColores(color, staticBalls.get(i).getPos());
                    }

                    indicesAEliminar.add(staticBalls.indexOf(staticBalls.get(i), false));
                    System.out.println("guardamos indice: " + staticBalls.indexOf(staticBalls.get(i), false));


                }
            }
            // izquierda
            if (staticBalls.get(i).getRect().contains(new Vector2(posOrigen.x - TAMBALL, posOrigen.y))) {
                System.out.println("Hay bola izquierda");

                if (staticBalls.get(i).getBallColor() == color) {
                    System.out.println("mismo color");

                    if (!indicesAEliminar.contains(staticBalls.indexOf(staticBalls.get(i), false))) {
                        System.out.println(indicesAEliminar);

                        System.out.println("indice no registrado: " + staticBalls.indexOf(staticBalls.get(i), false));
                        indicesAEliminar.add(staticBalls.indexOf(staticBalls.get(i), false));
                        System.out.println("guardamos indice: " + staticBalls.indexOf(staticBalls.get(i), false));
                        comprobarColores(color, staticBalls.get(i).getPos());
                    }

                    indicesAEliminar.add(staticBalls.indexOf(staticBalls.get(i), false));
                    System.out.println("guardamos indice: " + staticBalls.indexOf(staticBalls.get(i), false));

                }
            }
            // abajo izquierda
            if (staticBalls.get(i).getRect().contains(new Vector2(posOrigen.x - TAMBALL / 2, posOrigen.y - TAMBALL))) {
                System.out.println("Hay bola abajo izquirda");

                if (staticBalls.get(i).getBallColor() == color) {
                    System.out.println("mismo color");

                    if (!indicesAEliminar.contains(staticBalls.indexOf(staticBalls.get(i), false))) {
                        System.out.println(indicesAEliminar);

                        System.out.println("indice no registrado: " + staticBalls.indexOf(staticBalls.get(i), false));
                        indicesAEliminar.add(staticBalls.indexOf(staticBalls.get(i), false));
                        System.out.println("guardamos indice: " + staticBalls.indexOf(staticBalls.get(i), false));
                        comprobarColores(color, staticBalls.get(i).getPos());
                    }
                    indicesAEliminar.add(staticBalls.indexOf(staticBalls.get(i), false));
                    System.out.println("guardamos indice: " + staticBalls.indexOf(staticBalls.get(i), false));

                }
            }
            // abajo derecha
            if (staticBalls.get(i).getRect().contains(new Vector2(posOrigen.x + TAMBALL / 2, posOrigen.y - TAMBALL))) {
                System.out.println("Hay bola abajo derecha");

                if (staticBalls.get(i).getBallColor() == color) {
                    System.out.println("mismo color");

                    if (!indicesAEliminar.contains(staticBalls.indexOf(staticBalls.get(i), false))) {
                        System.out.println(indicesAEliminar);

                        System.out.println("indice no registrado: " + staticBalls.indexOf(staticBalls.get(i), false));
                        indicesAEliminar.add(staticBalls.indexOf(staticBalls.get(i), false));
                        System.out.println("guardamos indice: " + staticBalls.indexOf(staticBalls.get(i), false));
                        comprobarColores(color, staticBalls.get(i).getPos());
                    }
                    indicesAEliminar.add(staticBalls.indexOf(staticBalls.get(i), false));
                    System.out.println("guardamos indice: " + staticBalls.indexOf(staticBalls.get(i), false));

                }
            }
            // ARRIBA izquierda
            if (staticBalls.get(i).getRect().contains(new Vector2(posOrigen.x - TAMBALL / 2, posOrigen.y + TAMBALL))) {
                System.out.println("Hay bola arriba izquierda");

                if (staticBalls.get(i).getBallColor() == color) {
                    System.out.println("mismo color");

                    if (!indicesAEliminar.contains(staticBalls.indexOf(staticBalls.get(i), false))) {
                        System.out.println(indicesAEliminar);

                        System.out.println("indice no registrado: " + staticBalls.indexOf(staticBalls.get(i), false));
                        indicesAEliminar.add(staticBalls.indexOf(staticBalls.get(i), false));
                        System.out.println("guardamos indice: " + staticBalls.indexOf(staticBalls.get(i), false));
                        comprobarColores(color, staticBalls.get(i).getPos());
                    }
                    indicesAEliminar.add(staticBalls.indexOf(staticBalls.get(i), false));
                    System.out.println("guardamos indice: " + staticBalls.indexOf(staticBalls.get(i), false));

                }
            }
            // ARRIBA derecha
            if (staticBalls.get(i).getRect().contains(new Vector2(posOrigen.x + TAMBALL / 2, posOrigen.y + TAMBALL))) {
                System.out.println("Hay bola arriba derecha");

                if (staticBalls.get(i).getBallColor() == color) {
                    System.out.println("mismo color");

                    if (!indicesAEliminar.contains(staticBalls.indexOf(staticBalls.get(i), false))) {
                        System.out.println(indicesAEliminar);

                        System.out.println("indice no registrado: " + staticBalls.indexOf(staticBalls.get(i), false));
                        indicesAEliminar.add(staticBalls.indexOf(staticBalls.get(i), false));
                        System.out.println("guardamos indice: " + staticBalls.indexOf(staticBalls.get(i), false));
                        comprobarColores(color, staticBalls.get(i).getPos());
                    }

                    indicesAEliminar.add(staticBalls.indexOf(staticBalls.get(i), false));
                    System.out.println("guardamos indice: " + staticBalls.indexOf(staticBalls.get(i), false));

                }
            }
        }
    }

    public void bajarFilas(){
        for(StaticBall ball : staticBalls)
            ball.setPosition(ball.getX(), ball.getY()-TAMBALL);
    }

    public void generarShootingBall(){
        shootingBalls.add(new ShootingBall(getRandomColor(),new Vector2(Gdx.graphics.getWidth()/2-TAMBALL/2,0),TAMBALL));
    }

    public float getRotation(){
        float angle = (float) Math.atan2(getCursor().y - shootingBalls.get(0).getTrazadoCenterPos().y, getCursor().x - shootingBalls.get(0).getTrazadoCenterPos().x);
        return (float) Math.toDegrees(angle);
    }

    public Vector2 getCursor(){
        return new Vector2(Gdx.input.getX(),Gdx.graphics.getHeight() - Gdx.input.getY());
    }

    static Timer.Task task;
    int x,y = 0;
    int filasCreadas = 0;
    public void generarFilas(){
        task = new Timer.Task() {
            public void run() {

                int tamFila = TAMFILA;
                x = 0;
                y = Gdx.graphics.getHeight();

                if(filasCreadas%2 == 0){
                    x -= TAMBALL/2;
                    tamFila++;
                }

                for(int i = 0; i < tamFila; i++){

                    staticBalls.add(new StaticBall(getRandomColor(),new Vector2(x,y),TAMBALL));
                    x += TAMBALL;
                }

                filasCreadas++;
                bajarFilas();

            }
        };
        Timer.schedule( task, 1,6, 20);
    }

    public Ball.Color getRandomColor(){
        int random = MathUtils.random(0,2);
        if(random == 0)
            return Ball.Color.BLUE;
        else if(random == 1)
            return Ball.Color.GREEN;
        else
            return Ball.Color.RED;
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        for(StaticBall ball : staticBalls){
            ball.getTexture().dispose();
        }
    }
}
