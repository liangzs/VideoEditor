precision mediump float;
varying mediump vec2 textureCoordinate;
uniform sampler2D inputImageTexture;
uniform float strength;

void main()
{
    vec2 uv = textureCoordinate.xy;
    vec4 c = texture2D(inputImageTexture, uv);
    float resultAlpha=c.a*(1.0/(c.a+0.00001));
    vec4 c2 = texture2D(inputImageTexture, uv + vec2(0.005*strength, 0.005*strength));
    c = c - distance(c, c2);
    gl_FragColor = vec4(c.rgb, 1.0)*resultAlpha;
}