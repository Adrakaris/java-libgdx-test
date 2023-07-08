package adrakaris.threedtest.fings.collisionshape;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

public class SphereShape extends BaseShape {
    public float radius;

    public SphereShape(BoundingBox bounds) {
        super(bounds);
        radius = bounds.getDimensions(new Vector3()).len() / 2f;
    }

    @Override
    public boolean isVisible(Matrix4 transform, Camera camera) {
        return camera.frustum.sphereInFrustum(transform.getTranslation(position).add(center), radius);
    }

    @Override
    public float intersects(Matrix4 transform, Ray ray) {
        transform.getTranslation(position).add(center);
        final float len = ray.direction.dot(
                position.x-ray.origin.x,
                position.y-ray.origin.y,
                position.z-ray.origin.z
        );
        if (len < 0) return -1;
        float distSq = position.dst2(
                ray.origin.x+ray.direction.x*len,
                ray.origin.y+ray.direction.y*len,
                ray.origin.z+ray.direction.z*len
        );
        return (distSq <= radius*radius) ? distSq : -1;
    }
}
