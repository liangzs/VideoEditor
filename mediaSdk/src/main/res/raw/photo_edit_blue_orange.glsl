varying highp vec2 textureCoordinate;
precision highp float;

uniform sampler2D inputImageTexture;
uniform float strength;

void main()
{
    vec4 textureColorRes=texture2D(inputImageTexture, textureCoordinate);
    vec3 tex = textureColorRes.rgb;
    float resultAlpha=textureColorRes.a*(1.0/(textureColorRes.a+0.00001));
    float shade = dot(tex, vec3(0.333333));
    vec3 col = mix(vec3(0.1, 0.36, 0.8) * (1.0-2.0*abs(shade-0.5)), vec3(1.06, 0.8, 0.55), 1.0-shade);
    gl_FragColor = vec4(mix(tex, col, strength), 1.0)*resultAlpha;
}