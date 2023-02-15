precision mediump float;
varying mediump vec2 textureCoordinate;
uniform sampler2D inputImageTexture;
uniform float strength;
void main()
{
    vec2 uv = textureCoordinate.xy;
    vec4 oriColor = texture2D(inputImageTexture, uv);
    float resultAlpha=oriColor.a*(1.0/(oriColor.a+0.00001));
    vec4 c = texture2D(inputImageTexture, uv);
    c = sin(uv.x*10.+c*cos(c*6.28+1.0+uv.x)*sin(c+uv.y+1.0)*6.28)*.5+.5;
    c.b+=length(c.rg);
    gl_FragColor = mix(oriColor, vec4(c.rgb, 1.0)*resultAlpha, strength);
}