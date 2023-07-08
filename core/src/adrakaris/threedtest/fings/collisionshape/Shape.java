package adrakaris.threedtest.fings.collisionshape;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.Ray;

public interface Shape {

    /** Checks whether a transform matrix is visible from camera*/
    boolean isVisible(Matrix4 transform, Camera camera);

    /** @return -1 on no intersection, or when there is an intersection: the squared distance between the center of this
     * object and the point on the ray closest to this object when there is intersection. */
    float intersects(Matrix4 transform, Ray ray);
}
