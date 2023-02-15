varying mediump vec2 textureCoordinate;
precision lowp float;

uniform vec3                iResolution;
uniform sampler2D inputImageTexture;
uniform float strength;


float remap(float value, float inputMin, float inputMax, float outputMin, float outputMax)
{
    return (value - inputMin) * ((outputMax - outputMin) / (inputMax - inputMin)) + outputMin;
}

void mainImage(out vec4 fragColor, in vec2 fragCoord)
{
    vec2 uv = fragCoord.xy;
    float normalizedContrast = 1.0;
    float contrast = remap(normalizedContrast, 0.0, 1.0, 0.2, 4.0);

    vec4 srcColor = texture2D(inputImageTexture, uv);
    float resultAlpha=srcColor.a*(1.0/(srcColor.a+0.0001));
    vec4 dstColor = vec4((srcColor.rgb - vec3(0.5)) * contrast + vec3(0.5), 1.0);
    fragColor = mix(srcColor, clamp(dstColor, 0.0, 1.0)*resultAlpha, strength);
}

void main() {
    mainImage(gl_FragColor, textureCoordinate);
}