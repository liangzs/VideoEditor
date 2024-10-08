precision mediump float;
 
varying mediump vec2 textureCoordinate;
 
uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;
uniform sampler2D inputImageTexture3;

uniform float strength;
 
void main()
{
    vec4 originColor = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=originColor.a*(1.0/(originColor.a+0.00001));
    vec3 texel = texture2D(inputImageTexture, textureCoordinate).rgb;
     
    texel = vec3(
                  texture2D(inputImageTexture2, vec2(texel.r, .16666)).r,
                  texture2D(inputImageTexture2, vec2(texel.g, .5)).g,
                  texture2D(inputImageTexture2, vec2(texel.b, .83333)).b);
     
    vec2 tc = (2.0 * textureCoordinate) - 1.0;
    float d = dot(tc, tc);
    vec2 lookup = vec2(d, texel.r);
    texel.r = texture2D(inputImageTexture3, lookup).r;
    lookup.y = texel.g;
    texel.g = texture2D(inputImageTexture3, lookup).g;
    lookup.y = texel.b;
    texel.b	= texture2D(inputImageTexture3, lookup).b;
     
    texel.rgb = mix(originColor.rgb, texel.rgb, strength);

    gl_FragColor = vec4(texel, 1.0)*resultAlpha;
}