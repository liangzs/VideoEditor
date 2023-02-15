precision highp float;
uniform sampler2D inputImageTexture;
varying highp vec2 textureCoordinate;
uniform vec3 iResolution;
uniform float strength;

#define C_RED vec4(1.0, 0.0, 0.0, 1.0)
#define C_YELLOW vec4(1.0, 1.0, 0.0, 1.0)
#define C_BLUE vec4(0.0, 0.0, 1.0, 1.0)

void mainImage(out vec4 fragColor, in vec2 fragCoord)
{
    vec2 uv = fragCoord.xy / iResolution.xy;
    vec4 c = texture2D(inputImageTexture, uv);
    vec4 originalTextureColor = texture2D(inputImageTexture, textureCoordinate);
    float resultAlpha=originalTextureColor.a*(1.0/(originalTextureColor.a+0.00001));
    float luminance = 0.299 * c.r + 0.587 * c.g + 0.114 * c.b;
    float THRESHOLD = 100.0 / iResolution.x;
    vec4 newColor = (luminance < THRESHOLD) ? mix(C_BLUE, C_YELLOW, luminance * 2.0) : mix(C_YELLOW, C_RED, (luminance - 0.5) * 2.0);
    newColor.rgb *= 0.35*strength + 0.75 * pow(16.0 * uv.x * uv.y * (1.0 - uv.x) * (1.0 - uv.y), 0.15);
    fragColor = mix(c, newColor, strength)*resultAlpha;
}

void main(){
    mainImage(gl_FragColor, textureCoordinate * iResolution.xy);
}