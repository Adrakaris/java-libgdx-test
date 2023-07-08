package adrakaris.threedtest.fings;

import adrakaris.threedtest.fings.collisionshape.Shape;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

public class GameObject extends ModelInstance {
//    public final Vector3 center = new Vector3();
//    public final Vector3 dimensions = new Vector3();
//    public final float radius;  // sphere bounding box
//
//    private final static BoundingBox bounds = new BoundingBox();
//    private final static Vector3 position = new Vector3();
    public Shape shape;

    public GameObject(Model model, String rootNode, boolean mergeTransform) {
        super(model, rootNode, mergeTransform);
//        calculateBoundingBox(bounds);
//        bounds.getCenter(center);
//        bounds.getDimensions(dimensions);
//        radius = dimensions.len() / 2f;
    }

    public boolean isVisible(Camera camera) {
        return shape != null && shape.isVisible(this.transform, camera);
//        return camera.frustum.sphereInFrustum(transform.getTranslation(position).add(center), radius);
    }

    /** @return -1 on no intersection, or when there is an intersection: the squared distance between the center of this
     * object and the point on the ray closest to this object when there is intersection. */
    public float intersects(Ray ray) {
        return shape == null? -1 : shape.intersects(this.transform, ray);
//        transform.getTranslation(position).add(center);
//        final float len = ray.direction.dot(position.x - ray.origin.x, position.y - ray.origin.y, position.z - ray.origin.z);
//        if (len < 0) {
//            return -1;
//        }
//        float dist2 = position.dst2(ray.origin.x+ray.direction.x*len, ray.origin.y+ray.direction.y*len, ray.origin.z+ray.direction.z*len);
//        // if the distance is less than the radius squared
//        return (dist2 <= radius * radius) ? dist2 : -1;
    }
}
