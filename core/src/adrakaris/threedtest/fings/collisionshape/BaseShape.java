package adrakaris.threedtest.fings.collisionshape;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

public abstract class BaseShape implements Shape {
    protected final static Vector3 position = new Vector3();
    public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();

    public BaseShape(BoundingBox bounds) {
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);
    }
}
