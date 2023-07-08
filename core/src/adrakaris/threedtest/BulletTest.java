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
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;

import static com.badlogic.gdx.physics.bullet.collision.ebtDispatcherQueryType.BT_CLOSEST_POINT_ALGORITHMS;

public class BulletTest implements ApplicationListener {

    static class GameObject extends ModelInstance implements Disposable {
        public final btCollisionObject body;
        public boolean moving;

        public GameObject(Model model, String name, btCollisionShape shape) {
            super(model, name);
            body = new btCollisionObject();
            body.setCollisionShape(shape);
        }

        @Override
        public void dispose() {
            body.dispose();
        }

        // factory object
        static class Constructor implements Disposable {
            public final Model model;
            public final String node;
            public final btCollisionShape shape;

            public Constructor(Model model, String node, btCollisionShape shape) {
                this.model = model;
                this.node = node;
                this.shape = shape;
            }

            public GameObject construct() {
                return new GameObject(model, node, shape);
            }

            @Override
            public void dispose () {
                shape.dispose();
            }
        }
    }

    PerspectiveCamera camera;
    CameraInputController camController;

    ModelBatch modelBatch;
    Array<GameObject> instances;
    Environment environment;

    ArrayMap<String, GameObject.Constructor> constructors;

    Model model;
//    GameObject ground;
//    GameObject ball;

    // bullet
    btCollisionConfiguration collisionConfig;
    btDispatcher dispatcher;

    // render-used attrs
    float spawnTimer;
//    boolean collision;
//    btCollisionShape groundShape;
//    btCollisionShape ballShape;
//    btCollisionObject groundObject;
//    btCollisionObject ballObject;

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
        mpb = mb.part("ground", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED)));
        BoxShapeBuilder.build(mpb, 5,1,5);  // this is the accepted practice
        mb.node().id = "sphere";
        mpb = mb.part("sphere", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)));
        SphereShapeBuilder.build(mpb, 1,1,1,12,12);
        mb.node().id = "box";
        mpb = mb.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.BLUE)));
        BoxShapeBuilder.build(mpb, 1,1,1);
        mb.node().id = "cone";
        mpb = mb.part("cone", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.YELLOW)));
        ConeShapeBuilder.build(mpb, 1, 2, 1, 12);
        mb.node().id = "capsule";
        mpb = mb.part("capsule", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.CYAN)));
        CapsuleShapeBuilder.build(mpb, .5f, 2f, 12);
        mb.node().id = "cylinder";
        mpb = mb.part("cylinder", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.MAGENTA)));
        CylinderShapeBuilder.build(mpb, 1, 2, 1, 12);
        model = mb.end();
        // end modelbuilder

        constructors = new ArrayMap<>(String.class, GameObject.Constructor.class);
        constructors.put("ground", new GameObject.Constructor(model, "ground", new btBoxShape(new Vector3(2.5f, 0.5f, 2.5f))));
        constructors.put("sphere", new GameObject.Constructor(model, "sphere", new btSphereShape(0.5f)));
        constructors.put("box", new GameObject.Constructor(model, "box", new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f))));
        constructors.put("cone", new GameObject.Constructor(model, "cone", new btConeShape(0.5f, 2f)));
        constructors.put("capsule", new GameObject.Constructor(model, "capsule", new btCapsuleShape(.5f, 1f)));
        constructors.put("cylinder", new GameObject.Constructor(model, "cylinder", new btCylinderShape(new Vector3(.5f, 1f, .5f))));


//        ground = new ModelInstance(model, "ground");
//        ball = new ModelInstance(model, "ball");
//        ball.transform.setToTranslation(0,9,0);



        // add collision
//        ballShape = new btSphereShape(.5f);
//        groundShape = new btBoxShape(new Vector3(2.5f, .5f, 2.5f));
//
//        groundObject = new btCollisionObject();
//        groundObject.setCollisionShape(groundShape);
//        groundObject.setWorldTransform(ground.transform);
//        ballObject = new btCollisionObject();
//        ballObject.setCollisionShape(ballShape);
//        ballObject.setWorldTransform(ball.transform);

        // add model to instance
//        instances.add(ground);
//        instances.add(ball);

        instances.add(constructors.get("ground").construct());
    }

    private void spawn() {
        GameObject obj = constructors.values[1+ MathUtils.random(constructors.size-2)].construct();

        obj.moving = true;
        obj.transform.setFromEulerAngles(MathUtils.random(360f), MathUtils.random(360f), MathUtils.random(360f));
        obj.transform.trn(MathUtils.random(-2.5f, 2.5f), 9f, MathUtils.random(-2.5f, 2.5f));
        obj.body.setWorldTransform(obj.transform);
        instances.add(obj);

    }

    @Override
    public void render() {
        // logic
        final float delta = Math.min(1f/60f, Gdx.graphics.getDeltaTime());

        for (GameObject obj : instances) {
            if (obj.moving) {
                obj.transform.trn(0, -delta, 0);
                obj.body.setWorldTransform(obj.transform);
                if (checkCollision(obj.body, instances.get(0).body)) {
                    obj.moving = false;
                }
            }
        }

        if ((spawnTimer -= delta) < 0) {
            spawn();
            spawnTimer = 1.5f;
        }

//        if (!collision) {
//            ball.transform.translate(0, -3*delta, 0);
//            ballObject.setWorldTransform(ball.transform);
//
//            collision = checkCollision();
//        }

        // render
        camController.update();

        Gdx.gl.glClearColor(.3f, .3f, .3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }

    private boolean checkCollision(btCollisionObject obj1, btCollisionObject obj2) {
        // CollisionObjectWrapper is a wrapper over the c++ base class and thus does not have bt prefix
        CollisionObjectWrapper cow1 = new CollisionObjectWrapper(obj1);
        CollisionObjectWrapper cow2 = new CollisionObjectWrapper(obj2);

//        btCollisionAlgorithmConstructionInfo constrInfo = new btCollisionAlgorithmConstructionInfo();
//        constrInfo.setDispatcher1(dispatcher);
//        btCollisionAlgorithm algorithm = new btSphereBoxCollisionAlgorithm(null, constrInfo, cow1.wrapper, cow2.wrapper, false);
        // its masochistic to create an algorithm for every possible combination, so dispacher does it for us
        btCollisionAlgorithm algorithm = dispatcher.findAlgorithm(cow1.wrapper, cow2.wrapper, new btPersistentManifold(), BT_CLOSEST_POINT_ALGORITHMS);


        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(cow1.wrapper, cow2.wrapper);

        algorithm.processCollision(cow1.wrapper, cow2.wrapper, info, result);

        // get the "persistent manifold" of the simulation, and get the number of contacts between the ball and
        // the ground, and check if the number of contacts is greater than zero (a collision)
        boolean hit = result.getPersistentManifold().getNumContacts() > 0;

        result.dispose();
        info.dispose();
        algorithm.dispose();
//        constrInfo.dispose();
        cow1.dispose();
        cow2.dispose();

        return hit;
    }

    @Override
    public void dispose() {
        for (GameObject o : instances) {
            o.dispose();
        }
        instances.clear();

        for (GameObject.Constructor c : constructors.values) {
            c.dispose();
        }
        constructors.clear();

        model.dispose();
        modelBatch.dispose();

        // bullet
        // note since bullet is originally a c++ api you have to manually free every thing you create.
//        groundObject.dispose();
//        groundShape.dispose();
//        ballObject.dispose();
//        ballShape.dispose();
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
