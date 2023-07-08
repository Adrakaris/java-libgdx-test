package adrakaris.threedtest;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.SphereShapeBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.physics.bullet.collision.ebtDispatcherQueryType.BT_CLOSEST_POINT_ALGORITHMS;

public class BulletTest implements ApplicationListener {

    PerspectiveCamera camera;
    CameraInputController camController;

    ModelBatch modelBatch;
    Array<ModelInstance> instances;
    Environment environment;

    Model model;
    ModelInstance ground;
    ModelInstance ball;

    // bullet
    btCollisionConfiguration collisionConfig;
    btDispatcher dispatcher;

    // render-used attrs
    boolean collision;
    btCollisionShape groundShape;
    btCollisionShape ballShape;
    btCollisionObject groundObject;
    btCollisionObject ballObject;

    @Override
    public void create() {
        // initialise bullet, 3d physics engine
        Bullet.init();
        // bullet helper classes
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);

        // initialise required objects
        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
        environment.add(new DirectionalLight().set(.8f, .8f, .8f, -1f, -.8f, -.2f));

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(3f, 7f, 10f);
        camera.lookAt(0, 4, 0);
        camera.update();

        camController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(camController);

        instances = new Array<>();

        // initialise some models to use as example
        ModelBuilder mb = new ModelBuilder();
        MeshPartBuilder mpb;  // temporary reassigned variable
        mb.begin();
        mb.node().id = "ground";
        mpb = mb.part("box",
                GL20.GL_TRIANGLES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(Color.RED)));
        BoxShapeBuilder.build(mpb, 5,1,5);  // this is the accepted practice
        mb.node().id = "ball";
        mpb = mb.part("sphere", GL20.GL_TRIANGLES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)));
        SphereShapeBuilder.build(mpb, 1,1,1,12,12);
        model = mb.end();

        ground = new ModelInstance(model, "ground");
        ball = new ModelInstance(model, "ball");
        ball.transform.setToTranslation(0,9,0);

        // add collision
        ballShape = new btSphereShape(.5f);
        groundShape = new btBoxShape(new Vector3(2.5f, .5f, 2.5f));

        groundObject = new btCollisionObject();
        groundObject.setCollisionShape(groundShape);
        groundObject.setWorldTransform(ground.transform);
        ballObject = new btCollisionObject();
        ballObject.setCollisionShape(ballShape);
        ballObject.setWorldTransform(ball.transform);

        // add model to instance
        instances.add(ground);
        instances.add(ball);
    }

    @Override
    public void render() {
        // logic
        final float delta = Math.min(1f/60f, Gdx.graphics.getDeltaTime());

        if (!collision) {
            ball.transform.translate(0, -3*delta, 0);
            ballObject.setWorldTransform(ball.transform);

            collision = checkCollision();
        }

        // render
        camController.update();

        Gdx.gl.glClearColor(.3f, .3f, .3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }

    private boolean checkCollision() {
        // CollisionObjectWrapper is a wrapper over the c++ base class and thus does not have bt prefix
        CollisionObjectWrapper co_ball = new CollisionObjectWrapper(ballObject);
        CollisionObjectWrapper co_ground = new CollisionObjectWrapper(groundObject);

//        btCollisionAlgorithmConstructionInfo constrInfo = new btCollisionAlgorithmConstructionInfo();
//        constrInfo.setDispatcher1(dispatcher);
//        btCollisionAlgorithm algorithm = new btSphereBoxCollisionAlgorithm(null, constrInfo, co_ball.wrapper, co_ground.wrapper, false);
        // its masochistic to create an algorithm for every possible combination, so dispacher does it for us
        btCollisionAlgorithm algorithm = dispatcher.findAlgorithm(co_ball.wrapper, co_ground.wrapper, new btPersistentManifold(), BT_CLOSEST_POINT_ALGORITHMS);


        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(co_ball.wrapper, co_ground.wrapper);

        algorithm.processCollision(co_ball.wrapper, co_ground.wrapper, info, result);

        // get the "persistent manifold" of the simulation, and get the number of contacts between the ball and
        // the ground, and check if the number of contacts is greater than zero (a collision)
        boolean hit = result.getPersistentManifold().getNumContacts() > 0;

        result.dispose();
        info.dispose();
        algorithm.dispose();
//        constrInfo.dispose();
        co_ball.dispose();
        co_ground.dispose();

        return hit;
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        model.dispose();

        // bullet
        // note since bullet is originally a c++ api you have to manually free every thing you create.
        groundObject.dispose();
        groundShape.dispose();
        ballObject.dispose();
        ballShape.dispose();
        dispatcher.dispose();
        collisionConfig.dispose();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void resize(int width, int height) {
    }



}
