precision mediump float; 
uniform sampler2D u_TextureUnit;
varying float v_Alpha;
void main()
{
    gl_FragColor = texture2D(u_TextureUnit, gl_PointCoord)*v_Alpha;
}
