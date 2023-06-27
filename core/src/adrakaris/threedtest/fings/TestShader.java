package adrakaris.threedtest.fings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

import static com.badlogic.gdx.graphics.GL20.GL_BACK;
import static com.badlogic.gdx.graphics.GL20.GL_LEQUAL;

public class TestShader implements Shader {
    private ShaderProgram program;
    private Camera camera;
    private RenderContext context;

    // uniform locs
    private int u_projViewTrans;
    private int u_worldTrans;

    // right after shader creation
    @Override
    public void init() {
        String vert = Gdx.files.internal("shader/test.vert").readString();
        String frag = Gdx.files.internal("shader/test.frag").readString();

        program = new ShaderProgram(vert, frag);
        if (!program.isCompiled()) throw new GdxRuntimeException(program.getLog());

        u_projViewTrans =  program.getUniformLocation("u_projViewTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");
    }
    @Override
    public void dispose() {
        program.dispose();
    }


    @Override
    public void begin(Camera camera, RenderContext context) {
        this.camera = camera;
        this.context = context;
        program.bind();
        // set the uniforms
        program.setUniformMatrix(u_projViewTrans, camera.combined);
        // set backface cullling
        context.setDepthTest(GL_LEQUAL);
        context.setCullFace(GL_BACK);
    }

    @Override
    public void render(Renderable renderable) {
        // set uniform
        program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
        // set attributes, bind and render
        renderable.meshPart.render(program);
    }

    @Override
    public void end() {
//        program.end();  // DEPRECATED - no longer necessary to call end
    }

    // used by the modelBatch to decide what shader to use first
    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    // decide if the shader should be used to render the specified renderable
    @Override
    public boolean canRender(Renderable instance) {
        return true;
    }
}
