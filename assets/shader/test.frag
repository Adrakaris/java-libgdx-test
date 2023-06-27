#ifdef GL_ES  // set precision when on opengl-es (android/webgl)
precision mediump float;
#endif

varying vec2 v_texCoord0;

uniform vec3 u_colorU;
uniform vec3 u_colorV;

void main() {
//    gl_FragColor = vec4(v_texCoord0, 0.0, 1.0);
//    gl_FragColor = vec4(u_color, 1.0);
    gl_FragColor = vec4(v_texCoord0.x * u_colorU + v_texCoord0.y * u_colorV, 1.0);
}
