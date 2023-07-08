package adrakaris.threedtest.fings.collisionshape;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

public class DiscShape extends BaseShape {
    private float radius;

    public DiscShape(BoundingBox bounds) {
        super(bounds);
        radius = .5f * Math.max(dimensions.x, dimensions.z);
    }

    @Override
    public boolean isVisible(Matrix4 transform, Camera camera) {
        return camera.frustum.sphereInFrustum(transform.getTranslation(position).add(center), radius);
    }

    @Override
    public float intersects(Matrix4 transform, Ray ray) {
        // flat horizontal disc
        transform.getTranslation(position).add(center);
        final float len = (position.y - ray.origin.y) / ray.direction.y;
        final float distSq = position.dst2(
                ray.origin.x + len * ray.direction.x,
                ray.origin.y + len * ray.direction.y,
                ray.origin.z + len * ray.direction.z);
        return (distSq < radius*radius) ? distSq : -1;
    }
}
